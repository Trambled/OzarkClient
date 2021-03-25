package me.travis.wurstplus.mixins;

import me.travis.wurstplus.wurstplustwo.event.events.EventRenderEnchantingTable;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;

@Mixin(TileEntityEnchantmentTableRenderer.class)
public class MixinEnchantmentTable {
    @Inject(method = "render", at = @At(value = "INVOKE"), cancellable = true)
    private void renderEnchantingTableBook(TileEntityEnchantmentTable te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo ci) {
        EventRenderEnchantingTable l_Event = new EventRenderEnchantingTable();
        WurstplusEventBus.EVENT_BUS.post(l_Event);
        if(l_Event.isCancelled()) ci.cancel();
    }
}