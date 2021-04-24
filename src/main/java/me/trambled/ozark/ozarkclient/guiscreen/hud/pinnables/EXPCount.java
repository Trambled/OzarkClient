package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;


public class EXPCount extends Pinnable {
	ChatFormatting dg = ChatFormatting.DARK_GRAY;
	ChatFormatting db = ChatFormatting.DARK_BLUE;

	int exp = 0;

	public EXPCount() {
		super("EXP Count", "EXPCount", 1, 0, 0);
	}

	@Override
	public void render() {
		int nl_r = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
		int nl_g = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
		int nl_b = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);
		int nl_a = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(1);

		if (mc.player != null) {
			if (is_on_gui()) {
				create_rect(0, 0, this.get_width(), this.get_height(), 0, 0, 0, 50);
			}

			GlStateManager.pushMatrix();
			RenderHelper.enableGUIStandardItemLighting();

			exp = mc.player.inventory.mainInventory.stream().filter(stack -> stack.getItem() == Items.EXPERIENCE_BOTTLE).mapToInt(ItemStack::getCount).sum();

			int off = 0;

			for (int i = 0; i < 45; i++) {
				ItemStack stack = mc.player.inventory.getStackInSlot(i);
				ItemStack off_h = mc.player.getHeldItemOffhand();

				if (off_h.getItem() == Items.EXPERIENCE_BOTTLE) {
					off = off_h.getMaxStackSize();
				} else {
					off = 0;
				}

				if (stack.getItem() == Items.EXPERIENCE_BOTTLE) {
					mc.getRenderItem().renderItemAndEffectIntoGUI(stack, this.get_x(), this.get_y());

					create_line(Integer.toString(exp + off), 16 + 2, 16 - get(Integer.toString(exp + off), "height"), nl_r, nl_g, nl_b, nl_a);
				}
			}

			mc.getRenderItem().zLevel = 0.0f;

			RenderHelper.disableStandardItemLighting();		
			
			GlStateManager.popMatrix();

			this.set_width(16 + get(Integer.toString(exp) + off, "width") + 2);
			this.set_height(16);
		}
	}
}