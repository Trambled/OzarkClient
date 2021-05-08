package me.trambled.ozark.ozarkclient.util;

import net.minecraft.client.Minecraft;

public
class RotationUtil {

    private static final Minecraft mc = Minecraft.getMinecraft ( );

    private static float yaw;
    private static float pitch;

    public static
    void updateRotations ( ) {
        yaw = mc.player.rotationYaw;
        pitch = mc.player.rotationPitch;
    }

    public static
    void restoreRotations ( ) {
        mc.player.rotationYaw = yaw;
        mc.player.rotationYawHead = yaw;
        mc.player.rotationPitch = pitch;
    }

    public static
    void setPlayerRotations ( final float yaw , final float pitch ) {
        mc.player.rotationYaw = yaw;
        mc.player.rotationYawHead = yaw;
        mc.player.rotationPitch = pitch;
    }

    public
    float getYaw ( ) {
        return yaw;
    }

    public
    void setYaw ( final float yaw ) {
        RotationUtil.yaw = yaw;
    }

    public
    float getPitch ( ) {
        return pitch;
    }

    public
    void setPitch ( final float pitch ) {
        RotationUtil.pitch = pitch;
    }


}