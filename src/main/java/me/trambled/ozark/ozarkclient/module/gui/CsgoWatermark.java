package me.trambled.ozark.ozarkclient.module.gui;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.font.FontUtil;
import me.trambled.ozark.ozarkclient.util.render.GuiUtil;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class CsgoWatermark extends Module {
    private int startcolor1;
    private int endcolor1;
    public CsgoWatermark() {
        super(Category.GUI);

        this.name        = "CsgoWatermark";
        this.tag         = "CsgoWatermark";
        this.description = "csgo watermark.";
    }
    Setting redCS = create("R", "csred", 255, 0, 255);
    Setting greenCS = create("G", "csgreen", 255, 0, 255);
    Setting blueCS = create("B", "csblue", 255, 0, 255);
    Setting redCS1 = create("R1", "csred1", 255, 0, 255);
    Setting greenCS1 = create("G1", "csgreen1", 255, 0, 255);
    Setting blueCS1 = create("B1", "csblue1", 255, 0, 255);
    Setting alphaCS = create("A", "csalpha", 255, 0, 255);
    Setting alphaCS1 = create("A1", "csalpha1", 255, 0, 255);

    int r = redCS.get_value(1);
    int b = blueCS.get_value(1);
    int g = greenCS.get_value(1);
    int a =   alphaCS.get_value(1);

    @Override
     public void render() {
        startcolor1 = this.toRGBA(redCS.get_value(1), greenCS.get_value(1), blueCS.get_value(1), alphaCS.get_value(1));
        endcolor1 = this.toRGBA(redCS1.get_value(1), greenCS1.get_value(1), blueCS1.get_value(1), alphaCS1.get_value(1));
        this.drawRectangleCorrectly(4, 5, 51, 12, this.toRGBA(20, 20, 20, 200));
        GuiUtil.draw_outline(4, 5 , 51 + 4, 5 + 12 , r, g, b, a);
        this.drawGradientSideways(4, 4, 55, 6.8 , startcolor1, endcolor1);
        FontUtil.drawString(Ozark.DISPLAY_NAME, 5.5f, +7,r,g,b,a);
    }
    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }
    public static void drawRectangleCorrectly(int x, int y, int w, int h, int color) {
        GL11.glLineWidth(1.0f);
        Gui.drawRect(x, y, x + w, y + h, color);
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
    }}




