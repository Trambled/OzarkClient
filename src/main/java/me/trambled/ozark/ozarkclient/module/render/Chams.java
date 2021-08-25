package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.event.events.EventRenderEntityCrystal;
import me.trambled.ozark.ozarkclient.event.events.EventRenderEntityModel;
import me.trambled.ozark.ozarkclient.event.events.EventTotemPop;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.render.Tessellator;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;


import java.awt.*;
import java.util.Set;

public class Chams extends Module {

    public Chams() {
        super(Category.RENDER);

        this.name = "Chams";
        this.tag = "Chams";
        this.description = "minecraft render is so lame - kambing";
    }

    // leon/3tnt

    Setting page = create("Page", "Page", "Entities", combobox("Entities", "PopChams","Color"));
    Setting mode = create("Mode", "Mode", "Wireframe", combobox("Textured", "Wireframe", "Normal"));
    Setting wallsMode = create("WallsMode", "WallsMode", "Normal", combobox("XQZ", "Normal", "None"));
    Setting width = create("Width", "Width", 1,0.1,4);
    Setting cancel = create("Cancel", "Cancel",true);

    //colors
    Setting lol1 = create("info","Colors", "Info");
    Setting r = create("Red", "R", 255, 0, 255);
    Setting g = create("Green", "G", 0, 0, 255);
    Setting b = create("Blue", "B", 0, 0, 255);
    Setting a = create("Alpha", "A", 255, 0, 255);
    Setting lol = create("info","XQZ Colors", "InfoXQZ");
    Setting rx = create("Red XQZ", "RXQZ", 0, 0, 255);
    Setting gx = create("Green XQZ", "GXQZ", 255, 0, 255);
    Setting bx = create("Blue XQZ", "BXQZ", 0, 0, 255);
    Setting ax = create("Alpha XQZ", "AXQZ", 50, 0, 255);
    Setting lol2 = create("info","PopChams Colors", "InfoPop");
    Setting rp = create("Red", "Rp", 0, 0, 255);
    Setting gp = create("Green", "Gp", 255, 0, 255);
    Setting bp = create("Blue", "Bp", 0, 0, 255);
    Setting ap = create("Alpha", "Ap", 50, 0, 255);
    Setting lo3l = create("info","PopChams XQZ Colors", "InfoPopXQZ");
    Setting rxp = create("Red XQZ", "RXQZp", 0, 0, 255);
    Setting gxp = create("Green XQZ", "GXQZp", 255, 0, 255);
    Setting bxp = create("Blue XQZ", "BXQZp", 0, 0, 255);
    Setting axp = create("Alpha XQZ", "AXQZp", 50, 0, 255);
    Setting lol2e = create("info","Extra PopChams Colors", "InfoPop");
    Setting rpe = create("Red", "Rpe", 0, 0, 255);
    Setting gpe = create("Green", "Gpe", 255, 0, 255);
    Setting bpe = create("Blue", "Bpe", 0, 0, 255);
    Setting ape = create("Alpha", "Ape", 50, 0, 255);

    //entities
    Setting players = create("Players", "Players",true);
    Setting crystals = create("Crystals", "Crystals",true);

    // popchangs
    Setting pop = create("PopChams", "PopChams",true);
    Setting extra = create("Extra", "Extra",true);
    Setting extraRender = create("ExtraRenderMode", "PRModeE", "Wireframe", combobox("Textured", "Wireframe", "Normal"));
    Setting popExtraFade = create("Fade", "PFadeE",true);
    Setting popExtraTime = create("SyncTime", "PSyncTime",3000,0,10000);

    Setting popTime = create("Time", "PTime",3000,0,10000);
    Setting popMode = create("RenderMode", "PRMode", "Wireframe", combobox("Textured", "Wireframe", "Normal"));
    Setting popWallsMode = create("Mode", "Modep", "Normal", combobox("XQZ", "Normal", "None"));
    Setting popWidth = create("Width", "Widthp", 1,0.1,4);
    Setting popCancel = create("Cancel", "Cancelp","Skin",combobox("Skin","All", "None"));
    Setting popFade = create("Fade", "PFade",true);

