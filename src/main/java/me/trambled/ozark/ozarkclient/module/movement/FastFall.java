package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

//gamesense
public class FastFall extends Module
{
    public FastFall() {
        super(Category.MOVEMENT);
        this.name = "FastFall";
        this.tag = "FastFall";
        this.description = "Increases fall speed.";
    }
	
	Setting height = create("Height", "Height", 10, 0, 10);
	Setting strength = create("Strength", "Strength", 1f, 0f, 10f);
	
	private boolean inLiquid;
	private boolean onLiquid;
	
	@Override
	public void update() {
		if (full_null_check()) return;
        if ((mc.player.onGround || mc.player.isInWeb) && !inLiquid && !onLiquid) {
			for (double y = 0.0; y < this.height.get_value(1) + 0.5; y += 0.01) {
				if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
					mc.player.motionY = strength.get_value(1d) * -1;
					break;
				}
			}
		}
		
		final double y = mc.player.posY + 0.01;
		
		for (int x = MathHelper.floor(mc.player.posX); x < MathHelper.ceil(mc.player.posX); x++){
			for (int z = MathHelper.floor(mc.player.posZ); z < MathHelper.ceil(mc.player.posZ); z++){
				final BlockPos pos = new BlockPos(x, (int)y, z);
				inLiquid = mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid;
			}
		}
		for (int x = MathHelper.floor(mc.player.posX); x < MathHelper.ceil(mc.player.posX); x++){
			for (int z = MathHelper.floor(mc.player.posZ); z < MathHelper.ceil(mc.player.posZ); z++){
				final BlockPos pos = new BlockPos(x, (int)y, z);
				onLiquid = mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid;
			}
		}
    }
}