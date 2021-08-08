package me.trambled.ozark.ozarkclient.module.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.misc.FakePlayer;
import me.trambled.ozark.ozarkclient.util.player.EntityUtil;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.world.MathUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
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

		this.name        = "TotemPopCounter";
		this.tag         = "TotemPopCounter";
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

    Setting mode = create("Mode", "Mode", "Normal", combobox("Normal", "GayNigger"));
    Setting stack = create("Stack", "Stack", false);
    Setting chams = create("Chams", "Chams", false);
    public Setting r = create("R", "popChamsR", 255, 0, 255);
    public Setting g = create("G", "popChamsG", 255, 0, 255);
    public Setting b = create("B", "popChamsB", 255, 0, 255);
    public Setting a = create("A", "popChamsA", 100, 0, 255);

    public static final HashMap<String, Integer> totem_pop_counter = new HashMap<String, Integer>();
    public static ConcurrentHashMap<Integer, Integer> pops = new ConcurrentHashMap<>();
    
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
    private final Listener<EventPacket.ReceivePacket> packet_event = new Listener<>(event -> {


        if (event.get_packet() instanceof SPacketEntityStatus) {

            SPacketEntityStatus packet = (SPacketEntityStatus) event.get_packet();

            if (packet.getOpCode() == 35) {

                Entity entity = packet.getEntity(mc.world);

                int count = 1;


                if (totem_pop_counter.containsKey(entity.getName())) {
                    count = totem_pop_counter.get(entity.getName());
                    totem_pop_counter.put(entity.getName(), ++count);
                } else {
                    totem_pop_counter.put(entity.getName(), count);
                }


                if (FriendUtil.isFriend(entity.getName())) {
                    MessageUtil.send_client_message(aqua + entity.getName() + " popped " + red + count + " totems");
                } else {

                    MessageUtil.send_client_message(white + entity.getName() + " popped " + red + count + " totems");

                }
                if (chams.get_value(true)) {
                    if (mc.player.getDistanceSq(entity.getPosition()) > MathUtil.square(20)) {
                        Color color = EntityUtil.getColor(packet.getEntity(mc.world), r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), false);
                        Entity ee = packet.getEntity(mc.world);
                        ArrayList<Integer> idList = new ArrayList<>();
                        for (Entity e : mc.world.loadedEntityList) {
                            idList.add(e.getEntityId());
                        }               //this should be the player game profile but entity doesnt have gameprofile
                        EntityOtherPlayerMP popCham = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
                        popCham.copyLocationAndAnglesFrom(ee);
                        popCham.rotationYawHead = ee.getRotationYawHead();
                        popCham.rotationYaw = ee.rotationYaw;
                        popCham.rotationPitch = ee.rotationPitch;
                        popCham.setGameType(GameType.CREATIVE);
                        popCham.setHealth(20);
                        for (int i = 0; i > -10000; i--) {
                            if (!idList.contains(i)) {
                                mc.world.addEntityToWorld(i, popCham);
                                pops.put(i, color.getAlpha());
                                break;
                            }
                        }
                    }
                }
            }
        }

    });

    @Override
	public void update() {

        if (full_null_check()) return;
        if (mc.world.playerEntities == null) return; // for some reason i get crashes because of dis??
        for (EntityPlayer player : mc.world.playerEntities) {

            if (!totem_pop_counter.containsKey(player.getName())) continue;

            if (player.isDead || player.getHealth() <= 0) {

                int count = totem_pop_counter.get(player.getName());

                totem_pop_counter.remove(player.getName());

                if (player == mc.player) continue;

                if (mode.in("GayNigger")) {
                    if (FriendUtil.isFriend(player.getName())) {
                        client_message("dude, " + bold + green + player.getName() + reset + " has popped " + bold + count + reset + " totems. so dog water but idk there a homie");
                    } else {
                        client_message("dude, " + bold + red + player.getName() + reset + " has popped " + bold + count + reset + " totems. Stupid fucking retard");
                    }
                } else {
                    if (FriendUtil.isFriend(player.getName())) {
                        client_message("" + bold + aqua + player.getName() + reset + " died after popping " + bold + count + reset + " totems.");
                    } else {
                        client_message("" + bold + red + player.getName() + reset + " died after popping " + bold + count + reset + " totems");
                    }
                }

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