    Color popColor = new Color(rp.get_value(1),gp.get_value(1),bp.get_value(1),ap.get_value(1));
    Color color = new Color(r.get_value(1),g.get_value(1),b.get_value(1),a.get_value(1));
    Color popXqzColor = new Color(rxp.get_value(1),gxp.get_value(1),bxp.get_value(1),axp.get_value(1));
    Color xqzColor = new Color(rx.get_value(1),gx.get_value(1),bx.get_value(1),ax.get_value(1));
    Color popExtraColor = new Color(rpe.get_value(1),gpe.get_value(1),bpe.get_value(1),ape.get_value(1));
    private static Set<ExtraInfo> extraMap;
    private static Set<PopInfo> popMap;

    private boolean isExtra = false; // hack
    private ExtraInfo info;

    RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

    @Override
    public String array_detail() {
        return mode.get_current_value();
    }

    public boolean shouldRender(Entity e) {
        if(Ozark.get_module_manager().get_module_with_tag("Chams").is_active()) {
            if(e instanceof EntityEnderCrystal && crystals.get_value(true))
                return true;
            if(e instanceof EntityPlayer && players.get_value(true))
                return true;
        }
        return false;
    }

    @EventHandler
    private final Listener<EventRenderEntityCrystal> renderEntityCrystalListener = new Listener<>(event -> {
        chams(event, event.modelBase, event.entityIn, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scale, false, 0, false);
    });

    @EventHandler
    private final Listener<EventRenderEntityModel> renderEntityModelListener = new Listener<>(event -> {
        updatePop();
        final PopInfo info = getPop(event.entity);
        final float progress = isPop(event.entity) ? (1 - (((info.time + popTime.get_value(1)) - System.currentTimeMillis()) / popTime.get_value(1))) : 0;

        if(popCancel.in("None") && !isExtra && isPop(event.entity)) {
            chams(event, event.modelBase, event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale, false, 0, false);
            chams(event, event.modelBase, event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale, true, progress, false);
        } else
            chams(event, event.modelBase, event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale, isPop(event.entity), progress, isExtra);
    });

    @EventHandler
    private final Listener<EventTotemPop> totemListener = new Listener<>(event -> {
        if(extra.get_value(true)) extraMap.add(new ExtraInfo(event.getEntity()));
        if(pop.get_value(true)) {
            if(isPop(event.getEntity())) popMap.remove(getPop(event.getEntity()));
            popMap.add(new PopInfo(event.getEntity()));
        }
    });

    private void chams(me.trambled.ozark.ozarkclient.event.Event event, final ModelBase model, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, boolean pop, float progress, boolean extra) {
        if(!shouldRender(entity)) return;
        boolean shadows = mc.gameSettings.entityShadows;
        renderManager.setRenderShadow(false);

        final String renderMode = extra ? extraRender.get_current_value() : pop ? popMode.get_current_value() : mode.get_current_value();
        final String hiddenMode = extra || pop ? popWallsMode.get_current_value() : wallsMode.get_current_value();
        final float renderWidth = extra || pop ? popWidth.get_value(1) : width.get_value(1);
        final String cancelModel =  extra || pop ? popCancel.get_current_value() : String.valueOf((cancel.get_current_value()));

        final float extraProgress = extra ? (1 - (((info.time + popExtraTime.get_value(1)) - System.currentTimeMillis()) / popExtraTime.get_value(1))) : 0;

        GL11.glPushMatrix();
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.0156862745f);

        final boolean cancel = cancelModel.equalsIgnoreCase("All") || cancelModel.equalsIgnoreCase("true") || cancelModel.equalsIgnoreCase("Skin");

