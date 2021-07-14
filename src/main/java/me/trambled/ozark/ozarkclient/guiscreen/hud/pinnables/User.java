package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;
import me.trambled.ozark.ozarkclient.util.world.TimeUtil;

public class User extends Pinnable {
	public User() {
		super("User", "User", 1, 0, 0);
	}

	@Override
	public void render() {
		int nl_r = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
		int nl_g = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
		int nl_b = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);
		int nl_a = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(1);

		String line;
		int time = TimeUtil.get_hour();

		if (Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDUserMode").in("Time")) {
			if (time >= 0 && time < 12) {
				line = "Morning, " + mc.player.getName() + " you smell good today :)";
			} else if (time >= 12 && time < 16) {
				line = "Afternoon, "  + mc.player.getName() + " you're looking good today :)";
			} else if (time >= 16 && time < 24) {
				line = "Evening, " + mc.player.getName() + " you smell good today :)";
			} else {
				line = "Welcome, " +  mc.player.getName() + " you're looking fine today :)";
			}
		} else {
			line = "Welcome, " + mc.player.getName();
		}

		create_line(line, this.docking(1, line), 2, nl_r, nl_g, nl_b, nl_a);

		this.set_width(this.get(line, "width") + 2);
		this.set_height(this.get(line, "height") + 2);
	}

}
