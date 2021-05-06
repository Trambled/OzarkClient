package me.trambled.ozark.ozarkclient.module.gui;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;

public class CustomMainMenu extends Module {

	public CustomMainMenu() {
		super(Category.GUI);

		this.name        = "Custom Main Menu";
		this.tag         = "CustomMainMenu";
		this.description = "another main menu";
	}

}
