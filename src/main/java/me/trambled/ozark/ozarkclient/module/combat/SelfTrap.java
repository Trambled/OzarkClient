package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.world.BlockInteractionHelper;
import me.trambled.ozark.ozarkclient.util.world.BlockInteractionHelper.ValidResult;
import me.trambled.ozark.ozarkclient.util.world.BlockUtil;
import me.trambled.ozark.ozarkclient.util.world.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SelfTrap extends Module {
    
    public SelfTrap() {

        super(Category.COMBAT);

		this.name        = "SelfTrap";
		this.tag         = "SelfTrap";
		this.description = "Oh 'eck, ive trapped me sen again.";
    }

    Setting toggle = create("Toggle", "SelfTrapToggle", true);
    Setting rotate = create("Rotate", "SelfTrapRotate", false);
    Setting swing = create("Swing", "SelfTrapSwing", "Mainhand", combobox("Mainhand", "Offhand", "Both", "None"));
    Setting ghost_mode = create("Ghost Switch", "GhostSwitch", true);
    Setting delay = create("Delay", "SelfTrapDelay", 0, 0, 10);

    private BlockPos trap_pos;
    private int delay_counter;

    @Override
    protected void enable() {
        if (find_in_hotbar() == -1) {
            this.set_disable();
        }
    }

    @Override
    public void update() {
        final Vec3d pos = MathUtil.interpolateEntity(mc.player, mc.getRenderPartialTicks());
        trap_pos = new BlockPos(pos.x, pos.y + 2, pos.z);
        if (is_trapped()) {

            if (toggle.get_value(true)) {
                toggle();
                return;
            } 

        }

        if (delay_counter > delay.get_value(1)) {

            ValidResult result = BlockInteractionHelper.valid(trap_pos);

            if (result == ValidResult.AlreadyBlockThere && !mc.world.getBlockState(trap_pos).getMaterial().isReplaceable()) {
                return;
            }

            if (result == ValidResult.NoNeighbors) {

                BlockPos[] tests = {
                        trap_pos.north(),
                        trap_pos.south(),
                        trap_pos.east(),
                        trap_pos.west(),
                        trap_pos.up(),
                        trap_pos.down().west() // ????? salhack is weird and i dont care enough to remove this. who the fuck uses this shit anyways fr fucking jumpy
                };

                for (BlockPos pos_ : tests) {

                    ValidResult result_ = BlockInteractionHelper.valid(pos_);

                    if (result_ == ValidResult.NoNeighbors || result_ == ValidResult.NoEntityCollision) continue;

                    if (BlockUtil.placeBlock(pos_, find_in_hotbar(), rotate.get_value(true), rotate.get_value(true), swing, ghost_mode.get_value(true))) {
                        delay_counter = 0;
                        return;
                    }

                }

                delay_counter = 0;
                return;

            }

            BlockUtil.placeBlock(trap_pos, find_in_hotbar(), rotate.get_value(true), rotate.get_value(true), swing, ghost_mode.get_value(true));
            delay_counter = 0;
        }


        delay_counter++;
    }

    public boolean is_trapped() {

        if (trap_pos == null) return false;

        IBlockState state = mc.world.getBlockState(trap_pos);

        return state.getBlock() != Blocks.AIR && state.getBlock() != Blocks.WATER && state.getBlock() != Blocks.LAVA;

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
