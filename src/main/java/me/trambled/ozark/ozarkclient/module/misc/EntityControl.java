package me.trambled.ozark.ozarkclient.module.misc;

import me.trambled.ozark.ozarkclient.event.events.EventHorseSaddled;
import me.trambled.ozark.ozarkclient.event.events.EventSteerEntity;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

public class EntityControl extends Module
{
    public EntityControl() {
        super(Category.MISC);
        this.name = "EntityControl";
        this.tag = "EntityControl";
        this.description = "Controls entidies without saddles.";
    }

    @EventHandler
    private final Listener<EventSteerEntity> OnSteerEntity = new Listener<>(p_Event ->
    {
        p_Event.cancel();
    });

    @EventHandler
    private final Listener<EventHorseSaddled> OnHorseSaddled = new Listener<>(p_Event ->
    {
        p_Event.cancel();
    });
   
}
