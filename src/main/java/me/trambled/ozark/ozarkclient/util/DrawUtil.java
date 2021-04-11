package me.trambled.ozark.ozarkclient.util;

import me.trambled.turok.Turok;
import me.trambled.turok.draw.RenderHelp;
import me.trambled.turok.task.Rect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;

// Travis


public class DrawUtil {
	private static FontRenderer font_renderer = Minecraft.getMinecraft().fontRenderer;

	private float size;

	public DrawUtil(float size) {
		this.size = size;
	}

	public static void draw_rect(int x, int y, int w, int h, int r, int g, int b, int a) {
		Gui.drawRect(x, y, w, h, new TravisColor(r, g, b, a).color_int());
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
		Gui.drawRect(rect.get_x(), rect.get_y(), rect.get_screen_width(), rect.get_screen_height(), new TravisColor(r, g, b, a).color_int());
	}

	public static void draw_string(String string, int x, int y, int r, int g, int b, int a) {
		font_renderer.drawStringWithShadow(string, x, y, new TravisColor(r, g, b, a).color_int());
	}

	public void draw_string_gl(String string, int x, int y, int r, int g, int b) {
		Turok resize_gl = new Turok("Resize");

		resize_gl.resize(x, y, this.size);

		font_renderer.drawString(string, x, y, new TravisColor(r, g, b).color_int());

		resize_gl.resize(x, y, this.size, "end");

		GL11.glPushMatrix();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);

		GlStateManager.enableBlend();

		GL11.glPopMatrix();

		RenderHelp.release_gl();
	}

	public static Color rainbowEffect(long offset, float fade){
		float hue = (float) (System.nanoTime() + offset) / 1.0E10F % 1.0F;
		long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()), 16);
		Color c = new Color((int) color);
		return new Color(c.getRed()/255.0F*fade, c.getGreen()/255.0F*fade, c.getBlue()/255.0F*fade, c.getAlpha()/255.0F);
	}


	public int get_string_height() {
		FontRenderer fontRenderer = font_renderer;

		return (int) (fontRenderer.FONT_HEIGHT * this.size);
	}

	public int get_string_width(String string) {
		FontRenderer fontRenderer = font_renderer;

		return (int) (fontRenderer.getStringWidth(string) * this.size);
	}

	public static class TravisColor extends Color {
		public TravisColor(int r, int g, int b, int a) {
			super(r, g, b, a);
		}

		public TravisColor(int r, int g, int b) {
			super(r, g, b);
		}

		public int color_int() {
			return getRGB();
		}
	}
}