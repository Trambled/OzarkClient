package me.trambled.ozark.ozarkclient.util.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class WrapperUtil {

    public final static Minecraft mc = Minecraft.getMinecraft();

    public static EntityPlayerSP get_player() {
        return mc.player;
    }


}