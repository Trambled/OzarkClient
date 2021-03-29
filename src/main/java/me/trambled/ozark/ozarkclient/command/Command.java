package me.trambled.ozark.ozarkclient.command;


import me.trambled.ozark.ozarkclient.manager.CommandManager;
import net.minecraft.client.Minecraft;


public class Command {
	String name;
	String description;

	public static final Minecraft mc;

	public Command(String name, String description) {
		this.name        = name;
		this.description = description;
	}

	public boolean get_message(String[] message) {
		return false;
	}

	public String get_name() {
		return this.name;
	}

	public String get_description() {
		return this.description;
	}

	public String current_prefix() {
		return CommandManager.get_prefix();
	}

	static {
		mc = Minecraft.getMinecraft();
	}
}