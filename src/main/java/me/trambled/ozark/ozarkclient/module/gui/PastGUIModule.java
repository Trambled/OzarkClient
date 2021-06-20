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

		this.name = "OzarkGUI";
		this.tag = "PastGUI";
		this.description = "CUSTOM Ozark Gui.";
		set_bind(Ozark.KEY_GUI);
	}

	Setting red = create("Red", "PastGUIR", 255, 0, 255);
	Setting green = create("Green", "PastGUIG", 0, 0, 255);
	Setting blue = create("Blue", "PastGUIB", 0, 0, 255);
	Setting alpha = create("Alpha", "PastGUIA", 255, 0, 255);
	Setting red2 = create("ButtonRed", "PastGUIR2", 255, 0, 255);
	Setting green2 = create("ButtonGreen", "PastGUIG2", 0, 0, 255);
	Setting blue2 = create("ButtonBlue", "PastGUIB2", 0, 0, 255);
	Setting alpha2 = create("ButtonAlpha", "PastGUIA2", 124, 0, 255);
	Setting red3 = create("FrameRed", "PastGUIR3", 255, 0, 255);
	Setting green3 = create("FrameGreen", "PastGUIG3", 255, 0, 255);
	Setting blue3 = create("FrameBlue", "PastGUIB3", 255, 0, 255);
	Setting alpha3 = create("FrameAlpha", "PastGUIA3", 73, 0, 255);
	Setting rainbow = create("Rainbow", "PastGUIRainbow", false);
	Setting rainbow2 = create("ButtonRainbow", "PastGUIRainbow2", false);
	Setting rainbow3 = create("FrameRainbow", "PastGUIRainbow3", false);
	Setting trambled_mode = create("Trambled Mode", "PastGUITrampled", true);
	Setting red4 = create("TrambledRed", "PastGUIR4", 17, 0, 255);
	Setting green4 = create("TrambledGreen", "PastGUIG4", 17, 0, 255);
	Setting blue4 = create("TrambledBlue", "PastGUIB4", 17, 0, 255);
	Setting trambled_mode_a = create("TrambledModeA", "PastGUIA4", 132, 0, 255);
	Setting module_lines = create("Outline", "PastGUIModuleOutline", true);
	Setting bright_outline = create("Bright Outline", "PastGUIBrightOutline", true);

	Setting font = create("Font", "PastGUIFont", "Lato", combobox("Lato", "Arial", "Verdana", "None"));
	Setting scroll_speed = create("Scroll Speed", "PastGUIScrollSpeed", 10, 0, 20);
	Setting button_sound = create("Button Sound", "PastGUISound", true);
	Setting suffix = create("Module Suffix", "PastGUISuffix", "Dots", combobox("Dots", "Angle Bracket", "Triangle", "None"));
	Setting snow = create("Snow", "PastGUISnow", true);
	Setting blur = create("Blur", "PastGUIBlur", true); // credit to ferox for blur
	Setting font_shadow = create("Font Shadow", "PastGUIFontShadow", true);
	Setting descriptions = create("Descriptions", "PastGUIDescriptions", true);
	Setting hover_change = create("Hover Change", "PastGUIHoverChange", true);
	Setting pause_game = create("Pause Game", "PastGUIPauseGame", false);
	Setting kambing = create("Listed Gay Mode", "PastGUIKambing", false);


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
			mc.displayGuiScreen(null)
                        mc.entityRenderer.getShaderGroup().deleteShaderGroup();

		}
	}

	@Override
	public void update() {
		if (rainbow.get_value(true)) {
			cycle_rainbow();
		}
		if (rainbow2.get_value(true)) {
			cycle_rainbow2();
		}
		if (rainbow3.get_value(true)) {
			cycle_rainbow3();
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

	public void cycle_rainbow2() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

		red2.set_value((color_rgb_o >> 16) & 0xFF);
		green2.set_value((color_rgb_o >> 8) & 0xFF);
		blue2.set_value(color_rgb_o & 0xFF);
	}

	public void cycle_rainbow3() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

		red3.set_value((color_rgb_o >> 16) & 0xFF);
		green3.set_value((color_rgb_o >> 8) & 0xFF);
		blue3.set_value(color_rgb_o & 0xFF);
	}

	@Override
	public void update_always() {
		red4.set_shown(trambled_mode.get_value(true));
		green4.set_shown(trambled_mode.get_value(true));
		blue4.set_shown(trambled_mode.get_value(true));
		trambled_mode_a.set_shown(trambled_mode.get_value(true));
		bright_outline.set_shown(module_lines.get_value(true));
	}

}
