package me.trambled.ozark.mixins;

import net.minecraft.client.Minecraft;

public interface MixinInterface {

    Minecraft mc = Minecraft.getMinecraft();
    public boolean nullCheck = (mc.player == null || mc.world == null);
}
