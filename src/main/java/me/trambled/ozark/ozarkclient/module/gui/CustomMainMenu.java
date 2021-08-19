package me.trambled.ozark.ozarkclient.module.gui;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

public class CustomMainMenu extends Module {

	public CustomMainMenu() {
		super(Category.GUI);

		this.name        = "CustomMainMenu";
		this.tag         = "CustomMainMenu";
		this.description = "Better main menu.";
	}

	Setting phobos_mode = create("Phobos Mode", "CMMPhobosMode", false);

}
