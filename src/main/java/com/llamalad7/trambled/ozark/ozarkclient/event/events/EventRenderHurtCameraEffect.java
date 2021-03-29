package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;

public class EventRenderHurtCameraEffect extends Event
{
    public float Ticks;
    
    public EventRenderHurtCameraEffect(float p_Ticks)
    {
        super();
        Ticks = p_Ticks;
    }
}