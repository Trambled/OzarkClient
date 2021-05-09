package me.trambled.ozark.ozarkclient.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFood;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class PlayerUtil
{
    private static final Minecraft mc;
    public double firstJumpSpeed = 0.0;
    public double lastJumpSpeed = 0.0;
    public double percentJumpSpeedChanged = 0.0;
    public double jumpSpeedChanged = 0.0;
    public static boolean didJumpThisTick = false;
    public static boolean isJumping = false;
    public boolean didJumpLastTick = false;
    public long jumpInfoStartTime = 0L;
    public static double speedometerCurrentSpeed = 0.0;
    public boolean wasFirstJump = true;
    
    public static BlockPos GetLocalPlayerPosFloored() {
        return new BlockPos(Math.floor(PlayerUtil.mc.player.posX), Math.floor(PlayerUtil.mc.player.posY), Math.floor(PlayerUtil.mc.player.posZ));
    }

    public static FacingDirection GetFacing() {
        switch (MathHelper.floor(PlayerUtil.mc.player.rotationYaw * 8.0f / 360.0f + 0.5) & 0x7) {
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
        float strafe = 90.0f * PlayerUtil.mc.player.moveStrafing;
        strafe *= (float)((PlayerUtil.mc.player.moveForward != 0.0f) ? (PlayerUtil.mc.player.moveForward * 0.5) : 1.0);
        float yaw = PlayerUtil.mc.player.rotationYaw - strafe;
        yaw -= ((PlayerUtil.mc.player.moveForward < 0.0f) ? 180.0f : 0.0f);
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
        return Math.hypot(PlayerUtil.mc.player.motionX, PlayerUtil.mc.player.motionZ);
    }
    
    public void setSpeed(final Double speed) {
        final Double yaw = this.getMoveYaw();
        PlayerUtil.mc.player.motionX = -Math.sin(yaw) * speed;
        PlayerUtil.mc.player.motionZ = Math.cos(yaw) * speed;
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
        PlayerUtil.mc.timer.tickLength = 50.0f / speed;
    }
    
    public void step(final float height, final double[] offset, final boolean flag, final float speed) {
        if (flag) {
            this.setTimer(speed);
        }
        for (int i = 0; i < offset.length; ++i) {
            PlayerUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(PlayerUtil.mc.player.posX, PlayerUtil.mc.player.posY + offset[i], PlayerUtil.mc.player.posZ, PlayerUtil.mc.player.onGround));
        }
        PlayerUtil.mc.player.stepHeight = height;
    }
    
    static {
        mc = Minecraft.getMinecraft();
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

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.player == null) {
            return;
        }
        double distTraveledLastTickX = mc.player.posX - mc.player.prevPosX;
        double distTraveledLastTickZ = mc.player.posZ - mc.player.prevPosZ;
        this.speedometerCurrentSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
        if (didJumpThisTick && (!mc.player.onGround || isJumping)) {
            if (didJumpThisTick && !this.didJumpLastTick) {
                this.wasFirstJump = this.lastJumpSpeed == 0.0;
                this.percentJumpSpeedChanged = this.speedometerCurrentSpeed != 0.0 ? this.speedometerCurrentSpeed / this.lastJumpSpeed - 1.0 : -1.0;
                this.jumpSpeedChanged = this.speedometerCurrentSpeed - this.lastJumpSpeed;
                this.jumpInfoStartTime = Minecraft.getSystemTime();
                this.lastJumpSpeed = this.speedometerCurrentSpeed;
                this.firstJumpSpeed = this.wasFirstJump ? this.lastJumpSpeed : 0.0;
            }
            this.didJumpLastTick = didJumpThisTick;
        } else {
            this.didJumpLastTick = false;
            this.lastJumpSpeed = 0.0;
        }
    }

    public static double getSpeedKpH() {
        double speedometerkphdouble = turnIntoKpH(speedometerCurrentSpeed);
        speedometerkphdouble = (double)Math.round(10.0 * speedometerkphdouble) / 10.0;
        return speedometerkphdouble;
    }

    public static double turnIntoKpH(double input) {
        return (double)MathHelper.sqrt((double)input) * 71.2729367892;
    }

}
