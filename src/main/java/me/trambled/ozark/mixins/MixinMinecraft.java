package me.trambled.ozark.mixins;

import jline.internal.Nullable;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.Eventbus;
import me.trambled.ozark.ozarkclient.event.events.EventGUIScreen;
import me.trambled.ozark.ozarkclient.guiscreen.CustomMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// External.

@Mixin(value = Minecraft.class)
public abstract class MixinMinecraft {
	@Shadow
	public abstract void displayGuiScreen(@Nullable GuiScreen var1);

	@Inject(method = "shutdown", at = @At("HEAD"))
	private void shutdown(CallbackInfo info) {
		Ozark.get_config_manager().save_settings();
	}

	@Inject(method = "displayGuiScreen", at = @At("HEAD"))
	private void displayGuiScreen(GuiScreen guiScreenIn, CallbackInfo info) {
		EventGUIScreen guiscreen = new EventGUIScreen(guiScreenIn);

		Eventbus.EVENT_BUS.post(guiscreen);

		if (guiScreenIn instanceof GuiMainMenu) {
			this.displayGuiScreen(new CustomMainMenu());
		}
	}

	@Inject(method={"runTick()V"}, at={@At(value="RETURN")})
	private void runTick(CallbackInfo callbackInfo) {
		if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu && Ozark.get_hack_manager().get_module_with_tag("CustomMainMenu").is_active()) {
			Minecraft.getMinecraft().displayGuiScreen(new CustomMainMenu());
		}
	}
}