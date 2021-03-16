package me.travis.wurstplus.wurstplustwo.event.events;


import me.travis.wurstplus.wurstplustwo.event.WurstplusEventCancellable;

public class EventRenderPutColorMultiplier extends WurstplusEventCancellable {
    private float _opacity;

    public void setOpacity(float opacity) {
        _opacity = opacity;
    }

    public float getOpacity() {
        return _opacity;
    }
}