package me.trambled.ozark.ozarkclient.guiscreen.hud.items;


import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.util.render.GuiUtil;


public class PinnableButton {
	private final Pinnable pinnable;
	private final Frame master;

	private final String name;
	private final String tag;

	private int x;
	private int y;

	private int save_y;

	private int width;
	private int height;

	private final boolean first;

	private final GuiUtil font = new GuiUtil(1);

	public static int nc_r = 0;
	public static int nc_g = 0;
	public static int nc_b = 0;
	public static int nc_a = 0;

	public static int bg_r = 0;
	public static int bg_g = 0;
	public static int bg_b = 0;
	public static int bg_a = 0;

	public static int bd_r = 0;
	public static int bd_g = 0;
	public static int bd_b = 0;

	public PinnableButton(Frame master, String name, String tag) {
		this.master = master;

		this.name = name;
		this.tag  = tag;

		this.pinnable = Ozark.get_hud_manager().get_pinnable_with_tag(tag);

		this.x = master.get_x();
		this.y = master.get_y();

		this.save_y = this.y;

		this.width  = this.master.get_width();
		this.height = font.get_string_height();

		this.first = true;
	}

	public void set_x(int x) {
		this.x = x;
	}

	public void set_y(int y) {
		this.y = y;
	}

	public void set_save_y(int y) {
		this.save_y = y;
	}

	public void set_width(int width) {
		this.width = width;
	}

	public void set_height(int height) {
		this.height = height;
	}

	public int get_x() {
		return this.x;
	}

	public int get_y() {
		return this.y;
	}

	public int get_save_y() {
		return this.save_y;
	}

	public int get_width() {
		return this.width;
	}

	public int get_height() {
		return this.height;
	}

	public boolean motion(int mx, int my, int p_x, int p_y, int p_w, int p_h) {
		return mx >= p_x && my >= p_y && mx <= p_x + p_w && my <= p_y + p_h;
	}

	public boolean motion(int mx, int my) {
		return mx >= get_x ( ) && my >= get_save_y ( ) && mx <= get_x ( ) + get_width ( ) && my <= get_save_y ( ) + get_height ( );
	}

	public void click(int mx, int my, int mouse) {
		this.pinnable.click(mx, my, mouse);

		if (mouse == 0) {
			if (motion(mx, my)) {
				this.master.does_can(false);

				this.pinnable.set_active(!this.pinnable.is_active());
			}
		}
	}

	public void release(int mx, int my, int mouse) {
		this.pinnable.release(mx, my, mouse);

		this.master.does_can(true);
	}

	public void render(int mx, int my, int separate) {
		set_width(this.master.get_width() - separate);

		this.save_y = this.y + this.master.get_y() - 10;

		if (this.pinnable.is_active()) {
			GuiUtil.draw_rect(this.x, this.save_y, this.x + this.width - separate, this.save_y + this.height, bg_r, bg_g, bg_b, bg_a);
		
			GuiUtil.draw_string(this.pinnable.get_title(), this.x + separate, this.save_y, nc_r, nc_g, nc_b, nc_a);
		} else {
			GuiUtil.draw_string(this.pinnable.get_title(), this.x + separate, this.save_y, nc_r, nc_g, nc_b, nc_a);
		}

		this.pinnable.render(mx, my, 0);
	}
}