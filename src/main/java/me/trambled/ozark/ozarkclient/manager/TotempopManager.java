package me.trambled.ozark.ozarkclient.manager;


import me.trambled.ozark.ozarkclient.event.Event;
import me.trambled.ozark.ozarkclient.event.Eventbus;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.event.events.EventTotemPop;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;

public class TotempopManager implements Listenable {
    public static ConcurrentHashMap<EntityLivingBase, Integer> totemMap;

    public TotempopManager() {
        totemMap = new ConcurrentHashMap<>();
        Eventbus.EVENT_BUS.subscribe(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static void update() {
        for(EntityLivingBase entity : totemMap.keySet()) {
            if(!mc.world.loadedEntityList.contains(entity)) {
                totemMap.remove(entity);
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.player == null || mc.world == null) {
            totemMap.clear();
            return;
        }
        update();
    }

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> packetRecieveListener = new Listener<>(event -> {
        if (mc.player == null || mc.world == null) return;

        if (event.get_packet() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.get_packet();
            EntityLivingBase entity = (EntityLivingBase) packet.getEntity(mc.world);

            if (packet.getOpCode() == 35) {
                if(totemMap.containsKey(entity)) {
                    int times = totemMap.get(entity) + 1;
                    Eventbus.EVENT_BUS.post(new EventTotemPop(entity, times));
                    totemMap.remove(entity);
                    totemMap.put(entity, times);
                } else {
                    Eventbus.EVENT_BUS.post(new EventTotemPop(entity, 1));
                    totemMap.put(entity, 1);
                }
            }
        }
    });

    public static int getPops(EntityLivingBase entity) {
        //update();
        if(totemMap.containsKey(entity)) {
            return totemMap.get(entity);
        }
        return 0;
    }

    public static int getPops(String name) {
        //update();
        boolean flag = false;
        EntityLivingBase e = null;
        for(Entity entity : mc.world.loadedEntityList) {
            if(entity instanceof EntityLivingBase) {
                if(entity.getName().equals(name)) {
                    flag = true;
                    e = (EntityLivingBase) entity;
                    break;
                }
            }
        }
        if(flag) {
            if(totemMap.containsKey(e)) {
                return totemMap.get(e);
            }
        }
        return 0;
    }
}
