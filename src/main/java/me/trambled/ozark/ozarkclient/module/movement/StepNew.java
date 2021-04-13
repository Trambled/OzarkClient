package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;


// huge thanks to rte >:^)


public class StepNew extends Module {

    public StepNew() {
        super(Category.MOVEMENT);
                 this.name = "StepNew";
                 this.tag = "Step";
                 this.description = "FINALLY WORKED AFTER 1000 TRIES HOLY SHIT";

    }

    Setting mode = create("Mode", "StepMode", "Normal", combobox("Normal", "Vanilla", "Reverse"));

    @Override
    public void update() {
        if (mode.in("Vanilla")) mc.player.stepHeight = 2f;
        else mc.player.stepHeight = 0.5f;

        if (!mc.player.collidedHorizontally) return;
        if (!mc.player.onGround || mc.player.isOnLadder() || mc.player.isInWater() || mc.player.isInLava() || mc.player.movementInput.jump || mc.player.noClip)
            return;
        if (mc.player.moveForward == 0 && mc.player.moveStrafing == 0) return;

        final double n = get_n_normal();

        if (n < 0 || n > 2) return;

        if (mode.in("Normal")) {
            if (n == 2.0) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.78, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.63, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.51, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.9, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.21, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.45, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.43, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 2.0, mc.player.posZ);
            }
            if (n == 1.5) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805212, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.24918707874468, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.1707870772188, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
            }
            if (n == 1.0) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805212, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
            }

        }






    }

    @Override
    protected void disable() {
        mc.player.stepHeight = 0.5f;

    }

    public double get_n_normal() {

        mc.player.stepHeight = 0.5f;

        double max_y = -1;

        final AxisAlignedBB grow = mc.player.getEntityBoundingBox().offset(0, 0.05, 0).grow(0.05);

        if (!mc.world.getCollisionBoxes(mc.player, grow.offset(0, 2, 0)).isEmpty()) return 100;

        for (final AxisAlignedBB aabb : mc.world.getCollisionBoxes(mc.player, grow)) {

            if (aabb.maxY > max_y) {
                max_y = aabb.maxY;
            }
        }

        return max_y - mc.player.posY;

    }
}
