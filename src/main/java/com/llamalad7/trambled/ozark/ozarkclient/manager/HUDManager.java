package me.trambled.ozark.ozarkclient.manager;

import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;
import me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables.*;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Comparator;


public class HUDManager {

	public static java.util.ArrayList array_hud = new java.util.ArrayList();

	public HUDManager() {

		add_component_pinnable(new Watermark());
		add_component_pinnable(new Arraylist());
		add_component_pinnable(new Coordinates());
		add_component_pinnable(new InventoryPreview());
		add_component_pinnable(new InventoryXCarryPreview());
		add_component_pinnable(new ArmorPreview());
		add_component_pinnable(new User());
		add_component_pinnable(new TotemCount());
		add_component_pinnable(new CrystalCount());
		add_component_pinnable(new EXPCount());
		add_component_pinnable(new GappleCount());
		add_component_pinnable(new Time());
		add_component_pinnable(new Logo());
		add_component_pinnable(new FPS());
		add_component_pinnable(new Ping());
		add_component_pinnable(new SurroundBlocks());
		add_component_pinnable(new FriendList());
		add_component_pinnable(new ArmorDurabilityWarner());
		add_component_pinnable(new PvpHud());
		add_component_pinnable(new Compass());
		add_component_pinnable(new EffectHud());
		add_component_pinnable(new Speedometer());
		add_component_pinnable(new EntityList());
		add_component_pinnable(new TPS());
		add_component_pinnable(new PlayerList());
		add_component_pinnable(new Direction());

		array_hud.sort(Comparator.comparing(Pinnable::get_title));
	}

	public void add_component_pinnable(Pinnable module) {
		array_hud.add(module);
	}

	public ArrayList<Pinnable> get_array_huds() {
		return array_hud;
	}

	public void render() {
		for (Pinnable pinnables : get_array_huds()) {
			if (pinnables.is_active() && Minecraft.getMinecraft().world != null) {
				pinnables.render();
			}
		}
	}

	public Pinnable get_pinnable_with_tag(String tag) {
		Pinnable pinnable_requested = null;

		for (Pinnable pinnables : get_array_huds()) {
			if (pinnables.get_tag().equalsIgnoreCase(tag)) {
				pinnable_requested = pinnables;
			}
		}

		return pinnable_requested;
	}

}