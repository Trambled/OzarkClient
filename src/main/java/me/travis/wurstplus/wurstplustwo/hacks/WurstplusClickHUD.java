package me.travis.wurstplus.wurstplustwo.hacks;

import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusClickHUD;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import java.awt.*;

public class WurstplusClickHUD extends WurstplusHack {

	public WurstplusClickHUD() {
		super(WurstplusCategory.WURSTPLUS_GUI);

		this.name        = "HUD";
		this.tag         = "HUD";
		this.description = "gui for pinnables";
	}
		
	@Override
	public void enable() {
		if (mc.world != null && mc.player != null) {
			Wurstplus.get_hack_manager().get_module_with_tag("GUI").set_active(false);
				
			Wurstplus.click_hud.back = false;

			mc.displayGuiScreen(Wurstplus.click_hud);
		}
	}
}


