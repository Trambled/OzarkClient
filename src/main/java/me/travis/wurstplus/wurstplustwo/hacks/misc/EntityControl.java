package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.event.events.EventSteerEntity;
import me.travis.wurstplus.wurstplustwo.event.events.EventHorseSaddled;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

public class EntityControl extends WurstplusHack
{
    public EntityControl() {
        super(WurstplusCategory.WURSTPLUS_MISC);
        this.name = "EntityControl";
        this.tag = "EntityControl";
        this.description = "cum";
    }

    @EventHandler
    private Listener<EventSteerEntity> OnSteerEntity = new Listener<>(p_Event ->
    {
        p_Event.cancel();
    });

    @EventHandler
    private Listener<EventHorseSaddled> OnHorseSaddled = new Listener<>(p_Event ->
    {
        p_Event.cancel();
    });
   
}
