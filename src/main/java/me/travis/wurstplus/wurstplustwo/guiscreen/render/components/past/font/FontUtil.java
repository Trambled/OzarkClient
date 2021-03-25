package me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.font;

import me.travis.wurstplus.Wurstplus;
import net.minecraft.client.Minecraft;

public class FontUtil {

    /**
     * TODO: String width function
     */

    protected static Minecraft mc = Minecraft.getMinecraft();

    public static void drawString(String text, float x, float y, int colour) {
        if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Lato")) {
            Wurstplus.latoFont.drawString(text, x, y, colour);
        } else if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Verdana")) {
            Wurstplus.verdanaFont.drawString(text, x, y, colour);
        } else if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Arial")) {
            Wurstplus.arialFont.drawString(text, x, y, colour);
        } else {
            mc.fontRenderer.drawString(text, (int) x, (int) y, colour);
        }
    }

    public static void drawStringWithShadow(String text, float x, float y, int colour) {

        if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Lato")) {
            Wurstplus.latoFont.drawStringWithShadow(text, x, y, colour);
        } else if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Verdana")) {
            Wurstplus.verdanaFont.drawStringWithShadow(text, x, y, colour);
        } else if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Arial")) {
            Wurstplus.arialFont.drawStringWithShadow(text, x, y, colour);
        } else {
            mc.fontRenderer.drawStringWithShadow(text, (int) x, (int) y, colour);
        }
    }

    public static void drawTextHUD(String text, float x, float y, int colour) {
        if (Wurstplus.get_setting_manager().get_setting_with_tag("HUDEditor", "HUDShadow").get_value(true)) {
            drawStringWithShadow(text, x, y, colour);
        } else {
            drawString(text, x, y, colour);
        }
    }

    public static void drawText(String text, float x, float y, int colour) {
        if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFontShadow").get_value(true)) {
            drawStringWithShadow(text, x, y, colour);
        } else {
            drawString(text, x, y, colour);
        }
    }

    public static int getFontHeight() {
        if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Lato")) {
            return Wurstplus.latoFont.getHeight();
        } else if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Verdana")) {
            return Wurstplus.verdanaFont.getHeight();
        } else if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Arial")) {
            return Wurstplus.arialFont.getHeight();
        } else {
            return mc.fontRenderer.FONT_HEIGHT;
        }
    }
}
