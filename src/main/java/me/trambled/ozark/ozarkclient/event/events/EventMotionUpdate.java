package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;

public
class EventMotionUpdate extends Event {

    public int stage;

    public
    EventMotionUpdate ( int stage ) {
        super ( );
        this.stage = stage;
    }

}