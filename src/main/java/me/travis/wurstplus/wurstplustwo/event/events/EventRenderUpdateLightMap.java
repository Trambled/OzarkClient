package me.travis.wurstplus.wurstplustwo.event.events;

import me.travis.wurstplus.wurstplustwo.event.MinecraftEvent;

public class EventRenderUpdateLightMap extends MinecraftEvent {
    public float PartialTicks;

    public EventRenderUpdateLightMap(float p_PartialTicks)
    {
        super();
        PartialTicks = p_PartialTicks;
    }
}