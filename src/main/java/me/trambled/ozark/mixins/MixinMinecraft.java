package me.trambled.ozark.mixins;

import jline.internal.Nullable;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.Eventbus;
import me.trambled.ozark.ozarkclient.event.events.EventGUIScreen;
import me.trambled.ozark.ozarkclient.guiscreen.GuiCustomMainMenu;
import me.trambled.ozark.ozarkclient.guiscreen.GuiCustomMainMenu2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;

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
			if (Ozark.get_setting_manager().get_setting_with_tag("CustomMainMenu", "CMMPhobosMode").get_value(true)) {
				this.displayGuiScreen(new GuiCustomMainMenu());
			} else {
				this.displayGuiScreen(new GuiCustomMainMenu2());
			}
		}
	}

	@Inject(method={"runTick()V"}, at={@At(value="RETURN")})
	private void runTick(CallbackInfo callbackInfo) {
		if (mc.currentScreen instanceof GuiMainMenu && Ozark.get_module_manager().get_module_with_tag("CustomMainMenu").is_active()) {
			if (Ozark.get_setting_manager().get_setting_with_tag("CustomMainMenu", "CMMPhobosMode").get_value(true)) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiCustomMainMenu());
			} else {
				Minecraft.getMinecraft().displayGuiScreen(new GuiCustomMainMenu2());
			}
		}
	}
}