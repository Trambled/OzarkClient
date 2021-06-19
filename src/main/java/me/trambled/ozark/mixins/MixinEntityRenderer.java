package me.trambled.ozark.mixins;

import me.trambled.ozark.ozarkclient.event.Eventbus;
import me.trambled.ozark.ozarkclient.event.events.EventRenderHurtCameraEffect;
import me.trambled.ozark.ozarkclient.event.events.EventRenderUpdateLightMap;
import me.trambled.ozark.ozarkclient.event.events.EventSetupFog;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// External.

@Mixin(value = EntityRenderer.class)
public class MixinEntityRenderer {
    
    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    public void setupFog(int startCoords, float partialTicks, CallbackInfo p_Info)
    {
        EventSetupFog event = new EventSetupFog(startCoords, partialTicks);
        Eventbus.EVENT_BUS.post(event);
        
        if (event.isCancelled()) {
			return;
        }
        
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void hurtCameraEffect(float ticks, CallbackInfo info)
    {
        EventRenderHurtCameraEffect l_Event = new EventRenderHurtCameraEffect(ticks);
        
        Eventbus.EVENT_BUS.post(l_Event);
        
        if (l_Event.isCancelled())
            info.cancel();
    }
	
	@Inject(method = "updateLightmap", at = @At("HEAD"), cancellable = true)
    private void updateLightmap(float partialTicks, CallbackInfo p_Info)
    {
        EventRenderUpdateLightMap l_Event = new EventRenderUpdateLightMap(partialTicks);
        
        Eventbus.EVENT_BUS.post(l_Event);
        
        if (l_Event.isCancelled())
            p_Info.cancel();
    }


}