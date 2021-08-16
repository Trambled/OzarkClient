package me.trambled.ozark.ozarkclient.module.misc;

import me.trambled.ozark.ozarkclient.event.events.EventPlayerTravel;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Mouse;

public class MCP extends Module {
    private boolean clicked;
    public
    MCP () {
        super(Category.MISC);
        this.name = "MCP";
        this.tag = "MCP";
        this.description = "Throws a pearl when u middle click.";
    }

    @EventHandler
    private final Listener< EventPlayerTravel > listener = new Listener<>( p_Event -> {
        if (mc.currentScreen == null && Mouse.isButtonDown(2)) {
            if (!this.clicked ) {
                final int pearlSLot = findPearlInHotbar();
                if (pearlSLot != -1) {
                    final int oldSlot = mc.player.inventory.currentItem;
                    mc.player.inventory.currentItem = pearlSLot;
                    mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
                    mc.player.inventory.currentItem = oldSlot;
                }
            }
        }
    });

    private boolean isItemStackPearl(final ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemEnderPearl;
    }

    private int findPearlInHotbar() {
        for (int index = 0; InventoryPlayer.isHotbar(index); index++) {
            if (isItemStackPearl(mc.player.inventory.getStackInSlot(index))) return index;
        }
        return -1;
    }

}