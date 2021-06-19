package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.event.events.EventPlayerPushOutOfBlocks;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

//from salhack basically
public class NoPush extends Module {
    
    public NoPush() {
        super(Category.MOVEMENT);

		this.name        = "NoPush";
		this.tag         = "NoPush";
		this.description = "Prevents you getting raped by being forced to get out of a block.";
    }
	
    Setting burrow_only = create("Burrow Only", "BurrowOnly", false);

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
