package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;


public class AutoGapple extends Module {

    public AutoGapple() {
        super(Category.COMBAT);

        this.name        = "Auto Gapple";
        this.tag         = "AutoGapple";
        this.description = "switch to gapple";
    }

    Setting health  = create("Health", "Health", 12, 0, 36);

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
