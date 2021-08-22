package me.trambled.ozark.ozarkclient.module.misc;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.player.PlayerUtil;
import me.trambled.ozark.ozarkclient.util.world.BlockInteractionHelper;
import me.trambled.ozark.ozarkclient.util.world.BlockInteractionHelper.ValidResult;
import me.trambled.ozark.ozarkclient.util.world.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

/**
 * Made by @Trambled on 2/18/21
 * Modified from Socks
 * Modes taken from salhack and creepy salhack
 * no paste tho
 */

public class AutoBuilder extends Module {

    public AutoBuilder() {
		super(Category.MISC);

		this.name        = "AutoBuilder";
		this.tag         = "AutoBuilder";
		this.description = "Automatically builds certain things.";
    }

    Setting mode = create("Mode", "AutoBuilderMode", "Penis", combobox("Penis", "Swastika", "Highway", "Highway Small"));
    Setting delay = create("Delay", "AutoWitherDelay", 2, 0, 10);
    Setting rotate = create("Rotate", "AutoBuilderRotate", false);
    Setting swing = create("Swing", "AutoBuilderSwing", "Mainhand", combobox("Mainhand", "Offhand", "Both", "None"));
    Setting auto_switch = create("Auto Switch", "AutoBuilderAutoSwitch", false);

    private int delay_counter;

