package me.travis.wurstplus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

/**
 * Created by 086 on 11/11/2017. 
 */
//this is from a lot of clients
public class Wrapper {

    private static FontRenderer fontRenderer;

    public static Minecraft mc = Minecraft.getMinecraft();

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }

    public static Entity getRenderEntity() {
        return Wrapper.mc.getRenderViewEntity();
    }

    public static World getWorld() {
        return getMinecraft().world;
    }

    public static int getKey(String keyname) {
        return Keyboard.getKeyIndex(keyname.toUpperCase());
    }

    public static FontRenderer getFontRenderer() {
        return fontRenderer;
    }
    
    //salhack
    public static Minecraft GetMC()
    {
        return mc;
    }

    public static EntityPlayerSP GetPlayer()
    {
        return mc.player;
    }
}
