package me.trambled.ozark.ozarkclient.util.render;

import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

/**
 * @author lukflug
 */

public class OzarkColor extends Color {

    private static final long serialVersionUID = 1L;

    public OzarkColor(int rgb) {
        super(rgb);
    }

    public OzarkColor(int rgba, boolean hasalpha) {
        super(rgba, hasalpha);
    }

    public OzarkColor(int r, int g, int b) {
        super(r, g, b);
    }

    public OzarkColor(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public OzarkColor(Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public OzarkColor(OzarkColor color, int a) {
        super(color.getRed(), color.getGreen(), color.getBlue(), a);
    }

    public static OzarkColor fromHSB(float hue, float saturation, float brightness) {
        return new OzarkColor(Color.getHSBColor(hue, saturation, brightness));
    }

    public float getHue() {
        return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[0];
    }

    public float getSaturation() {
        return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[1];
    }

    public float getBrightness() {
        return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[2];
    }

    public void glColor() {
        GlStateManager.color(getRed() / 255.0f, getGreen() / 255.0f, getBlue() / 255.0f, getAlpha() / 255.0f);
    }
}
