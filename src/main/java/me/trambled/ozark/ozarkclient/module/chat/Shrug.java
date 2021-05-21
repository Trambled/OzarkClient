package me.trambled.ozark.ozarkclient.module.chat;


import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

public final class Shrug extends Module {

    public Shrug() {
        super(Category.CHAT);

        this.name = "Shrug";
        this.tag = "Shrug";
        this.description = "Replaces <shrug> in chat with \u00Af\\_(\u30C4)_/\u00AF.";
    }

    @EventHandler
    private final Listener<EventPacket.SendPacket> on_chat = new Listener<>(event ->
    {
        if (!(event.get_packet() instanceof CPacketChatMessage)) {
            return;
        }

        String packet_message = ((CPacketChatMessage) event.get_packet()).getMessage();

        if (packet_message.startsWith("/"))
            return;

        String message = packet_message.replace("<shrug>", "\u00Af\\_(\u30C4)_/\u00AF");

        ((CPacketChatMessage) event.get_packet()).message = message;
    });

}