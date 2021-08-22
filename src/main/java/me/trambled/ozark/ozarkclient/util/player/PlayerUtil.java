package me.trambled.ozark.ozarkclient.util.player;

import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;

public class PlayerUtil
{
    private static final DecimalFormat formatter = new DecimalFormat("#.#");
    
    public static BlockPos getLocalPlayerPosFloored() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static FacingDirection GetFacing() {
        switch (MathHelper.floor(mc.player.rotationYaw * 8.0f / 360.0f + 0.5) & 0x7) {
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

    public double getMoveYaw() {
        float strafe = 90.0f * mc.player.moveStrafing;
        strafe *= (float)((mc.player.moveForward != 0.0f) ? (mc.player.moveForward * 0.5) : 1.0);
        float yaw = mc.player.rotationYaw - strafe;
        yaw -= ((mc.player.moveForward < 0.0f) ? 180.0f : 0.0f);
        return Math.toRadians(yaw);
    }

    public static void placeBlock(BlockPos pos) {
        for (EnumFacing enumFacing : EnumFacing.values()) {

            if (!mc.world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR) && !isIntercepted(pos)) {

                Vec3d vec = new Vec3d(pos.getX() + 0.5D + (double) enumFacing.getXOffset() * 0.5D, pos.getY() + 0.5D + (double) enumFacing.getYOffset() * 0.5D, pos.getZ() + 0.5D + (double) enumFacing.getZOffset() * 0.5D);

                float[] old = new float[]{mc.player.rotationYaw, Minecraft.getMinecraft().player.rotationPitch};

                mc.player.connection.sendPacket(new CPacketPlayer.Rotation((float) Math.toDegrees(Math.atan2((vec.z - Minecraft.getMinecraft().player.posZ), (vec.x - Minecraft.getMinecraft().player.posX))) - 90.0F, (float) (-Math.toDegrees(Math.atan2((vec.y - (Minecraft.getMinecraft().player.posY + (double) Minecraft.getMinecraft().player.getEyeHeight())), (Math.sqrt((vec.x - Minecraft.getMinecraft().player.posX) * (vec.x - Minecraft.getMinecraft().player.posX) + (vec.z - Minecraft.getMinecraft().player.posZ) * (vec.z - Minecraft.getMinecraft().player.posZ)))))), Minecraft.getMinecraft().player.onGround));
                mc.player.connection.sendPacket(new CPacketEntityAction(Minecraft.getMinecraft().player, CPacketEntityAction.Action.START_SNEAKING));
                mc.playerController.processRightClickBlock(Minecraft.getMinecraft().player, Minecraft.getMinecraft().world, pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketEntityAction(Minecraft.getMinecraft().player, CPacketEntityAction.Action.STOP_SNEAKING));
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(old[0], old[1], Minecraft.getMinecraft().player.onGround));

                return;
            }
        }
    }
    
    public double getSpeed() {
        return Math.hypot(mc.player.motionX, mc.player.motionZ);
    }
    
    public void setSpeed(final Double speed) {
        final Double yaw = this.getMoveYaw();
        mc.player.motionX = -Math.sin(yaw) * speed;
        mc.player.motionZ = Math.cos(yaw) * speed;
    }



    public static boolean isIntercepted(BlockPos pos) {
        for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }
    
    public void addSpeed(final Double speed) {
        final Double yaw = this.getMoveYaw();
        mc.player.motionX -= Math.sin(yaw) * speed;
        mc.player.motionZ += Math.cos(yaw) * speed;
    }
    
    public void setTimer(final float speed) {
        mc.timer.tickLength = 50.0f / speed;
    }
    
    public void step(final float height, final double[] offset, final boolean flag, final float speed) {
        if (flag) {
            this.setTimer(speed);
        }
        for (int i = 0; i < offset.length; ++i) {
            mc.player.connection.sendPacket( new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset[i], mc.player.posZ, mc.player.onGround) );
        }
        mc.player.stepHeight = height;
    }

    
    public enum FacingDirection
    {
        North, 
        South, 
        East, 
        West
    }

    public static boolean isMoving() {
        return (double)mc.player.moveForward != 0.0 || (double)mc.player.moveStrafing != 0.0;
    }

    public static boolean IsEating()
    {
        return mc.player != null && mc.player.getHeldItemMainhand().getItem() instanceof ItemFood && mc.player.isHandActive();
    }

    // Find player you are looking
    public static EntityPlayer findLookingPlayer(double rangeMax) {
        // Get player
        ArrayList<EntityPlayer> listPlayer = new ArrayList<>();
        // Only who is in a distance of enemyRange
        for(EntityPlayer playerSin : mc.world.playerEntities) {
            if (playerSin.getName().equals(mc.player.getName()) || FriendUtil.isFriend(playerSin.getName()) || playerSin.isDead)
                continue;
            if (mc.player.getDistance(playerSin) <= rangeMax) {
                listPlayer.add(playerSin);
            }
        }

        EntityPlayer target = null;
        // Get coordinate eyes + rotation
        Vec3d positionEyes = mc.player.getPositionEyes(mc.getRenderPartialTicks());
        Vec3d rotationEyes = mc.player.getLook(mc.getRenderPartialTicks());
        // Precision
        int precision = 2;
        // Iterate for every blocks
        for(int i = 0; i < (int) rangeMax; i++) {
            // Iterate for the precision
            for(int j = precision; j > 0 ; j--) {
                // Iterate for all players
                for(Entity targetTemp : listPlayer) {
                    // Get box of the player
                    AxisAlignedBB playerBox = targetTemp.getEntityBoundingBox();
                    // Get coordinate of the vec3d
                    double xArray = positionEyes.x + (rotationEyes.x * i) + rotationEyes.x/j;
                    double yArray = positionEyes.y + (rotationEyes.y * i) + rotationEyes.y/j;
                    double zArray = positionEyes.z + (rotationEyes.z * i) + rotationEyes.z/j;
                    // If it's inside
                    if (   playerBox.maxY >= yArray && playerBox.minY <= yArray
                            && playerBox.maxX >= xArray && playerBox.minX <= xArray
                            && playerBox.maxZ >= zArray && playerBox.minZ <= zArray) {
                        // Get target
                        target = (EntityPlayer) targetTemp;
                    }
                }
            }
        }

        return target;
    }

    public static String speed() {
        float currentTps = mc.timer.tickLength / 1000.0f;
        return formatter.format((double)(MathHelper.sqrt(Math.pow(coordsDiff("x"), 2.0) + Math.pow(coordsDiff("y"), 2.0)) / currentTps) * 3.6);
    }

    private static double coordsDiff(String s) {
        switch (s) {
            case "x": {
                return mc.player.posX - mc.player.prevPosX;
            }
            case "z": {
                return mc.player.posZ - mc.player.prevPosZ;
            }
        }
        return 0.0;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }}
