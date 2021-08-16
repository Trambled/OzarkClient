package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;

import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ArmorDurabilityWarner extends Pinnable { // STILL BROKEN
    
    public ArmorDurabilityWarner() {
        super("Armor Warner", "ArmorWarner", 1, 0, 0);
    }

    @Override
	public void render() {

		String line = "ur armor is kinda low rn :/";

        if (is_damaged()) {
            create_line(line, this.docking(1, line), 2, 255, 20, 20, 255);
        }

		this.set_width(this.get(line, "width") + 2);
		this.set_height(this.get(line, "height") + 2);
    }

    private boolean is_damaged() {

        for (Map.Entry<Integer, ItemStack> armor_slot : get_armor().entrySet()) {
            if (armor_slot.getValue().isEmpty()) continue;
            final ItemStack stack = armor_slot.getValue();

            double max_dam = stack.getMaxDamage();
            double dam_left = stack.getMaxDamage() - stack.getItemDamage();
            double percent = (dam_left / max_dam) * 100;

            if (percent <  30) {
                return true;
            }
        }

        return false;
    }

    private Map<Integer, ItemStack> get_armor() {
        return get_inv_slots(5, 8);
    }

    private Map<Integer, ItemStack> get_inv_slots(int current, final int last) {
        final Map<Integer, ItemStack> full_inv_slots = new HashMap <> ( );
        while (current <= last) {
            full_inv_slots.put(current, mc.player.inventoryContainer.getInventory().get(current) );
            ++current;
        }
        return full_inv_slots;
    }

}