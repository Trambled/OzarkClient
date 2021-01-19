package me.travis.wurstplus.wurstplustwo.event.events;

import me.travis.wurstplus.wurstplustwo.event.MinecraftEvent;

public class EventPlayerSendChatMessage extends MinecraftEvent
{
    public String Message;

    public EventPlayerSendChatMessage(String p_Message)
    {
        super();
        
        Message = p_Message;
    }

}
