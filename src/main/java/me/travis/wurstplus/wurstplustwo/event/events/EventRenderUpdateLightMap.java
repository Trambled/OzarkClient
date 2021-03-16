package me.travis.wurstplus.wurstplustwo.event.events;

import me.travis.wurstplus.wurstplustwo.event.WurstplusEventCancellable;

public class EventRenderUpdateLightMap extends WurstplusEventCancellable {
    public float PartialTicks;

    public EventRenderUpdateLightMap(float p_PartialTicks)
    {
        super();
        PartialTicks = p_PartialTicks;
    }
}