    @Override
	public void update() {
        if (auto_switch.get_value(true)) {
            if (find_in_hotbar() == -1) {
                MessageUtil.send_client_error_message("No obby in hotbar!");
                this.set_disable();
            }
        }

        BlockPos center_pos = PlayerUtil.getLocalPlayerPosFloored();
        ArrayList<BlockPos> blocks_to_fill = new ArrayList<>();

        if (mode.in("Penis"))
        {
            switch (PlayerUtil.GetFacing())
            {
                case East:
                    blocks_to_fill.add(center_pos.east().east());
                    blocks_to_fill.add(center_pos.east().east().north());
                    blocks_to_fill.add(center_pos.east().east().south());
                    blocks_to_fill.add(center_pos.east().east().up());
                    blocks_to_fill.add(center_pos.east().east().up().up());
                    break;
                case North:
                    blocks_to_fill.add(center_pos.north().north());
                    blocks_to_fill.add(center_pos.north().north().west());
                    blocks_to_fill.add(center_pos.north().north().east());
                    blocks_to_fill.add(center_pos.north().north().up());
                    blocks_to_fill.add(center_pos.north().north().up().up());
                    break;
                case South:
                    blocks_to_fill.add(center_pos.south().south());
                    blocks_to_fill.add(center_pos.south().south().west());
                    blocks_to_fill.add(center_pos.south().south().east());
                    blocks_to_fill.add(center_pos.south().south().up());
                    blocks_to_fill.add(center_pos.south().south().up().up());
                    break;
                case West:
                    blocks_to_fill.add(center_pos.west().west());
                    blocks_to_fill.add(center_pos.west().west().north());
                    blocks_to_fill.add(center_pos.west().west().south());
                    blocks_to_fill.add(center_pos.west().west().up());
                    blocks_to_fill.add(center_pos.west().west().up().up());
                    break;
                default:
                    break;
            }


        } else if (mode.in("Highway")) {
            switch (PlayerUtil.GetFacing()) {
                case East:
                    //normal north
                    blocks_to_fill.add(center_pos.down());
                    blocks_to_fill.add(center_pos.down().north());
                    blocks_to_fill.add(center_pos.down().north().north());
                    blocks_to_fill.add(center_pos.down().north().north().north());
                    //highway outer blocks north
                    blocks_to_fill.add(center_pos.down().north().north().north().north());
                    blocks_to_fill.add(center_pos.north().north().north().north());
                    //normal south
                    blocks_to_fill.add(center_pos.down().south());
                    blocks_to_fill.add(center_pos.down().south().south());
                    blocks_to_fill.add(center_pos.down().south().south().south());
                    //highway outer blocks south
                    blocks_to_fill.add(center_pos.down().south().south().south().south());
                    blocks_to_fill.add(center_pos.south().south().south().south());
                    //normal north 2
                    blocks_to_fill.add(center_pos.down().east());
                    blocks_to_fill.add(center_pos.down().east().north());
                    blocks_to_fill.add(center_pos.down().east().north().north());
                    blocks_to_fill.add(center_pos.down().east().north().north().north());
                    //highway outer blocks north 2
                    blocks_to_fill.add(center_pos.down().east().north().north().north().north());
                    blocks_to_fill.add(center_pos.east().north().north().north().north());
                    //normal south
                    blocks_to_fill.add(center_pos.down().east().south());
                    blocks_to_fill.add(center_pos.down().east().south().south());
                    blocks_to_fill.add(center_pos.down().east().south().south().south());
                    //highway outer blocks south 2
                    blocks_to_fill.add(center_pos.east().south().south().south().south());
                    blocks_to_fill.add(center_pos.down().east().south().south().south().south());
                    break;
                case North:
                    //normal east
                    blocks_to_fill.add(center_pos.down());
                    blocks_to_fill.add(center_pos.down().east());
                    blocks_to_fill.add(center_pos.down().east().east());
                    blocks_to_fill.add(center_pos.down().east().east().east());
                    //highway outer blocks east
                    blocks_to_fill.add(center_pos.down().east().east().east().east());
                    blocks_to_fill.add(center_pos.east().east().east().east());
                    //normal west
                    blocks_to_fill.add(center_pos.down().west());
                    blocks_to_fill.add(center_pos.down().west().west());
                    blocks_to_fill.add(center_pos.down().west().west().west());
                    //highway outer blocks west
                    blocks_to_fill.add(center_pos.down().west().west().west().west());
                    blocks_to_fill.add(center_pos.west().west().west().west());
                    //normal east 2
                    blocks_to_fill.add(center_pos.down().north());
                    blocks_to_fill.add(center_pos.down().north().east());
                    blocks_to_fill.add(center_pos.down().north().east().east());
                    blocks_to_fill.add(center_pos.down().north().east().east().east());
                    //highway outer blocks east 2
                    blocks_to_fill.add(center_pos.down().north().east().east().east().east());
                    blocks_to_fill.add(center_pos.north().east().east().east().east());
                    //normal west
                    blocks_to_fill.add(center_pos.down().north().west());
                    blocks_to_fill.add(center_pos.down().north().west().west());
                    blocks_to_fill.add(center_pos.down().north().west().west().west());
                    //highway outer blocks west 2
                    blocks_to_fill.add(center_pos.north().east().east().east().east());
                    blocks_to_fill.add(center_pos.down().north().east().east().east().east());
                    break;
                case South:
                    //normal east
                    blocks_to_fill.add(center_pos.down());
                    blocks_to_fill.add(center_pos.down().east());
                    blocks_to_fill.add(center_pos.down().east().east());
                    blocks_to_fill.add(center_pos.down().east().east().east());
                    //highway outer blocks east
                    blocks_to_fill.add(center_pos.down().east().east().east().east());
                    blocks_to_fill.add(center_pos.east().east().east().east());
                    //normal west
                    blocks_to_fill.add(center_pos.down().west());
                    blocks_to_fill.add(center_pos.down().west().west());
                    blocks_to_fill.add(center_pos.down().west().west().west());
                    //highway outer blocks west
                    blocks_to_fill.add(center_pos.down().west().west().west().west());
                    blocks_to_fill.add(center_pos.west().west().west().west());
                    //normal east 2
                    blocks_to_fill.add(center_pos.down().south());
                    blocks_to_fill.add(center_pos.down().south().east());
                    blocks_to_fill.add(center_pos.down().south().east().east());
                    blocks_to_fill.add(center_pos.down().south().east().east().east());
                    //highway outer blocks east 2
                    blocks_to_fill.add(center_pos.down().south().east().east().east().east());
                    blocks_to_fill.add(center_pos.south().east().east().east().east());
                    //normal west
                    blocks_to_fill.add(center_pos.down().south().west());
                    blocks_to_fill.add(center_pos.down().south().west().west());
                    blocks_to_fill.add(center_pos.down().south().west().west().west());
                    //highway outer blocks west 2
                    blocks_to_fill.add(center_pos.south().east().east().east().east());
                    blocks_to_fill.add(center_pos.down().south().east().east().east().east());
                    break;
                case West:
                    //normal north
                    blocks_to_fill.add(center_pos.down());
                    blocks_to_fill.add(center_pos.down().north());
                    blocks_to_fill.add(center_pos.down().north().north());
                    blocks_to_fill.add(center_pos.down().north().north().north());
                    //highway outer blocks north
                    blocks_to_fill.add(center_pos.down().north().north().north().north());
                    blocks_to_fill.add(center_pos.north().north().north().north());
                    //normal south
                    blocks_to_fill.add(center_pos.down().south());
                    blocks_to_fill.add(center_pos.down().south().south());
                    blocks_to_fill.add(center_pos.down().south().south().south());
                    //highway outer blocks south
                    blocks_to_fill.add(center_pos.down().south().south().south().south());
                    blocks_to_fill.add(center_pos.south().south().south().south());
                    //normal north 2
                    blocks_to_fill.add(center_pos.down().west());
                    blocks_to_fill.add(center_pos.down().west().north());
                    blocks_to_fill.add(center_pos.down().west().north().north());
                    blocks_to_fill.add(center_pos.down().west().north().north().north());
                    //highway outer blocks north 2
                    blocks_to_fill.add(center_pos.down().west().north().north().north().north());
                    blocks_to_fill.add(center_pos.west().north().north().north().north());
                    //normal south
                    blocks_to_fill.add(center_pos.down().west().south());
                    blocks_to_fill.add(center_pos.down().west().south().south());
                    blocks_to_fill.add(center_pos.down().west().south().south().south());
                    //highway outer blocks south 2
                    blocks_to_fill.add(center_pos.west().south().south().south().south());
                    blocks_to_fill.add(center_pos.down().west().south().south().south().south());
                    break;
                default:
                    break;
            }
        } else if (mode.in("Swastika")) {
            switch (PlayerUtil.GetFacing()) {
                case East:
                    blocks_to_fill.add(center_pos.east().east());
                    blocks_to_fill.add(center_pos.east().east().up());
                    blocks_to_fill.add(center_pos.east().east().up().up());
                    blocks_to_fill.add(center_pos.east().east().up().up().up());
                    blocks_to_fill.add(center_pos.east().east().up().up().up().up());
                    blocks_to_fill.add(center_pos.east().east().up().up().up().up().south());
                    blocks_to_fill.add(center_pos.east().east().up().up().up().up().south().south());
                    blocks_to_fill.add(center_pos.east().east().up().up().south());
                    blocks_to_fill.add(center_pos.east().east().up().up().south().south());
                    blocks_to_fill.add(center_pos.east().east().up().south().south());
                    blocks_to_fill.add(center_pos.east().east().south().south());
                    blocks_to_fill.add(center_pos.east().east().up().up().north());
                    blocks_to_fill.add(center_pos.east().east().up().up().north().north());
                    blocks_to_fill.add(center_pos.east().east().up().up().up().north().north());
                    blocks_to_fill.add(center_pos.east().east().up().up().up().up().north().north());
                    blocks_to_fill.add(center_pos.east().east().north().north());
                    blocks_to_fill.add(center_pos.east().east().north());
                    break;
                case North:
                    blocks_to_fill.add(center_pos.north().north());
                    blocks_to_fill.add(center_pos.north().north().up());
                    blocks_to_fill.add(center_pos.north().north().up().up());
                    blocks_to_fill.add(center_pos.north().north().up().up().up());
                    blocks_to_fill.add(center_pos.north().north().up().up().up().up());
                    blocks_to_fill.add(center_pos.north().north().up().up().up().up().east());
                    blocks_to_fill.add(center_pos.north().north().up().up().up().up().east().east());
                    blocks_to_fill.add(center_pos.north().north().up().up().east());
                    blocks_to_fill.add(center_pos.north().north().up().up().east().east());
                    blocks_to_fill.add(center_pos.north().north().up().east().east());
                    blocks_to_fill.add(center_pos.north().north().east().east());
                    blocks_to_fill.add(center_pos.north().north().up().up().west());
                    blocks_to_fill.add(center_pos.north().north().up().up().west().west());
                    blocks_to_fill.add(center_pos.north().north().up().up().up().west().west());
                    blocks_to_fill.add(center_pos.north().north().up().up().up().up().west().west());
                    blocks_to_fill.add(center_pos.north().north().west().west());
                    blocks_to_fill.add(center_pos.north().north().west());
                    break;
                case South:
                    blocks_to_fill.add(center_pos.south().south());
                    blocks_to_fill.add(center_pos.south().south().up());
                    blocks_to_fill.add(center_pos.south().south().up().up());
                    blocks_to_fill.add(center_pos.south().south().up().up().up());
                    blocks_to_fill.add(center_pos.south().south().up().up().up().up());
                    blocks_to_fill.add(center_pos.south().south().up().up().up().up().west());
                    blocks_to_fill.add(center_pos.south().south().up().up().up().up().west().west());
                    blocks_to_fill.add(center_pos.south().south().up().up().west());
                    blocks_to_fill.add(center_pos.south().south().up().up().west().west());
                    blocks_to_fill.add(center_pos.south().south().up().west().west());
                    blocks_to_fill.add(center_pos.south().south().west().west());
                    blocks_to_fill.add(center_pos.south().south().up().up().east());
                    blocks_to_fill.add(center_pos.south().south().up().up().east().east());
                    blocks_to_fill.add(center_pos.south().south().up().up().up().east().east());
                    blocks_to_fill.add(center_pos.south().south().up().up().up().up().east().east());
                    blocks_to_fill.add(center_pos.south().south().east().east());
                    blocks_to_fill.add(center_pos.south().south().east());
                    break;
                case West:
                    blocks_to_fill.add(center_pos.west().west());
                    blocks_to_fill.add(center_pos.west().west().up());
                    blocks_to_fill.add(center_pos.west().west().up().up());
                    blocks_to_fill.add(center_pos.west().west().up().up().up());
                    blocks_to_fill.add(center_pos.west().west().up().up().up().up());
                    blocks_to_fill.add(center_pos.west().west().up().up().up().up().north());
                    blocks_to_fill.add(center_pos.west().west().up().up().up().up().north().north());
                    blocks_to_fill.add(center_pos.west().west().up().up().north());
                    blocks_to_fill.add(center_pos.west().west().up().up().north().north());
                    blocks_to_fill.add(center_pos.west().west().up().north().north());
                    blocks_to_fill.add(center_pos.west().west().north().north());
                    blocks_to_fill.add(center_pos.west().west().up().up().south());
                    blocks_to_fill.add(center_pos.west().west().up().up().south().south());
                    blocks_to_fill.add(center_pos.west().west().up().up().up().south().south());
                    blocks_to_fill.add(center_pos.west().west().up().up().up().up().south().south());
                    blocks_to_fill.add(center_pos.west().west().south().south());
                    blocks_to_fill.add(center_pos.west().west().south());
                    break;
                default:
                    break;
            }
        } else if (mode.in("Highway Small")) {
            switch (PlayerUtil.GetFacing()) {
                case East:
                    //normal north
                    blocks_to_fill.add(center_pos.down());
                    blocks_to_fill.add(center_pos.down().north());
                    blocks_to_fill.add(center_pos.down().north().north());
                    //highway outer blocks north
                    blocks_to_fill.add(center_pos.down().north().north().north());
                    blocks_to_fill.add(center_pos.north().north().north());
                    //normal south
                    blocks_to_fill.add(center_pos.down().south());
                    blocks_to_fill.add(center_pos.down().south().south());
                    //highway outer blocks south
                    blocks_to_fill.add(center_pos.down().south().south().south());
                    blocks_to_fill.add(center_pos.south().south().south());
                    //normal north 2
                    blocks_to_fill.add(center_pos.down().east());
                    blocks_to_fill.add(center_pos.down().east().north());
                    blocks_to_fill.add(center_pos.down().east().north().north());
                    //highway outer blocks north 2
                    blocks_to_fill.add(center_pos.down().east().north().north().north());
                    blocks_to_fill.add(center_pos.east().north().north().north());
                    //normal south
                    blocks_to_fill.add(center_pos.down().east().south());
                    blocks_to_fill.add(center_pos.down().east().south().south());
                    //highway outer blocks south 2
                    blocks_to_fill.add(center_pos.east().south().south().south());
                    blocks_to_fill.add(center_pos.down().east().south().south().south());
                    break;
                case North:
                    //normal east
                    blocks_to_fill.add(center_pos.down());
                    blocks_to_fill.add(center_pos.down().east());
                    blocks_to_fill.add(center_pos.down().east().east());
                    //highway outer blocks east
                    blocks_to_fill.add(center_pos.down().east().east().east());
                    blocks_to_fill.add(center_pos.east().east().east());
                    //normal west
                    blocks_to_fill.add(center_pos.down().west());
                    blocks_to_fill.add(center_pos.down().west().west());
                    //highway outer blocks west
                    blocks_to_fill.add(center_pos.down().west().west().west());
                    blocks_to_fill.add(center_pos.west().west().west());
                    //normal east 2
                    blocks_to_fill.add(center_pos.down().north());
                    blocks_to_fill.add(center_pos.down().north().east());
                    blocks_to_fill.add(center_pos.down().north().east().east());
                    //highway outer blocks east 2
                    blocks_to_fill.add(center_pos.down().north().east().east().east());
                    blocks_to_fill.add(center_pos.north().east().east().east());
                    //normal west 2
                    blocks_to_fill.add(center_pos.down().north().west());
                    blocks_to_fill.add(center_pos.down().north().west().west());
                    //highway outer blocks west 2
                    blocks_to_fill.add(center_pos.north().east().east().east());
                    blocks_to_fill.add(center_pos.down().north().east().east().east());
                    break;
                case South:
                    //normal east
                    blocks_to_fill.add(center_pos.down());
                    blocks_to_fill.add(center_pos.down().east());
                    blocks_to_fill.add(center_pos.down().east().east());
                    //highway outer blocks east
                    blocks_to_fill.add(center_pos.down().east().east().east());
                    blocks_to_fill.add(center_pos.east().east().east());
                    //normal west
                    blocks_to_fill.add(center_pos.down().west());
                    blocks_to_fill.add(center_pos.down().west().west());
                    //highway outer blocks west
                    blocks_to_fill.add(center_pos.down().west().west());
                    blocks_to_fill.add(center_pos.west().west());
                    //normal east 2
                    blocks_to_fill.add(center_pos.down().south());
                    blocks_to_fill.add(center_pos.down().south().east());
                    blocks_to_fill.add(center_pos.down().south().east().east());
                    //highway outer blocks east 2
                    blocks_to_fill.add(center_pos.down().south().east().east().east());
                    blocks_to_fill.add(center_pos.south().east().east().east());
                    //normal west 2
                    blocks_to_fill.add(center_pos.down().south().west());
                    blocks_to_fill.add(center_pos.down().south().west().west());
                    blocks_to_fill.add(center_pos.down().south().west().west().west());
                    //highway outer blocks west 2
                    blocks_to_fill.add(center_pos.south().west().west().west());
                    blocks_to_fill.add(center_pos.down().south().west().west().west());
                    break;
                case West:
                    //normal north
                    blocks_to_fill.add(center_pos.down());
                    blocks_to_fill.add(center_pos.down().north());
                    blocks_to_fill.add(center_pos.down().north().north());
                    //highway outer blocks north
                    blocks_to_fill.add(center_pos.down().north().north().north());
                    blocks_to_fill.add(center_pos.north().north().north());
                    //normal south
                    blocks_to_fill.add(center_pos.down().south());
                    blocks_to_fill.add(center_pos.down().south().south());
                    //highway outer blocks south
                    blocks_to_fill.add(center_pos.down().south().south().south());
                    blocks_to_fill.add(center_pos.south().south().south());
                    //normal north 2
                    blocks_to_fill.add(center_pos.down().west());
                    blocks_to_fill.add(center_pos.down().west().north());
                    blocks_to_fill.add(center_pos.down().west().north().north());
                    //highway outer blocks north 2
                    blocks_to_fill.add(center_pos.down().west().north().north().north());
                    blocks_to_fill.add(center_pos.west().north().north().north());
                    //normal south 2
                    blocks_to_fill.add(center_pos.down().west().south());
                    blocks_to_fill.add(center_pos.down().west().south().south());
                    //highway outer blocks south 2
                    blocks_to_fill.add(center_pos.west().south().south().south());
                    blocks_to_fill.add(center_pos.down().west().south().south().south());
                    break;
                default:
                    break;
            }
        }



        BlockPos pos_to_fill = null;

        for (BlockPos pos : blocks_to_fill) {

            ValidResult result = BlockInteractionHelper.valid(pos);

            if (result != ValidResult.Ok) continue;

            if (pos == null) continue;

            pos_to_fill = pos;
            break;

        }

        if (auto_switch.get_value(true)) {
            mc.player.inventory.currentItem = find_in_hotbar();
        }

        if (delay_counter > delay.get_value(1)) {
            BlockUtil.placeBlock(pos_to_fill, rotate.get_value(true), false, swing);
            delay_counter = 0;
        }

        delay_counter++;

    }

    private int find_in_hotbar() {

        for (int i = 0; i < 9; ++i) {

            final ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {

                final Block block = ((ItemBlock) stack.getItem()).getBlock();

                if (block instanceof BlockEnderChest)
                    return i;

                else if (block instanceof BlockObsidian)
                    return i;

            }
        }
        return -1;
    }

}