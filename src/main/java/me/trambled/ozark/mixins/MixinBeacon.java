package me.trambled.ozark.mixins;

import me.trambled.ozark.ozarkclient.event.Eventbus;
import me.trambled.ozark.ozarkclient.event.events.EventRenderBeacon;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.tileentity.TileEntityBeacon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityBeaconRenderer.class)
public class MixinBeacon {
    @Inject(method = "render", at = @At("INVOKE"), cancellable = true)
    private void renderBeacon(TileEntityBeacon te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo ci) {
        EventRenderBeacon l_Event = new EventRenderBeacon();
        Eventbus.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled()) ci.cancel();
    }
}