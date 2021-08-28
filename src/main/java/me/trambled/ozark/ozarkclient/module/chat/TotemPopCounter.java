package me.trambled.ozark.ozarkclient.module.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.ozarkclient.event.events.EventEntityRemoved;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.event.events.EventTotemPop;
import me.trambled.ozark.ozarkclient.manager.TotempopManager;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.player.EntityUtil;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import me.trambled.ozark.ozarkclient.util.world.TimerUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.world.GameType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public class TotemPopCounter extends Module {
    public static TotemPopCounter INSTANCE = new TotemPopCounter();

    public TotemPopCounter() {
        super(Category.CHAT);

        this.name = "TotemPopCounter";
        this.tag = "TotemPopCounter";
        this.description = "Automatically says who ur EZZZZZZZZZZING.";

    }

    public static TotemPopCounter getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new TotemPopCounter();
        }
        return INSTANCE;
    }

    public void setInstance() {
        INSTANCE = this;
    }

    Setting stack = create("Stack", "Stack", false);


    public static ChatFormatting red = ChatFormatting.DARK_RED;
    public static ChatFormatting green = ChatFormatting.GREEN;
    public static ChatFormatting gold = ChatFormatting.GOLD;
    public static ChatFormatting grey = ChatFormatting.GRAY;
    public static ChatFormatting bold = ChatFormatting.BOLD;
    public static ChatFormatting black = ChatFormatting.BLACK;
    public static ChatFormatting white = ChatFormatting.WHITE;
    public static ChatFormatting reset = ChatFormatting.RESET;
    public static ChatFormatting aqua = ChatFormatting.AQUA;
    int value = 0;


    @EventHandler
    private final Listener<EventTotemPop> packet_event = new Listener<>(event -> {

        Entity entity = event.getEntity();
        int count = TotempopManager.getPops(event.getEntity().getName());

                if (entity == mc.player) return;

                if (FriendUtil.isFriend(entity.getName())) {
                    client_message(aqua + entity.getName() + " popped " + red + count + " totems");
                } else {

                    client_message(white + entity.getName() + " popped " + red + count + " totems");

                }
    });


    @Override
    public void update() {
        if (full_null_check()) return;
        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                if (e == mc.player) continue;
                final EntityLivingBase living = (EntityLivingBase) e;
                if (TotempopManager.totemMap.containsKey(e) && living.getHealth() <= 0) {
                    final String message = ChatFormatting.DARK_RED + living.getName() + ChatFormatting.RESET + " Died after " + ChatFormatting.LIGHT_PURPLE + TotempopManager.getPops(living) + ChatFormatting.RESET
                            + (TotempopManager.getPops(living) == 1 ? " totem" : " totems");
                    client_message(message);
                }
                TotempopManager.totemMap.remove(living);
            }
        }
    }


    public void client_message(String message) {
        if (stack.get_value(true)) {
            MessageUtil.send_client_message_simple(message);
        } else {
            MessageUtil.send_client_message(message);
        }
    }
}
