package me.travis.wurstplus.wurstplustwo.event.events;

import me.travis.wurstplus.wurstplustwo.event.WurstplusEventCancellable;

public class EventPlayerSendChatMessage extends WurstplusEventCancellable
{
    public String Message;

    public EventPlayerSendChatMessage(String p_Message)
    {
        super();
        
        Message = p_Message;
    }

}
