package me.trambled.ozark.ozarkclient.manager;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.Eventbus;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.event.events.EventRotation;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import me.trambled.ozark.ozarkclient.util.RotationUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Comparator;
import java.util.concurrent.LinkedBlockingQueue;

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

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (!Ozark.get_module_manager().get_module_with_tag("AutoCrystal").is_active()) {
            rotationQueue.clear();
            return;
        }
        rotationQueue.stream().sorted(Comparator.comparing(rotation -> rotation.rotationPriority.getPriority()));
        if (currentRotation != null)
            currentRotation = null;

        if (!rotationQueue.isEmpty()) {
            currentRotation = rotationQueue.poll();
            currentRotation.updateRotations();
        }

        tick++;
    }

    @EventHandler
    private Listener<EventRotation> rotation = new Listener<>(event -> {
        try {
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
        } catch (Exception ignored) {

        }
    });

    @EventHandler
    private Listener<EventPacket.SendPacket> player_move = new Listener<>(event -> {
        if (currentRotation != null && !rotationQueue.isEmpty() && event.get_packet() instanceof CPacketPlayer) {
            if (RotationUtil.is_rotating((CPacketPlayer) event.get_packet()))
                serverRotation = new RotationUtil.Rotation(((CPacketPlayer) event.get_packet()).yaw, ((CPacketPlayer) event.get_packet()).pitch, RotationUtil.RotationMode.Packet, RotationUtil.RotationPriority.Lowest);
        }
    });

    public void resetTicks() {
        tick = 0;
    }
}
