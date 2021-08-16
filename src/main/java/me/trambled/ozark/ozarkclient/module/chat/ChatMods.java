package me.trambled.ozark.ozarkclient.module.chat;


import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class ChatMods extends Module {

    public ChatMods() {
        super(Category.CHAT);

        this.name = "ChatModifications";
        this.tag = "ChatModifications";
        this.description = "This breaks things.";
    }

    Setting timestamps = create("Timestamps", "ChatModsTimeStamps", true);
    Setting dateformat = create("Date Format", "ChatModsDateFormat", "12HR", combobox("24HR", "12HR"));
    Setting name_highlight = create("Name BlockHighlight", "ChatModsNameHighlight", false);
    Setting infinite_chat = create("Infinite Chat", "ChatModsInfiniteChat", true);
    Setting smooth = create("Smooth", "Smooth", true);
    Setting clear = create("Clear", "Clear", false);
    Setting custom_font = create("Custom Font", "customfont", true);
    Setting rainbow = create("Rainbow Ozark", "rainbowozark", true);


    @EventHandler
    private final Listener<EventPacket.ReceivePacket> packet_event = new Listener<>(event -> {

        if (event.get_packet() instanceof SPacketChat) {

            final SPacketChat packet = (SPacketChat) event.get_packet();

            if (packet.getChatComponent() instanceof TextComponentString) {
                final TextComponentString component = (TextComponentString) packet.getChatComponent();

                if (timestamps.get_value(true)) {

                    String date = "";

                    if (dateformat.in("12HR")) {
                        date = new SimpleDateFormat("h:mm a").format(new Date());
                    }

                    if (dateformat.in("24HR")) {
                        date = new SimpleDateFormat("k:mm").format(new Date());

                    }

                    component.text = "\2477[" + date + "]\247r " + component.text;

                }

                String text = component.getFormattedText();

                if (text.contains("combat for")) return;

                if (name_highlight.get_value(true) && mc.player != null) {

                    if (text.toLowerCase().contains(mc.player.getName().toLowerCase())) {

                        text = text.replaceAll("(?i)" + mc.player.getName(), ChatFormatting.GOLD + mc.player.getName() + ChatFormatting.RESET);

                    }

                }

                event.cancel();

                MessageUtil.client_message(text);

            }
        }
    });
}