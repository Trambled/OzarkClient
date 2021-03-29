package me.trambled.ozark.ozarkclient.module;

import me.trambled.ozark.Ozark;

public class ClickHUD extends Module {

	public ClickHUD() {
		super(Category.GUI);

		this.name        = "HUD Editor";
		this.tag         = "HUD";
		this.description = "gui for pinnables";
	}
		
	@Override
	public void enable() {
		if (mc.world != null && mc.player != null) {
			Ozark.get_hack_manager().get_module_with_tag("GUI").set_active(false);
				
			Ozark.click_hud.back = false;

			mc.displayGuiScreen(Ozark.click_hud);
		}
	}
}


