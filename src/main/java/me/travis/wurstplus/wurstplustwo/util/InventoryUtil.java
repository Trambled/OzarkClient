package me.travis.wurstplus.wurstplustwo.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.client.Minecraft;

//from xanax, very cool
public final class InventoryUtil
{
    private static final Minecraft mc;

    public static int getHotbarSlot(final Item item)
    {
        for (int i = 0; i < 9; i++)
        {
            final Item item1 = mc.player.inventory.getStackInSlot(i).getItem();

            if (item.equals(item1)) return i;
        }
        return -1;
    }

    public static int getHotbarSlot(final Block block)
    {
        for (int i = 0; i < 9; i++)
        {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();

            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(block)) return i;
        }

        return -1;
    }

    static {
        mc = Minecraft.getMinecraft();
    }
}
