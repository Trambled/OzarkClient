package me.trambled.ozark.ozarkclient.module;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class Setting {
	private Module master;

	private String name;
	private String tag;

	private boolean button;
	private boolean shown = true;

	private List<String> combobox;
	private String current;
	private String message;
	private String preset_message;

	private String label;

	private double slider;
	private double min;
	private double max;
	private int bind;

	private String type;

	public Setting(Module master, String name, String tag, int value) {
		this.master = master;
		this.name   = name;
		this.tag    = tag;
		this.bind = value;
		this.type  = "bind";
	}

	public Setting(Module master, String name, String tag, boolean value) {
		this.master = master;
		this.name   = name;
		this.tag    = tag;
		this.button = value;
		this.type   = "button";
	}

	public Setting(Module master, String name, String tag, List<String> values, String value) {
		this.master   = master;
		this.name     = name;
		this.tag      = tag;
		this.combobox = values;
		this.current  = value;
		this.type     = "combobox";
	}

	public Setting(Module master, String name, String tag, String value) {
		this.master = master;
		this.name   = name;
		this.tag    = tag;
		this.label  = value;
		this.type   = "label";
	}

	public Setting(Module master, String name, String tag, double value, double min, double max) {
		this.master = master;
		this.name   = name;
		this.tag    = tag;
		this.slider = value;
		this.min    = min;
		this.max    = max;
		this.type   = "doubleslider";
	}

	public Setting(Module master, String name, String tag, String value, String id) {
		this.master = master;
		this.name   = name;
		this.tag    = tag;
		this.message = value;
		this.preset_message = value;
		this.type  = "string";
	}

	public Setting(Module master, String name, String tag, int value, int min, int max) {
		this.master = master;
		this.name   = name;
		this.tag    = tag;
		this.slider = value;
		this.min    = min;
		this.max    = max;
		this.type   = "integerslider";
	}

	public Module get_master() {
		return this.master;
	}

	public String get_name() {
		return this.name;
	}

	public String get_tag() {
		return this.tag;
	}

	public void set_value(boolean value) {
		if (Minecraft.getMinecraft().world != null && master.is_active()) {
			master.value_change();
		}
		this.button = value;
	}

	public void set_current_value(String value) {
		if (Minecraft.getMinecraft().world != null && master.is_active()) {
			master.value_change();
		}
		this.current = value;
	}

	public void set_value(String value) {
		this.label = value;
	}

	public void set_value(double value) {
		if (Minecraft.getMinecraft().world != null && master.is_active()) {
			master.value_change();
		}
		if (value >= get_max(value)) {
			this.slider = get_max(value);
		} else if (value <= get_min(value)) {
			this.slider = get_min(value);
		} else {
			this.slider = value;
		}
	}

	public void set_shown(boolean value) {
		this.shown = value;
	}

	public void set_value(int value) {
		if (Minecraft.getMinecraft().world != null && master.is_active()) {
			master.value_change();
		}
		if (value >= get_max(value)) {
			this.slider = get_max(value);
		} else if (value <= get_min(value)) {
			this.slider = get_min(value);
		} else {
			this.slider = value;
		}
	}

	public void set_bind(int value) {
		this.bind = value;
	}

	public void set_message(String value) {
		this.message = value;
	}

	public boolean is_info() {
		return this.name.equalsIgnoreCase("info");
	}

	public boolean in(String value) {
		return this.current.equalsIgnoreCase(value);
	}

	public boolean get_value(boolean type) {
		return this.button;
	}

	public List<String> get_values() {
		return this.combobox;
	}

	public String get_current_value() {
		return this.current;
	}

	public String get_value(String type) {
		return this.label;
	}

	public double get_value(double type) {
		return this.slider;
	}

	public int get_value(int type) {
		return ((int) Math.round(this.slider));
	}

	public double get_min(double type) {
		return this.min;
	}

	public double get_max(double type) {
		return this.max;
	}

	public int get_min(int type) {
		return ((int) this.min);
	}

	public int get_max(int type) {
		return ((int) this.max);
	}

	public String get_message(String type) {
		return message;
	}

	public String get_preset_message(String type) {
		return preset_message;
	}

	public String get_type() {
		return this.type;
	}

	public String get_bind(String type) {
		String converted_bind = "null";

		if (get_bind(0) < 0) {
			converted_bind = "NONE";
		}

		if (!(converted_bind.equals("NONE"))) {
			String key     = Keyboard.getKeyName(get_bind(0));
			converted_bind = Character.toUpperCase(key.charAt(0)) + (key.length() != 1 ? key.substring(1).toLowerCase() : "");
		} else {
			converted_bind = "None";
		}

		return converted_bind;
	}

	public int get_bind(int type) {
		return this.bind;
	}


	// experimental
	public boolean is_shown() {
		return this.shown;
	}
}