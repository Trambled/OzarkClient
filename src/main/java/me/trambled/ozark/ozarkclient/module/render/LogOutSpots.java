package me.trambled.ozark.ozarkclient.module.render;

import joptsimple.internal.Strings;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.render.GuiUtil;
import me.trambled.ozark.ozarkclient.util.render.RainbowUtil;
import me.trambled.ozark.ozarkclient.util.render.RenderUtil;
import me.trambled.ozark.ozarkclient.util.world.MathUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.math.AxisAlignedBB;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogOutSpots extends Module {

    public LogOutSpots() {
        super(Category.RENDER);

        this.name = "LogoutSpots";
        this.tag = "LogoutSpots";
        this.description = "Ezz log.";
    }

    Setting r = create("R", "LogOutR", 255, 0, 255);
    Setting g = create("G", "LogOutG", 255, 0, 255);
    Setting b = create("B", "LogOutB", 255, 0, 255);
    Setting rainbow = create("Rainbow", "LogOutRainbow", true);
    Setting range = create("Range", "LogoutRange", 200f, 50f, 500f);
    Setting scaleing  = create("Scale", "LogOutScale", false);
    Setting scaling = create("Size", "LogoutSize", 4.0f, 0.1f, 20f);
    Setting factor = create("Factor", "LogoutFactor", 0.3f, 0.1f, 1f);
    Setting rect = create("Rectangle", "LogoutRect", true);
    Setting coords = create("Coords", "LogoutCoords", true);
    Setting smartScale = create("Smart Scale", "LogoutSmartScale", true);
    Setting message = create("Message", "LogoutMessage", true);

    private final List<LogoutPos> spots = new CopyOnWriteArrayList <> ( );

    @Override
    public void log_out() {
        this.spots.clear();
    }

    @Override
    protected void disable() {
        this.spots.clear();
    }

    @Override
    public void render(EventRender event) {
        if (!this.spots.isEmpty()) {
            synchronized (this.spots) {
                this.spots.forEach(spot -> {
                    if (spot.getEntity() != null) {
                        AxisAlignedBB bb = RenderUtil.interpolateAxis(spot.getEntity().getEntityBoundingBox());
                        RenderUtil.drawBlockOutline(bb,new Color(this.r.get_value(1), this.g.get_value(1), this.b.get_value(1), 255), 1.0f);
                        double x = this.interpolate(spot.getEntity().lastTickPosX, spot.getEntity().posX, event.get_partial_ticks()) - mc.getRenderManager().renderPosX;
                        double y = this.interpolate(spot.getEntity().lastTickPosY, spot.getEntity().posY, event.get_partial_ticks()) - mc.getRenderManager().renderPosY;
                        double z = this.interpolate(spot.getEntity().lastTickPosZ, spot.getEntity().posZ, event.get_partial_ticks()) - mc.getRenderManager().renderPosZ;
                        this.renderNameTag(spot.getName(), x, y, z, event.get_partial_ticks(), spot.getX(), spot.getY(), spot.getZ());
                    }
                });
            }
        }
    }

    @Override
    public void update() {
        if (!full_null_check()) {
            this.spots.removeIf(spot -> mc.player.getDistanceSq( spot.getEntity() ) >= MathUtil.square(this.range.get_value(1)));
        }
        if (rainbow.get_value(true)) {
            cycle_rainbow();
        }
    }

    public void onConnection(int stage, EntityPlayer player, UUID Uuid, String player_name) {
        if (stage == 0) {
            EntityPlayer entity = mc.world.getPlayerEntityByUUID( Uuid );
            if (entity != null && this.message.get_value(true)) {
                MessageUtil.send_client_message(player_name + " just logged in" + (this.coords.get_value(true) ? " at (" + (int)entity.posX + ", " + (int)entity.posY + ", " + (int)entity.posZ + ")!" : "!"));
            }
            this.spots.removeIf(pos -> pos.getName().equalsIgnoreCase(player_name));
        } else if (stage == 1) {
            if (this.message.get_value(true)) {
                MessageUtil.send_client_message(player_name + " just logged out" + (this.coords.get_value(true) ? " at (" + (int) player.posX + ", " + (int) player.posY + ", " + (int) player.posZ + ")!" : "!"));
            }
            if ( player_name != null && player != null && Uuid != null) {
                this.spots.add(new LogoutPos( player_name , Uuid , player ));
            }
        }
    }

    private void renderNameTag(String name, double x, double yi, double z, float delta, double xPos, double yPos, double zPos) {
        double y = yi + 0.7;
        net.minecraft.entity.Entity camera = mc.getRenderViewEntity();
        assert (camera != null);
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        String displayTag = name + " XYZ: " + (int)xPos + ", " + (int)yPos + ", " + (int)zPos;
        double distance = camera.getDistance(x + mc.getRenderManager().viewerPosX, y + mc.getRenderManager().viewerPosY, z + mc.getRenderManager().viewerPosZ);
        int width = RainbowUtil.get_string_width(displayTag) / 2;
        double scale = (0.0018 + this.scaling.get_value(1d) * (distance * this.factor.get_value(1d) )) / 1000.0;
        if (distance <= 8.0 && this.smartScale.get_value(true)) {
            scale = 0.0245;
        }
        if (!this.scaleing.get_value(true)) {
            scale = this.scaling.get_value(1d) / 100.0;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset( 1.0f , -1500000.0f );
        GlStateManager.disableLighting();
        GlStateManager.translate( (float)x , (float)y + 1.4f , (float)z );
        GlStateManager.rotate( -mc.getRenderManager().playerViewY , 0.0f , 1.0f , 0.0f );
        GlStateManager.rotate( mc.getRenderManager().playerViewX , mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f , 0.0f , 0.0f );
        GlStateManager.scale( -scale , -scale , scale );
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        if (this.rect.get_value(true)) {
            RenderUtil.drawRect(-width - 2, -(RainbowUtil.get_string_height() + 1), (float)width + 2.0f, 1.5f, 0x55000000);
        }
        GlStateManager.disableBlend();
        RainbowUtil.drawString(displayTag, -width, -(RainbowUtil.get_string_height() - 1), new GuiUtil.OzarkColor(r.get_value(1), g.get_value(1), b.get_value(1), 255).hex());
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset( 1.0f , 1500000.0f );
        GlStateManager.popMatrix();
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double)delta;
    }

    private static class LogoutPos {
        private final String name;
        private final UUID uuid;
        private final EntityPlayer entity;
        private final double x;
        private final double y;
        private final double z;

        public LogoutPos(String name, UUID uuid, EntityPlayer entity) {
            this.name = name;
            this.uuid = uuid;
            this.entity = entity;
            this.x = entity.posX;
            this.y = entity.posY;
            this.z = entity.posZ;
        }

        public String getName() {
            return this.name;
        }

        public UUID getUuid() {
            return this.uuid;
        }

        public EntityPlayer getEntity() {
            return this.entity;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getZ() {
            return this.z;
        }
    }

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> receive_listener = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketPlayerListItem) {
            SPacketPlayerListItem packet = (SPacketPlayerListItem) event.get_packet();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals( packet.getAction() ) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals( packet.getAction() )) {
                return;
            }
            packet.getEntries().stream().filter(Objects::nonNull).filter(data -> !Strings.isNullOrEmpty( data.getProfile().getName() ) || data.getProfile().getId() != null).forEach( data -> {
                UUID id = data.getProfile().getId();
                switch (packet.getAction()) {
                    case ADD_PLAYER: {
                        String name = data.getProfile().getName();
                        EntityPlayer entity = mc.world.getPlayerEntityByUUID(id);
                        onConnection(0, entity, id, name);
                        break;
                    }
                    case REMOVE_PLAYER: {
                        EntityPlayer entity = mc.world.getPlayerEntityByUUID(id);
                        if (entity != null) {
                            String logoutName = entity.getName();
                            onConnection(1, entity, id, logoutName);
                            break;
                        }
                    }
                }
            });
        }
    });

    public void cycle_rainbow() {

        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

        r.set_value((color_rgb_o >> 16) & 0xFF);
        g.set_value((color_rgb_o >> 8) & 0xFF);
        b.set_value(color_rgb_o & 0xFF);

    }

    @Override
    public String array_detail() {
            return "Spots:" + spots.size();

        }
    }