package me.trambled.turok.draw;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static me.trambled.ozark.ozarkclient.util.WrapperUtil.mc;
import static org.lwjgl.opengl.GL11.*;

/**
* @author 086
*
* Update by me.
* 08/04/20.
*
*/
public class RenderHelp extends Tessellator {
    public static RenderHelp INSTANCE = new RenderHelp();

    public RenderHelp() {
        super(0x200000);
    }

    public static void prepare(String mode_requested) {
    	int mode = 0;

    	if (mode_requested.equalsIgnoreCase("quads")) {
    		mode = GL_QUADS;
    	} else if (mode_requested.equalsIgnoreCase("lines")) {
    		mode = GL_LINES;
    	}

        prepare_gl();
        begin(mode);
    }

    public static void prepare_gl() {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(1.5F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.color(1, 1, 1);
    }

    public static void prepare_gl_2d() {
        glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        glEnable(GL11.GL_LINE_SMOOTH);
        glEnable(GL32.GL_DEPTH_CLAMP);
    }

    public static void release_gl_2d() {
        GL11.glDisable(GL32.GL_DEPTH_CLAMP);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(GL11.GL_FLAT);
        glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
    }

    public static void begin(int mode) {
        INSTANCE.getBuffer().begin(mode, DefaultVertexFormats.POSITION_COLOR);
    }

    public static void release() {
        render();
        release_gl();
    }

    public static void render() {
        INSTANCE.draw();
    }

    public static void release_gl() {
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
    }

    public static void draw_cube(BlockPos blockPos, int argb, String sides) {
        final int a = (argb >>> 24) & 0xFF;
        final int r = (argb >>> 16) & 0xFF;
        final int g = (argb >>> 8) & 0xFF;
        final int b = argb & 0xFF;
        draw_cube(blockPos, r, g, b, a, sides);
    }

    public static void draw_cube(float x, float y, float z, int argb, String sides) {
        final int a = (argb >>> 24) & 0xFF;
        final int r = (argb >>> 16) & 0xFF;
        final int g = (argb >>> 8) & 0xFF;
        final int b = argb & 0xFF;
        draw_cube(INSTANCE.getBuffer(), x, y, z, 1, 1, 1, r, g, b, a, sides);
    }

    public static void draw_cube(BlockPos blockPos, int r, int g, int b, int a, String sides) {
        draw_cube(INSTANCE.getBuffer(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, 1, 1, r, g, b, a, sides);
    }
    public static void drawTriangleOutline(float x, float y, float size, float widthDiv, float heightDiv, float outlineWidth, int color) {
        boolean blend = GL11.glIsEnabled(3042);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glLineWidth(outlineWidth);
        GL11.glBegin(2);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x - size / widthDiv, y - size);
        GL11.glVertex2d(x, y - size / heightDiv);
        GL11.glVertex2d(x + size / widthDiv, y - size);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        if (!blend) {
            GL11.glDisable(3042);
        }
        GL11.glDisable(2848);
    }

    public static void draw_cube_line(BlockPos blockPos, int argb, String sides) {
        final int a = (argb >>> 24) & 0xFF;
        final int r = (argb >>> 16) & 0xFF;
        final int g = (argb >>> 8) & 0xFF;
        final int b = argb & 0xFF;
        draw_cube_line(blockPos, r, g, b, a, sides);
    }

    public static void draw_cube_line(float x, float y, float z, int argb, String sides) {
        final int a = (argb >>> 24) & 0xFF;
        final int r = (argb >>> 16) & 0xFF;
        final int g = (argb >>> 8) & 0xFF;
        final int b = argb & 0xFF;
        draw_cube_line(INSTANCE.getBuffer(), x, y, z, 1, 1, 1, r, g, b, a, 1, sides);
    }

    public static void draw_cube_line(BlockPos blockPos, int r, int g, int b, int a, String sides) {
        draw_cube_line(INSTANCE.getBuffer(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, 1, 1, r, g, b, a, 1, sides);
    }



    public static BufferBuilder get_buffer_build() {
        return INSTANCE.getBuffer();
    }

    public static void draw_cube(final BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, String sides) {
        if (((boolean) Arrays.asList(sides.split("-")).contains("down")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("up")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("north")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("south")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("south")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("south")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
        }
    }



    public static void draw_cube_line(final BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, float width, String sides) {
        GL11.glLineWidth(width);
        if (((boolean) Arrays.asList(sides.split("-")).contains("downwest")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("upwest")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("downeast")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("upeast")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
        }

       if (((boolean) Arrays.asList(sides.split("-")).contains("downnorth")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("upnorth")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("downsouth")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("upsouth")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("nortwest")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("norteast")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("southweast")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
        }

        if (((boolean) Arrays.asList(sides.split("-")).contains("southeast")) || sides.equalsIgnoreCase("all")) {
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
        }
    }
    public static void draw_gradiant_cube(final BufferBuilder buffer, float x, float y, float z, float w, float h, float d, Color startColor, Color endColor, String sides) {
        int r1 = startColor.getRed();
        int g1 = startColor.getGreen();
        int b1 = startColor.getBlue();
        int a1 = startColor.getAlpha();

        int r2 = endColor.getRed();
        int g2 = endColor.getGreen();
        int b2 = endColor.getBlue();
        int a2 = endColor.getAlpha();

        List<String> sidesList = Arrays.asList(sides.split("-"));
        if (sidesList.contains("north") || sides.equalsIgnoreCase("all")) {
            buffer.pos(x + w, y, z).color(r1, g1, b1, a1).endVertex();
            buffer.pos(x, y, z).color(r1, g1, b1, a1).endVertex();
            buffer.pos(x, y + h, z).color(r2, g2, b2, a2).endVertex();
            buffer.pos(x + w, y + h, z).color(r2, g2, b2, a2).endVertex();
        }

        if (sidesList.contains("south") || sides.equalsIgnoreCase("all")) {
            buffer.pos(x, y, z + d).color(r1, g1, b1, a1).endVertex();
            buffer.pos(x + w, y, z + d).color(r1, g1, b1, a1).endVertex();
            buffer.pos(x + w, y + h, z + d).color(r2, g2, b2, a2).endVertex();
            buffer.pos(x, y + h, z + d).color(r2, g2, b2, a2).endVertex();
        }

        if (sidesList.contains("west") || sides.equalsIgnoreCase("all")) {
            buffer.pos(x, y, z).color(r1, g1, b1, a1).endVertex();
            buffer.pos(x, y, z + d).color(r1, g1, b1, a1).endVertex();
            buffer.pos(x, y + h, z + d).color(r2, g2, b2, a2).endVertex();
            buffer.pos(x, y + h, z).color(r2, g2, b2, a2).endVertex();
        }

        if (sidesList.contains("east") || sides.equalsIgnoreCase("all")) {
            buffer.pos(x + w, y, z + d).color(r1, g1, b1, a1).endVertex();
            buffer.pos(x + w, y, z).color(r1, g1, b1, a1).endVertex();
            buffer.pos(x + w, y + h, z).color(r2, g2, b2, a2).endVertex();
            buffer.pos(x + w, y + h, z + d).color(r2, g2, b2, a2).endVertex();
        }
    }

    public static void draw_gradiant_outline(final BufferBuilder buffer, double x, double y, double z, double height, Color startColor, Color endColor,  String sides) {
        List<String> sidesList = Arrays.asList(sides.split("-"));
        boolean drawAll = sides.equalsIgnoreCase("all");
        if (sidesList.contains("northwest") || drawAll) draw_gradiant_line(buffer, x, y, z, x, y + height, z, startColor, endColor); // NW
        if (sidesList.contains("northeast") || drawAll) draw_gradiant_line(buffer, x + 1, y, z, x + 1, y + height, z, startColor, endColor); // NE
        if (sidesList.contains("southwest") || drawAll) draw_gradiant_line(buffer, x, y, z + 1, x, y + height, z + 1, startColor, endColor); // SW
        if (sidesList.contains("southeast") || drawAll) draw_gradiant_line(buffer, x + 1, y, z + 1, x + 1, y + height, z + 1, startColor, endColor); // SE
    }

    public static void draw_gradiant_line(final BufferBuilder buffer, double x1, double y1, double z1, double x2, double y2, double z2, Color startColor, Color endColor) {
        buffer.pos(x1, y1, z1).color(startColor.getRed(), startColor.getGreen(), startColor.getBlue(), startColor.getAlpha()).endVertex();
        buffer.pos(x2, y2, z2).color(endColor.getRed(), endColor.getGreen(), endColor.getBlue(), endColor.getAlpha()).endVertex();
    }

    private static void colorVertex(double x, double y, double z, Color color, int alpha, BufferBuilder bufferbuilder) {
        bufferbuilder.pos(x - mc.getRenderManager().viewerPosX, y - mc.getRenderManager().viewerPosY, z - mc.getRenderManager().viewerPosZ).color(color.getRed(), color.getGreen(), color.getBlue(), alpha).endVertex();
    }
}