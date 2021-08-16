package me.trambled.ozark.ozarkclient.guiscreen.gui.main.widgets;


import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.gui.main.AbstractWidget;
import me.trambled.ozark.ozarkclient.guiscreen.gui.main.Frame;
import me.trambled.ozark.ozarkclient.guiscreen.gui.main.ModuleButton;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.render.GuiUtil;


public class Button extends AbstractWidget {
	private final Frame frame;
	private final ModuleButton master;
	private final Setting setting;

	private final String button_name;

	private int x;
	private int y;

	private int width;
	private int height;

	private int save_y;

	private boolean can;

	private final GuiUtil font = new GuiUtil(1);

	private final int border_size = 0;

	public Button(Frame frame, ModuleButton master, String tag, int update_postion) {
		this.frame   = frame;
		this.master  = master;
		this.setting = Ozark.get_setting_manager().get_setting_with_tag(master.get_module(), tag);

		this.x = master.get_x();
		this.y = update_postion;

		this.save_y = this.y;

		this.width  = master.get_width();
		this.height = font.get_string_height();

		this.button_name = this.setting.get_name();

		this.can = true;
	}

	public Setting get_setting() {
		return this.setting;
	}

	@Override
	public void does_can(boolean value) {
		this.can = value;
	}

	@Override
	public void set_x(int x) {
		this.x = x;
	}

	@Override
	public void set_y(int y) {
		this.y = y;
	}

	@Override
	public void set_width(int width) {
		this.width = width;
	}

	@Override
	public void set_height(int height) {
		this.height = height;
	}

	@Override
	public int get_x() {
		return this.x;
	}

	@Override
	public int get_y() {
		return this.y;
	}

	@Override
	public int get_width() {
		return this.width;
	}

	@Override
	public int get_height() {
		return this.height;
	}

	public int get_save_y() {
		return this.save_y;
	}

	@Override
	public boolean motion_pass(int mx, int my) {
		return motion(mx, my);
	}

	public boolean motion(int mx, int my) {
		return mx >= get_x ( ) && my >= get_save_y ( ) && mx <= get_x ( ) + get_width ( ) && my <= get_save_y ( ) + get_height ( );
	}

	public boolean can() {
		return this.can;
	}

	@Override
	public void mouse(int mx, int my, int mouse) {
		if (mouse == 0) {
			if (motion(mx, my) && this.master.is_open() && can()) {
				this.frame.does_can(false);

				this.setting.set_value(!(this.setting.get_value(true)));
			}
		}
	}

	@Override
	public void render(int master_y, int separe, int absolute_x, int absolute_y) {
		set_width(this.master.get_width() - separe);

		this.save_y = this.y + master_y;

		int ns_r = Ozark.main_gui.theme_widget_name_r;
		int ns_g = Ozark.main_gui.theme_widget_name_g;
		int ns_b = Ozark.main_gui.theme_widget_name_b;
		int ns_a = Ozark.main_gui.theme_widget_name_a;

		int bg_r = Ozark.main_gui.theme_widget_background_r;
		int bg_g = Ozark.main_gui.theme_widget_background_g;
		int bg_b = Ozark.main_gui.theme_widget_background_b;
		int bg_a = Ozark.main_gui.theme_widget_background_a;

		int bd_r = Ozark.main_gui.theme_widget_border_r;
		int bd_g = Ozark.main_gui.theme_widget_border_g;
		int bd_b = Ozark.main_gui.theme_widget_border_b;

		if (this.setting.get_value(true)) { // filling in the button box if button is enabled
			GuiUtil.draw_rect(get_x(), this.save_y, get_x() + this.width, this.save_y + this.height, bg_r, bg_g, bg_b, bg_a);
		}
		GuiUtil.draw_string(this.button_name, this.x + 2, this.save_y, ns_r, ns_g, ns_b, ns_a);
	}
}