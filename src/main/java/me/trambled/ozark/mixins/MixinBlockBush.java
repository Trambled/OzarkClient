package me.trambled.ozark.mixins;

import me.trambled.ozark.ozarkclient.event.Eventbus;
import me.trambled.ozark.ozarkclient.event.events.EventBlockGetRenderLayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.util.BlockRenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBush.class)
public class MixinBlockBush
{
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
