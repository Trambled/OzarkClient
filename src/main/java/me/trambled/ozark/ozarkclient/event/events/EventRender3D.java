package me.trambled.ozark.ozarkclient.event.events;


import me.trambled.ozark.ozarkclient.event.Event;

public class EventRender3D
extends Event {
    private final float partialTicks;

    public EventRender3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}