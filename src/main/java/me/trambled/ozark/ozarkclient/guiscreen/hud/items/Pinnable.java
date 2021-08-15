package me.trambled.ozark.ozarkclient.guiscreen.hud.items;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.util.font.FontUtil;
import me.trambled.ozark.ozarkclient.util.render.GuiUtil;
import me.trambled.ozark.ozarkclient.util.render.RainbowUtil;
import me.trambled.turok.draw.RenderHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;


// Travis.


public class Pinnable {
	private final String title;
	private final String tag;

	private boolean state;
	private boolean move;

	public GuiUtil font;

	private int x;
	private int y;

	private int width;
	private int height;

	private int move_x;
	private int move_y;

	private boolean dock = true;

	public final Minecraft mc = Minecraft.getMinecraft();

	public Pinnable(String title, String tag, float font_, int x, int y) {
		this.title = title;
		this.tag   = tag;
		this.font  = new GuiUtil(font_);

		this.x = x;
		this.y = y;

		this.width  = 1;
		this.height = 10;

		this.move = false;
	}

	public void set_move(boolean value) {
		this.move = value;
	}

	public void set_active(boolean value) {
		this.state = value;
	}

	public void set_x(int x) {
		this.x = x;
	}

	public void set_y(int y) {
		this.y = y;
	}

	public void set_width(int width) {
		this.width = width;
	}

	public void set_height(int height) {
		this.height = height;
	}

	public void set_move_x(int x) {
		this.move_x = x;
	}

	public void set_move_y(int y) {
		this.move_y = y;
	}

	public void set_dock(boolean value) {
		this.dock = value;
	}

	public boolean is_moving() {
		return this.move;
	}

	public String get_title() {
		return this.title;
	}

	public String get_tag() {
		return this.tag;
	}

	public int get_title_height() {
		return this.font.get_string_height();
	}

	public int get_x() {
		return this.x;
	}

	public int get_y() {
		return this.y;
	}

	public int get_width() {
		return this.width;
	}

	public int get_height() {
		return this.height;
	}

	public boolean get_dock() {
		return this.dock;
	}

	public boolean is_active() {
		return this.state;
	}

	public boolean motion(int mx, int my) {
		return mx >= get_x ( ) && my >= get_y ( ) && mx <= get_x ( ) + get_width ( ) && my <= get_y ( ) + get_height ( );
	}

	public void crush(int mx, int my) {
		// Get current screen real length.
		int screen_x = (mc.displayWidth / 2);
		int screen_y = (mc.displayHeight / 2);

		set_x(mx - this.move_x);
		set_y(my - this.move_y);

		if (this.x + this.width >= screen_x) {
			this.x = screen_x - this.width - 1;
		}

		if (this.x <= 0) {
			this.x = 1;
		}

		if (this.y + this.height >= screen_y) {
			this.y = screen_y - this.height - 1;
		}

		if (this.y <= 0) {
			this.y = 1;
		}

		if (this.x % 2 != 0) {
			this.x += this.x % 2;
		}

		if (this.y % 2 != 0) {
			this.y += this.y % 2;
		}
	}

	public void render() {}

	public void click(int mx, int my, int mouse) {
		if (mouse == 0) {
			if (is_active() && motion(mx, my)) {
				set_move(true);

				set_move_x(mx - get_x());
				set_move_y(my - get_y());
			}
		}
	}

	public void release(int mx, int my, int mouse) {
		set_move(false);
	}

	public void render(int mx, int my, int tick) {
		if (is_moving()) {
			crush(mx, my);
		}

		if (this.x + this.width <= (mc.displayWidth / 2) / 2) {
			set_dock(true);
		} else if (this.x + this.width >= (mc.displayWidth / 2) / 2) {
			set_dock(false);
		}

		if (is_active()) {
			render();

			GL11.glPushMatrix();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);

			GlStateManager.enableBlend();

			GL11.glPopMatrix();

			RenderHelp.release_gl();

			if (motion(mx, my)) {
				GuiUtil.draw_rect(this.x - 1, this.y - 1, this.width + 1, this.height + 1, 0, 0, 0, 90, 2, "right-left-down-up");
			}
		}
	}

	protected void create_line(String string, int pos_x, int pos_y) {
		GuiUtil.draw_string(string, this.x + pos_x, this.y + pos_y, 255, 255, 255, 255);
	}

	protected void create_line(String string, int pos_x, int pos_y, int r, int g, int b, int a) {
		//TODO: ADD SEPERATE SETTIING FOR ARRAYLST FLOW BECAUSE KAMBING DOESNT LIKE ARRAYLIST FLOW
	if(Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDFont").get_value(true) && Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDFlow").get_value(true)) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		RainbowUtil.drawRainbowStringChatCustomFont(string, this.x + pos_x, this.y + pos_y, new GuiUtil.OzarkColor(r, g, b, a).hex(), 100.0f);
	}else if (Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDFont").get_value(true)) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		FontUtil.drawStringWithShadow(string, this.x + pos_x, this.y + pos_y, r, g, b, a);
	}else if (Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDFlow").get_value(true)) {
			RainbowUtil.drawRainbowString(string, this.x + pos_x, this.y + pos_y, new GuiUtil.OzarkColor(r, g, b, a).hex(), 100.0f);
		} else {
			GuiUtil.draw_string(string, this.x + pos_x, this.y + pos_y, r, g, b, a);
		}
	}

	protected void create_line_regular(String string, int pos_x, int pos_y, int r, int g, int b, int a) {
		GuiUtil.draw_string(string, this.x + pos_x, this.y + pos_y, r, g, b, a);

	}

	protected void create_rect(int pos_x, int pos_y, int width, int height, int r, int g, int b, int a) {
		GuiUtil.draw_rect(this.x + pos_x, this.y + pos_y, this.x + width, this.y + height, r, g, b, a);
	}

	protected void create_outline(int pos_x, int pos_y, int width, int height, int r, int g, int b, int a) {
		GuiUtil.draw_outline(this.x + pos_x, this.y + pos_y, this.x + width, this.y + height, r, g, b, a);
	}

	protected void create_gradient(int pos_x, int pos_y, int width, int height, int startColor, int endColor) {
		GuiUtil.drawGradientRectP(this.x + pos_x, this.y + pos_y, this.x + width, this.y + height, startColor, endColor);
	}


	protected int get(String string, String type) {
		int value_to_request = 0;

		if (type.equals("width")) {
			value_to_request = this.font.get_string_width(string);
		}

		if (type.equals("height")) {
			value_to_request = this.font.get_string_height();
		}

		return value_to_request;
	}

	protected int getCFONT(String string, String type) {
		int value_to_request = 0;

		if (type.equals("width")) {
			value_to_request = FontUtil.getFontWidth(string);
		}

		if (type.equals("height")) {
			value_to_request = FontUtil.getFontHeight();
		}

		return value_to_request;
	}

	public int docking(int position_x, String string) {
		if (this.dock) {
			return position_x;
		} else {
			return (this.width - get(string, "width")) - position_x; 
		}
	}

	protected boolean is_on_gui() {
		return Ozark.main_hud.on_gui;
	}
}