        switch (renderMode) {
            case "Wireframe": {
                GL11.glPushMatrix();
                GL11.glPushAttrib(1048575);
                GL11.glPolygonMode(1032, 6913);
                GL11.glDisable(3553);
                GL11.glEnable(2848);
                GL11.glEnable(GL11.GL_BLEND);
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.glLineWidth(renderWidth);

                if (hiddenMode.equalsIgnoreCase("XQZ")) {
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glDepthMask(false);

                    GL11.glDepthRange(0.1, 1.0f);
                    GL11.glDepthFunc(GL11.GL_GREATER);
                    Tessellator.color(getColor(progress, extraProgress, pop, extra, true));
                    model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    GL11.glDepthFunc(GL11.GL_LESS);
                    GL11.glDepthRange(0.0f, 1.0f);

                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glDepthMask(false);

                    Tessellator.color(getColor(progress, extraProgress, pop, extra, false));
                    model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glDepthMask(true);
                } else {
                    GL11.glEnable(GL11.GL_DEPTH_TEST);

                    if(hiddenMode.equalsIgnoreCase("Normal")) {
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                    }

                    GL11.glDepthMask(false);

                    Tessellator.color(getColor(progress, extraProgress, pop, extra, false));
                    model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glDepthMask(true);

                    if(hiddenMode.equalsIgnoreCase("Normal")) {
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                    }
                }

                GL11.glPopAttrib();
                GL11.glPopMatrix();

                if(cancel) event.cancel();

                break;
            }
            case "Textured": {
                GL11.glPushMatrix();
                GL11.glPushAttrib(GL11.GL_ALL_CLIENT_ATTRIB_BITS);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_BLEND);
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GL11.glLineWidth(1.5f);

                if (hiddenMode.equalsIgnoreCase("XQZ")) {
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glDepthMask(false);

                    // walls
                    GL11.glDepthRange(0.01, 1.0f);
                    GL11.glDepthFunc(GL11.GL_GREATER);
                    Tessellator.color(getColor(progress, extraProgress, pop, extra, true));
                    model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    GL11.glDepthFunc(GL11.GL_LESS);
                    GL11.glDepthRange(0.0f, 1.0f);

                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glDepthMask(false);

                    // normal
                    Tessellator.color(getColor(progress, extraProgress, pop, extra, false));
                    model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                } else {
                    if(hiddenMode.equalsIgnoreCase("Normal")) {
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                        GL11.glDepthMask(false);
                    }

                    Tessellator.color(getColor(progress, extraProgress, pop, extra, false));
                    model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

                    if(hiddenMode.equalsIgnoreCase("Normal")) {
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        GL11.glDepthMask(true);
                    }
                }

                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopAttrib();
                GL11.glPopMatrix();

                if(cancel) event.cancel();
                break;
            }
            case "Normal": {
                GL11.glEnable(32823);
                GL11.glPolygonOffset(1.0f, -1100000.0f);

                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

                GL11.glPolygonOffset(1.0f, 1000000.0f);
                GL11.glDisable(32823);

                event.cancel();
                break;
            }
        }

        GL11.glPopMatrix();
        renderManager.setRenderShadow(shadows);
    }


    private void updatePop() {
        final long time = System.currentTimeMillis();
        popMap.removeIf(e -> (e.time + popTime.get_value(1)) < time);
    }

    private Color getColor(float progress, float progress0, boolean flag, boolean flag2, boolean hidden) {
        if(flag2) {
            if(hidden) {
                return popExtraFade.get_value(true) ? lower(popXqzColor, progress0) : popXqzColor;
            } else {
                return popExtraFade.get_value(true) ? lower(popExtraColor, progress0) : popExtraColor;

            }
        } else if(flag) {
            return popFade.get_value(true) ? lower(hidden ? popXqzColor : popColor, progress) : hidden ? popXqzColor : popColor;
        } else {
            return hidden ? xqzColor : color;
        }
    }

    private Color lower(Color color, float progress) {
        return new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                (int) (color.getAlpha() * (1 - progress))
        );
    }

    private boolean isPop(Entity entity) {
        return getPop(entity) != null;
    }

    public PopInfo getPop(Entity entity) {
        for(PopInfo info : popMap) {
            if(info.entity == entity) {
                return info;
            }
        }
        return null;
    }

    private static class ExtraInfo {
        public double x, y, z;
        public EntityLivingBase entity;
        public long time;

        private ExtraInfo(EntityLivingBase entity) {
            this.entity = entity;
            this.time = System.currentTimeMillis();
            this.x = entity.posX;
            this.y = entity.posY;
            this.z = entity.posZ;
        }
    }

    private static class PopInfo {
        public Entity entity;
        public long time;

        private PopInfo(Entity entity) {
            this.entity = entity;
            this.time = System.currentTimeMillis();
        }
    }
}

