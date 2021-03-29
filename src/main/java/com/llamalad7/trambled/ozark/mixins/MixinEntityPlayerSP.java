package me.trambled.ozark.mixins;

import me.trambled.ozark.Ozark;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EntityPlayerSP.class)
public class MixinEntityPlayerSP extends MixinEntity {
	@Redirect(method = { "notifyDataManagerChange" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;playSound(Lnet/minecraft/client/audio/ISound;)V"))
	private void playElytraSound(final SoundHandler soundHandler, final ISound sound) {
		if (!Ozark.get_hack_manager().get_module_with_tag("AntiSound").is_active() || !Ozark.get_setting_manager().get_setting_with_tag("AntiSound", "Elytra").get_value(true)) {
			soundHandler.playSound(sound);
		}
	}

}