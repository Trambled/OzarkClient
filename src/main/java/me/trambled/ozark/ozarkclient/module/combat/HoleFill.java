package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.player.PlayerUtil;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import me.trambled.ozark.ozarkclient.util.world.BlockInteractionHelper;
import me.trambled.ozark.ozarkclient.util.world.BlockInteractionHelper.ValidResult;
import me.trambled.ozark.ozarkclient.util.world.BlockUtil;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class HoleFill extends Module {

    public HoleFill() {
        super(Category.COMBAT);

        this.name = "HoleFill";
        this.tag = "HoleFill";
        this.description = "Turn holes into floors.";
    }

    Setting smart_mode = create("Smart Mode", "HoleFillSmart", false);
    Setting smart_range = create("Smart Range", "HoleFillSmartRange", 2.11, 0, 6);
    Setting hole_toggle = create("Toggle", "HoleFillToggle", true);
    Setting hole_rotate = create("Rotate", "HoleFillRotate", true);
    Setting button = create("Button", "HoleFillButton", false);
    Setting hole_range = create("Range", "HoleFillRange", 4, 1, 6);
    Setting swing = create("Swing", "HoleFillSwing", "Mainhand", combobox("Mainhand", "Offhand", "Both", "None"));
    Setting ghost_mode = create("Ghost Switch", "HoleFillGhostSwitch", true);
    Setting msg = create("Chat MSG", "Holefillchat", false);
    private final ArrayList<BlockPos> holes = new ArrayList<>();

    @Override
    public void enable() {
        if (find_in_hotbar() == -1) {
            MessageUtil.send_client_error_message("No obby!");
            this.set_disable();
        }
        find_new_holes();
    }

    @Override
    public void disable() {
        holes.clear();
    }

    @Override
    public void update() {

        if (find_in_hotbar() == -1) {
            MessageUtil.send_client_error_message("No obby!");
            this.set_disable();
            return;
        }

        if (holes.isEmpty()) {
            if (hole_toggle.get_value(true)) {
                this.set_disable();
                MessageUtil.toggle_message(this);
                return;
            }
        } else {
            if (msg.get_value(true)) {
                MessageUtil.send_client_message("Filling " + holes.size() + " holes");
            }
        }


        BlockPos pos_to_fill = null;

        boolean do_smart = false;

        for (BlockPos pos : new ArrayList<>(holes)) {

            if (pos == null) continue;

            BlockInteractionHelper.ValidResult result = BlockInteractionHelper.valid(pos);

            for (Entity player : mc.world.playerEntities) {
                if (player == mc.player) continue;

                if (FriendUtil.isFriend(player.getName())) continue;

                if (player.getDistance(mc.player) >= 11) continue;

                final EntityPlayer target = (EntityPlayer) player;

                if (target.isDead || target.getHealth() <= 0) continue;

                if (target.getDistanceSqToCenter(pos) < smart_range.get_value(1) * smart_range.get_value(1)) {
                    do_smart = true;
                }
            }


            if (result != ValidResult.Ok) {
                holes.remove(pos);
                continue;
            }
            pos_to_fill = pos;
            break;
        }

        if (do_smart || !smart_mode.get_value(true)) {
            if (pos_to_fill != null) {
                if (BlockUtil.placeBlock(pos_to_fill, find_in_hotbar(), hole_rotate.get_value(true), hole_rotate.get_value(true), swing, ghost_mode.get_value(true))) {
                    holes.remove(pos_to_fill);
                }
            }
        }
    }

    public void find_new_holes() {

        holes.clear();

        for (BlockPos pos : BlockInteractionHelper.getSphere(PlayerUtil.getLocalPlayerPosFloored(), hole_range.get_value(1), hole_range.get_value(1), false, true, 0)) {

            if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            if (!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            boolean possible = true;

            for (BlockPos seems_blocks : new BlockPos[]{
                    new BlockPos(0, -1, 0),
                    new BlockPos(0, 0, -1),
                    new BlockPos(1, 0, 0),
                    new BlockPos(0, 0, 1),
                    new BlockPos(-1, 0, 0)
            }) {
                Block block = mc.world.getBlockState(pos.add(seems_blocks)).getBlock();

                if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                    possible = false;
                    break;
                }
            }

            if (possible) {
                holes.add(pos);
            }
        }
    }

    private int find_in_hotbar() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock) stack.getItem()).getBlock();

                if (block instanceof BlockEnderChest && !button.get_value(true)) {
                    return i;
                }

                if (block instanceof BlockObsidian && !button.get_value(true)) {
                    return i;
                }

                if (block instanceof BlockButtonStone && button.get_value(true)) {
                    return i;
                }

                if (block instanceof BlockButtonWood && button.get_value(true)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public void update_always() {
        smart_range.set_shown(smart_mode.get_value(true));
    }

}
