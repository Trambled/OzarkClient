package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;
import net.minecraft.entity.Entity;

public class EventEntityRemoved extends Event {
    
    private final Entity entity;

    public EventEntityRemoved(Entity entity) {
        this.entity = entity;
    }

    public Entity get_entity() {
        return this.entity;
    }

}