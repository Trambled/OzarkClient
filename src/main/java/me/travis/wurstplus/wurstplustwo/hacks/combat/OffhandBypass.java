package me.travis.wurstplus.wurstplustwo.hacks.combat;

import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import me.zero.alpine.fork.listener.EventHandler;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import me.zero.alpine.fork.listener.Listener;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

//vethack
public class OffhandBypass extends WurstplusHack
{	
    public OffhandBypass() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name = "Offhand Bypass";
        this.tag = "OffhandBypass";
        this.description = "makes your hand that u use set to offhand for gapple offhand"; 
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