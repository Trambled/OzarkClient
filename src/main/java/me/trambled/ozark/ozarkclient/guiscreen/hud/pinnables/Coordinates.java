package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;


public class Coordinates extends Pinnable {
	ChatFormatting dg = ChatFormatting.DARK_GRAY;
	ChatFormatting db = ChatFormatting.DARK_BLUE;
	ChatFormatting dr = ChatFormatting.DARK_RED;

	public Coordinates() {
		super("Coordinates", "Coordinates", 1, 0, 0);
	}

	@Override
	public void render() {
		int nl_r = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
		int nl_g = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
		int nl_b = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);
		int nl_a = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(1);

		String x = Ozark.g + "[" + Ozark.r + (int) ( mc.player.posX ) + Ozark.g + "]" + Ozark.r;
		String y = Ozark.g + "[" + Ozark.r + (int) ( mc.player.posY ) + Ozark.g + "]" + Ozark.r;
		String z = Ozark.g + "[" + Ozark.r + (int) ( mc.player.posZ ) + Ozark.g + "]" + Ozark.r;

		String x_nether = Ozark.g + "[" + Ozark.r + Math.round ( mc.player.dimension != - 1 ? ( mc.player.posX / 8 ) : ( mc.player.posX * 8 ) ) + Ozark.g + "]" + Ozark.r;
		String z_nether = Ozark.g + "[" + Ozark.r + Math.round ( mc.player.dimension != - 1 ? ( mc.player.posZ / 8 ) : ( mc.player.posZ * 8 ) ) + Ozark.g + "]" + Ozark.r;

		String line = "XYZ " + x + y + z + " XZ " + x_nether + z_nether;

		create_line(line, this.docking(1, line), 2, nl_r, nl_g, nl_b, nl_a);

		this.set_width(this.get(line, "width"));
		this.set_height(this.get(line, "height") + 2);
	}
}