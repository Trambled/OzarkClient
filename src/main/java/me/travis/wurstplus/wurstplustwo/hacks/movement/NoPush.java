package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.event.events.EventPlayerPushOutOfBlocks;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

public class NoPush extends WurstplusHack {
    
    public NoPush() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);

		this.name        = "NoPush";
		this.tag         = "NoPush";
		this.description = "prevents you getting raped by being stuck in a block";
                this.toggle_message = false;
    }

    @EventHandler
    private Listener<EventPlayerPushOutOfBlocks> PushOutOfBlocks = new Listener<>(p_Event ->
    {
        p_Event.cancel();
    });
}