package me.trambled.ozark.ozarkclient.util.render.mainmenu;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.GuiCustomMainMenu;
import me.trambled.ozark.ozarkclient.util.font.FontUtil;
import me.trambled.ozark.ozarkclient.util.render.RainbowUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.opengl.GL11;


import java.awt.*;


/**
 * Created by peanut on 26/02/2021 //hyderogen client
 */
public class MainMenu extends GuiScreen {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static void drawMenu(int mouseX, int mouseY) {

        drawRect(40, 0, 140, GuiCustomMainMenu.getScaledRes().getScaledHeight(), 0x60000000);

        String mds = String.format("%s mods loaded, %s mods active", Loader.instance().getModList().size(), Loader.instance().getActiveModList().size());
        String fml = String.format("Powered by Forge %s", ForgeVersion.getVersion());
        String mcp = "MCP 9.42";
        String mcv = "Minecraft 1.12.2";
        String name = String.format("%s %s", Ozark.get_name(), Ozark.get_version());
        String mname = String.format("Logged in as \u00A77%s", Minecraft.getMinecraft().getSession().getUsername());

        FontUtil.drawStringWithShadow(mds, GuiCustomMainMenu.getScaledRes().getScaledWidth() - FontUtil.getFontWidth(mds) - 4, GuiCustomMainMenu.getScaledRes().getScaledHeight() - 14, Color.WHITE.getRGB());
        FontUtil.drawStringWithShadow(fml, GuiCustomMainMenu.getScaledRes().getScaledWidth() - FontUtil.getFontWidth(fml) - 4, GuiCustomMainMenu.getScaledRes().getScaledHeight() - 26, Color.WHITE.getRGB());
        FontUtil.drawStringWithShadow(mcp, GuiCustomMainMenu.getScaledRes().getScaledWidth() - FontUtil.getFontWidth(mcp) - 4, GuiCustomMainMenu.getScaledRes().getScaledHeight() - 38, Color.WHITE.getRGB());
        FontUtil.drawStringWithShadow(mcv, GuiCustomMainMenu.getScaledRes().getScaledWidth() - FontUtil.getFontWidth(mcv) - 4, GuiCustomMainMenu.getScaledRes().getScaledHeight() - 50, Color.WHITE.getRGB());

        RainbowUtil.drawRainbowStringChatCustomFont(name, (float) GuiCustomMainMenu.getScaledRes().getScaledWidth() - (float) FontUtil.getFontWidth(name) - 4, (float) 4, RainbowUtil.getMultiColour().getRGB(), 200f);
        FontUtil.drawStringWithShadow("Developed by " + TextFormatting.GRAY + "Trambled, x3th0_" + TextFormatting.RESET + " and " + TextFormatting.GRAY + "ProfKambing", GuiCustomMainMenu.getScaledRes().getScaledWidth() - FontUtil.getFontWidth("Developed by " + "Trambled, Ethan and ProfKambing") - 4, 28, Color.WHITE.getRGB());
        FontUtil.drawStringWithShadow(mname, GuiCustomMainMenu.getScaledRes().getScaledWidth() - FontUtil.getFontWidth(mname) - 4, 16, Color.WHITE.getRGB());

        float scale = 5F;

        GL11.glScalef(scale, scale, scale);
        RainbowUtil.drawRainbowStringChat(Ozark.get_name(), (int)(GuiCustomMainMenu.getScaledRes().getScaledWidth() / 2 / scale - 13), (int)(GuiCustomMainMenu.getScaledRes().getScaledHeight() / 2 / scale - 5F), RainbowUtil.getMultiColour().getRGB(), 50f);
        GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);


    }

    public static void rect(float x1, float y1, float x2, float y2, int fill) {
        GlStateManager.color(0, 0, 0);
        GL11.glColor4f(0, 0, 0, 0);

        float f = (fill >> 24 & 0xFF) / 255.0F;
        float f1 = (fill >> 16 & 0xFF) / 255.0F;
        float f2 = (fill >> 8 & 0xFF) / 255.0F;
        float f3 = (fill & 0xFF) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);

        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y1);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x1, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }}
