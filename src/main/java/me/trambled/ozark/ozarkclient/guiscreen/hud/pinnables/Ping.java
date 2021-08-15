package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;


import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;

import java.util.Objects;

public class Ping extends Pinnable {
    
    public Ping() {
        super("Ping", "Ping", 1, 0, 0);
    }

    @Override
	public void render() {
		int nl_r = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
		int nl_g = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
		int nl_b = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);
		int nl_a = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(1);

		String line = "[Ping]: " + get_ping();

		create_line(line, this.docking(1, line), 2, nl_r, nl_g, nl_b, nl_a);

		this.set_width(this.get(line, "width") + 2);
		this.set_height(this.get(line, "height") + 2);
    }
    
    public String get_ping() {
        try {
            int ping = Objects.requireNonNull ( mc.getConnection ( ) ).getPlayerInfo(mc.player.getUniqueID()).getResponseTime();
		if (ping <= 50) {
			return "\u00A7a"+ ping;
		} else if (ping <= 150) {
			return "\u00A73"+ ping;
		} else {
			return "\u00A74"+ ping;
		}
        } catch (Exception e) {
            return "0";
        }
		
	}

}
