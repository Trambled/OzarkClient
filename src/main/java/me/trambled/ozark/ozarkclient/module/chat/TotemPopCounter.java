package me.trambled.ozark.ozarkclient.module.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.util.FriendUtil;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;

import java.util.HashMap;


public class TotemPopCounter extends Module {
    
    public TotemPopCounter() {
		super(Category.CHAT);

		this.name        = "Totem Pop Counter";
		this.tag         = "TotemPopCounter";
		this.description = "Automatically says who ur EZZZZZZZZZZING";

    }

    Setting mode = create("Mode", "Mode", "Normal", combobox("Normal", "Lempity"));


    public static final HashMap<String, Integer> totem_pop_counter = new HashMap<String, Integer>();
    
    public static ChatFormatting red = ChatFormatting.DARK_RED;
    public static ChatFormatting green = ChatFormatting.GREEN;
    public static ChatFormatting gold = ChatFormatting.GOLD;
    public static ChatFormatting grey = ChatFormatting.GRAY;
    public static ChatFormatting bold = ChatFormatting.BOLD;
    public static ChatFormatting black = ChatFormatting.BLACK;
    public static ChatFormatting white = ChatFormatting.WHITE;
    public static ChatFormatting reset = ChatFormatting.RESET;
    public static ChatFormatting aqua = ChatFormatting.AQUA;

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

                if (entity == mc.player) return;

                if (FriendUtil.isFriend(entity.getName())) {
                    MessageUtil.send_client_message( red + "" + bold + "[TotemPop] " + reset + aqua + entity.getName() + " popped " + red + count + " totems");
                } else {
                    MessageUtil.send_client_message( red + "" + bold + "[TotemPop] " + reset + white + entity.getName() + " popped " + red + count + " totems");
                }

            }

        }

    });

    @Override
	public void update() {
        
        for (EntityPlayer player : mc.world.playerEntities) {

            if (!totem_pop_counter.containsKey(player.getName())) continue;

            if (player.isDead || player.getHealth() <= 0) {

                int count = totem_pop_counter.get(player.getName());

                totem_pop_counter.remove(player.getName());

                if (player == mc.player) continue;

                if (mode.in("Lempity")) {
                    if (FriendUtil.isFriend(player.getName())) {
                        MessageUtil.send_client_message( red + "" + bold + " TotemPop " + reset + grey + " > " + reset + "dude, " + bold + green + player.getName() + reset + " has popped " + bold + count + reset + " totems. so dog water but idk there a homie");
                    } else {
                        MessageUtil.send_client_message( red + "" + bold + " TotemPop " + reset + grey + " > " + reset + "dude, " + bold + red + player.getName() + reset + " has popped " + bold + count + reset + " totems. Stupid fucking retard");
                    }
                } else {
                    if (FriendUtil.isFriend(player.getName())) {
                        MessageUtil.send_client_message( red + "" + bold + " TotemPop " + reset + grey + " > " + reset + "" + bold + aqua + player.getName() + reset + " died after popping " + bold + count + reset + " totems.");
                    } else {
                        MessageUtil.send_client_message( red + "" + bold + " TotemPop " + reset + grey + " > " + reset + "" + bold + red + player.getName() + reset + " died after popping " + bold + count + reset + " totems");
                    }
                }

            }

        }

	}

}
