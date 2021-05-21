package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

//salhack
public class RetardChat extends Module {

    public RetardChat() {
        super(Category.CHAT);

        this.name = "RetardChat";
        this.tag = "RetardChat";
        this.description = "Makes you sound retarded.";
    }
 
    @EventHandler
    private final Listener<EventPacket.SendPacket> on_chat_message = new Listener<>(event ->
    {

        if (!(event.get_packet() instanceof CPacketChatMessage)) {
            return;
        }

        String message = ((CPacketChatMessage) event.get_packet()).getMessage();

        if (message.startsWith("/"))
            return;

        String l_Message = "";
        
  
        boolean l_Flag = false;
                
        for (char l_Char : message.toCharArray())
        {
            String l_Val = String.valueOf(l_Char);
                    
            l_Message += l_Flag ? l_Val.toUpperCase() : l_Val.toLowerCase();
                    
            if (l_Char != ' ') {
                l_Flag = !l_Flag;
			}
        }

        ((CPacketChatMessage) event.get_packet()).message = l_Message;
    });
}
