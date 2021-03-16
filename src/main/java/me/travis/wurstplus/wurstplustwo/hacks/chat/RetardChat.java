package me.travis.wurstplus.wurstplustwo.hacks.chat;

import me.travis.wurstplus.wurstplustwo.event.events.EventPlayerSendChatMessage;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

//salhack
public class RetardChat extends WurstplusHack {

    public RetardChat() {
        super(WurstplusCategory.WURSTPLUS_CHAT);

        this.name = "Retard Chat";
        this.tag = "RetardChat";
        this.description = "makes you sound retarded";
    }
 
    @EventHandler
    private final Listener<EventPlayerSendChatMessage> OnSendChatMsg = new Listener<>(p_Event ->
    {
        if (p_Event.Message.startsWith("/"))
            return;

        String l_Message = "";
        
  
        boolean l_Flag = false;
                
        for (char l_Char : p_Event.Message.toCharArray())
        {
            String l_Val = String.valueOf(l_Char);
                    
            l_Message += l_Flag ? l_Val.toUpperCase() : l_Val.toLowerCase();
                    
            if (l_Char != ' ') {
                l_Flag = !l_Flag;
			}
        }
        
        p_Event.cancel();
        mc.getConnection().sendPacket(new CPacketChatMessage(l_Message));
    });
}
