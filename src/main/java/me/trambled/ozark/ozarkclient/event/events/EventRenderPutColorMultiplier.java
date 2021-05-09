package me.trambled.ozark.ozarkclient.event.events;


import me.trambled.ozark.ozarkclient.event.Event;

public class EventRenderPutColorMultiplier extends Event {
    private float _opacity;

    public void setOpacity(float opacity) {
        _opacity = opacity;
    }

    public float getOpacity() {
        return _opacity;
    }
}