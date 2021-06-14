package me.trambled.ozark.ozarkclient.util;

import me.trambled.turok.Turok;
import me.trambled.turok.draw.RenderHelp;
import me.trambled.turok.task.Rect;
import static me.trambled.ozark.ozarkclient.util.WrapperUtil.mc;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;

// Travis


public class GuiUtil {
	private static FontRenderer font_renderer = mc.fontRenderer;

	private float size;

	public GuiUtil(float size) {
		this.size = size;
	}

	public static void draw_rect(int x, int y, int w, int h, int r, int g, int b, int a) {
		Gui.drawRect(x, y, w, h, new OzarkColor(r, g, b, a).hex());
	}

	public static void draw_rect(int x, int y, int w, int h, int r, int g, int b, int a, int size, String type) {
		if (Arrays.asList(type.split("-")).contains("up")) {
			draw_rect(x, y, x + w, y + size, r, g, b, a);
		}

		if (Arrays.asList(type.split("-")).contains("down")) {
			draw_rect(x, y + h - size, x + w, y + h, r, g, b, a);
		}

		if (Arrays.asList(type.split("-")).contains("left")) {
			draw_rect(x, y, x + size, y + h, r, g, b, a);
		}

		if (Arrays.asList(type.split("-")).contains("right")) {
			draw_rect(x + w - size, y, x + w, y + h, r, g, b, a);
		}
	}


	public static void draw_rect(Rect rect, int r, int g, int b, int a) {
		Gui.drawRect(rect.get_x(), rect.get_y(), rect.get_screen_width(), rect.get_screen_height(), new OzarkColor(r, g, b, a).hex());
	}

	public static void draw_string(String string, int x, int y, int r, int g, int b, int a) {
		font_renderer.drawStringWithShadow(string, x, y, new OzarkColor(r, g, b, a).hex());
	}

	public static void draw_string(String string, int x, int y, int color) {
		font_renderer.drawStringWithShadow(string, x, y, color);
	}

	public void draw_string_gl(String string, int x, int y, int r, int g, int b) {
		Turok resize_gl = new Turok("Resize");

		resize_gl.resize(x, y, this.size);

		font_renderer.drawString(string, x, y, new OzarkColor(r, g, b).hex());

		resize_gl.resize(x, y, this.size, "end");

		GL11.glPushMatrix();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);

		GlStateManager.enableBlend();

		GL11.glPopMatrix();

		RenderHelp.release_gl();
	}

	public int get_string_height() {
		FontRenderer fontRenderer = font_renderer;

		return (int) (fontRenderer.FONT_HEIGHT * this.size);
	}

	public int get_string_width(String string) {
		FontRenderer fontRenderer = font_renderer;

		return (int) (fontRenderer.getStringWidth(string));
	}

	public static class OzarkColor extends Color {
		public OzarkColor(int r, int g, int b, int a) {
			super(r, g, b, a);
		}

		public OzarkColor(int r, int g, int b) {
			super(r, g, b);
		}

		public int hex() {
			return getRGB();
		}
	}
}