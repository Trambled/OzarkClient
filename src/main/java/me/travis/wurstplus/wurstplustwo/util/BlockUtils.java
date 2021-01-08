package me.travis.wurstplus.wurstplustwo.util;

import net.minecraft.init.Blocks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;

public class BlockUtils
{
    private static Object PlayerUtil;
    public static Minecraft mc;
    
    public static void placeBlockScaffold(final BlockPos pos, final boolean rotate) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (canBeClicked(neighbor)) {
                final Vec3d hitVec = new Vec3d((Vec3i)neighbor).add(new Vec3d(0.5, 0.5, 0.5)).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                if (rotate) {
                    faceVectorPacketInstant(hitVec);
                }
                BlockUtils.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtils.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                BlockUtils.mc.playerController.processRightClickBlock(BlockUtils.mc.player, BlockUtils.mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
                BlockUtils.mc.player.swingArm(EnumHand.MAIN_HAND);
                BlockUtils.mc.rightClickDelayTimer = 0;
                BlockUtils.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtils.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                return;
            }
        }
    }
    
    public static void placeBlock(final BlockPos pos, final boolean rotate) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (canBeClicked(neighbor)) {
                final Vec3d hitVec = new Vec3d((Vec3i)neighbor).add(new Vec3d(0.5, 0.5, 0.5)).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                if (rotate) {
                    faceVectorPacketInstant(hitVec);
                }
                BlockUtils.mc.playerController.processRightClickBlock(BlockUtils.mc.player, BlockUtils.mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
                BlockUtils.mc.player.swingArm(EnumHand.MAIN_HAND);
                BlockUtils.mc.rightClickDelayTimer = 0;
                return;
            }
        }
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return BlockUtils.mc.world.getBlockState(pos).getBlock().canCollideCheck(BlockUtils.mc.world.getBlockState(pos), false);
    }
    
    public static void faceVectorPacketInstant(final Vec3d vec) {
        final float[] rotations = getNeededRotations2(vec);
        BlockUtils.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], BlockUtils.mc.player.onGround));
    }
    
    private static float[] getNeededRotations2(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { BlockUtils.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - BlockUtils.mc.player.rotationYaw), BlockUtils.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - BlockUtils.mc.player.rotationPitch) };
    }
    
    public static Vec3d getEyesPos() {
        return new Vec3d(BlockUtils.mc.player.posX, BlockUtils.mc.player.posY + BlockUtils.mc.player.getEyeHeight(), BlockUtils.mc.player.posZ);
    }
    
    public static List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> blocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        blocks.add(l);
                    }
                }
            }
        }
        return blocks;
    }
    
    public static boolean IsObbyHole(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 0, 0);
        final BlockPos boost3 = blockPos.add(0, 0, -1);
        final BlockPos boost4 = blockPos.add(1, 0, 0);
        final BlockPos boost5 = blockPos.add(-1, 0, 0);
        final BlockPos boost6 = blockPos.add(0, 0, 1);
        final BlockPos boost7 = blockPos.add(0, 2, 0);
        final BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        final BlockPos boost9 = blockPos.add(0, -1, 0);
        return BlockUtils.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && !IsBRockHole(blockPos) && BlockUtils.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && BlockUtils.mc.world.getBlockState(boost7).getBlock() == Blocks.AIR && (BlockUtils.mc.world.getBlockState(boost3).getBlock() == Blocks.OBSIDIAN || BlockUtils.mc.world.getBlockState(boost3).getBlock() == Blocks.BEDROCK) && (BlockUtils.mc.world.getBlockState(boost4).getBlock() == Blocks.OBSIDIAN || BlockUtils.mc.world.getBlockState(boost4).getBlock() == Blocks.BEDROCK) && (BlockUtils.mc.world.getBlockState(boost5).getBlock() == Blocks.OBSIDIAN || BlockUtils.mc.world.getBlockState(boost5).getBlock() == Blocks.BEDROCK) && (BlockUtils.mc.world.getBlockState(boost6).getBlock() == Blocks.OBSIDIAN || BlockUtils.mc.world.getBlockState(boost6).getBlock() == Blocks.BEDROCK) && BlockUtils.mc.world.getBlockState(boost8).getBlock() == Blocks.AIR && (BlockUtils.mc.world.getBlockState(boost9).getBlock() == Blocks.OBSIDIAN || BlockUtils.mc.world.getBlockState(boost9).getBlock() == Blocks.BEDROCK);
    }
    
    public static boolean IsBRockHole(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 0, 0);
        final BlockPos boost3 = blockPos.add(0, 0, -1);
        final BlockPos boost4 = blockPos.add(1, 0, 0);
        final BlockPos boost5 = blockPos.add(-1, 0, 0);
        final BlockPos boost6 = blockPos.add(0, 0, 1);
        final BlockPos boost7 = blockPos.add(0, 2, 0);
        final BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        final BlockPos boost9 = blockPos.add(0, -1, 0);
        return BlockUtils.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && BlockUtils.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && BlockUtils.mc.world.getBlockState(boost7).getBlock() == Blocks.AIR && BlockUtils.mc.world.getBlockState(boost3).getBlock() == Blocks.BEDROCK && BlockUtils.mc.world.getBlockState(boost4).getBlock() == Blocks.BEDROCK && BlockUtils.mc.world.getBlockState(boost5).getBlock() == Blocks.BEDROCK && BlockUtils.mc.world.getBlockState(boost6).getBlock() == Blocks.BEDROCK && BlockUtils.mc.world.getBlockState(boost8).getBlock() == Blocks.AIR && BlockUtils.mc.world.getBlockState(boost9).getBlock() == Blocks.BEDROCK;
    }
    
    public static boolean isHole(final BlockPos blockPos) {
        final BlockPos b1 = blockPos.add(0, 1, 0);
        final BlockPos b2 = blockPos.add(0, 0, 0);
        final BlockPos b3 = blockPos.add(0, 0, -1);
        final BlockPos b4 = blockPos.add(1, 0, 0);
        final BlockPos b5 = blockPos.add(-1, 0, 0);
        final BlockPos b6 = blockPos.add(0, 0, 1);
        final BlockPos b7 = blockPos.add(0, 2, 0);
        final BlockPos b8 = blockPos.add(0.5, 0.5, 0.5);
        final BlockPos b9 = blockPos.add(0, -1, 0);
        return BlockUtils.mc.world.getBlockState(b1).getBlock() == Blocks.AIR && BlockUtils.mc.world.getBlockState(b2).getBlock() == Blocks.AIR && BlockUtils.mc.world.getBlockState(b7).getBlock() == Blocks.AIR && (BlockUtils.mc.world.getBlockState(b3).getBlock() == Blocks.OBSIDIAN || BlockUtils.mc.world.getBlockState(b3).getBlock() == Blocks.BEDROCK) && (BlockUtils.mc.world.getBlockState(b4).getBlock() == Blocks.OBSIDIAN || BlockUtils.mc.world.getBlockState(b4).getBlock() == Blocks.BEDROCK) && (BlockUtils.mc.world.getBlockState(b5).getBlock() == Blocks.OBSIDIAN || BlockUtils.mc.world.getBlockState(b5).getBlock() == Blocks.BEDROCK) && (BlockUtils.mc.world.getBlockState(b6).getBlock() == Blocks.OBSIDIAN || BlockUtils.mc.world.getBlockState(b6).getBlock() == Blocks.BEDROCK) && BlockUtils.mc.world.getBlockState(b8).getBlock() == Blocks.AIR && (BlockUtils.mc.world.getBlockState(b9).getBlock() == Blocks.OBSIDIAN || BlockUtils.mc.world.getBlockState(b9).getBlock() == Blocks.BEDROCK);
    }
    
    static {
        BlockUtils.mc = Minecraft.getMinecraft();
    }
}
