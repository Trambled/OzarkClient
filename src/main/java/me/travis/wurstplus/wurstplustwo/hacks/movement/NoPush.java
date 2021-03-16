package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.event.events.EventPlayerPushOutOfBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

//from salhack basically
public class NoPush extends WurstplusHack {
    
    public NoPush() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);

		this.name        = "NoPush";
		this.tag         = "NoPush";
		this.description = "prevents you getting raped by being forced to get out of a block";
        this.toggle_message = false;
    }
	
    WurstplusSetting burrow_only = create("Burrow Only", "BurrowOnly", false);

    @EventHandler
    private final Listener<EventPlayerPushOutOfBlocks> push_out_of_blocks = new Listener<>(p_Event ->
    {
		if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) && burrow_only.get_value(true))	{
			p_Event.cancel();
		}
		
		if (!burrow_only.get_value(true)) {
			p_Event.cancel();
		}
    });
}