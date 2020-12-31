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
	WurstplusSetting shu_ = create("Shulker Color", "StorageESPShulker", "HUD", combobox("HUD", "Client"));
	WurstplusSetting enc_ = create("Enchest Color", "StorageESPEnchest", "Client", combobox("HUD", "Client"));
	WurstplusSetting che_ = create("Chest Color", "StorageESPChest", "Client", combobox("HUD", "Client"));
	WurstplusSetting oth_ = create("Others Color", "StorageESPOthers", "Client", combobox("HUD", "Client"));
	WurstplusSetting ot_a = create("Outline A", "StorageESPOutlineA", 150, 0, 255);
	WurstplusSetting a = create("Solid A", "StorageESPSolidA", 150, 0, 255);

	private int color_alpha;

	public StorageESP() {
		super(WurstplusCategory.WURSTPLUS_RENDER);

		// Info.
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
				final TileEntityShulkerBox shulker = (TileEntityShulkerBox) tiles;

				int hex = (255 << 24) | shulker.getColor().getColorValue() & 0xFFFFFFFF;

				if (shu_.in("HUD")) {
					draw(tiles, nl_r, nl_g, nl_b);
				} else {
					draw(tiles, (hex & 0xFF0000) >> 16, (hex & 0xFF00) >> 8, (hex & 0xFF));
				}
			}

			if (tiles instanceof TileEntityEnderChest) {
				if (enc_.in("HUD")) {
					draw(tiles, nl_r, nl_g, nl_b);
				} else {
					draw(tiles, 204, 0, 255);
				}
			}

			if (tiles instanceof TileEntityChest) {
				if (che_.in("HUD")) {
					draw(tiles, nl_r, nl_g, nl_b);
				} else {
					draw(tiles, 153, 102, 0);
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
					draw(tiles, 190, 190, 190);
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