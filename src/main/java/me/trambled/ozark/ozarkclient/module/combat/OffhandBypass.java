package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

//vethack
public class OffhandBypass extends Module
{	
    public OffhandBypass() {
        super(Category.COMBAT);

        this.name = "OffhandBypass";
        this.tag = "OffhandBypass";
        this.description = "Makes your hand that u use set to offhand for gapple offhand.";
    }
	
    @EventHandler
    private final Listener<PlayerDestroyItemEvent> listener = new Listener<>(event ->
    {
        if (event.getHand() == EnumHand.MAIN_HAND && OffhandBypass.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
            event.setCanceled(true);
        }
    });


    @Override
    public void update() {
        if (OffhandBypass.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && OffhandBypass.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && OffhandBypass.mc.gameSettings.keyBindUseItem.isKeyDown() && OffhandBypass.mc.player.getActiveHand() == EnumHand.MAIN_HAND) {
            OffhandBypass.mc.player.setActiveHand(EnumHand.OFF_HAND);
        }
    }
}