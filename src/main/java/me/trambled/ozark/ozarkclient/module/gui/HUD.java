package me.trambled.ozark.ozarkclient.module.gui;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

import java.awt.*;

public class HUD extends Module {
	int xd;
	public HUD() {
		super(Category.GUI);

		this.name = "HUD";
		this.tag = "HUD";
		this.description = "Allows u to modify the hud.";
	}

	Setting secret = create("Secret Modules", "Secret uuuuuuuuuu", false);
	Setting rainbowoffset = create("RainbowCsgoOffset", "offset", 4000, 0, 20000);
	Setting strings_r = create("Color R", "HUDStringsColorR", 245, 0, 255);
	Setting strings_g = create("Color G", "HUDStringsColorG", 127, 0, 255);
	Setting strings_b = create("Color B", "HUDStringsColorB", 142, 0, 255);
	Setting strings_a = create("Alpha", "HUDStringsColorA", 230, 0, 255);
	Setting rainbow = create("Rainbow", "HUDRainbow", true);
	Setting flow = create("Flow", "HUDFlow", true);
	Setting array = create("ArraylistFlow", "HUDArrayFlow", true); // i made ths because i dont like my araylist being flow - kambing
	Setting cfont = create("CFont", "HUDFont", false);
	Setting compass_scale = create("Compass Scale", "HUDCompassScale", 16, 1, 60);
	Setting user_mode = create("User Mode", "HUDUserMode", "Time", combobox("Time", "Simple"));
	Setting arraylist_mode = create("ArrayList", "HUDArrayList", "Free", combobox("Free", "Top R", "Top L", "Bottom R", "Bottom L"));
	Setting show_all_pots = create("All Potions", "HUDAllPotions", false);
	Setting max_player_list = create("Max Players", "HUDMaxPlayers", 24, 1, 64);
	Setting ram = create("ClearRam(scret)", "ClearRamEvery50Seconds", false);

	@Override
	public void enable() {
		if (mc.world != null && mc.player != null) {
			Ozark.get_module_manager().get_module_with_tag("GUI").set_active(false);

			Ozark.main_hud.back = false;

			mc.displayGuiScreen(Ozark.main_hud);
		}
	}

	@Override
	public void update_always() {
		ram.set_shown(secret.get_value(true));
		if (rainbow.get_value(true)) {
			cycle_rainbow();
		}
		if (ram.get_value(true)) {
			xd++;
			if(xd == 1000) {
				System.gc();
				xd = 0;
	}}}

	public void cycle_rainbow() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

		strings_r.set_value((color_rgb_o >> 16) & 0xFF);
		strings_g.set_value((color_rgb_o >> 8) & 0xFF);
		strings_b.set_value(color_rgb_o & 0xFF);
	}

}
