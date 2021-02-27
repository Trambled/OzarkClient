package me.travis.wurstplus.wurstplustwo.command;


import me.travis.wurstplus.wurstplustwo.manager.WurstplusCommandManager;
import net.minecraft.client.Minecraft;


public class WurstplusCommand {
	String name;
	String description;

	public static final Minecraft mc;

	public WurstplusCommand(String name, String description) {
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
		return WurstplusCommandManager.get_prefix();
	}

	static {
		mc = Minecraft.getMinecraft();
	}
}