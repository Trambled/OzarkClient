package me.trambled.ozark.mixins;

import me.trambled.ozark.ozarkclient.event.EventBus;
import me.trambled.ozark.ozarkclient.event.events.EventSteerEntity;
import me.trambled.ozark.ozarkclient.event.events.EventHorseSaddled;
import net.minecraft.entity.passive.EntityPig;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPig.class)
public class MixinEntityPig
{
    @Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)
    public void canBeSteered(CallbackInfoReturnable<Boolean> cir)
    {
        EventSteerEntity event = new EventSteerEntity();
        EventBus.EVENT_BUS.post(event);

        if (event.isCancelled())
        {
            cir.cancel();
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getSaddled", at = @At("HEAD"), cancellable = true)
    public void getSaddled(CallbackInfoReturnable<Boolean> cir)
    {
        EventHorseSaddled event = new EventHorseSaddled();
        EventBus.EVENT_BUS.post(event);

        if (event.isCancelled())
        {
            cir.cancel();
            cir.setReturnValue(true);
        }
    }
}
