package me.trambled.ozark.mixins;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.Eventbus;
import me.trambled.ozark.ozarkclient.event.events.EventBlockGetRenderLayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ BlockPortal.class })
public class MixinBlockPortal
{
    @Redirect(method = { "randomDisplayTick" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(DDDLnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FFZ)V"))
    private void onPortalSound(final World world, final double x, final double y, final double z, final SoundEvent soundIn, final SoundCategory category, final float volume, final float pitch, final boolean distanceDelay) {
        if (!Ozark.get_module_manager().get_module_with_tag("AntiSound").is_active() || !Ozark.get_setting_manager().get_setting_with_tag("AntiSound", "Portals").get_value(true)) {
            world.playSound(x, y, z, soundIn, category, volume, pitch, distanceDelay);
        }
    }

    @Inject(method = "getRenderLayer", at = @At("HEAD"), cancellable = true)
    public void getRenderLayer(CallbackInfoReturnable<BlockRenderLayer> callback)
    {
        EventBlockGetRenderLayer event = new EventBlockGetRenderLayer((Block) (Object) this);
        Eventbus.EVENT_BUS.post(event);

        if (event.isCancelled())
        {
            callback.cancel();
            callback.setReturnValue(event.getBlockRenderLayer());
        }
    }
}
