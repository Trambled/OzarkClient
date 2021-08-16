package me.trambled.ozark.ozarkclient.manager;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.Eventbus;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.event.events.EventRotation;
import me.trambled.ozark.ozarkclient.util.player.RotationUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;

import java.util.Comparator;
import java.util.concurrent.LinkedBlockingQueue;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;

/**
 * @author linustouchtips
 * @since 01/30/2021
 */

public class RotationManager implements Listenable {
    public RotationManager() {
        MinecraftForge.EVENT_BUS.register(this);
        Eventbus.EVENT_BUS.subscribe(this);
    }

    public LinkedBlockingQueue<RotationUtil.Rotation> rotationQueue = new LinkedBlockingQueue<>();
    public RotationUtil.Rotation serverRotation = null;
    public RotationUtil.Rotation currentRotation = null;

    public float yawleftOver = 0;
    public float pitchleftOver = 0;

    public int tick = 5;

    public void update() {
        if (!Ozark.get_module_manager().get_module_with_tag("AutoCrystal").is_active()) {
            rotationQueue.clear();
            return;
        }
        if (Ozark.get_setting_manager().get_setting_with_tag("AutoCrystal", "CaQueue").get_value(true)) {
            rotationQueue.stream().sorted(Comparator.comparing(rotation -> rotation.rotationPriority.getPriority()));
            if (currentRotation != null)
                currentRotation = null;

            if (!rotationQueue.isEmpty()) {
                setCurrentRotation(rotationQueue.poll());
                if (Ozark.get_setting_manager().get_setting_with_tag("AutoCrystal", "CaAccurate").get_value(true)) {
                    rotationQueue.clear();
                }
            }
        }

        tick++;
    }

    @EventHandler
    private final Listener<EventRotation> rotation = new Listener<>( event -> {
        if (currentRotation != null && currentRotation.mode.equals(RotationUtil.RotationMode.Packet)) {
            event.cancel();

            if (tick == 1) {
                event.setYaw(currentRotation.yaw + yawleftOver);
                event.setPitch(currentRotation.pitch + pitchleftOver);
            }

            else {
                event.setYaw(currentRotation.yaw);
                event.setPitch(currentRotation.pitch);
            }
        }
    });

    @EventHandler
    private final Listener<EventPacket.SendPacket> player_move = new Listener<>( event -> {
        if (currentRotation != null && !rotationQueue.isEmpty() && event.get_packet() instanceof CPacketPlayer) {
            if (RotationUtil.is_rotating((CPacketPlayer) event.get_packet()))
                serverRotation = new RotationUtil.Rotation(((CPacketPlayer) event.get_packet()).yaw, ((CPacketPlayer) event.get_packet()).pitch, RotationUtil.RotationMode.Packet, RotationUtil.RotationPriority.Lowest);
        }
    });

    public void resetTicks() {
        tick = 0;
    }

    public void setCurrentRotation(RotationUtil.Rotation rotation) {
        currentRotation = rotation;
        currentRotation.updateRotations();
    }

    public void restoreDefaultRotation() {
        if (currentRotation != null) {
            currentRotation.restoreRotation();
        }
        CPacketPlayer packet = new CPacketPlayer(mc.player.onGround);
        packet.yaw = mc.player.rotationYaw;
        packet.pitch = mc.player.rotationPitch;
        mc.player.connection.sendPacket(packet);
    }

}
