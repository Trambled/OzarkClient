package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;


public class WurstplusAutoGapple extends WurstplusHack {

    public WurstplusAutoGapple() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name        = "Auto Gapple";
        this.tag         = "AutoGapple";
        this.description = "switch to gapple";
    }

    WurstplusSetting health  = create("Health", "Health", 12, 0, 36);

    @Override
    public void update() {
		
		float hp = mc.player.getHealth()+mc.player.getAbsorptionAmount();

        if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory) {
			
			if (hp > health.get_value(1)) {
				switch_to_gapple();
			}
			
        }

    }
	
    private int switch_to_gapple() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.GOLDEN_APPLE) {
                return i;
            }
        }
        return -1;
    }

}
