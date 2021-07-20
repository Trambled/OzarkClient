package me.trambled.ozark.ozarkclient.util.font;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.util.render.RainbowUtil;

import java.awt.*;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;

public class FontUtil {

    public static void drawString(String text, float x, float y, int colour) {
        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Lato")) {
            Ozark.latoFont.drawString(text, x, y, colour);
        } else if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Verdana")) {
            Ozark.verdanaFont.drawString(text, x, y, colour);
        } else if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Arial")) {
            Ozark.arialFont.drawString(text, x, y, colour);
        } else {
            mc.fontRenderer.drawString(text, (int) x, (int) y, colour);
        }
    }

    public static void drawString(String text, float x, float y, int r, int g, int b, int a) {
        int colour = new Color(r, g, b, a).getRGB();
        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Lato")) {
            Ozark.latoFont.drawString(text, x, y, colour);
        } else if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Verdana")) {
            Ozark.verdanaFont.drawString(text, x, y, colour);
        } else if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Arial")) {
            Ozark.arialFont.drawString(text, x, y, colour);
        } else {
            mc.fontRenderer.drawString(text, (int) x, (int) y, colour);
        }
    }

    public static void drawStringWithShadow(String text, float x, float y, int colour) {

        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Lato")) {
            Ozark.latoFont.drawStringWithShadow(text, x, y, colour);
        } else if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Verdana")) {
            Ozark.verdanaFont.drawStringWithShadow(text, x, y, colour);
        } else if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Arial")) {
            Ozark.arialFont.drawStringWithShadow(text, x, y, colour);
        } else {
            mc.fontRenderer.drawStringWithShadow(text, (int) x, (int) y, colour);
        }
    }

    public static void drawStringWithShadow(String text, float x, float y, int r, int g, int b, int a) {
        int colour = new Color(r, g, b, a).getRGB();
        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Lato")) {
            Ozark.latoFont.drawStringWithShadow(text, x, y, colour);
        } else if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Verdana")) {
            Ozark.verdanaFont.drawStringWithShadow(text, x, y, colour);
        } else if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Arial")) {
            Ozark.arialFont.drawStringWithShadow(text, x, y, colour);
        } else {
            mc.fontRenderer.drawStringWithShadow(text, (int) x, (int) y, colour);
        }
    }

    public static void drawText(String text, float x, float y, int colour) {
        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFontShadow").get_value(true)) {
            drawStringWithShadow(text, x, y, colour);
        } else {
            drawString(text, x, y, colour);
        }
    }

    public static int getFontHeight() {
        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Lato")) {
            return Ozark.latoFont.getHeight();
        } else if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Verdana")) {
            return Ozark.verdanaFont.getHeight();
        } else if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Arial")) {
            return Ozark.arialFont.getHeight();
        } else {
            return mc.fontRenderer.FONT_HEIGHT;
        }
    }

    public static int getFontWidth(String text) {
        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Lato")) {
            return Ozark.latoFont.getStringWidth(text);
        } else if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Verdana")) {
            return Ozark.verdanaFont.getStringWidth(text);
        } else if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Arial")) {
            return Ozark.arialFont.getStringWidth(text);
        } else {
            return RainbowUtil.get_string_width(text);
        }
    }

    public static void drawTotalCenteredStringWithShadow3(String text, float x, float y, Color color) {
        drawString(text, x - getFontWidth(text) / 2, y - getFontHeight() / 2F, color.getRGB());
    }}
