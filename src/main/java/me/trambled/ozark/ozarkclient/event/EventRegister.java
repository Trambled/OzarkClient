package me.trambled.ozark.ozarkclient.event;

import me.trambled.ozark.ozarkclient.manager.CommandManager;
import me.trambled.ozark.ozarkclient.manager.EventManager;
import me.trambled.ozark.ozarkclient.manager.RotationManager;
import net.minecraftforge.common.MinecraftForge;


public class EventRegister {
	public static void register_command_manager(CommandManager manager) {
		MinecraftForge.EVENT_BUS.register(manager);
	}

	public static void register_module_manager(EventManager manager) {
		MinecraftForge.EVENT_BUS.register(manager);
	}

	public static void register_rotation_manager(RotationManager manager) {
		MinecraftForge.EVENT_BUS.register(manager);
		Eventbus.EVENT_BUS.subscribe(manager);
	}
}