package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.EzMessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AutoEz extends Module {
    
    public AutoEz() {
        super(Category.CHAT);

        this.name = "AutoEz";
        this.tag = "AutoEz";
        this.description = "You just got niggered by Ozark. (da song reminds me of csgo hvh) -kambing";
    }

    int delay_count = 0;

    Setting discord = create("Discord", "EzDiscord", false);
    Setting custom = create("Custom", "EzCustom", false);

    private static final ConcurrentHashMap targeted_players = new ConcurrentHashMap();

    @EventHandler
    private final Listener<EventPacket.SendPacket> send_listener = new Listener<>( event -> {

        if (mc.player == null) return;

        if (event.get_packet() instanceof CPacketUseEntity) {
            CPacketUseEntity cPacketUseEntity = (CPacketUseEntity) event.get_packet();
            if (cPacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK)) {
                Entity target_entity = cPacketUseEntity.getEntityFromWorld(mc.world);
                if (target_entity instanceof EntityPlayer) {
                    add_target(target_entity.getName());
                    }
                }
            }

    });

    @EventHandler
    private final Listener<LivingDeathEvent> living_death_listener = new Listener<>( event -> {

        if (mc.player == null) return;

        EntityLivingBase e = event.getEntityLiving();
        if (e == null) return;

        if (e instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e;

            if (player.getHealth() <= 0) {
                if (targeted_players.containsKey(player.getName())) {
                    announce(player.getName());
                }
            }
        }

    });

    @Override
    public void update() {

        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (player.getHealth() <= 0) {
                    if (targeted_players.containsKey(player.getName())) {
                        announce(player.getName());
                        }
                    }
                }
            }


        targeted_players.forEach((name, timeout) -> {
            if ((int)timeout <= 0) {
                targeted_players.remove(name);
            } else {
                targeted_players.put(name, (int)timeout - 1);
            }

        });

        delay_count++;

    }

    public void announce(String name) {
        if (delay_count < 150) {
            return;
        }
        delay_count = 0;
        targeted_players.remove(name);
        String message = "";
        if (custom.get_value(true)) {
            message += EzMessageUtil.get_message().replace("[", "").replace("]", "");
        } else {
            message += "You just got niggered by OzarkClient";
        }
        if (discord.get_value(true)) {
            message += "discord.gg/SUt67ygdY7"; //nigga what
        }
        mc.player.connection.sendPacket(new CPacketChatMessage(message));
    }

    public static void add_target(String name) {
        if (!Objects.equals(name, mc.player.getName())) {
            targeted_players.put(name, 20);
        }
    }

}