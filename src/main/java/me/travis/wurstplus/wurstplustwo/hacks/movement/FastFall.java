package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.Wurstplus;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

//gamesense
public class FastFall extends WurstplusHack
{
    public FastFall() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "FastFall";
        this.tag = "FastFall";
        this.description = "Increases fall speed";
    }
	
	WurstplusSetting height = create("Height", "Height", 3.25, 0, 10);
	WurstplusSetting strength = create("Strength", "Strength", 1, 0, 10);
	
	private boolean inLiquid;
	private boolean onLiquid;
	
	@Override
	public void update() {
        if (mc.player.onGround && !inLiquid && !onLiquid) {
			for (double y = 0.0; y < this.height.get_value(1) + 0.5; y += 0.01) {
				if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
					mc.player.motionY = strength.get_value(1) * -1;
					break;
				}
			}
		}
		
		final double y = mc.player.posY + 0.01;
		
		for (int x = MathHelper.floor(mc.player.posX); x < MathHelper.ceil(mc.player.posX); x++){
			for (int z = MathHelper.floor(mc.player.posZ); z < MathHelper.ceil(mc.player.posZ); z++){
				final BlockPos pos = new BlockPos(x, (int)y, z);
				if (mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid){
					inLiquid = true;
				} else {
					inLiquid = false;
				}
			}
		}
		for (int x = MathHelper.floor(mc.player.posX); x < MathHelper.ceil(mc.player.posX); x++){
			for (int z = MathHelper.floor(mc.player.posZ); z < MathHelper.ceil(mc.player.posZ); z++){
				final BlockPos pos = new BlockPos(x, (int)y, z);
				if (mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid){
					onLiquid = true;
				} else {
					onLiquid = false;
				}
			}
		}
    }
}