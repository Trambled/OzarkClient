package me.trambled.ozark.mixins;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.Eventbus;
import me.trambled.ozark.ozarkclient.event.events.EventPlayerTravel;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityPlayer.class)
public class MixinPlayer extends MixinEntity {
    
    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
	public void travel(float strafe, float vertical, float forward, CallbackInfo info) {
        EventPlayerTravel event_packet = new EventPlayerTravel(strafe, vertical, forward);

		Eventbus.EVENT_BUS.post(event_packet);

		if (event_packet.isCancelled()) {
			move(MoverType.SELF, motionX, motionY, motionZ);
			info.cancel();
		}
	}

	@Inject(method={"isEntityInsideOpaqueBlock"}, at={@At(value="HEAD")}, cancellable=true)
	private void isEntityInsideOpaqueBlockHook(CallbackInfoReturnable<Boolean> info) {
		if (Ozark.get_module_manager().get_module_with_tag("PacketFly").is_active()) {
			info.setReturnValue(false);
		}
	}

}