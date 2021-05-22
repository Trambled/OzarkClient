package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;

/**
 * @author linustouchtips
 * @since 01/01/2020
 * only used for overriding vanilla entityplayersp packet sending
 */

public class EventRotation extends Event {

    float yaw;
    float pitch;
    
    public EventRotation() {}

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

}
