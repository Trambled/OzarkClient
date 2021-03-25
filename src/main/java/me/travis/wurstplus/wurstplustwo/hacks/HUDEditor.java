package me.travis.wurstplus.wurstplustwo.hacks;

import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import java.awt.*;

public class HUDEditor extends WurstplusHack {

	public HUDEditor() {
		super(WurstplusCategory.WURSTPLUS_GUI);

		this.name        = "HUD";
		this.tag         = "HUDEditor";
		this.description = "modifies hud";
		this.toggle_message = false;
	}

	WurstplusSetting frame_view = create("info", "HUDStringsList", "Strings");

	WurstplusSetting strings_r = create("Color R", "HUDStringsColorR", 255, 0, 255);
	WurstplusSetting strings_g = create("Color G", "HUDStringsColorG", 255, 0, 255);
	WurstplusSetting strings_b = create("Color B", "HUDStringsColorB", 255, 0, 255);
	WurstplusSetting strings_a = create("Alpha", "HUDStringsColorA", 230, 0, 255);
	WurstplusSetting rainbow = create("Rainbow", "HUDRainbow", true);
	WurstplusSetting compass_scale = create("Compass Scale", "HUDCompassScale", 16, 1, 60);
	WurstplusSetting arraylist_mode = create("ArrayList", "HUDArrayList", "Free", combobox("Free", "Top R", "Top L", "Bottom R", "Bottom L"));
	WurstplusSetting show_all_pots = create("All Potions", "HUDAllPotions", false);
	WurstplusSetting max_player_list = create("Max Players", "HUDMaxPlayers", 24, 1, 64);
	
    @Override
    public void update() {
		if (rainbow.get_value(true)) {
			cycle_rainbow();
		}
    }

    @Override
	protected void disable() {
    	//you can never defeat me
    	this.set_enable();
	}

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