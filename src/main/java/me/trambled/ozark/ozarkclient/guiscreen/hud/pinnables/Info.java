package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;

public class Info extends Pinnable {
	public Info() {
		super("Info", "Info", 1, 0, 0);
	}

	@Override
	public void render() {
		int nl_r = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
		int nl_g = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
		int nl_b = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);
		int nl_a = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(1);

		String info = Ozark.DISPLAY_NAME + " | " + get_ping() + "ms | " + get_fps() + "fps";
		create_rect(-6, -6, mc.fontRenderer.getStringWidth(info) + 6, mc.fontRenderer.FONT_HEIGHT + 6, 40, 40, 40, 255);
		create_rect(-5, -5, mc.fontRenderer.getStringWidth(info) + 5, mc.fontRenderer.FONT_HEIGHT + 5, 70, 70, 70, 255);
		create_rect(-4, -4, mc.fontRenderer.getStringWidth(info) + 4, mc.fontRenderer.FONT_HEIGHT + 4, 40, 40, 40, 255);
		create_rect(-3, -3, mc.fontRenderer.getStringWidth(info) + 3, mc.fontRenderer.FONT_HEIGHT + 3, 0, 0, 0, 255);

		create_line(info, 1, 2, nl_r, nl_g, nl_b, nl_a);

		set_width(mc.fontRenderer.getStringWidth(info) + 3);
		set_height(mc.fontRenderer.FONT_HEIGHT + 2);
	}

	public String get_ping() {
		try {
			int ping = mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime();
			if (ping <= 50) {
				return "\u00A7a"+(ping);
			} else if (ping <= 150) {
				return "\u00A73"+(ping);
			} else {
				return "\u00A74"+(ping);
			}
		} catch (Exception e) {
			return "0";
		}

	}

	public String get_fps() {
		int fps = mc.getDebugFPS();
		if (fps >= 60) {
			return "\u00A7a"+(fps);
		} else if (fps >= 30) {
			return "\u00A73"+(fps);
		} else {
			return "\u00A74"+(fps);
		}
	}
}