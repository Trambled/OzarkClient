package me.trambled.ozark.ozarkclient.manager;

import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

import java.util.ArrayList;


public class SettingManager {

	public ArrayList<Setting> array_setting;

	public SettingManager() {
		this.array_setting = new ArrayList<>();
	}

	public void register(Setting setting) {
		this.array_setting.add(setting);
	}

	public ArrayList<Setting> get_array_settings() {
		return this.array_setting;
	}

	public Setting get_setting_with_tag(Module module, String setting_tag) {
		Setting setting_requested = null;

		for (Setting settings : get_array_settings()) {
			if (settings.get_master().equals(module) && settings.get_tag().equalsIgnoreCase(setting_tag)) {
				setting_requested = settings;
			}
		}

		return setting_requested;
	}

	public Setting get_setting_with_tag(String module_tag, String setting_tag) {
		Setting setting_requested = null;

		for (Setting settings : get_array_settings()) {
			if (settings.get_master().get_tag().equalsIgnoreCase(module_tag) && settings.get_tag().equalsIgnoreCase(setting_tag)) {
				setting_requested = settings;
				break;
			}
		}

		return setting_requested;
	}

	public ArrayList<Setting> get_settings_with_module(Module module) {
		ArrayList<Setting> setting_requesteds = new ArrayList<>();

		for (Setting settings : get_array_settings()) {
			if (settings.get_master().equals(module)) {
				setting_requesteds.add(settings);
			}
		}

		return setting_requesteds;
	}

	public void bind(int event_key) {
		if (event_key == 0) {
			return;
		}

		for (Setting settings : get_array_settings()) {
			if (!settings.get_type().equals("bind")) continue;
			if (!settings.get_master().is_active()) continue;
			if (settings.get_bind(0) == event_key) {
				settings.get_master().on_bind(settings.get_tag());
			}
		}
	}
}