package me.travis.wurstplus.wurstplustwo.hacks.render;

import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import java.awt.Color;
import java.util.*;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import me.travis.wurstplus.Wurstplus;
import me.travis.turok.draw.RenderHelp;

/**
* @author Rina
*
* Created by Rina.
* 20/05/2020.
*
*/
public class StorageESP extends WurstplusHack {
	
	WurstplusSetting shulker_r = create("Shulker R", "ShulkerR", 56, 0, 255);
    WurstplusSetting shulker_g = create("Shulker G", "ShulkerG", 212, 0, 255);
	WurstplusSetting shulker_b = create("Shulker B", "ShulkerB", 151, 0, 255);
	WurstplusSetting other_r = create("Other R", "OtherR", 190, 0, 255);
	WurstplusSetting other_g = create("Other G", "OtherG", 190, 0, 255);
	WurstplusSetting other_b = create("Other B", "OtherB", 190, 0, 255);
	WurstplusSetting echest_r = create("Echest R", "EchestR", 204, 0, 255);
	WurstplusSetting echest_g = create("Echest G", "EchestG", 0, 0, 255);
	WurstplusSetting echest_b = create("Echest B", "EchestB", 255, 0, 255);
	WurstplusSetting chest_r = create("Chest R", "ChestR", 153, 0, 255);
    WurstplusSetting chest_g = create("Chest G", "ChestG", 102, 0, 255);
	WurstplusSetting chest_b = create("Chest B", "ChestB", 0, 0, 255);

	WurstplusSetting shu_ = create("Shulker Color", "StorageESPShulker", "Client", combobox("HUD", "Client"));
	WurstplusSetting enc_ = create("Enchest Color", "StorageESPEnchest", "Client", combobox("HUD", "Client"));
	WurstplusSetting che_ = create("Chest Color", "StorageESPChest", "Client", combobox("HUD", "Client"));
	WurstplusSetting oth_ = create("Others Color", "StorageESPOthers", "Client", combobox("HUD", "Client"));
	WurstplusSetting ot_a = create("Outline A", "StorageESPOutlineA", 150, 0, 255);
	WurstplusSetting a = create("Solid A", "StorageESPSolidA", 150, 0, 255);

	private int color_alpha;

	public StorageESP() {
		super(WurstplusCategory.WURSTPLUS_RENDER);

		this.name        = "Storage ESP";
		this.tag         = "StorageESP";
		this.description = "Is able to see storages in world";
	}

	@Override
	public void render(WurstplusEventRender event) {
		int nl_r = Wurstplus.client_r;
		int nl_g = Wurstplus.client_g;
		int nl_b = Wurstplus.client_b;

		color_alpha = a.get_value(1);

		for (TileEntity tiles : mc.world.loadedTileEntityList) {
			if (tiles instanceof TileEntityShulkerBox) {
				if (shu_.in("HUD")) {
					draw(tiles, nl_r, nl_g, nl_b);
				} else {
					draw(tiles, shulker_r.get_value(1), shulker_g.get_value(1), shulker_b.get_value(1));
				}
			}

			if (tiles instanceof TileEntityEnderChest) {
				if (enc_.in("HUD")) {
					draw(tiles, nl_r, nl_g, nl_b);
				} else {
					draw(tiles, echest_r.get_value(1), echest_g.get_value(1), echest_b.get_value(1));
				}
			}

			if (tiles instanceof TileEntityChest) {
				if (che_.in("HUD")) {
					draw(tiles, nl_r, nl_g, nl_b);
				} else {
					draw(tiles, chest_r.get_value(1), chest_g.get_value(1), chest_b.get_value(1));
				}
			}

			if (tiles instanceof TileEntityDispenser ||
				tiles instanceof TileEntityDropper   ||
				tiles instanceof TileEntityHopper    ||
				tiles instanceof TileEntityFurnace   ||
				tiles instanceof TileEntityBrewingStand) {
				if (oth_.in("HUD")) {
					draw(tiles, nl_r, nl_g, nl_b);
				} else {
					draw(tiles, other_r.get_value(1), other_g.get_value(1), other_b.get_value(1));
				}
			}
		}
	}

	public void draw(TileEntity tile_entity, int r, int g, int b) {
		// Solid.
		RenderHelp.prepare("quads");
		RenderHelp.draw_cube(tile_entity.getPos(), r, g, b, a.get_value(1), "all");
		RenderHelp.release();

		// Outline.
		RenderHelp.prepare("lines");
		RenderHelp.draw_cube_line(tile_entity.getPos(), r, g, b, ot_a.get_value(1), "all");
	        RenderHelp.release();
	}
}