package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

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

		String info = Ozark.DISPLAY_NAME + " | " + get_ping() + "ms | " + Minecraft.getDebugFPS () + "fps | " + mc.player.getName();

		create_rect(-6, -6, mc.fontRenderer.getStringWidth(info) + 6, mc.fontRenderer.FONT_HEIGHT + 6, 40, 40, 40, 255);
		create_rect(-5, -5, mc.fontRenderer.getStringWidth(info) + 5, mc.fontRenderer.FONT_HEIGHT + 5, 70, 70, 70, 255);
		create_rect(-4, -4, mc.fontRenderer.getStringWidth(info) + 4, mc.fontRenderer.FONT_HEIGHT + 4, 40, 40, 40, 255);
		create_rect(-3, -3, mc.fontRenderer.getStringWidth(info) + 3, mc.fontRenderer.FONT_HEIGHT + 3, 0, 0, 0, 255);
		drawHLineG(this.get_x() - 3, this.get_y() - 3, mc.fontRenderer.getStringWidth(info) + 6, getColour().hashCode(), getFurtherColour(Ozark.get_setting_manager().get_setting_with_tag("HUD", "offset").get_value(1)).hashCode());

		create_line(info, 1, 2, nl_r, nl_g, nl_b, nl_a);

		set_width(mc.fontRenderer.getStringWidth(info) + 3);
		set_height(mc.fontRenderer.FONT_HEIGHT + 2);
	}

	public static void drawHLineG(int x, int y, int length, int color, int color2){
		drawGradientSideways(x, y, x+length, y+1, color, color2);
	}
	public static Color getFurtherColour(int offset) {
		return Color.getHSBColor(((System.currentTimeMillis() + offset) % (360 * 32)) / (360f * 32), 1, 1);
	}
	public static Color getColour() {
		return Color.getHSBColor((System.currentTimeMillis() % (360 * 32)) / (360f * 32), 1, 1);
	}


	public String get_ping() {
		try {
			int ping = Objects.requireNonNull ( mc.getConnection ( ) ).getPlayerInfo(mc.player.getUniqueID()).getResponseTime();
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

	public static void drawGradientSideways(final double leftpos, final double top, final double right, final double bottom, final int col1, final int col2) {
		final float f = (col1 >> 24 & 0xFF) / 255.0f;
		final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
		final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
		final float f4 = (col1 & 0xFF) / 255.0f;
		final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
		final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
		final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
		final float f8 = (col2 & 0xFF) / 255.0f;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glShadeModel(7425);
		GL11.glPushMatrix();
		GL11.glBegin(7);
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glVertex2d(leftpos, top);
		GL11.glVertex2d(leftpos, bottom);
		GL11.glColor4f(f6, f7, f8, f5);
		GL11.glVertex2d(right, bottom);
		GL11.glVertex2d(right, top);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glShadeModel(7424);
	}

}
