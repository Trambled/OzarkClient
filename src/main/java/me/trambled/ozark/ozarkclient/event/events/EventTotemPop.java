package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;
import net.minecraft.entity.EntityLivingBase;

public class EventTotemPop extends Event {
    private final EntityLivingBase entity;
    private final int times;

    public EventTotemPop(EntityLivingBase entity, int times) {
        this.entity = entity;
        this.times = times;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public int getTimes() {
        return times;
    }
}