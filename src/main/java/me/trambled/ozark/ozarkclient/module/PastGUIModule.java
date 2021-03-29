package me.trambled.ozark.ozarkclient.module;

import me.trambled.ozark.Ozark;

import java.awt.*;

public class PastGUIModule extends Module {

	public PastGUIModule() {
		super(Category.GUI);

		this.name        = "Past GUI";
		this.tag         = "PastGUI";
		this.description = "skidded past gui";
		toggle_message = false;
		set_bind(Ozark.KEY_GUI);
	}

	Setting red = create("Red", "PastGUIR", 16, 0, 255);
	Setting green = create("Green", "PastGUIG", 16, 0, 255);
	Setting blue = create("Blue", "PastGUIB", 16, 0, 255);
	Setting alpha = create("Alpha", "PastGUIA", 255, 0, 255);
	Setting rainbow = create("Rainbow", "PastGUIRainbow", true);

	Setting font = create("Font", "PastGUIFont", "Lato", combobox("Lato", "Verdana", "Arial", "None"));
	Setting scroll_speed = create("Scroll Speed", "PastGUIScrollSpeed", 10, 0, 20);
	Setting button_sound = create("Button Sound", "PastGUISound", true);
	Setting snow = create("Snow", "PastGUISnow", true);
	Setting font_shadow = create("Font Shadow", "PastGUIFontShadow", true);
	Setting descriptions = create("Descriptions", "PastGUIDescriptions", true);
	Setting hover_change = create("Hover Change", "PastGUIHoverChange", true);
	Setting pause_game = create("Pause Game", "PastGUIPauseGame", true);

	@Override
	protected void enable() {
		if (!full_null_check()) {
			mc.displayGuiScreen(Ozark.past_gui);
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