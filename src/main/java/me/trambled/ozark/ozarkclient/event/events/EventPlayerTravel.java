package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;

public class EventPlayerTravel extends Event {
    
    public float Strafe;
    public float Vertical;
    public float Forward;

    public EventPlayerTravel(float p_Strafe, float p_Vertical, float p_Forward) {
        Strafe = p_Strafe;
        Vertical = p_Vertical;
        Forward = p_Forward;
    }

}