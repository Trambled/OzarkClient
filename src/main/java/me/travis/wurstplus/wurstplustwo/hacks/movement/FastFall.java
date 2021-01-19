package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
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

public class FastFall extends WurstplusHack
{
    public FastFall() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "FastFall";
        this.tag = "FastFall";
        this.description = "I broke my legs";
    }
	
	private boolean inLiquid;
	private boolean onLiquid;
	
	@Override
	public void update() {
        if (mc.player.onGround && !inLiquid && !onLiquid) {
            --mc.player.motionY;
		}
		
		final double y = mc.player.posY + 0.01;
		
		for (int x = MathHelper.floor(HoleTP.mc.player.posX); x < MathHelper.ceil(HoleTP.mc.player.posX); x++){
			for (int z = MathHelper.floor(HoleTP.mc.player.posZ); z < MathHelper.ceil(HoleTP.mc.player.posZ); z++){
				final BlockPos pos = new BlockPos(x, (int)y, z);
				if (HoleTP.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid){
					inLiquid = true;
				} else {
					inLiquid = false;
				}
			}
		}
		for (int x = MathHelper.floor(HoleTP.mc.player.posX); x < MathHelper.ceil(HoleTP.mc.player.posX); x++){
			for (int z = MathHelper.floor(HoleTP.mc.player.posZ); z < MathHelper.ceil(HoleTP.mc.player.posZ); z++){
				final BlockPos pos = new BlockPos(x, (int)y, z);
				if (HoleTP.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid){
					onLiquid = true;
				} else {
					onLiquid = false;
				}
			}
		}
    }
}