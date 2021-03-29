package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;

public class EventPlayerJump extends Event {
    
    public double motion_x;
    public double motion_y;

    public EventPlayerJump(double motion_x, double motion_y) {
        super();

        this.motion_x = motion_x;
        this.motion_y = motion_y;
    }

}