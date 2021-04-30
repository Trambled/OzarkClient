package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;

public class EventPlayerSendChatMessage extends Event
{
    public String message;

    public EventPlayerSendChatMessage(String p_Message)
    {
        super();
        
        message = p_Message;
    }

}
