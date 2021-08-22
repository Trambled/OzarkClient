package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.turok.draw.RenderHelp;
import net.minecraft.tileentity.*;

/**
* @author Rina
*
* Created by Rina.
* 20/05/2020.
*
*/
public class StorageESP extends Module {
	
	Setting shulker_r = create("Shulker R", "ShulkerR", 56, 0, 255);
    Setting shulker_g = create("Shulker G", "ShulkerG", 212, 0, 255);
	Setting shulker_b = create("Shulker B", "ShulkerB", 151, 0, 255);
	Setting other_r = create("Other R", "OtherR", 190, 0, 255);
	Setting other_g = create("Other G", "OtherG", 190, 0, 255);
	Setting other_b = create("Other B", "OtherB", 190, 0, 255);
	Setting echest_r = create("Echest R", "EchestR", 204, 0, 255);
	Setting echest_g = create("Echest G", "EchestG", 0, 0, 255);
	Setting echest_b = create("Echest B", "EchestB", 255, 0, 255);
	Setting chest_r = create("Chest R", "ChestR", 153, 0, 255);
    Setting chest_g = create("Chest G", "ChestG", 102, 0, 255);
	Setting chest_b = create("Chest B", "ChestB", 0, 0, 255);
	Setting outline_a = create("Outline A", "StorageESPOutlineA", 0, 0, 255);
	Setting solid_a = create("Solid A", "StorageESPSolidA", 150, 0, 255);

	public StorageESP() {
		super(Category.RENDER);

		this.name        = "StorageESP";
		this.tag         = "StorageESP";
		this.description = "Is able to see storages in world.";
	}

	@Override
	public void render(EventRender event) {

		for (TileEntity tiles : mc.world.loadedTileEntityList) {
			if (tiles instanceof TileEntityShulkerBox) {
				draw(tiles, shulker_r.get_value(1), shulker_g.get_value(1), shulker_b.get_value(1));
			}

			if (tiles instanceof TileEntityEnderChest) {
				draw(tiles, echest_r.get_value(1), echest_g.get_value(1), echest_b.get_value(1));
			}

			if (tiles instanceof TileEntityChest) {
				draw(tiles, chest_r.get_value(1), chest_g.get_value(1), chest_b.get_value(1));
			}

			if ( tiles instanceof TileEntityDispenser || tiles instanceof TileEntityHopper || tiles instanceof TileEntityFurnace || tiles instanceof TileEntityBrewingStand ) {
				draw(tiles, other_r.get_value(1), other_g.get_value(1), other_b.get_value(1));
			}
		}
	}

	public void draw(TileEntity tile_entity, int r, int g, int b) {
		// Solid.
		RenderHelp.prepare("quads");
		RenderHelp.draw_cube(tile_entity.getPos(), r, g, b, solid_a.get_value(1), "all");
		RenderHelp.release();

		// Outline.
		RenderHelp.prepare("lines");
		RenderHelp.draw_cube_line(tile_entity.getPos(), r, g, b, outline_a.get_value(1), "all");
	    RenderHelp.release();
	}
}