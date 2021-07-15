package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;
import me.trambled.ozark.ozarkclient.module.gui.CsgoWatermark;

import java.awt.*;

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

		String info = Ozark.DISPLAY_NAME + " | " + mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime() + "ms | " + mc.getDebugFPS() + "fps | " + mc.player.getName();

		create_rect(-6, -6, mc.fontRenderer.getStringWidth(info) + 6, mc.fontRenderer.FONT_HEIGHT + 6, 40, 40, 40, 255);
		create_rect(-5, -5, mc.fontRenderer.getStringWidth(info) + 5, mc.fontRenderer.FONT_HEIGHT + 5, 70, 70, 70, 255);
		create_rect(-4, -4, mc.fontRenderer.getStringWidth(info) + 4, mc.fontRenderer.FONT_HEIGHT + 4, 40, 40, 40, 255);
		create_rect(-3, -3, mc.fontRenderer.getStringWidth(info) + 3, mc.fontRenderer.FONT_HEIGHT + 3, 0, 0, 0, 255);
		drawHLineG(this.get_x() - 3, this.get_y() - 3, mc.fontRenderer.getStringWidth(info) + 6, getColour().hashCode(), this.getFurtherColour(Ozark.get_setting_manager().get_setting_with_tag("HUD", "offset").get_value(1)).hashCode());

		create_line(info, 1, 2, nl_r, nl_g, nl_b, nl_a);

		set_width(mc.fontRenderer.getStringWidth(info) + 3);
		set_height(mc.fontRenderer.FONT_HEIGHT + 2);
	}

	public static void drawHLineG(int x, int y, int length, int color, int color2){
		CsgoWatermark.drawGradientSideways(x, y, x+length, y+1, color, color2);
	}
	public static Color getFurtherColour(int offset) {
		return Color.getHSBColor(((System.currentTimeMillis() + offset) % (360 * 32)) / (360f * 32), 1, 1);
	}
	public static Color getColour() {
		return Color.getHSBColor((System.currentTimeMillis() % (360 * 32)) / (360f * 32), 1, 1);
	}}
