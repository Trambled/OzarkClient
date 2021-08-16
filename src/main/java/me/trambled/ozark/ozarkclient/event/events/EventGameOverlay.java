package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventGameOverlay extends Event {

    public float partial_ticks;
    private final ScaledResolution scaled_resolution;

    public EventGameOverlay(float partial_ticks, ScaledResolution scaled_resolution) {
        
        this.partial_ticks = partial_ticks;
        this.scaled_resolution = scaled_resolution;

    }

    public ScaledResolution get_scaled_resoltion() {
        return scaled_resolution;
    }
    
}