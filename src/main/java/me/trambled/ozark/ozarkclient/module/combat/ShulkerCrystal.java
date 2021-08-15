package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.event.events.EventGUIScreen;
import me.trambled.ozark.ozarkclient.guiscreen.MainGUI;
import me.trambled.ozark.ozarkclient.guiscreen.MainHUD;
import me.trambled.ozark.ozarkclient.guiscreen.PastGUI;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.player.EntityUtil;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import me.trambled.ozark.ozarkclient.util.world.BlockInteractionHelper;
import me.trambled.ozark.ozarkclient.util.world.BlockUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.stream.Collectors;

/**
 * Made by @Trambled
 * on 4/16/21
 */

// this is inmcredibly retardaed code
// credit to gamesense for some of dese
// also sum stuff from bedauara
public class ShulkerCrystal extends Module
{
    public ShulkerCrystal() {
        super(Category.COMBAT);

        this.name = "ShulkerCrystal";
        this.tag = "ShulkerCrystal";
        this.description = "Automatically pushes crystals into holes using shulkers.";
    }

    Setting break_range = create("Break Range", "BreakRange", 5, 0, 6);
    Setting crystal_delay = create("Crystal Delay", "CrystalDelay", 2, 0, 20);
    Setting r_click_delay = create("R Click Delay", "RClickDelay", 2, 0, 20);
    Setting hit_delay = create("Hit Delay", "HitDelay", 2, 0, 20);
    Setting debug = create("Debug", "Debug", false);

    private int stage;
    private int delay_counter;
    private int[] delay_table;

    @Override
    protected void enable() {
        delay_table = new int[] {
                crystal_delay.get_value(1),
                r_click_delay.get_value(1),
                hit_delay.get_value(1)
        };
        stage = 0;
        delay_counter = 0;
    }

