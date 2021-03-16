package me.travis.wurstplus.mixins;

import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventMotionUpdate;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventMove;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventSwing;
import me.travis.wurstplus.wurstplustwo.event.events.EventPlayerPushOutOfBlocks;
import me.travis.wurstplus.wurstplustwo.event.events.EventPlayerSendChatMessage;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// External.


@Mixin(value = EntityPlayerSP.class)
public class WurstplusMixinEntitySP extends WurstplusMixinEntity {

	@Inject(method = "move", at = @At("HEAD"), cancellable = true)
	private void move(MoverType type, double x, double y, double z, CallbackInfo info) {

		WurstplusEventMove event = new WurstplusEventMove(type, x, y, z);
		WurstplusEventBus.EVENT_BUS.post(event);

		if (event.isCancelled()) {
            super.move(type, event.get_x(), event.get_y(), event.get_z());
			info.cancel();
		}
	}

	@Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void OnPreUpdateWalkingPlayer(CallbackInfo p_Info) {

        WurstplusEventMotionUpdate l_Event = new WurstplusEventMotionUpdate(0);
        WurstplusEventBus.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
            p_Info.cancel();

    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"), cancellable = true)
    public void OnPostUpdateWalkingPlayer(CallbackInfo p_Info) {

        WurstplusEventMotionUpdate l_Event = new WurstplusEventMotionUpdate(1);
        WurstplusEventBus.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
            p_Info.cancel();

    }

    @Inject(method = "pushOutOfBlocks(DDD)Z", at = @At("HEAD"), cancellable = true)
    public void pushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> callbackInfo)
    {
        EventPlayerPushOutOfBlocks l_Event = new EventPlayerPushOutOfBlocks(x, y, z);
        WurstplusEventBus.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
            callbackInfo.setReturnValue(false);
    }

    @Inject(method = "swingArm", at = @At("RETURN"), cancellable = true)
    public void swingArm(EnumHand p_Hand, CallbackInfo p_Info) {

        WurstplusEventSwing l_Event = new WurstplusEventSwing(p_Hand);
        WurstplusEventBus.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
            p_Info.cancel();

    }


    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void swingArm(String p_Message, CallbackInfo p_Info)
    {
        EventPlayerSendChatMessage l_Event = new EventPlayerSendChatMessage(p_Message);
        WurstplusEventBus.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
            p_Info.cancel();
    }
}