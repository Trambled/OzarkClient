package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;
import net.minecraft.util.EnumHand;

public class EventSwing extends Event {
    
    public EnumHand hand;

    public EventSwing(EnumHand hand) {
        super();
        this.hand = hand;
    }

}