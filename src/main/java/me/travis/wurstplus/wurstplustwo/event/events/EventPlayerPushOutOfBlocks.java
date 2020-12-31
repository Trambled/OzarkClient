package me.travis.wurstplus.wurstplustwo.event.events;

import me.travis.wurstplus.wurstplustwo.event.MinecraftEvent;

public class EventPlayerPushOutOfBlocks extends MinecraftEvent
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
