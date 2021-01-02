package me.travis.wurstplus.mixins;

import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.travis.wurstplus.wurstplustwo.event.events.EventSteerEntity;
import me.travis.wurstplus.wurstplustwo.event.events.EventHorseSaddled;
import net.minecraft.entity.passive.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public class MixinAbstractHorse
{
    @Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)
    public void canBeSteered(CallbackInfoReturnable<Boolean> cir)
    {
        EventSteerEntity l_Event = new EventSteerEntity();
        WurstplusEventBus.EVENT_BUS.post(l_Event);

        if (l_Event.isCancelled())
        {
            cir.cancel();
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isHorseSaddled", at = @At("HEAD"), cancellable = true)
    public void isHorseSaddled(CallbackInfoReturnable<Boolean> cir)
    {
        EventHorseSaddled l_Event = new EventHorseSaddled();
        WurstplusEventBus.EVENT_BUS.post(l_Event);

        if (l_Event.isCancelled())
        {
            cir.cancel();
            cir.setReturnValue(true);
        }
    }
}