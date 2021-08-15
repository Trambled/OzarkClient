package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.util.world.TimerUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;


public class ChorusViewer extends Module {

    public ChorusViewer() {
        super(Category.RENDER);

        this.name = "ChorusViewer";
        this.tag = "ChorusViewer";
        this.description = "where is the nn go with corus?";
    }
    private final TimerUtil renderTimer = new TimerUtil();

    private BlockPos pos;

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> receive_listener = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.get_packet();
            if (packet.getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT || packet.getSound() == SoundEvents.ENTITY_ENDERMEN_TELEPORT) {
                pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
                renderTimer.reset();
            }
        }});


    @Override
    public void render() {
        if (pos != null) {
            if (renderTimer.passed(4000)) {
                pos = null;
                return;
            }
            renderBox(pos, new Color(0xC586FF), true, 4);
        }
    }
    public static void renderBox(BlockPos pos, Color color, boolean outline, int alpha) {
        enableGL3D();
        final AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - mc.getRenderManager().viewerPosX, pos.getY() - mc.getRenderManager().viewerPosY, pos.getZ() - mc.getRenderManager().viewerPosZ, pos.getX() + 1 - mc.getRenderManager().viewerPosX, pos.getY() + 1 - mc.getRenderManager().viewerPosY, pos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
        RenderGlobal.renderFilledBox(bb, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha / 255F);
        if (outline) {
            RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
        }
        disableGL3D();
    }
    public static void disableGL3D() {
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    public static void enableGL3D() {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GlStateManager.glLineWidth(1F);
    }}

