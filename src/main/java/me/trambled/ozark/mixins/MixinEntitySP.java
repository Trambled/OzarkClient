package me.trambled.ozark.mixins;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.Eventbus;
import me.trambled.ozark.ozarkclient.event.events.EventMove;
import me.trambled.ozark.ozarkclient.event.events.EventPlayerPushOutOfBlocks;
import me.trambled.ozark.ozarkclient.event.events.EventRotation;
import me.trambled.ozark.ozarkclient.event.events.EventSwing;
import me.trambled.ozark.ozarkclient.util.player.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.MoverType;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// External.


@Mixin(value = EntityPlayerSP.class)
public class MixinEntitySP extends MixinEntity {

	@Inject(method = "move", at = @At("HEAD"), cancellable = true)
	private void move(MoverType type, double x, double y, double z, CallbackInfo info) {

		EventMove event = new EventMove(type, x, y, z);
		Eventbus.EVENT_BUS.post(event);

		if (event.isCancelled()) {
            super.move(type, event.get_x(), event.get_y(), event.get_z());
			info.cancel();
		}
	}

	@Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void OnPreUpdateWalkingPlayer(CallbackInfo p_Info) {
        EventRotation event = new EventRotation();
        Eventbus.EVENT_BUS.post(event);

        if (event.isCancelled()) {
            p_Info.cancel();

            RotationUtil.updateRotationPackets(event);
        }

    }

    @Inject(method = "pushOutOfBlocks(DDD)Z", at = @At("HEAD"), cancellable = true)
    public void pushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> callbackInfo)
    {
        EventPlayerPushOutOfBlocks l_Event = new EventPlayerPushOutOfBlocks(x, y, z);
        Eventbus.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
            callbackInfo.setReturnValue(false);
    }

    @Inject(method = "swingArm", at = @At("RETURN"), cancellable = true)
    public void swingArm(EnumHand p_Hand, CallbackInfo p_Info) {

        EventSwing l_Event = new EventSwing(p_Hand);
        Eventbus.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
            p_Info.cancel();

    }


    @Redirect(method = { "notifyDataManagerChange" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;playSound(Lnet/minecraft/client/audio/ISound;)V"))
    private void playElytraSound(final SoundHandler soundHandler, final ISound sound) {
        if (!Ozark.get_module_manager().get_module_with_tag("AntiSound").is_active() || !Ozark.get_setting_manager().get_setting_with_tag("AntiSound", "Elytra").get_value(true)) {
            soundHandler.playSound(sound);
        }
    }

    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V"))
    public void closeScreen(final EntityPlayerSP entityPlayerSP) {
        Ozark.get_module_manager ( ).get_module_with_tag ( "Portals" );
    }

    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
    public void closeScreen(final Minecraft minecraft, final GuiScreen screen) {
        Ozark.get_module_manager ( ).get_module_with_tag ( "Portals" );
    }

}