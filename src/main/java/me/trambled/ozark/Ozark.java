package me.trambled.ozark;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.turok.Turok;
import me.trambled.turok.task.TurokFont;
import me.trambled.ozark.ozarkclient.event.EventHandler;
import me.trambled.ozark.ozarkclient.event.EventRegister;
import me.trambled.ozark.ozarkclient.guiscreen.GUI;
import me.trambled.ozark.ozarkclient.guiscreen.HUD;
import me.trambled.ozark.ozarkclient.guiscreen.PastGUI;
import me.trambled.ozark.ozarkclient.guiscreen.gui.past.font.CustomFontRenderer;
import me.trambled.ozark.ozarkclient.manager.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.awt.*;

@Mod(modid = "ozarkclient", version = Ozark.VERSION)
public class Ozark {

	@Mod.Instance
	private static Ozark MASTER;

	public static final String NAME = "OzarkClient";
	public static final String VERSION = "1.11.6";
	public static final String SIGN = " ";

	public static final int KEY_GUI = Keyboard.KEY_RSHIFT;
	public static final int KEY_DELETE = Keyboard.KEY_DELETE;
	public static final int KEY_GUI_ESCAPE = Keyboard.KEY_ESCAPE;

	public static Logger register_log;

	private static SettingManager setting_manager;
	private static ConfigManager config_manager;
	private static ModuleManager module_manager;
	private static HUDManager hud_manager;
	public static PastGUI past_gui;

	public static GUI click_gui;

	public static CustomFontRenderer latoFont;
	public static CustomFontRenderer verdanaFont;
	public static CustomFontRenderer arialFont;

	public static HUD click_hud;
	public static Turok turok;

	public static ChatFormatting g = ChatFormatting.DARK_GRAY;
	public static ChatFormatting r = ChatFormatting.RESET;
	
    public static int client_r = 0;
	public static int client_g = 0;
	public static int client_b = 0;

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

		init_log(NAME);
		send_minecraft_log("Version " + VERSION);

		EventHandler.INSTANCE = new EventHandler();

		send_minecraft_log("Initialising managers");

		setting_manager = new SettingManager();
		config_manager = new ConfigManager();
		module_manager = new ModuleManager();
		hud_manager = new HUDManager();

		EventManager event_manager = new EventManager();
		CommandManager command_manager = new CommandManager(); // hack

		send_minecraft_log("Done");

		send_minecraft_log("Initialising guis");

		Display.setTitle("OzarkClient");
		click_gui = new GUI();
		click_hud = new HUD();
		past_gui = new PastGUI();

		send_minecraft_log("Done");

		send_minecraft_log("Initialising CUSTOM framework");

		turok = new Turok("Turok");

		send_minecraft_log("Done");

		send_minecraft_log("Initialising commands and events");

		// Register event modules and manager.
		EventRegister.register_command_manager(command_manager);
		EventRegister.register_module_manager(event_manager);

		send_minecraft_log("Done");

		send_minecraft_log("Loading settings");

		config_manager.load_settings();

		send_minecraft_log("Done");

		send_minecraft_log("Loading fonts");

		latoFont = new CustomFontRenderer(new Font("Lato", 0, 18), true, false);
		verdanaFont = new CustomFontRenderer(new Font("Verdana", 0, 18), true, false);
		arialFont = new CustomFontRenderer(new Font("Arial", 0, 18), true, false);
		send_minecraft_log("Custom fonts loaded (from past, however it was made by 086)");

		// module fixing
		if (module_manager.get_module_with_tag("GUI").is_active()) {
			send_minecraft_log("Fixing GUI");
			module_manager.get_module_with_tag("GUI").set_active(false);
			send_minecraft_log("Fixed");
		}

		if (module_manager.get_module_with_tag("HUD").is_active()) {
			send_minecraft_log("Fixing HUD");
			module_manager.get_module_with_tag("HUD").set_active(false);
			send_minecraft_log("Fixed");
		}

		if (!module_manager.get_module_with_tag("HUDEditor").is_active()) {
			send_minecraft_log("Fixing HUDEditor");
			module_manager.get_module_with_tag("HUDEditor").set_active(true);
			send_minecraft_log("Fixed");
		}

		if (module_manager.get_module_with_tag("PastGUI").is_active()) {
			send_minecraft_log("Fixing Past GUI");
			module_manager.get_module_with_tag("PastGUI").set_active(false);
			send_minecraft_log("Done");
		}

		if (module_manager.get_module_with_tag("DiscordRPC").is_active()) {
			send_minecraft_log("Loading discord rpc");
			RPC.init();
			send_minecraft_log("Done");
		}

		
		client_r = get_setting_manager().get_setting_with_tag("HUDEditor", "HUDStringsColorR").get_value(1);
		client_g = get_setting_manager().get_setting_with_tag("HUDEditor", "HUDStringsColorG").get_value(1);
		client_b = get_setting_manager().get_setting_with_tag("HUDEditor", "HUDStringsColorB").get_value(1);
		
		send_minecraft_log("Client started");
		send_minecraft_log("We gaming");
	}
	
	public void init_log(String name) {
		register_log = LogManager.getLogger(name);

		send_minecraft_log("Starting OzarkClient");
	}

	public static void send_minecraft_log(String log) {
		register_log.info(log);
	}

	public static String get_name() {
		return NAME;
	}

	public static String get_version() {
		return VERSION;
	}

	public static String get_actual_user() {
		return Minecraft.getMinecraft().getSession().getUsername();
	}

	public static ConfigManager get_config_manager() {
		return config_manager;
	}

	public static ModuleManager get_hack_manager() {
		return module_manager;
	}

	public static SettingManager get_setting_manager() {
		return setting_manager;
	}

	public static HUDManager get_hud_manager() {
		return hud_manager;
	}

	public static ModuleManager get_module_manager() { return module_manager; }

	public static EventHandler get_event_handler() {
		return EventHandler.INSTANCE;
	}

	public static String smoth(String base) {
		return TurokFont.smoth(base);
	}
}
