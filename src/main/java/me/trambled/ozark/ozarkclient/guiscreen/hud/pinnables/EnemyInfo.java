package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;

import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;
import me.trambled.ozark.ozarkclient.module.combat.AutoCrystal;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static me.trambled.ozark.ozarkclient.event.Eventbus.EVENT_BUS;
import static me.trambled.ozark.ozarkclient.util.render.TabUtil.section_sign;

public class EnemyInfo extends Pinnable implements Listenable {

    public EnemyInfo() {
        super("Enemy Info", "EnemyInfo", 1, 0, 0);

        this.set_height(80);
        this.set_width(150);

        EVENT_BUS.subscribe(this);
    }

    @Override
    public void render() {
        update_pops();

        if (mc.world == null || mc.player == null) {
            return;
        }

        EntityPlayer target = mc.player;

        float lowest_distance = 999F;

        for (EntityPlayer e : mc.world.playerEntities) {
            if (e.getDistance(mc.player) < lowest_distance && !e.getName().equals(mc.player.getName()) && e.getDistance(mc.player) != 0) {
                target = e;
                lowest_distance = e.getDistance(mc.player);
            }
        }

        if (AutoCrystal.get_target() != null && !AutoCrystal.get_target().isDead) target = AutoCrystal.get_target();

        create_rect(0, 0, this.get_width(), this.get_height(), 0, 0, 0, 69);

        float target_hp = target.getHealth() + target.getAbsorptionAmount();
        String ping_str = "Ping: ";
        try {
            final int response_time = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(target.getUniqueID()).getResponseTime();
            ping_str += response_time + "ms";
        } catch (Exception ignored) {}
        float distance_to_target = target.getDistance(mc.player);

        int hp_r = 16;
        int hp_g = 171;
        int hp_b = 11;

        if (target_hp < 10) {
            hp_r = 200;
            hp_g = 15;
            hp_b =15;
        } else if (target_hp < 15) {
            hp_r = 171;
            hp_g = 140;
            hp_b =15;
        } else if (target_hp > 20) {
            hp_r = 110;
            hp_g = 2;
            hp_b = 88;
        }

        String pop_str = "";

        try {
            pop_str += (totem_pop_counter.get(target.getName()) == null ? section_sign() + "70" : section_sign() + "c " + totem_pop_counter.get(target.getName()));
        } catch (Exception ignore) {}

        int str_height = this.get("00hpRRRta", "height") + 3;

        try {
            create_line_regular(target.getName() + " HP: " + target_hp, 3, 3, hp_r, hp_g, hp_b, 255);
            create_line(ping_str, 3, str_height);
            create_line("Distance: " + (int) distance_to_target, 3, str_height * 2);
            create_line("Totems Popped: " + pop_str, 3, str_height * 3);

            ArrayList<Block> surroundblocks = get_surround_blocks(target);

            renderItemStack(new ItemStack(surroundblocks.get(0)), this.get_x() + 75, this.get_y() - 20 + 40);
            renderItemStack(new ItemStack(surroundblocks.get(1)), this.get_x() + 20 + 75, this.get_y() + 40);
            renderItemStack(new ItemStack(surroundblocks.get(2)), this.get_x() + 75, this.get_y() + 20 + 40);
            renderItemStack(new ItemStack(surroundblocks.get(3)), this.get_x() - 20 + 75, this.get_y() + 40);

            int i1 = 3;
            for (int i = target.inventory.armorInventory.size(); i > 0; i--) {
                final ItemStack stack2 = target.inventory.armorInventory.get(i - 1);
                final ItemStack armourStack = stack2.copy();
                if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
                    armourStack.stackSize = 1;
                }
                renderItemStack(armourStack, this.get_x() + i1, this.get_y() + str_height * 4);
                i1 += 16;
            }

            create_rect(0, this.get_height(), (int) (target_hp / 36 * this.get_width()), this.get_height() -5, hp_r, hp_g, hp_b, 255);

            GL11.glColor3f(1f, 1f, 1f);
            GuiInventory.drawEntityOnScreen(this.get_x() + this.get_width() -20, this.get_y()
                    + this.get_height() - 10, 30, -target.rotationYaw, -target.rotationPitch, target);
        } catch (Exception ignored){}
    }

    private ArrayList<Block> get_surround_blocks(EntityLivingBase e) {
        ArrayList<Block> surroundblocks = new ArrayList<>();
        BlockPos entityblock = new BlockPos(Math.floor(e.posX), Math.floor(e.posY), Math.floor(e.posZ));
        surroundblocks.add(mc.world.getBlockState(entityblock.north()).getBlock());
        surroundblocks.add(mc.world.getBlockState(entityblock.east()).getBlock());
        surroundblocks.add(mc.world.getBlockState(entityblock.south()).getBlock());
        surroundblocks.add(mc.world.getBlockState(entityblock.west()).getBlock());
        return surroundblocks;
    }

    private void renderItemStack(final ItemStack stack, final int x, final int y) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, x, y);
        mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }

    public static final HashMap<String, Integer> totem_pop_counter = new HashMap <> ( );


    @EventHandler
    private final Listener<EventPacket.ReceivePacket> packet_event = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketEntityStatus) {

            SPacketEntityStatus packet = (SPacketEntityStatus) event.get_packet();

            if (packet.getOpCode() == 35) {

                Entity entity = packet.getEntity(mc.world);

                int count = 1;

                if (totem_pop_counter.containsKey(entity.getName())) {
                    count = totem_pop_counter.get(entity.getName());
                    totem_pop_counter.put(entity.getName(), ++count);
                } else {
                    totem_pop_counter.put(entity.getName(), count);
                }

            }

        }
    });


    private void update_pops () {
        for (EntityPlayer player : mc.world.playerEntities) {

            if (!totem_pop_counter.containsKey(player.getName())) continue;

            if (player.isDead || player.getHealth() <= 0) {

                totem_pop_counter.remove(player.getName());
            }
        }
    }
}