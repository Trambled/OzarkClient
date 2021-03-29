package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;

public class EventPlayerPushOutOfBlocks extends Event
{
    public double X, Y, Z;
    
    public EventPlayerPushOutOfBlocks(double p_X, double p_Y, double p_Z)
    {
        super();
        
        X = p_X;
        Y = p_Y;
        Z = p_Z;
    }
}
