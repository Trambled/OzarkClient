package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.*;
import me.travis.wurstplus.wurstplustwo.util.WurstplusBlockInteractHelper.ValidResult;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButtonWood;
import net.minecraft.block.BlockButtonStone;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class WurstplusHoleFill extends WurstplusHack {
    
    public WurstplusHoleFill() {
		super(WurstplusCategory.WURSTPLUS_COMBAT);

		this.name        = "Hole Fill"; 
		this.tag         = "HoleFill";
		this.description = "Turn holes into floors";
    }

    WurstplusSetting smart_mode = create("Smart Mode", "HoleFillSmart", true);
    WurstplusSetting smart_range = create("Smart Range", "HoleFillSmartRange", 2.11, 0,6);
    WurstplusSetting hole_toggle = create("Toggle", "HoleFillToggle", true);
    WurstplusSetting hole_rotate = create("Rotate", "HoleFillRotate", true);
    WurstplusSetting button = create("Button", "HoleFillButton", false);
    WurstplusSetting hole_range = create("Range", "HoleFillRange", 4, 1, 6);
    WurstplusSetting swing = create("Swing", "HoleFillSwing", "Mainhand", combobox("Mainhand", "Offhand", "Both", "None"));


    private final ArrayList<BlockPos> holes = new ArrayList<>();

    @Override
	public void enable() {
		if (find_in_hotbar() == -1) {
		    WurstplusMessageUtil.send_client_error_message("No obby!");
		    this.set_disable();
        }
        find_new_holes();
	}

	@Override
	public void disable() {
        holes.clear();
    }

    @Override
    public void update_always() {
        smart_range.set_shown(smart_mode.get_value(true));
    }
    
    @Override
	public void update() {

        if (find_in_hotbar() == -1) {
            WurstplusMessageUtil.send_client_error_message("No obby!");
            this.set_disable();
            return;
        }

        if (holes.isEmpty()) {
            if (hole_toggle.get_value(true)) {
                this.set_disable();
                WurstplusMessageUtil.toggle_message(this);
                return;

            } else {
                find_new_holes();
            }
        }

        BlockPos pos_to_fill = null;

        boolean do_smart = false;

        for (BlockPos pos : new ArrayList<>(holes)) {

            if (pos == null) continue;

            WurstplusBlockInteractHelper.ValidResult result = WurstplusBlockInteractHelper.valid(pos);

            for (Entity player : mc.world.playerEntities) {
                if (player == mc.player) continue;

                if (WurstplusFriendUtil.isFriend(player.getName())) continue;

                //stops lag maybe?
                if (player.getDistance(mc.player) >= 11) continue;

                final EntityPlayer target = (EntityPlayer) player;

                if (target.isDead || target.getHealth() <= 0) continue;

                if (target.getDistanceSqToCenter(pos) < smart_range.get_value(1)*smart_range.get_value(1)) {
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

        if (find_in_hotbar() == -1) {
            WurstplusMessageUtil.send_client_error_message("No obby!");
            this.set_disable();
            return;
        }

        if (smart_mode.get_value(true)) {
            if (do_smart) {
                if (pos_to_fill != null) {
                    if (WurstplusBlockUtil.placeBlock(pos_to_fill, find_in_hotbar(), hole_rotate.get_value(true), hole_rotate.get_value(true), swing)) {
                        holes.remove(pos_to_fill);
                    }
                }
            }
        } else {
            if (pos_to_fill != null) {
                if (WurstplusBlockUtil.placeBlock(pos_to_fill, find_in_hotbar(), hole_rotate.get_value(true), hole_rotate.get_value(true), swing)) {
                    holes.remove(pos_to_fill);
                }
            }
        }
    }

    public void find_new_holes() {

        holes.clear();

        for (BlockPos pos : WurstplusBlockInteractHelper.getSphere(WurstplusPlayerUtil.GetLocalPlayerPosFloored(), hole_range.get_value(1), hole_range.get_value(1), false, true, 0)) {

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

            for (BlockPos seems_blocks : new BlockPos[] {
            new BlockPos( 0, -1,  0),
            new BlockPos( 0,  0, -1),
            new BlockPos( 1,  0,  0),
            new BlockPos( 0,  0,  1),
            new BlockPos(-1,  0,  0)
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

}