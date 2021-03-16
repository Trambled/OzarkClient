package me.travis.wurstplus.wurstplustwo.hacks;

import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;

import java.awt.*;

public class WurstplusClickGUI extends WurstplusHack {

	public WurstplusClickGUI() {
		super(WurstplusCategory.WURSTPLUS_GUI);

		this.name        = "GUI";
		this.tag         = "GUI";
		this.description = "The main gui";
		this.toggle_message = false;
	}

	WurstplusSetting scroll_speed = create("Scroll Speed", "ClickGUIScrollSpeed", 10, 0, 30);

	WurstplusSetting label_frame = create("info", "ClickGUIInfoFrame", "Frames");
	WurstplusSetting name_frame_r = create("Name R", "ClickGUINameFrameR", 255, 0, 255);
	WurstplusSetting name_frame_g = create("Name G", "ClickGUINameFrameG", 255, 0, 255);
	WurstplusSetting name_frame_b = create("Name B", "ClickGUINameFrameB", 255, 0, 255);
	WurstplusSetting rainbow_name = create("Rainbow Name", "ClickGUIRainbowName", false);

	WurstplusSetting background_frame_r = create("Background R", "ClickGUIBackgroundFrameR", 0, 0, 255);
	WurstplusSetting background_frame_g = create("Background G", "ClickGUIBackgroundFrameG", 0, 0, 255);
	WurstplusSetting background_frame_b = create("Background B", "ClickGUIBackgroundFrameB", 0, 0, 255);
	WurstplusSetting background_frame_a = create("Background A", "ClickGUIBackgroundFrameA", 255, 0, 255);
	WurstplusSetting rainbow_background = create("Rainbow Back Ground", "ClickGUIRainbowBackGround", false);

	WurstplusSetting border_frame_r = create("Border R", "ClickGUIBorderFrameR", 255, 0, 255);
	WurstplusSetting border_frame_g = create("Border G", "ClickGUIBorderFrameG", 255, 0, 255);
	WurstplusSetting border_frame_b = create("Border B", "ClickGUIBorderFrameB", 255, 0, 255);
	WurstplusSetting rainbow_border = create("Rainbow Border", "ClickGUIRainbowBorder", false);

	WurstplusSetting label_widget = create("info", "ClickGUIInfoWidget", "Widgets");

	WurstplusSetting name_widget_r = create("Name R", "ClickGUINameWidgetR", 255, 0, 255);
	WurstplusSetting name_widget_g = create("Name G", "ClickGUINameWidgetG", 255, 0, 255);
	WurstplusSetting name_widget_b = create("Name B", "ClickGUINameWidgetB", 255, 0, 255);
	WurstplusSetting rainbow_name_widget = create("Rainbow Name", "ClickGUIRainbowWidgetName", false);

	WurstplusSetting background_widget_r = create("Background R", "ClickGUIBackgroundWidgetR", 255, 0, 255);
	WurstplusSetting background_widget_g = create("Background G", "ClickGUIBackgroundWidgetG", 255, 0, 255);
	WurstplusSetting background_widget_b = create("Background B", "ClickGUIBackgroundWidgetB", 255, 0, 255);
	WurstplusSetting background_widget_a = create("Background A", "ClickGUIBackgroundWidgetA", 100, 0, 255);
	WurstplusSetting rainbow_widget_background = create("Rainbow Background", "ClickGUIRainbowWidgetBackground", false);

	WurstplusSetting border_widget_r = create("Border R", "ClickGUIBorderWidgetR", 255, 0, 255);
	WurstplusSetting border_widget_g = create("Border G", "ClickGUIBorderWidgetG", 255, 0, 255);
	WurstplusSetting border_widget_b = create("Border B", "ClickGUIBorderWidgetB", 255, 0, 255);
	WurstplusSetting rainbow_widget_border = create("Rainbow Border", "ClickGUIRainbowWidgetBorder", false);

