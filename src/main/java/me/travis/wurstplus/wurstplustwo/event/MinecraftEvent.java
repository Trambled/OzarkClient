package me.travis.wurstplus.wurstplustwo.event;

import me.zero.alpine.fork.event.type.Cancellable;
import net.minecraft.client.Minecraft;

public class MinecraftEvent extends Cancellable
{
    private static final Minecraft mc;
    private Era era = Era.PRE;
    private final float partialTicks;

    public MinecraftEvent()
    {
        partialTicks = mc.getRenderPartialTicks();
    }
    
    public MinecraftEvent(Era p_Era)
    {
        partialTicks = mc.getRenderPartialTicks();
        era = p_Era;
    }

    public Era getEra()
    {
        return era;
    }

    public float getPartialTicks()
    {
        return partialTicks;
    }
    
    public enum Era
    {
        PRE,
        PERI,
        POST
    }

    static {
        mc = Minecraft.getMinecraft();
    }

}
