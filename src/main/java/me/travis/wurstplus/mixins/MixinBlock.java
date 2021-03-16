package me.travis.wurstplus.mixins;


import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.travis.wurstplus.wurstplustwo.event.events.EventBlockGetRenderLayer;
import me.travis.wurstplus.wurstplustwo.hacks.render.Xray;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlock
{
    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    public void shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> callback)
    {
        if (Wurstplus.get_hack_manager().get_module_with_tag("Xray").is_active())
            Xray.processShouldSideBeRendered((Block)(Object)this, blockState, blockAccess, pos, side, callback);
    }

    @Inject(method = "getRenderLayer", at = @At("HEAD"), cancellable = true)
    public void getRenderLayer(CallbackInfoReturnable<BlockRenderLayer> callback)
    {
        EventBlockGetRenderLayer event = new EventBlockGetRenderLayer((Block) (Object) this);
        WurstplusEventBus.EVENT_BUS.post(event);

        if (event.isCancelled())
        {
            callback.cancel();
            callback.setReturnValue(event.getBlockRenderLayer());
        }
    }

    @Inject(method = "getLightValue", at = @At("HEAD"), cancellable = true)
    public void getLightValue(CallbackInfoReturnable<Integer> callback)
    {
        if (Wurstplus.get_hack_manager().get_module_with_tag("Xray").is_active())
            Xray.processGetLightValue((Block)(Object)this, callback);
    }

}