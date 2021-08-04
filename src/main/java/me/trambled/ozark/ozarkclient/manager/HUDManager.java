package me.trambled.ozark.ozarkclient.manager;

import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;
import me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables.*;

import java.util.ArrayList;
import java.util.Comparator;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;


public class HUDManager {

	public static ArrayList array_hud = new ArrayList();

	public HUDManager() {

		add_pinnable(new Watermark());
		add_pinnable(new Arraylist());
		add_pinnable(new Coordinates());
		add_pinnable(new InventoryPreview());
		add_pinnable(new InventoryXCarryPreview());
		add_pinnable(new ArmorPreview());
		add_pinnable(new User());
		add_pinnable(new TotemCount());
		add_pinnable(new CrystalCount());
		add_pinnable(new EXPCount());
		add_pinnable(new GappleCount());
		add_pinnable(new Time());
		add_pinnable(new Logo());
		add_pinnable(new FPS());
		add_pinnable(new Ping());
		add_pinnable(new SurroundBlocks());
		add_pinnable(new FriendList());
		add_pinnable(new ArmorDurabilityWarner());
		add_pinnable(new PvpHud());
		add_pinnable(new Compass());
		add_pinnable(new EffectHud());
		add_pinnable(new Speedometer());
		add_pinnable(new EntityList());
		add_pinnable(new TPS());
		add_pinnable(new PlayerList());
		add_pinnable(new Direction());
		add_pinnable(new Info());
		add_pinnable(new EnemyInfo());
		add_pinnable(new Pitch());

		array_hud.sort(Comparator.comparing(Pinnable::get_title));
	}

	public void add_pinnable(Pinnable module) {
		array_hud.add(module);
	}

	public ArrayList<Pinnable> get_array_huds() {
		return array_hud;
	}

	public void render() {
		for (Pinnable pinnables : get_array_huds()) {
			if (pinnables.is_active() && mc.world != null) {
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