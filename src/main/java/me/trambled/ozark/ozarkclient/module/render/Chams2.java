package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.event.events.EventRenderEntity;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class Chams2 extends Module {

    public Chams2() {
        super(Category.RENDER);

        this.name = "ChamsRewrite";
        this.tag = "ChamsRewrite";
        this.description = "Shiny ooooooooooooooooooo";
    }
    Setting mode = create("Mode", "ChamsMode", "Color", combobox("Texture", "Color"));
    Setting range = create("Range", "ChamsRange", 100, 0, 250);
    Setting player = create("Player", "ChamsPlayer", true);
    Setting mob = create("Mob", "ChamsMob", false);
    Setting crystal = create("Crystals", "ChamsCrystals", true);
    Setting self = create("Self", "ChamsSelf", true);
    Setting rainbow = create("Rainbow", "ChamsRainbow", false);
    Setting r = create("r", "ChamsR", 70, 0, 255);
    Setting g = create("g", "ChamsG", 150, 0, 255);
    Setting b = create("b", "ChamsB", 200, 0, 255);
    Setting a = create("a", "ChamsA", 100, 0, 255);

    @Override
    public void update() {
        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };
        int color_rgb = Color.HSBtoRGB(tick_color[0], 1, 1);
        if (rainbow.get_value(true)) {
            r.set_value(((color_rgb >> 16) & 0xFF));
            g.set_value(((color_rgb >> 8) & 0xFF));
            b.set_value((color_rgb & 0xFF));
        }
    }

    @EventHandler
    public Listener<EventRenderEntity.Head> renderHeadListener = new Listener<>(event -> {
        if (event.getType() == EventRenderEntity.Type.COLOR && mode.in("Texture")) return;
        else if (event.getType() == EventRenderEntity.Type.TEXTURE && mode.in("Color")) return;
        Entity entity = event.getEntity();
        if (mc.player.getDistance(entity) > range.get_value(1)) return;
        if (entity instanceof EntityPlayer && player.get_value(true) && (self.get_value(true) || entity != mc.player)) {
            renderChamsPre(true);
        }

        if (mob.get_value(true) && (entity instanceof EntityCreature || entity instanceof EntitySlime || entity instanceof EntitySquid)) {
            renderChamsPre(false);
        }

        if (crystal.get_value(true) && entity instanceof EntityEnderCrystal) {
            renderChamsPre(false);
        }
    });

    @EventHandler
    public Listener<EventRenderEntity.Return> returnListener = new Listener<>(event -> {
        if (event.getType() == EventRenderEntity.Type.COLOR && mode.in("Texture")) return;
        else if (event.getType() == EventRenderEntity.Type.TEXTURE && mode.in("Color")) return;
        Entity entity = event.getEntity();
        if (mc.player.getDistance(entity) > range.get_value(1)) return;
        if (entity instanceof EntityPlayer && player.get_value(true) && (self.get_value(true) || entity != mc.player)) {
            renderChamsPost(true);
        }

        if (mob.get_value(true) && (entity instanceof EntityCreature || entity instanceof EntitySlime || entity instanceof EntitySquid)) {
            renderChamsPost(false);
        }

        if (crystal.get_value(true) && entity instanceof EntityEnderCrystal) {
            renderChamsPost(false);
        }
    });

    private void renderChamsPre(boolean isPlayer) {
        if (mode.in("Texture")) {
            createChamsPre();
        } else if (mode.in("Color")) {
            createColorPre(isPlayer);
        }
    }

    private void renderChamsPost(boolean isPlayer) {
        if (mode.in("Texture")) {
            createChamsPost();
        } else if (mode.in("Color")) {
            createColorPost(isPlayer);
        }
    }

    private void createChamsPre() {
        mc.getRenderManager().setRenderShadow(false);
        mc.getRenderManager().setRenderOutlines(false);
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(1.0f, -5300000.0f);
        GlStateManager.popMatrix();
    }

    private void createColorPre(boolean isPlayer) {
        mc.getRenderManager().setRenderShadow(false);
        mc.getRenderManager().setRenderOutlines(false);
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(1.0f, -5300000.0f);
        glDisable(GL11.GL_TEXTURE_2D);
        if (!isPlayer) {
            GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
        }
        GlStateManager.color(r.get_value(1) / 255f, g.get_value(1) / 255f, b.get_value(1) / 255f, a.get_value(1) / 255f);
        GlStateManager.popMatrix();
    }

    private void createChamsPost() {
        boolean shadow = mc.getRenderManager().isRenderShadow();
        mc.getRenderManager().setRenderShadow(shadow);
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(false);
        glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(1.0f, -5300000.0f);
        GlStateManager.popMatrix();
    }

    private void createColorPost(boolean isPlayer) {
        boolean shadow = mc.getRenderManager().isRenderShadow();
        mc.getRenderManager().setRenderShadow(shadow);
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(false);
        if (!isPlayer) {
            GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
        }
        glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(1.0f, -5300000.0f);
        glEnable(GL11.GL_TEXTURE_2D);
        GlStateManager.popMatrix();
    }
}
