package me.trambled.ozark.ozarkclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;

// Travis


public class RainbowUtil {
	private static FontRenderer font_renderer = Minecraft.getMinecraft().fontRenderer;
	private static Minecraft mc = Minecraft.getMinecraft();
	private static boolean flag;
	private static boolean chat_flag = false;
	private static boolean shouldRainbow;

	public static void drawRainbowStringChat(String text, float x, float y, int color, float factor, boolean flow) {
		Color currentColor = new Color(color);
		if (!flow) {
			drawString(text, x, y, color);
			return;
		}
		float hueIncrement = 1.0f / factor;
		float currentHue = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[0];
		float saturation = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[1];
		float brightness = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[2];
		int currentWidth = 0;
		for (int i = 0; i < text.length(); ++i) {
			char currentChar = text.charAt(i);
			char nextChar = text.charAt(MathUtil.clamp(i + 1, 0, text.length() - 1));
			char nextNextChar = text.charAt(MathUtil.clamp(i + 2, 0, text.length() - 2));
			if ((String.valueOf(currentChar) + nextChar + nextNextChar).equals("\u00a74O")) {
				shouldRainbow = true;
			} else if ((String.valueOf(currentChar) + nextChar + nextNextChar).equals("\u00a77>")) {
				shouldRainbow = false;
			}
			if (!shouldRainbow || (!String.valueOf(currentChar).equals("\u00a7") && !chat_flag)) {
				drawString(String.valueOf(currentChar), x + currentWidth, y, shouldRainbow ? currentColor.getRGB() : Color.WHITE.getRGB());
				currentWidth += get_string_width(String.valueOf(currentChar));
				currentColor = new Color(Color.HSBtoRGB(currentHue, saturation, brightness));
				currentHue += hueIncrement;
			} else if (chat_flag) {
				chat_flag = false;
			} else if (String.valueOf(currentChar).equals("\u00a7")) {
				chat_flag = true;
			}
		}
	}

	public static void drawRainbowString(String text, int x, int y, int color, float factor) {
		Color currentColor = new Color(color);
		float hueIncrement = 1.0f / factor;
		float currentHue = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[0];
		float saturation = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[1];
		float brightness = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[2];
		int currentWidth = 0;
		for (int i = 0; i < text.length(); ++i) {
			char currentChar = text.charAt(i);
			if (String.valueOf(currentChar).equals("\u00a7")) {
				flag = true;
				continue;
			}
			if (flag) {
				flag = false;
				continue;
			}
			GuiUtil.draw_string(String.valueOf(currentChar), x + currentWidth, y, currentColor.getRGB());
			currentWidth += get_string_width(String.valueOf(currentChar));
			currentColor = new Color(Color.HSBtoRGB(currentHue, saturation, brightness));
			currentHue += hueIncrement;
		}
	}

	public static int get_string_width(String string) {
		FontRenderer fontRenderer = font_renderer;

		return (fontRenderer.getStringWidth(string));
	}


	public static int get_string_height() {
		FontRenderer fontRenderer = font_renderer;

		return (fontRenderer.FONT_HEIGHT);
	}

	public static float drawString(String text, float x, float y, int color) {
		return mc.fontRenderer.drawString(text, x, y, color, true);
	}


}