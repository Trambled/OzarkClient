package me.travis.wurstplus.wurstplustwo.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.item.Item;
import net.minecraft.client.Minecraft;

//xenon
public class InventoryUtils
{
    public static final Minecraft mc;
    
    public static int getHotbarItemSlot(final Item item) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (InventoryUtils.mc.player.inventory.getStackInSlot(i).getItem() == item) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            return InventoryUtils.mc.player.inventory.currentItem;
        }
        return slot;
    }
    
    public static void switchToSlot(final int slot) {
        InventoryUtils.mc.player.inventory.currentItem = slot;
    }
    
    public static void switchToSlot(final Item item) {
        InventoryUtils.mc.player.inventory.currentItem = getHotbarItemSlot(item);
    }
    
    public static void switchToSlotGhost(final int slot) {
        InventoryUtils.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
    }
    
    public static void switchToSlotGhost(final Item item) {
        switchToSlotGhost(getHotbarItemSlot(item));
    }
    
    public static int getItemAmount(EntityPlayer player, Item item) {
        int amount = 0;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack.getItem() == item) {
                amount += stack.getCount();
            }
        }
        return amount;
    }
    
    public static int getInventoryItemSlot(final Item item) {
        int slot = -1;
        for (int i = 45; i > 0; --i) {
            if (InventoryUtils.mc.player.inventory.getStackInSlot(i).getItem() == item) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    
    public static void moveItemToOffhand(final int slot) {
        boolean startMoving = true;
        boolean moving = false;
        boolean returning = false;
        int returnSlot = 0;
        if (slot == -1) {
            return;
        }
        if (!moving && startMoving) {
            InventoryUtils.mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
            moving = true;
            startMoving = false;
        }
        if (moving) {
            InventoryUtils.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
            moving = false;
            returning = true;
        }
        if (returning) {
            for (int i = 0; i < 45; ++i) {
                if (InventoryUtils.mc.player.inventory.getStackInSlot(i).isEmpty()) {
                    returnSlot = i;
                    break;
                }
            }
            if (returnSlot != -1) {
                InventoryUtils.mc.playerController.windowClick(0, (returnSlot < 9) ? (returnSlot + 36) : returnSlot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
            }
            returning = false;
        }
        startMoving = true;
    }
    
    public static void moveItemToOffhand(final Item item) {
        final int slot = getInventoryItemSlot(item);
        if (slot != -1) {
            moveItemToOffhand(slot);
        }
    }
    
    public static void moveItem(final int slot, final int slotOut) {
        boolean startMoving = true;
        boolean moving = false;
        boolean returning = false;
        if (!moving && startMoving) {
            InventoryUtils.mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
            moving = true;
            startMoving = false;
        }
        if (moving) {
            InventoryUtils.mc.playerController.windowClick(0, (slotOut < 9) ? (slotOut + 36) : slotOut, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
            moving = false;
            returning = true;
        }
        if (returning) {
            InventoryUtils.mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
            returning = false;
        }
        startMoving = true;
    }
    
    public static void moveItem(final Item item, final int slot) {
        moveItem(getInventoryItemSlot(item), slot);
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}