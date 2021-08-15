package me.trambled.ozark.ozarkclient.util.render;

import me.trambled.ozark.Ozark;
import me.trambled.turok.Turok;
import me.trambled.turok.draw.RenderHelp;
import me.trambled.turok.task.Rect;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;

// Travis


public class GuiUtil {
	private static final FontRenderer font_renderer = mc.fontRenderer;

	private final float size;

	public GuiUtil(float size) {
		this.size = size;
	}

	public static void draw_rect(int x, int y, int w, int h, int r, int g, int b, int a) {
		Gui.drawRect(x, y, w, h, new OzarkColor(r, g, b, a).hex());
	}

	public static void draw_rect(int x, int y, int w, int h, int r, int g, int b, int a, int rainbowOff) {
		if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIRainbowRolling").get_value(true)) {
			float hueIncrement = 0.01f;
			float hue = Color.RGBtoHSB(r, g, b, null)[0];
			float saturation = Color.RGBtoHSB(r, g, b, null)[1];
			float brightness = Color.RGBtoHSB(r, g, b, null)[2];
			hue += hueIncrement * rainbowOff;
			int color = Color.HSBtoRGB(hue, saturation, brightness);
			Gui.drawRect(x, y, w, h, color);
		} else {
			Gui.drawRect(x, y, w, h, new OzarkColor(r, g, b, a).hex());
		}
	}

	public static void draw_outline(int x, int y, int x2, int y2, Color color)
	{
		Gui.drawRect(x - 1, y - 1, x, y2 + 1, color.getRGB());
		Gui.drawRect(x2, y - 1, x2 + 1, y2 + 1, color.getRGB());
		Gui.drawRect(x, y - 1, x2, y, color.getRGB());
		Gui.drawRect(x, y2, x2, y2 + 1, color.getRGB());
	}

	public static void draw_outline(int x, int y, int x2, int y2, int r, int g, int b, int a)
	{
		Color color = new Color(r, g, b, a);
		Gui.drawRect(x - 1, y - 1, x, y2 + 1, color.getRGB());
		Gui.drawRect(x2, y - 1, x2 + 1, y2 + 1, color.getRGB());
		Gui.drawRect(x, y - 1, x2, y, color.getRGB());
		Gui.drawRect(x, y2, x2, y2 + 1, color.getRGB());
	}


	public static void draw_rect_gradient(int x, int y, int w, int h, int r, int g, int b, int a, int rainbowOff) {
		if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIRainbowRolling").get_value(true)) {
			float hueIncrement = 0.01f;
			float hue = Color.RGBtoHSB(r, g, b, null)[0];
			float saturation = Color.RGBtoHSB(r, g, b, null)[1];
			float brightness = Color.RGBtoHSB(r, g, b, null)[2];
			hue += hueIncrement * rainbowOff;
			float second_hue = hue + 0.01f;
			int color = Color.HSBtoRGB(hue, saturation, brightness);
			int second_color = Color.HSBtoRGB(second_hue, saturation, brightness);
			drawGradientRectP(x, y, w, h, color, second_color);
		} else {
			Gui.drawRect(x, y, w, h, new OzarkColor(r, g, b, a).hex());
		}
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

		return (int) ( font_renderer.FONT_HEIGHT * this.size);
	}

	public int get_string_width(String string) {

		return font_renderer.getStringWidth(string);
	}

	public static void drawGradientRectP(int left, int top, int right, int bottom, int startColor, int endColor)
	{
		float f = (float)(startColor >> 24 & 255) / 255.0F;
		float f1 = (float)(startColor >> 16 & 255) / 255.0F;
		float f2 = (float)(startColor >> 8 & 255) / 255.0F;
		float f3 = (float)(startColor & 255) / 255.0F;
		float f4 = (float)(endColor >> 24 & 255) / 255.0F;
		float f5 = (float)(endColor >> 16 & 255) / 255.0F;
		float f6 = (float)(endColor >> 8 & 255) / 255.0F;
		float f7 = (float)(endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(right, top, 300).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos(left, top, 300).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos(left, bottom, 300).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.pos(right, bottom, 300).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
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