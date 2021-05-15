package me.trambled.ozark.ozarkclient.module.gui;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class PastGUIModule extends Module {

	public PastGUIModule() {
		super(Category.GUI);

		this.name        = "PastGUI";
		this.tag         = "PastGUI";
		this.description = "CUSTOM Past Gui";
		this.toggle_message = false;
		set_bind(Ozark.KEY_GUI);
	}

	Setting red = create("Red", "PastGUIR", 245, 0, 255);
	Setting green = create("Green", "PastGUIG", 127, 0, 255);
	Setting blue = create("Blue", "PastGUIB", 142, 0, 255);
	Setting alpha = create("Alpha", "PastGUIA", 255, 0, 255);
	Setting rainbow = create("Rainbow", "PastGUIRainbow", false);

	Setting font = create("Font", "PastGUIFont", "Lato", combobox("Lato", "Verdana", "Arial", "None"));
	Setting scroll_speed = create("Scroll Speed", "PastGUIScrollSpeed", 10, 0, 20);
	Setting button_sound = create("Button Sound", "PastGUISound", true);
	Setting snow = create("Snow", "PastGUISnow", true);
	Setting blur = create("Blur", "PastGUIBlur", true); // credit for ferox for blur
	Setting font_shadow = create("Font Shadow", "PastGUIFontShadow", true);
	Setting descriptions = create("Descriptions", "PastGUIDescriptions", true);
	Setting hover_change = create("Hover Change", "PastGUIHoverChange", true);
	Setting pause_game = create("Pause Game", "PastGUIPauseGame", false);

	@Override
	protected void enable() {
		if (!full_null_check()) {
			mc.displayGuiScreen(Ozark.past_gui);
		}
		if (OpenGlHelper.shadersSupported) {
			try {
				if (blur.get_value(true)) {
					mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
				}
			} catch (Exception ignored) {}
		}
	}

	@Override
	protected void disable() {
		if (!full_null_check()) {
			mc.displayGuiScreen(null);
		}
	}

	@Override
	public void update() {
		if (rainbow.get_value(true)) {
			cycle_rainbow();
		}
	}

	public void cycle_rainbow() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

		red.set_value((color_rgb_o >> 16) & 0xFF);
		green.set_value((color_rgb_o >> 8) & 0xFF);
		blue.set_value(color_rgb_o & 0xFF);

	}
}
