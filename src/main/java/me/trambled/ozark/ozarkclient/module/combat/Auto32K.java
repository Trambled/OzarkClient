package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.world.BlockUtil;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.util.Objects;

public class Auto32K extends Module {

    public Auto32K() {
        super(Category.COMBAT);

        this.name        = "Auto32k";
        this.tag         = "Auto32k";
        this.description = "Fastest in the west.";
    }

    private BlockPos pos;

    private int hopper_slot;
    private int redstone_slot;
    private int shulker_slot;
    private int ticks_past;
    private int[] rot;

    private boolean setup;
    private boolean place_redstone;
    private boolean dispenser_done;
  
    Setting place_mode = create("Place Mode", "AutotkPlaceMode", "Auto", combobox("Auto", "Looking", "Hopper"));
    Setting swing = create("Swing", "AutotkSwing", "Mainhand", combobox("Mainhand", "Offhand", "Both", "None"));
    Setting delay = create("Delay", "AutotkDelay", 4, 0, 10);
    Setting rotate = create("Rotate", "Autotkrotate", false);
    Setting ghost_mode = create("Ghost Switch", "GhostSwitch", true);
    Setting debug = create("Debug", "AutotkDebug", false);

    @Override
    protected void enable() {

        ticks_past = 0;

        setup = false;
        dispenser_done = false;
        place_redstone = false;

        hopper_slot = -1;
        int dispenser_slot = -1;
        redstone_slot = -1;
        shulker_slot = -1;
        int block_slot = -1;

        for (int i = 0; i < 9; i++) {

            Item item = mc.player.inventory.getStackInSlot(i).getItem();

            if (item == Item.getItemFromBlock(Blocks.HOPPER)) hopper_slot = i;
            else if (item == Item.getItemFromBlock(Blocks.DISPENSER)) dispenser_slot = i;
            else if (item == Item.getItemFromBlock(Blocks.REDSTONE_BLOCK)) redstone_slot = i;
            else if (item instanceof ItemShulkerBox) shulker_slot = i;
            else if (item instanceof ItemBlock) block_slot = i;

        }

        if ((hopper_slot == -1 || dispenser_slot == -1 || redstone_slot == -1 || shulker_slot == -1 || block_slot == -1) && !place_mode.in("Hopper")) {
            MessageUtil.send_client_message("missing item");
            this.set_disable();
            return;
        } else if (hopper_slot == -1 || shulker_slot == -1) {
            MessageUtil.send_client_message("missing item");
            this.set_disable();
            return;
        }

        if (place_mode.in("Looking")) {

            RayTraceResult r = mc.player.rayTrace(5.0D, mc.getRenderPartialTicks());

            pos = Objects.requireNonNull(r).getBlockPos().up();

            double pos_x = (double) pos.getX() - mc.player.posX;
            double pos_z = (double) pos.getZ() - mc.player.posZ;

            rot = Math.abs(pos_x) > Math.abs(pos_z) ? (pos_x > 0.0D ? new int[] {-1, 0} : new int[] {1, 0}) : (pos_z > 0.0D ? new int[] {0, -1} : new int[] {0, 1});

            if (BlockUtil.canPlaceBlock(this.pos) && BlockUtil.isBlockEmpty(this.pos) && BlockUtil.isBlockEmpty(this.pos.add(this.rot[0], 0, this.rot[1])) &&
                    BlockUtil.isBlockEmpty(this.pos.add(0, 1, 0)) && BlockUtil.isBlockEmpty(this.pos.add(0, 2, 0)) && BlockUtil.isBlockEmpty(this.pos.add(this.rot[0], 1, this.rot[1]))) {

                BlockUtil.placeBlock(pos, block_slot, rotate.get_value(true), false, swing, ghost_mode.get_value(true));
                BlockUtil.rotatePacket((double) this.pos.add(-this.rot[0], 1, -this.rot[1]).getX() + 0.5D, this.pos.getY() + 1, (double) this.pos.add(-this.rot[0], 1, -this.rot[1]).getZ() + 0.5D);
                BlockUtil.placeBlock(this.pos.up(), dispenser_slot, false, false, swing, ghost_mode.get_value(true));
                BlockUtil.openBlock(this.pos.up());

                setup = true;

            } else {
                MessageUtil.send_client_message("unable to place");
                this.set_disable();
            }
        } else if (place_mode.in("Auto")) {
            for (int x = -2; x <= 2; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -2; z <= 2; z++) {

                        this.rot = Math.abs(x) > Math.abs(z) ? (x > 0 ? new int[] {-1, 0} : new int[] {1, 0}) : (z > 0 ? new int[] {0, -1} : new int[] {0, 1});
                        this.pos = mc.player.getPosition().add(x, y, z);

                        if (mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(mc.player.getPositionVector().add(x - rot[0] / 2f, (double) y + 0.5D, z + rot[1] / 2)) <= 4.5D && mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(mc.player.getPositionVector().add((double) x + 0.5D, (double) y + 2.5D, (double) z + 0.5D)) <= 4.5D && BlockUtil.canPlaceBlock(this.pos) && BlockUtil.isBlockEmpty(this.pos) && BlockUtil.isBlockEmpty(this.pos.add(this.rot[0], 0, this.rot[1])) && BlockUtil.isBlockEmpty(this.pos.add(0, 1, 0)) && BlockUtil.isBlockEmpty(this.pos.add(0, 2, 0)) && BlockUtil.isBlockEmpty(this.pos.add(this.rot[0], 1, this.rot[1])))
                        {
                            BlockUtil.placeBlock(this.pos, block_slot, rotate.get_value(true), false, swing, ghost_mode.get_value(true));
                            BlockUtil.rotatePacket((double) this.pos.add(-this.rot[0], 1, -this.rot[1]).getX() + 0.5D, this.pos.getY() + 1, (double) this.pos.add(-this.rot[0], 1, -this.rot[1]).getZ() + 0.5D);
                            BlockUtil.placeBlock(this.pos.up(), dispenser_slot, false, false, swing, ghost_mode.get_value(true));
                            BlockUtil.openBlock(this.pos.up());

                            setup = true;

                            return;
                        }

                    }
                }
            }
            MessageUtil.send_client_message("unable to place");
            this.set_disable();
        } else {
            for (int z = -2; z <= 2; z++) {
                for (int y = -1; y <= 2; y++) {
                    for (int x = -2; x <= 2; x++) {
                        if ((z != 0 || y != 0 || x != 0) && (z != 0 || y != 1 || x != 0) && BlockUtil.isBlockEmpty(mc.player.getPosition().add(z, y, x)) && mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(mc.player.getPositionVector().add((double) z + 0.5D, (double) y + 0.5D, (double) x + 0.5D)) < 4.5D && BlockUtil.isBlockEmpty(mc.player.getPosition().add(z, y + 1, x)) && mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(mc.player.getPositionVector().add((double) z + 0.5D, (double) y + 1.5D, (double) x + 0.5D)) < 4.5D)  {

                            BlockUtil.placeBlock(mc.player.getPosition().add(z, y, x), hopper_slot, rotate.get_value(true), false, swing,ghost_mode.get_value(true));
                            BlockUtil.placeBlock(mc.player.getPosition().add(z, y + 1, x), shulker_slot, rotate.get_value(true), false, swing, ghost_mode.get_value(true));
                            BlockUtil.openBlock(mc.player.getPosition().add(z, y, x));

                            pos = mc.player.getPosition().add(z, y, x);

                            dispenser_done = true;
                            place_redstone = true;
                            setup = true;

                            return;

                        }
                    }
                }
            }
        }
    }

    @Override
    public void update() {

        if (ticks_past > 50 && !(mc.currentScreen instanceof GuiHopper)) {
            MessageUtil.send_client_message("inactive too long, disabling");
            this.set_disable();
            return;
        }

        if (setup && ticks_past > this.delay.get_value(1)) {

            if (!dispenser_done) { // ching chong
                try {
                    mc.playerController.windowClick(mc.player.openContainer.windowId, 36 + shulker_slot, 0, ClickType.QUICK_MOVE, mc.player);
                    dispenser_done = true;
                    if (debug.get_value(true)) {
                        MessageUtil.send_client_message("sent item");
                    }
                } catch (Exception ignored) {

                }
            }

            if (!place_redstone) {
                BlockUtil.placeBlock(pos.add(0, 2, 0), redstone_slot, rotate.get_value(true), false, swing, ghost_mode.get_value(true));
                if (debug.get_value(true)) {
                    MessageUtil.send_client_message("placed redstone");
                }
                place_redstone = true;
                return;
            }

            if (!place_mode.in("Hopper") && mc.world.getBlockState(this.pos.add(this.rot[0], 1, this.rot[1])).getBlock() instanceof BlockShulkerBox
                    && mc.world.getBlockState(this.pos.add(this.rot[0], 0, this.rot[1])).getBlock() != Blocks.HOPPER
                    && place_redstone && dispenser_done && !(mc.currentScreen instanceof GuiInventory)) {
                BlockUtil.placeBlock(this.pos.add(this.rot[0], 0, this.rot[1]), hopper_slot, rotate.get_value(true), false, swing, ghost_mode.get_value(true));
                BlockUtil.openBlock(this.pos.add(this.rot[0], 0, this.rot[1]));
                if (debug.get_value(true)) {
                    MessageUtil.send_client_message("in the hopper");
                }
            }

            if (mc.currentScreen instanceof GuiHopper) {

                GuiHopper gui = (GuiHopper) mc.currentScreen;

                int slot;
                for (slot = 32; slot <= 40; slot++) {
                    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, gui.inventorySlots.getSlot(slot).getStack()) > 5)  {
                        mc.player.inventory.currentItem = slot - 32;
                        break;
                    }
                }

                if (!(gui.inventorySlots.inventorySlots.get(0).getStack().getItem() instanceof ItemAir)) {
                    boolean swapReady = ! ( (GuiContainer) mc.currentScreen ).inventorySlots.getSlot ( 0 ).getStack ( ).isEmpty;
                    if (!((GuiContainer)mc.currentScreen).inventorySlots.getSlot(shulker_slot + 32).getStack().isEmpty) {
                        swapReady = false;
                    }
                    if (swapReady) {
                        mc.playerController.windowClick(((GuiContainer)mc.currentScreen).inventorySlots.windowId, 0, shulker_slot, ClickType.SWAP, mc.player);
                        this.disable();
                    }
                }

            }

        }

        ticks_past++;

    }

}
