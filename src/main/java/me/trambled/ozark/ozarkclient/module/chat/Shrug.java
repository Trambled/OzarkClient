package me.trambled.ozark.ozarkclient.module.chat;


import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.events.EventPlayerSendChatMessage;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

public final
class Shrug extends Module {

    @EventHandler
    private final Listener < EventPlayerSendChatMessage > on_chat = new Listener <> ( event ->
    {
        if ( event.message.startsWith ( "/" ) )
            return;

        String message = event.message;

        message = message.replace ( "<shrug>" , "\u00Af\\_(\u30C4)_/\u00AF" );

        event.cancel ( );
        mc.getConnection ( ).sendPacket ( new CPacketChatMessage ( message ) );
    } );

    public
    Shrug ( ) {
        super ( Category.CHAT );

        this.name = "Shrug";
        this.tag = "Shrug";
        this.description = "replaces <shrug> in chat with \u00Af\\_(\u30C4)_/\u00AF";
    }

    @Override
    public
    void update ( ) {
        if ( Ozark.get_module_manager ( ).get_module_with_tag ( "RetardChat" ).is_active ( ) ) {
            Ozark.get_module_manager ( ).get_module_with_tag ( "RetardChat" ).set_disable ( );
            MessageUtil.send_client_message ( "Retardchat conflicts with shrug" );
        }
    }

}