package me.trambled.ozark.ozarkclient.module.movement;

import io.netty.util.internal.ConcurrentSet;
import me.trambled.ozark.ozarkclient.event.events.EventMove;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.event.events.EventPlayerPushOutOfBlocks;
import me.trambled.ozark.ozarkclient.event.events.EventRotation;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.player.EntityUtil;
import me.trambled.ozark.ozarkclient.util.world.TimerUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PacketFly extends Module {

    public PacketFly() {
        super(Category.MOVEMENT);

		this.name        = "PacketFly";
		this.tag         = "PacketFly";
		this.description = "Uses packets to fly.";
    }

    Setting flight = create("Flight", "PFlyFlight", true);
    Setting flight_mode = create("Flight Mode", "PFlyFlightmode", 0, 0, 1);
    Setting do_anti_factor = create("Factorize", "PFlyFactorize", true);
    Setting anti_factor = create("Anti Factor", "PFlyAntiFactor", 2.5, 0.1, 3.0);
    Setting extra_factor = create("Extra Factor", "PFlyExtraFactor", 1.0, 0.1, 3.0);
    Setting strafe_factor = create("Strafe Factor", "PFlyStrafeFactor", true);
    Setting loops = create("Loops", "PFlyLoops", 1, 1, 10);
    Setting clear_tele_map = create("Clear Map", "PFlyClearMap", true);
    Setting map_time = create("Map Time", "PFlyMapTime", 30, 1, 500);
    Setting clear_ids = create("Clear ID", "PFlyClearID", true);
    Setting set_yaw = create("Set Yaw", "PFlySetYaw", true);
    Setting set_id = create("Set ID", "PFlySetID", true);
    Setting set_move = create("Set Move", "PFlySetMove", false);
    Setting set_pos = create("Set Pos", "PFlySetPos", false);
    Setting noclip = create("Noclip", "PFlyNoclip", false);
    Setting send_teleport = create("Teleport", "PFlySendTeleport", true);
    Setting reset_id = create("Reset ID", "PFlyResetID", true);
    Setting invalid_packet = create("Invalid Packet", "PFlyInvalidPacket", true);

    private final Set<CPacketPlayer> packets = new ConcurrentSet<>();
    private final Map<Integer, IDtime> teleportmap = new ConcurrentHashMap<>();
    private int flightCounter = 0;
    private int teleportID = 0;


    @Override
    public void update() {
        this.teleportmap.entrySet().removeIf(idTime -> clear_tele_map.get_value(true) && idTime.getValue().getTimer().passed(map_time.get_value(1)));
    }

    @EventHandler
    private final Listener<EventRotation> motion_update = new Listener<>(event -> {
        PacketFly.mc.player.setVelocity(0.0, 0.0, 0.0);
        double speed = 0.0;
        boolean checkCollisionBoxes = this.checkHitBoxes();
        speed = PacketFly.mc.player.movementInput.jump && (checkCollisionBoxes || !EntityUtil.isMoving()) ? (this.flight.get_value(true) && !checkCollisionBoxes ? (this.flight_mode.get_value(1) == 0 ? (this.resetCounter(10) ? -0.032 : 0.062) : (this.resetCounter(20) ? -0.032 : 0.062)) : 0.062) : (PacketFly.mc.player.movementInput.sneak ? -0.062 : (!checkCollisionBoxes ? (this.resetCounter(4) ? (flight.get_value(true) ? -0.04 : 0.0) : 0.0) : 0.0));
        if (do_anti_factor.get_value(true) && checkCollisionBoxes && EntityUtil.isMoving() && speed != 0.0) {
            speed /= anti_factor.get_value(1d);
        }
        double[] strafing = this.getMotion(strafe_factor.get_value(true) && checkCollisionBoxes ? 0.031 : 0.26);
        for (int i = 1; i < this.loops.get_value(1) + 1; ++i) {
            PacketFly.mc.player.motionX = strafing[0] * (double) i * this.extra_factor.get_value(1d);
            PacketFly.mc.player.motionY = speed * (double) i;
            PacketFly.mc.player.motionZ = strafing[1] * (double) i * this.extra_factor.get_value(1d);
            this.sendPackets(PacketFly.mc.player.motionX, PacketFly.mc.player.motionY, PacketFly.mc.player.motionZ, send_teleport.get_value(true));
        }
    });

    @EventHandler
    private final Listener<EventMove> move = new Listener<>(event -> {
        if (this.set_move.get_value(true) && this.flightCounter != 0) {
            event.set_x(PacketFly.mc.player.motionX);
            event.set_y(PacketFly.mc.player.motionY);
            event.set_z(PacketFly.mc.player.motionZ);
            if (this.noclip.get_value(true) && this.checkHitBoxes()) {
                PacketFly.mc.player.noClip = true;
            }
        }
    });

    @EventHandler
    private final Listener<EventPacket.SendPacket> send_packet = new Listener<>(event -> {
        CPacketPlayer packet;
        if (event.get_packet() instanceof CPacketPlayer && !this.packets.remove(packet = (CPacketPlayer) event.get_packet())) {
            event.cancel();
        }
    });

    @EventHandler
    private final Listener<EventPlayerPushOutOfBlocks> push_out_of_blocks = new Listener<>(event ->
    {
        event.cancel();
    });

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> receive_packet = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketPlayerPosLook && !full_null_check()) {
            BlockPos pos;
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.get_packet();
            if (PacketFly.mc.player.isEntityAlive() && PacketFly.mc.world.isBlockLoaded(pos = new BlockPos(PacketFly.mc.player.posX, PacketFly.mc.player.posY, PacketFly.mc.player.posZ), false) && !(PacketFly.mc.currentScreen instanceof GuiDownloadTerrain) && this.clear_ids.get_value(true)) {
                this.teleportmap.remove(packet.getTeleportId());
            }
            if (this.set_yaw.get_value(true)) {
                packet.yaw = PacketFly.mc.player.rotationYaw;
                packet.pitch = PacketFly.mc.player.rotationPitch;
            }
            if (this.set_id.get_value(true)) {
                this.teleportID = packet.getTeleportId();
            }
        }
    });


    private boolean checkHitBoxes() {
        return !PacketFly.mc.world.getCollisionBoxes(PacketFly.mc.player, PacketFly.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty();
    }

    private boolean resetCounter(int counter) {
        if (++this.flightCounter >= counter) {
            this.flightCounter = 0;
            return true;
        }
        return false;
    }

    private double[] getMotion(double speed) {
        float moveForward = PacketFly.mc.player.movementInput.moveForward;
        float moveStrafe = PacketFly.mc.player.movementInput.moveStrafe;
        float rotationYaw = PacketFly.mc.player.prevRotationYaw + (PacketFly.mc.player.rotationYaw - PacketFly.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float) (moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float) (moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double) moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + (double) moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double) moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - (double) moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    private void sendPackets(double x, double y, double z, boolean teleport) {
        Vec3d vec = new Vec3d(x, y, z);
        Vec3d position = PacketFly.mc.player.getPositionVector().add(vec);
        Vec3d outOfBoundsVec = this.outOfBoundsVec(vec, position);
        this.packetSender(new CPacketPlayer.Position(position.x, position.y, position.z, PacketFly.mc.player.onGround));
        if (invalid_packet.get_value(true)) {
            this.packetSender(new CPacketPlayer.Position(outOfBoundsVec.x, outOfBoundsVec.y, outOfBoundsVec.z, PacketFly.mc.player.onGround));
        }
        if (set_pos.get_value(true)) {
            PacketFly.mc.player.setPosition(position.x, position.y, position.z);
        }
        this.teleportPacket(position, teleport);
    }

    private void teleportPacket(Vec3d pos, boolean shouldTeleport) {
        if (shouldTeleport) {
            PacketFly.mc.player.connection.sendPacket(new CPacketConfirmTeleport(++this.teleportID));
            this.teleportmap.put(this.teleportID, new IDtime(pos, new TimerUtil()));
        }
    }

    private Vec3d outOfBoundsVec(Vec3d offset, Vec3d position) {
        return position.add(0.0, 1337.0, 0.0);
    }

    private void packetSender(CPacketPlayer packet) {
        this.packets.add(packet);
        PacketFly.mc.player.connection.sendPacket(packet);
    }

    private void clean() {
        this.teleportmap.clear();
        this.flightCounter = 0;
        if (reset_id.get_value(true)) {
            this.teleportID = 0;
        }
        this.packets.clear();
    }

    public static class IDtime {
        private final Vec3d pos;
        private final TimerUtil timer;

        public IDtime(Vec3d pos, TimerUtil timer) {
            this.pos = pos;
            this.timer = timer;
            this.timer.reset();
        }

        public Vec3d getPos() {
            return this.pos;
        }

        public TimerUtil getTimer() {
            return this.timer;
        }
    }
}
