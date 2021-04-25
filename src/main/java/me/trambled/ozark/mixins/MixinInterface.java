package me.trambled.ozark.mixins;

import net.minecraft.client.Minecraft;

public class MixinInterface {
	public Minecraft mc = Minecraft.getMinecraft();
	boolean nullCheck = (mc.player == null || mc.world == null);
}