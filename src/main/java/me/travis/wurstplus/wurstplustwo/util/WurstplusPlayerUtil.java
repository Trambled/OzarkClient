package me.travis.wurstplus.wurstplustwo.util;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class WurstplusPlayerUtil  //merged with past
{
    private static final Minecraft mc;
    
    public static BlockPos GetLocalPlayerPosFloored() {
        return new BlockPos(Math.floor(WurstplusPlayerUtil.mc.player.posX), Math.floor(WurstplusPlayerUtil.mc.player.posY), Math.floor(WurstplusPlayerUtil.mc.player.posZ));
    }
    
    public static FacingDirection GetFacing() {
        switch (MathHelper.floor(WurstplusPlayerUtil.mc.player.rotationYaw * 8.0f / 360.0f + 0.5) & 0x7) {
            case 0:
            case 1: {
                return FacingDirection.South;
            }
            case 2:
            case 3: {
                return FacingDirection.West;
            }
            case 4:
            case 5: {
                return FacingDirection.North;
            }
            case 6:
            case 7: {
                return FacingDirection.East;
            }
            default: {
                return FacingDirection.North;
            }
        }
    }

    public static double getDirection() {
        float rotationYaw = mc.player.rotationYaw;
        if (mc.player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (mc.player.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (mc.player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (mc.player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (mc.player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
    
    public double getMoveYaw() {
        float strafe = 90.0f * WurstplusPlayerUtil.mc.player.moveStrafing;
        strafe *= (float)((WurstplusPlayerUtil.mc.player.moveForward != 0.0f) ? (WurstplusPlayerUtil.mc.player.moveForward * 0.5) : 1.0);
        float yaw = WurstplusPlayerUtil.mc.player.rotationYaw - strafe;
        yaw -= ((WurstplusPlayerUtil.mc.player.moveForward < 0.0f) ? 180.0f : 0.0f);
        return Math.toRadians(yaw);
    }

    public static void placeBlock(BlockPos pos) {
        for (EnumFacing enumFacing : EnumFacing.values()) {

            if (!Minecraft.getMinecraft().world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR) && !isIntercepted(pos)) {

                Vec3d vec = new Vec3d(pos.getX() + 0.5D + (double) enumFacing.getXOffset() * 0.5D, pos.getY() + 0.5D + (double) enumFacing.getYOffset() * 0.5D, pos.getZ() + 0.5D + (double) enumFacing.getZOffset() * 0.5D);

                float[] old = new float[]{Minecraft.getMinecraft().player.rotationYaw, Minecraft.getMinecraft().player.rotationPitch};

                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Rotation((float) Math.toDegrees(Math.atan2((vec.z - Minecraft.getMinecraft().player.posZ), (vec.x - Minecraft.getMinecraft().player.posX))) - 90.0F, (float) (-Math.toDegrees(Math.atan2((vec.y - (Minecraft.getMinecraft().player.posY + (double) Minecraft.getMinecraft().player.getEyeHeight())), (Math.sqrt((vec.x - Minecraft.getMinecraft().player.posX) * (vec.x - Minecraft.getMinecraft().player.posX) + (vec.z - Minecraft.getMinecraft().player.posZ) * (vec.z - Minecraft.getMinecraft().player.posZ)))))), Minecraft.getMinecraft().player.onGround));
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketEntityAction(Minecraft.getMinecraft().player, CPacketEntityAction.Action.START_SNEAKING));
                Minecraft.getMinecraft().playerController.processRightClickBlock(Minecraft.getMinecraft().player, Minecraft.getMinecraft().world, pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);
                Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketEntityAction(Minecraft.getMinecraft().player, CPacketEntityAction.Action.STOP_SNEAKING));
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Rotation(old[0], old[1], Minecraft.getMinecraft().player.onGround));

                return;
            }
        }
    }
    
    public double getSpeed() {
        return Math.hypot(WurstplusPlayerUtil.mc.player.motionX, WurstplusPlayerUtil.mc.player.motionZ);
    }
    
    public void setSpeed(final Double speed) {
        final Double yaw = this.getMoveYaw();
        WurstplusPlayerUtil.mc.player.motionX = -Math.sin(yaw) * speed;
        WurstplusPlayerUtil.mc.player.motionZ = Math.cos(yaw) * speed;
    }
    
    public void setBoatSpeed(final Double speed, final Entity boat) {
        final Double yaw = this.getMoveYaw();
        boat.motionX = -Math.sin(yaw) * speed;
        boat.motionZ = Math.cos(yaw) * speed;
    }


    public static int getBlockInHotbar(Block block) {
        for (int i = 0; i < 9; i++) {
            Item item = Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(block)) {
                return i;
            }
        }
        return -1;
    }

    public static int getAnyBlockInHotbar() {
        for (int i = 0; i < 9; i++) {
            Item item = Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isIntercepted(BlockPos pos) {
        for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }

    public static int getItemInHotbar(Item designatedItem) {
        for (int i = 0; i < 9; i++) {
            Item item = Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem();
            if (item instanceof Item && item.equals(designatedItem)) {
                return i;
            }
        }
        return -1;
    }

    
    public void addSpeed(final Double speed) {
        final Double yaw = this.getMoveYaw();
        final EntityPlayerSP player = WurstplusPlayerUtil.mc.player;
        player.motionX -= Math.sin(yaw) * speed;
        final EntityPlayerSP player2 = WurstplusPlayerUtil.mc.player;
        player2.motionZ += Math.cos(yaw) * speed;
    }
    
    public void setTimer(final float speed) {
        WurstplusPlayerUtil.mc.timer.tickLength = 50.0f / speed;
    }
    
    public void step(final float height, final double[] offset, final boolean flag, final float speed) {
        if (flag) {
            this.setTimer(speed);
        }
        for (int i = 0; i < offset.length; ++i) {
            WurstplusPlayerUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(WurstplusPlayerUtil.mc.player.posX, WurstplusPlayerUtil.mc.player.posY + offset[i], WurstplusPlayerUtil.mc.player.posZ, WurstplusPlayerUtil.mc.player.onGround));
        }
        WurstplusPlayerUtil.mc.player.stepHeight = height;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public enum FacingDirection
    {
        North, 
        South, 
        East, 
        West;
    }
}
