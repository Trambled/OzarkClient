package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class HoleTP extends Module //made by gamesense
{
	public HoleTP() {
		super(Category.MOVEMENT);
		this.name = "HoleTP";
		this.tag = "HoleTP";
		this.description = "Gets into holes fast.";
	}

	private int packets;
	private boolean jumped;
	private final double[] oneblockPositions = new double[]{0.42, 0.75};

	@Override
	public void update() {
		if (mc.world == null || HoleTP.mc.player == null || Ozark.get_module_manager().get_module_with_tag("Strafe").is_active()) {
			return;
		}
		if (!HoleTP.mc.player.onGround) {
			if (HoleTP.mc.gameSettings.keyBindJump.isKeyDown()) {
				this.jumped = true;
			}
		} else {
			this.jumped = false;
		}
		if (!this.jumped && mc.player.fallDistance < 0.5 && this.isInHole() && HoleTP.mc.player.posY - this.getNearestBlockBelow() <= 1.125 && HoleTP.mc.player.posY - this.getNearestBlockBelow() <= 0.95 && !this.isOnLiquid() && !this.isInLiquid()) {
			if (!mc.player.onGround) {
				this.packets++;
			}
			if (!mc.player.onGround && !mc.player.isInsideOfMaterial(Material.WATER) && !HoleTP.mc.player.isInsideOfMaterial(Material.LAVA) && !HoleTP.mc.gameSettings.keyBindJump.isKeyDown() && !HoleTP.mc.player.isOnLadder() && this.packets > 0) {
				final BlockPos blockPos = new BlockPos(mc.player.posX, HoleTP.mc.player.posY, HoleTP.mc.player.posZ);
				for (final double position : this.oneblockPositions) {
					HoleTP.mc.player.connection.sendPacket(new CPacketPlayer.Position(blockPos.getX() + 0.5f, HoleTP.mc.player.posY - position, blockPos.getZ() + 0.5f, true));
				}
				mc.player.setPosition(blockPos.getX() + 0.5f, this.getNearestBlockBelow() + 0.1, blockPos.getZ() + 0.5f);
				this.packets = 0;
			}
		}
	}

	private boolean isInHole(){
		final BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
		final IBlockState blockState = mc.world.getBlockState(blockPos);
		return this.isBlockValid(blockState, blockPos);
	}

	private double getNearestBlockBelow(){
		for (double y = HoleTP.mc.player.posY; y > 0.0; y -= 0.001){
			if (!(HoleTP.mc.world.getBlockState(new BlockPos(HoleTP.mc.player.posX, y, HoleTP.mc.player.posZ)).getBlock() instanceof BlockSlab) && HoleTP.mc.world.getBlockState(new BlockPos(HoleTP.mc.player.posX, y, HoleTP.mc.player.posZ)).getBlock().getDefaultState().getCollisionBoundingBox(HoleTP.mc.world, new BlockPos(0, 0, 0)) != null){
				return y;
			}
		}
		return -1.0;
	}

	private boolean isBlockValid(final IBlockState blockState, final BlockPos blockPos){
		return blockState.getBlock() == Blocks.AIR && HoleTP.mc.player.getDistanceSq(blockPos) >= 1.0 && HoleTP.mc.world.getBlockState(blockPos.up()).getBlock() == Blocks.AIR && HoleTP.mc.world.getBlockState(blockPos.up(2)).getBlock() == Blocks.AIR && (this.isBedrockHole(blockPos) || this.isObbyHole(blockPos) || this.isBothHole(blockPos) || this.isElseHole(blockPos));
	}

	private boolean isObbyHole(final BlockPos blockPos){
		final BlockPos[] array = new BlockPos[]{ blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
		for (final BlockPos touching : array){
			final IBlockState touchingState = HoleTP.mc.world.getBlockState(touching);
			if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.OBSIDIAN){
				return false;
			}
		}
		return true;
	}

	private boolean isBedrockHole(final BlockPos blockPos){
		final BlockPos[] array = new BlockPos[]{ blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
		for (final BlockPos touching : array){
			final IBlockState touchingState = HoleTP.mc.world.getBlockState(touching);
			if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.BEDROCK){
				return false;
			}
		}
		return true;
	}

	private boolean isBothHole(final BlockPos blockPos){
		final BlockPos[] array = new BlockPos[]{ blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
		for (final BlockPos touching : array){
			final IBlockState touchingState = HoleTP.mc.world.getBlockState(touching);
			if (touchingState.getBlock() == Blocks.AIR || (touchingState.getBlock() != Blocks.BEDROCK && touchingState.getBlock() != Blocks.OBSIDIAN)){
				return false;
			}
		}
		return true;
	}

	private boolean isElseHole(final BlockPos blockPos){
		final BlockPos[] array = new BlockPos[]{ blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
		for (final BlockPos touching : array){
			final IBlockState touchingState = HoleTP.mc.world.getBlockState(touching);
			if (touchingState.getBlock() == Blocks.AIR || !touchingState.isFullBlock()){
				return false;
			}
		}
		return true;
	}

	private boolean isOnLiquid(){
		final double y = HoleTP.mc.player.posY - 0.03;
		for (int x = MathHelper.floor(HoleTP.mc.player.posX); x < MathHelper.ceil(HoleTP.mc.player.posX); x++){
			for (int z = MathHelper.floor(HoleTP.mc.player.posZ); z < MathHelper.ceil(HoleTP.mc.player.posZ); z++){
				final BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
				if (HoleTP.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid){
					return true;
				}
			}
		}
		return false;
	}

	private boolean isInLiquid(){
		final double y = HoleTP.mc.player.posY + 0.01;
		for (int x = MathHelper.floor(HoleTP.mc.player.posX); x < MathHelper.ceil(HoleTP.mc.player.posX); x++){
			for (int z = MathHelper.floor(HoleTP.mc.player.posZ); z < MathHelper.ceil(HoleTP.mc.player.posZ); z++){
				final BlockPos pos = new BlockPos(x, (int)y, z);
				if (HoleTP.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid){
					return true;
				}
			}
		}
		return false;
	}
}