package me.trambled.ozark.ozarkclient.module;

public enum Category {
	CHAT("Chat", "CategoryChat", false),
	COMBAT("Combat", "CategoryCombat", false),
	MOVEMENT("Movement", "CategoryMovement", false),
	RENDER("Render", "CategoryRender", false),
	EXPLOIT("Exploit", "CategoryExploit", false),
	MISC("Misc", "CategoryMisc", false),
	GUI("GUI", "CategoryGUI", false),
	HIDDEN("Hidden", "CategoryHidden", true);

	String name;
	String tag;
	boolean hidden;

	Category(String name, String tag, boolean hidden) {
		this.name   = name;
		this.tag    = tag;
		this.hidden = hidden;
	}

	public boolean is_hidden() {
		return this.hidden;
	}

	public String get_name() {
		return this.name;
	}

	public String get_tag() {
		return this.tag;
	}
}