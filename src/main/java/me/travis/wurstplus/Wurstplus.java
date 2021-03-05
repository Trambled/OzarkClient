package me.travis.wurstplus;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.turok.Turok;
import me.travis.turok.task.TurokFont;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventHandler;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventRegister;
import me.travis.wurstplus.wurstplustwo.guiscreen.WurstplusGUI;
import me.travis.wurstplus.wurstplustwo.guiscreen.WurstplusHUD;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.PastGUI;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.font.CustomFontRenderer;
import me.travis.wurstplus.wurstplustwo.manager.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.awt.*;

@Mod(modid = "ozarkclient", version = Wurstplus.WURSTPLUS_VERSION)
public class Wurstplus {

	@Mod.Instance
	private static Wurstplus MASTER;

	public static final String WURSTPLUS_NAME = "OzarkClient";
	public static final String WURSTPLUS_VERSION = "1.11";
	public static final String WURSTPLUS_SIGN = " ";

	public static final int WURSTPLUS_KEY_GUI = Keyboard.KEY_RSHIFT;
	public static final int WURSTPLUS_KEY_DELETE = Keyboard.KEY_DELETE;
	public static final int WURSTPLUS_KEY_GUI_ESCAPE = Keyboard.KEY_ESCAPE;

	public static Logger wurstplus_register_log;

	private static WurstplusSettingManager setting_manager;
	private static WurstplusConfigManager config_manager;
	private static WurstplusModuleManager module_manager;
	private static WurstplusHUDManager hud_manager;
	public static PastGUI past_gui;

	public static WurstplusGUI click_gui;

	public static CustomFontRenderer latoFont;
	public static CustomFontRenderer verdanaFont;
	public static CustomFontRenderer arialFont;

	public static WurstplusHUD click_hud;
	public static Turok turok;

	public static ChatFormatting g = ChatFormatting.DARK_GRAY;
	public static ChatFormatting r = ChatFormatting.RESET;
	
    public static int client_r = 0;
	public static int client_g = 0;
	public static int client_b = 0;

	@Mod.EventHandler
	public void WurstplusStarting(FMLInitializationEvent event) {

		init_log(WURSTPLUS_NAME);

		WurstplusEventHandler.INSTANCE = new WurstplusEventHandler();

		send_minecraft_log("Initialising managers");

		setting_manager = new WurstplusSettingManager();
		config_manager = new WurstplusConfigManager();
		module_manager = new WurstplusModuleManager();
		hud_manager = new WurstplusHUDManager();

		WurstplusEventManager event_manager = new WurstplusEventManager();
		WurstplusCommandManager command_manager = new WurstplusCommandManager(); // hack

		send_minecraft_log("Done");

		send_minecraft_log("Initialising guis");

		Display.setTitle("OzarkClient");
		click_gui = new WurstplusGUI();
		click_hud = new WurstplusHUD();
		past_gui = new PastGUI();

		send_minecraft_log("Done");

		send_minecraft_log("Initialising skidded framework");

		turok = new Turok("Turok");

		send_minecraft_log("Done");

		send_minecraft_log("Initialising commands and events");

		// Register event modules and manager.
		WurstplusEventRegister.register_command_manager(command_manager);
		WurstplusEventRegister.register_module_manager(event_manager);

		send_minecraft_log("Done");

		send_minecraft_log("Loading settings");

		config_manager.load_settings();

		send_minecraft_log("Done");

		send_minecraft_log("Loading fonts");

		latoFont = new CustomFontRenderer(new Font("Lato", 0, 18), true, false);
		verdanaFont = new CustomFontRenderer(new Font("Verdana", 0, 18), true, false);
		arialFont = new CustomFontRenderer(new Font("Arial", 0, 18), true, false);
		send_minecraft_log("Custom fonts loaded (from past, however it was made by 086)");
		

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
			send_minecraft_log("Fixed");
		}

		if (!module_manager.get_module_with_tag("HUDEditor").is_active()) {
			send_minecraft_log("Fixing HUDEditor");
			module_manager.get_module_with_tag("HUDEditor").set_active(true);
			send_minecraft_log("Fixed");
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
		send_minecraft_log("Made by trambled!");
	}
	
	public void init_log(String name) {
		wurstplus_register_log = LogManager.getLogger(name);

		send_minecraft_log("starting ozarkclient");
	}

	public static void send_minecraft_log(String log) {
		wurstplus_register_log.info(log);
	}

	public static String get_name() {
		return  WURSTPLUS_NAME;
	}

	public static String get_version() {
		return WURSTPLUS_VERSION;
	}

	public static String get_actual_user() {
		return Minecraft.getMinecraft().getSession().getUsername();
	}

	public static WurstplusConfigManager get_config_manager() {
		return config_manager;
	}

	public static WurstplusModuleManager get_hack_manager() {
		return module_manager;
	}

	public static WurstplusSettingManager get_setting_manager() {
		return setting_manager;
	}

	public static WurstplusHUDManager get_hud_manager() {
		return hud_manager;
	}

	public static WurstplusModuleManager get_module_manager() { return module_manager; }

	public static WurstplusEventHandler get_event_handler() {
		return WurstplusEventHandler.INSTANCE;
	}

	public static String smoth(String base) {
		return TurokFont.smoth(base);
	}
}
