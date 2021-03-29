package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;

public class EventRenderUpdateLightMap extends Event {
    public float PartialTicks;

    public EventRenderUpdateLightMap(float p_PartialTicks)
    {
        super();
        PartialTicks = p_PartialTicks;
    }
}