	@Override
	public void update() {
		// Update frame colors.
		Wurstplus.click_gui.theme_frame_name_r = name_frame_r.get_value(1);
		Wurstplus.click_gui.theme_frame_name_g = name_frame_g.get_value(1);
		Wurstplus.click_gui.theme_frame_name_b = name_frame_b.get_value(1);

		Wurstplus.click_gui.theme_frame_background_r = background_frame_r.get_value(1);
		Wurstplus.click_gui.theme_frame_background_g = background_frame_g.get_value(1);
		Wurstplus.click_gui.theme_frame_background_b = background_frame_b.get_value(1);
		Wurstplus.click_gui.theme_frame_background_a = background_frame_a.get_value(1);

		Wurstplus.click_gui.theme_frame_border_r = border_frame_r.get_value(1);
		Wurstplus.click_gui.theme_frame_border_g = border_frame_g.get_value(1);
		Wurstplus.click_gui.theme_frame_border_b = border_frame_b.get_value(1);

		// Update widget colors.
		Wurstplus.click_gui.theme_widget_name_r = name_widget_r.get_value(1);
		Wurstplus.click_gui.theme_widget_name_g = name_widget_g.get_value(1);
		Wurstplus.click_gui.theme_widget_name_b = name_widget_b.get_value(1);

		Wurstplus.click_gui.theme_widget_background_r = background_widget_r.get_value(1);
		Wurstplus.click_gui.theme_widget_background_g = background_widget_g.get_value(1);
		Wurstplus.click_gui.theme_widget_background_b = background_widget_b.get_value(1);
		Wurstplus.click_gui.theme_widget_background_a = background_widget_a.get_value(1);

		Wurstplus.click_gui.theme_widget_border_r = border_widget_r.get_value(1);
		Wurstplus.click_gui.theme_widget_border_g = border_widget_g.get_value(1);
		Wurstplus.click_gui.theme_widget_border_b = border_widget_b.get_value(1);

		if (rainbow_name.get_value(true)) {
			cycle_rainbow_name_1();
		}

		if (rainbow_name_widget.get_value(true)) {
			cycle_rainbow_name_2();
		}

		if (rainbow_background.get_value(true)) {
			cycle_rainbow_background_1();
		}

		if (rainbow_widget_background.get_value(true)) {
			cycle_rainbow_background_2();
		}

		if (rainbow_border.get_value(true)) {
			cycle_rainbow_border_1();
		}

		if (rainbow_widget_border.get_value(true)) {
			cycle_rainbow_border_2();
		}
	}

	@Override
	public void enable() {
		if (mc.world != null && mc.player != null) {
			mc.displayGuiScreen(Wurstplus.click_gui);
		}
	}

	@Override
	public void disable() {
		if (mc.world != null && mc.player != null) {
			mc.displayGuiScreen(null);
		}
	}

	public void cycle_rainbow_name_1() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

		name_frame_r.set_value((color_rgb_o >> 16) & 0xFF);
		name_frame_g.set_value((color_rgb_o >> 8) & 0xFF);
		name_frame_b.set_value(color_rgb_o & 0xFF);
	}

	public void cycle_rainbow_name_2() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

		name_widget_r.set_value((color_rgb_o >> 16) & 0xFF);
		name_widget_g.set_value((color_rgb_o >> 8) & 0xFF);
		name_widget_b.set_value(color_rgb_o & 0xFF);
	}

	public void cycle_rainbow_background_1() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

		background_frame_r.set_value((color_rgb_o >> 16) & 0xFF);
		background_frame_g.set_value((color_rgb_o >> 8) & 0xFF);
		background_frame_b.set_value(color_rgb_o & 0xFF);
	}

	public void cycle_rainbow_background_2() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

		background_widget_r.set_value((color_rgb_o >> 16) & 0xFF);
		background_widget_g.set_value((color_rgb_o >> 8) & 0xFF);
		background_widget_b.set_value(color_rgb_o & 0xFF);
	}

	public void cycle_rainbow_border_1() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

		border_frame_r.set_value((color_rgb_o >> 16) & 0xFF);
		border_frame_g.set_value((color_rgb_o >> 8) & 0xFF);
		border_frame_b.set_value(color_rgb_o & 0xFF);
	}

	public void cycle_rainbow_border_2() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

		border_widget_r.set_value((color_rgb_o >> 16) & 0xFF);
		border_widget_g.set_value((color_rgb_o >> 8) & 0xFF);
		border_widget_b.set_value(color_rgb_o & 0xFF);
	}
}