    @Override
    public void update() {
        if (find_closest_target() == null) {
            MessageUtil.send_client_error_message("Cannot find target");
            this.set_disable();
            return;
        }
        if (find_crystals_hotbar() == -1) {
            MessageUtil.send_client_error_message("No crystals in hotbar");
            this.set_disable();
            return;
        }
        if (mc.currentScreen instanceof GuiContainer) {
            mc.displayGuiScreen(null);
        }
        BlockPos target_pos = new BlockPos(find_closest_target().getPosition());

        if (delay_counter < delay_table[stage]){
            delay_counter++;
            return;
        } else {
            delay_counter = 0;
        }


        switch (stage) {
            case 0:
                for (BlockPos pos : BlockInteractionHelper.getSphere(get_pos_floor(mc.player), (float) break_range.get_value(1), break_range.get_value(1), false, true, 0).stream().filter(ShulkerCrystal::is_shulker).collect(Collectors.toList())) {
                    if (pos.getX() == target_pos.getX() + 2) {
                        BlockPos crystal_pos = new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ());
                        if (mc.player.inventory.currentItem != find_crystals_hotbar()) {
                            mc.player.inventory.currentItem = find_crystals_hotbar();
                            return;
                        }
                        BlockUtil.placeCrystalOnBlock(crystal_pos, EnumHand.MAIN_HAND, true);
                    } else if (pos.getX() == target_pos.getX() - 2) {
                        BlockPos crystal_pos = new BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ());
                        if (mc.player.inventory.currentItem != find_crystals_hotbar()) {
                            mc.player.inventory.currentItem = find_crystals_hotbar();
                            return;
                        }
                        BlockUtil.placeCrystalOnBlock(crystal_pos, EnumHand.MAIN_HAND, true);
                    } else if (pos.getZ() == target_pos.getZ() + 2) {
                        BlockPos crystal_pos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() - 1);
                        if (mc.player.inventory.currentItem != find_crystals_hotbar()) {
                            mc.player.inventory.currentItem = find_crystals_hotbar();
                            return;
                        }
                        BlockUtil.placeCrystalOnBlock(crystal_pos, EnumHand.MAIN_HAND, true);
                    } else if (pos.getZ() == target_pos.getZ() - 2) {
                        BlockPos crystal_pos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() + 1);
                        if (mc.player.inventory.currentItem != find_crystals_hotbar()) {
                            mc.player.inventory.currentItem = find_crystals_hotbar();
                            return;
                        }
                        BlockUtil.placeCrystalOnBlock(crystal_pos, EnumHand.MAIN_HAND, true);
                    } else {
                        if (debug.get_value(true)) {
                            MessageUtil.send_client_message("Cannot find confirmed shulkers");
                            MessageUtil.send_client_message("shulker pos x = " +  pos.getX());
                            MessageUtil.send_client_message("shulker pos z = " +  pos.getZ());
                            MessageUtil.send_client_message("target pos x = " +  target_pos.getX());
                            MessageUtil.send_client_message("target pos z = " +  target_pos.getZ());
                        }
                    }
                }
                stage++;
                break;
            case 1:
                for (BlockPos pos : BlockInteractionHelper.getSphere(get_pos_floor(mc.player), (float) break_range.get_value(1), break_range.get_value(1), false, true, 0).stream().filter(ShulkerCrystal::is_shulker).collect(Collectors.toList())) {
                    if (pos.getX() == target_pos.getX() + 2) {
                        final RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5));
                        final EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                        if (mc.player.isSneaking()) {
                            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                            mc.player.setSneaking(false);
                        }
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                    } else if (pos.getX() == target_pos.getX() - 2) {
                        final RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5));
                        final EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                        if (mc.player.isSneaking()) {
                            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                            mc.player.setSneaking(false);
                        }
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                        mc.displayGuiScreen(null);
                    } else if (pos.getZ() == target_pos.getZ() + 2) {
                        final RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5));
                        final EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                        if (mc.player.isSneaking()) {
                            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                            mc.player.setSneaking(false);
                        }
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                        mc.displayGuiScreen(null);
                    } else if (pos.getZ() == target_pos.getZ() - 2) {
                        final RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5));
                        final EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                        if (mc.player.isSneaking()) {
                            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                            mc.player.setSneaking(false);
                        }
                        mc.displayGuiScreen(null);
                    }
                }
                stage++;
                break;
            case 2:
                for (BlockPos pos : BlockInteractionHelper.getSphere(get_pos_floor(mc.player), (float) break_range.get_value(1), break_range.get_value(1), false, true, 0).stream().filter(ShulkerCrystal::is_shulker).collect(Collectors.toList())) {
                    if (pos.getX() == target_pos.getX() + 2) {
                        for (Entity t : mc.world.loadedEntityList) {
                            if (t instanceof EntityEnderCrystal) {
                                if ((int) t.posX == (int) find_closest_target().posX && (int) t.posZ == (int) find_closest_target().posZ) {
                                    EntityUtil.attackEntity(t);
                                }
                            }
                        }
                    } else if (pos.getX() == target_pos.getX() - 2) {
                        for (Entity t : mc.world.loadedEntityList) {
                            if (t instanceof EntityEnderCrystal) {
                                if ((int) t.posX == (int) find_closest_target().posX && (int) t.posZ == (int) find_closest_target().posZ) {
                                    EntityUtil.attackEntity(t);
                                }
                            }
                        }
                    } else if (pos.getZ() == target_pos.getZ() + 2) {
                        for (Entity t : mc.world.loadedEntityList) {
                            if (t instanceof EntityEnderCrystal) {
                                if ((int) t.posX == (int) find_closest_target().posX && (int) t.posZ == (int) find_closest_target().posZ) {
                                    EntityUtil.attackEntity(t);
                                }
                            }
                        }
                    } else if (pos.getZ() == target_pos.getZ() - 2) {
                        for (Entity t : mc.world.loadedEntityList) {
                            if (t instanceof EntityEnderCrystal) {
                                if ((int) t.posX == (int) find_closest_target().posX && (int) t.posZ == (int) find_closest_target().posZ) {
                                    EntityUtil.attackEntity(t);
                                }
                            }
                        }
                    }
                }
                stage = 0;
                break;
        }
    }

    @EventHandler
    private final Listener<EventGUIScreen> gui_listener = new Listener<>(event -> {
        if (event.get_guiscreen() instanceof PastGUI || event.get_guiscreen() instanceof MainGUI || event.get_guiscreen() instanceof MainHUD) {
            this.set_disable();
        }
    });

    public static BlockPos get_pos_floor(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static boolean is_shulker(final BlockPos pos) {
        final Block block = mc.world.getBlockState(pos).getBlock();
        // fucking hell
        return block == Blocks.GREEN_SHULKER_BOX || block == Blocks.YELLOW_SHULKER_BOX
                || block == Blocks.RED_SHULKER_BOX || block == Blocks.BLACK_SHULKER_BOX
                || block == Blocks.BLUE_SHULKER_BOX || block == Blocks.PURPLE_SHULKER_BOX
                || block == Blocks.MAGENTA_SHULKER_BOX || block == Blocks.LIME_SHULKER_BOX
                || block == Blocks.BROWN_SHULKER_BOX || block == Blocks.PINK_SHULKER_BOX
                || block == Blocks.LIGHT_BLUE_SHULKER_BOX || block == Blocks.ORANGE_SHULKER_BOX
                || block == Blocks.WHITE_SHULKER_BOX || block == Blocks.CYAN_SHULKER_BOX
                || block == Blocks.SILVER_SHULKER_BOX || block == Blocks.GRAY_SHULKER_BOX;
    }

    private int find_crystals_hotbar() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                return i;
            }
        }
        return -1;
    }

    // autotrap
    public EntityPlayer find_closest_target()  {

        if (mc.world.playerEntities.isEmpty())
            return null;

        EntityPlayer closestTarget = null;

        for (final EntityPlayer target : mc.world.playerEntities)
        {
            if (target == mc.player)
                continue;

            if (FriendUtil.isFriend(target.getName()))
                continue;

            if (!EntityUtil.isLiving(target))
                continue;

            if (target.getHealth() <= 0.0f)
                continue;

            if (closestTarget != null)
                if (mc.player.getDistance(target) > mc.player.getDistance(closestTarget))
                    continue;

            closestTarget = target;
        }

        return closestTarget;
    }
}