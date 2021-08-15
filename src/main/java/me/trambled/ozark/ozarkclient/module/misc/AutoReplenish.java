package me.trambled.ozark.ozarkclient.module.misc;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.PairUtil;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AutoReplenish extends Module {

    public AutoReplenish() {
        super(Category.MISC);

        this.name = "HotbarReplenish";
        this.tag = "HotbarReplenish";
        this.description = "Automatically replaces a used stack with a fresh new one from ur inv.";
    }

    Setting threshold = create("Threshold", "AutoReplenishThreshold", 16, 1, 63);
    Setting tickdelay = create("Delay", "AutoReplenishDelay", 1, 1, 10);

    private int delay_step = 0;

    @Override
    public void update() {

        if (mc.currentScreen instanceof GuiContainer) return;

        if (delay_step < tickdelay.get_value(1)) {
            delay_step++;
            return;
        }

        delay_step = 0;

        final PairUtil<Integer, Integer> slots = findReplenishableHotbarSlot();
        if (slots == null) return;

        final int inventorySlot = slots.getKey();
        final int hotbarSlot = slots.getValue();
        mc.playerController.windowClick(0, inventorySlot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, hotbarSlot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, inventorySlot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.updateController();

    }

    private PairUtil<Integer, Integer> findReplenishableHotbarSlot() {
        PairUtil<Integer, Integer> returnPair = null;
        for (final Map.Entry<Integer, ItemStack> hotbarSlot : get_hotbar().entrySet()) {
            final ItemStack stack = hotbarSlot.getValue();
            if (!stack.isEmpty) {
                if (stack.getItem() == Items.AIR) {
                    continue;
                }
                if (!stack.isStackable()) {
                    continue;
                }
                if (stack.stackSize >= stack.getMaxStackSize()) {
                    continue;
                }
                if (stack.stackSize > this.threshold.get_value(1)) {
                    continue;
                }
                final int inventorySlot = this.findCompatibleInventorySlot(stack);
                if (inventorySlot == -1) {
                    continue;
                }
                returnPair = new PairUtil<>(inventorySlot, hotbarSlot.getKey());
            }
        }
        return returnPair;
    }
    
    private int findCompatibleInventorySlot(final ItemStack hotbarStack) {
        int inventorySlot = -1;
        int smallestStackSize = 999;
        for (final Map.Entry<Integer, ItemStack> entry : get_inventory().entrySet()) {
            final ItemStack inventoryStack = entry.getValue();
            if (!inventoryStack.isEmpty) {
                if (inventoryStack.getItem() == Items.AIR) {
                    continue;
                }
                if (!this.isCompatibleStacks(hotbarStack, inventoryStack)) {
                    continue;
                }
                final int currentStackSize = mc.player.inventoryContainer.getInventory().get(entry.getKey()).stackSize;
                if (smallestStackSize <= currentStackSize) {
                    continue;
                }
                smallestStackSize = currentStackSize;
                inventorySlot = entry.getKey();
            }
        }
        return inventorySlot;
    }
    
    private boolean isCompatibleStacks(final ItemStack stack1, final ItemStack stack2) {
        if (!stack1.getItem().equals(stack2.getItem())) {
            return false;
        }
        if (stack1.getItem() instanceof ItemBlock && stack2.getItem() instanceof ItemBlock) {
            final Block block1 = ((ItemBlock)stack1.getItem()).getBlock();
            final Block block2 = ((ItemBlock)stack2.getItem()).getBlock();
            if (!block1.material.equals(block2.material)) {
                return false;
            }
        }
        return stack1.getDisplayName().equals(stack2.getDisplayName()) && stack1.getItemDamage() == stack2.getItemDamage();
    }

    private Map<Integer, ItemStack> get_inventory() {
        return get_inv_slots(9, 35);
    }
    
    private Map<Integer, ItemStack> get_hotbar() {
        return get_inv_slots(36, 44);
    }

    private Map<Integer, ItemStack> get_inv_slots(int current, final int last) {
        final Map<Integer, ItemStack> fullInventorySlots = new HashMap <> ( );
        while (current <= last) {
            fullInventorySlots.put(current, mc.player.inventoryContainer.getInventory().get(current));
            ++current;
        }
        return fullInventorySlots;
    }

}
