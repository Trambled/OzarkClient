package me.trambled.ozark;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.ozarkclient.event.EventHandler;
import me.trambled.ozark.ozarkclient.event.EventRegister;
import me.trambled.ozark.ozarkclient.guiscreen.MainGUI;
import me.trambled.ozark.ozarkclient.guiscreen.MainHUD;
import me.trambled.ozark.ozarkclient.guiscreen.PastGUI;
import me.trambled.ozark.ozarkclient.manager.*;
import me.trambled.ozark.ozarkclient.util.font.CustomFontRenderer;
import me.trambled.ozark.ozarkclient.util.misc.DiscordUtil;
import me.trambled.ozark.ozarkclient.util.misc.InjectUtil;
import me.trambled.turok.Turok;
import me.trambled.turok.task.TurokFont;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.awt.*;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;

@Mod(modid = "ozarkclient", version = Ozark.VERSION)
public class Ozark {

	@Mod.Instance
	private static Ozark MASTER;

	public static final String NAME = "OzarkClient";
	public static final String VERSION = "1.14";
	public static final String SIGN = " ";
	public static String DISPLAY_NAME = "Ozark";
	public static String TARGET_NAME = "NULL";

	public static final int KEY_GUI = Keyboard.KEY_RSHIFT;
	public static final int KEY_DELETE = Keyboard.KEY_DELETE;
	public static final int KEY_GUI_ESCAPE = Keyboard.KEY_ESCAPE;

	public static Logger register_log;

	private static SettingManager setting_manager;
	private static ConfigManager config_manager;
	private static ModuleManager module_manager;
	private static HUDManager hud_manager;
	private static RotationManager rotation_manager;
	private static NotificationManager notification_manager;
	private static TotempopManager totempop_manager;
	public static PastGUI past_gui;
	public static MainGUI main_gui;
	public static MainHUD main_hud;

	public static CustomFontRenderer latoFont;
	public static CustomFontRenderer verdanaFont;
	public static CustomFontRenderer arialFont;

	public static Turok turok;

	public static ChatFormatting g = ChatFormatting.DARK_GRAY;
	public static ChatFormatting r = ChatFormatting.RESET;
	public static ChatFormatting w = ChatFormatting.WHITE;

        public static int client_r = 0;
	public static int client_g = 0;
	public static int client_b = 0;
	public static Ozark instance = new Ozark();

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
		rotation_manager = new RotationManager();
		notification_manager = new NotificationManager();
		totempop_manager = new TotempopManager();


		EventManager event_manager = new EventManager();
		CommandManager command_manager = new CommandManager();

		send_minecraft_log("Done");

		send_minecraft_log("Initialising BetterChat (made by llamalad7)");

		MinecraftForge.EVENT_BUS.register(new InjectUtil());

		send_minecraft_log("Done");

		send_minecraft_log("Initialising guis");

		Display.setTitle("OzarkClient");
		main_gui = new MainGUI();
		main_hud = new MainHUD();
		past_gui = new PastGUI();

		send_minecraft_log("Done");

		send_minecraft_log("Initialising CUSTOM framework");

		turok = new Turok("Turok");

		send_minecraft_log("Done");

		send_minecraft_log("Initialising commands and events");

		EventRegister.register_command_manager(command_manager);
		EventRegister.register_module_manager(event_manager);
		EventRegister.register_rotation_manager(rotation_manager);

		send_minecraft_log("Done");

		send_minecraft_log("Loading settings");

		config_manager.load_settings();

		send_minecraft_log("Done");

		send_minecraft_log("Loading fonts");

		latoFont = new CustomFontRenderer(new Font("Lato", 0, 18), true, false);
		verdanaFont = new CustomFontRenderer(new Font("Verdana", 0, 18), true, false);
		arialFont = new CustomFontRenderer(new Font("Arial", 0, 18), true, false);
		send_minecraft_log("Custom fonts loaded (from past, and used for past gui, however it was made by 086)");

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

		if (module_manager.get_module_with_tag("PastGUI").is_active()) {
			send_minecraft_log("Fixing Past GUI");
			module_manager.get_module_with_tag("PastGUI").set_active(false);
			send_minecraft_log("Done");
		}

		if (module_manager.get_module_with_tag("AutoCrystal").is_active()) {
			send_minecraft_log("Fixing AutoCrystal");
			module_manager.get_module_with_tag("AutoCrystal").set_active(false);
			send_minecraft_log("Done");
		}

		if (module_manager.get_module_with_tag("FakePlayer").is_active()) {
			send_minecraft_log("Fixing FakePlayer");
			module_manager.get_module_with_tag("FakePlayer").set_active(false);
			send_minecraft_log("Done");
		}

		if (module_manager.get_module_with_tag("DiscordRPC").is_active()) {
			send_minecraft_log("Loading discord rpc");
			DiscordUtil.init();
			send_minecraft_log("Done");
		}


		client_r = get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
		client_g = get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
		client_b = get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);

		send_minecraft_log("Client started");
		send_minecraft_log("we bout to do a little trollin");
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
		return mc.getSession().getUsername();
	}

	public static ConfigManager get_config_manager() {
		return config_manager;
	}

	public static ModuleManager get_module_manager() {
		return module_manager;
	}

	public static SettingManager get_setting_manager() {
		return setting_manager;
	}

	public static HUDManager get_hud_manager() {
		return hud_manager;
	}

	public static RotationManager get_rotation_manager() {
		return rotation_manager;
	}

	public static NotificationManager get_notification_manager() {
		return notification_manager;
	}

	public static TotempopManager get_totempop_manager() {return totempop_manager;}

	public static EventHandler get_event_handler() {
		return EventHandler.INSTANCE;
	}

	public static String smoth(String base) {
		return TurokFont.smoth(base);
	}

}

