package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;


import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;


public class InventoryXCarryPreview extends Pinnable {
	public InventoryXCarryPreview() {
		super("Inventory XCarry", "InventoryXPreview", 1, 0, 0);
	}

	@Override
	public void render() {
		if (mc.player != null) {
			GlStateManager.pushMatrix();
			RenderHelper.enableGUIStandardItemLighting();

			create_rect(0, 0, this.get_width(), this.get_height(), 0, 0, 0, 60);

			this.set_width(16 * 2);
			this.set_height(16 * 2);

			for (int i = 1; i < 5; i++) {
				ItemStack item_stack = mc.player.inventoryContainer.inventorySlots.get(i).getStack();

				// CHING CONG I CBA TO DO MATHS ITS 3 AM
				int item_position_x = this.get_x();
				int item_position_y = this.get_y();
				if (i == 2) {
					item_position_x += 16;
				}
				if (i == 3) {
					item_position_y += 16;
				}
				if (i == 4) {
					item_position_x += 16;
					item_position_y += 16;
				}

				mc.getRenderItem().renderItemAndEffectIntoGUI(item_stack, item_position_x, item_position_y);
				mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, item_stack, item_position_x, item_position_y, null);
			}

			mc.getRenderItem().zLevel = - 5.0f;

			RenderHelper.disableStandardItemLighting();			
			
			GlStateManager.popMatrix();
		}
	}
}