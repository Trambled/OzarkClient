package me.travis.wurstplus.wurstplustwo.hacks;

import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.util.WurstplusTimer;

import java.awt.*;

public class PastGUIHack extends WurstplusHack {

	public PastGUIHack() {
		super(WurstplusCategory.WURSTPLUS_GUI);

		this.name        = "Past GUI";
		this.tag         = "PastGUI";
		this.description = "skidded past gui";
		toggle_message = false;
	}

	WurstplusSetting red = create("Red", "PastGUIR", 255, 0, 255);
	WurstplusSetting green = create("Green", "PastGUIG", 255, 0, 255);
	WurstplusSetting blue = create("Blue", "PastGUIB", 255, 0, 255);
	WurstplusSetting alpha = create("Alpha", "PastGUIA", 255, 0, 255);
	WurstplusSetting rainbow = create("Rainbow", "Rainbow", true);

	WurstplusSetting font = create("Font", "PastGUIFont", "Lato", combobox("Lato", "Verdana", "Arial", "None"));
	WurstplusSetting scroll_speed = create("Scroll Speed", "PastGUIScrollSpeed", 10, 0, 20);
	WurstplusSetting button_sound = create("Button Sound", "PastGUISound", true);
	WurstplusSetting font_shadow = create("Font Shadow", "PastGUIFontShadow", true);
	WurstplusSetting descriptions = create("Descriptions", "PastGUIDescriptions", true);
	WurstplusSetting hover_change = create("Hover Change", "PastGUIHoverChange", true);
	WurstplusSetting pause_game = create("Pause Game", "PastGUIPauseGame", true);

	WurstplusTimer timer = new WurstplusTimer();



	@Override
	protected void enable() {
		if (!nullCheck()) {
			mc.displayGuiScreen(Wurstplus.past_gui);
		}
	}

	@Override
	protected void disable() {
		if (!nullCheck()) {
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