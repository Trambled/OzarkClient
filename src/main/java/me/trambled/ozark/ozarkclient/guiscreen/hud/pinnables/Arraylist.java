package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.font.FontUtil;
import me.trambled.ozark.ozarkclient.util.misc.DrawnUtil;
import me.trambled.ozark.ozarkclient.util.render.RainbowUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class Arraylist extends Pinnable {
	public int[] counter = {1};
	public Arraylist() {
		super("Arraylist", "Arraylist", 1, 0, 0);
	}

	int modCount;
	public boolean flag;
	private int scaled_width;
	private int scaled_height;
	private int scale_factor;

	public void render() {
		Setting color = Ozark.get_setting_manager().get_setting_with_tag("Arraylist", "ColorMode");
		Setting mode = Ozark.get_setting_manager().get_setting_with_tag("Arraylist", "Mode");

		updateResolution();
		modCount = 0;
		int position_update_y = 2;
		boolean bottom = get_y() > scaled_height / 2;
		boolean retard = Ozark.get_setting_manager().get_setting_with_tag("ArrayList", "ArrayListRetardMode").get_value(true);


		final ScaledResolution resolution = new ScaledResolution(mc);
		List<Module> pretty_modules = Ozark.get_module_manager().get_array_active_modules().stream()
				.sorted(Comparator.comparing(module -> FontUtil.getFontWidth(module.array_detail() == null ? module.get_name() : module.get_name() + ChatFormatting.GRAY + " [" + Ozark.w + module.array_detail() + Ozark.r + ChatFormatting.GRAY + "]") * (-1)))
				.collect(Collectors.toList());

		if ((retard && (Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDArrayList").in("Bottom R") || Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDArrayList").in("Bottom L"))) || (mode.in("Free") && bottom) || (!retard && (Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDArrayList").in("Top R") || Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDArrayList").in("Top L")))) {
			pretty_modules = Lists.reverse(pretty_modules);
		}

		for (Module module : pretty_modules) {
			flag = true;
			for (String s : DrawnUtil.hidden_tags) {
				if (module.get_tag().equalsIgnoreCase(s)) {
					flag = false;
					break;
				}
				if (!flag) break;
			}
			if (flag) {
				String mod = module.array_detail() == null ? module.get_name() : module.get_name() + ChatFormatting.GRAY + " [" + Ozark.w + module.array_detail() + Ozark.r + ChatFormatting.GRAY + "]";
				int x = resolution.getScaledWidth();
				if (mode.in("Free")) {
					FontUtil.drawStringWithShadow(mod, get_x() + this.docking(2, mod), get_y() + position_update_y, generateColor(color));
					position_update_y += getCFONT(mod, "height") + 2;

					if (getCFONT(mod, "width") > this.get_width()) {
						this.set_width(getCFONT(mod, "width") + 2);
					}

					this.set_height(position_update_y);
				} else {
					if (mode.in("Top R")) {
						FontUtil.drawStringWithShadow(mod, x - 2 - FontUtil.getFontWidth(mod), 1 + (modCount * 10), generateColor(color));
					} else if (mode.in("Top L")) {
						FontUtil.drawStringWithShadow(mod, 2, 3 + modCount * 10,generateColor(color));
					} else if (mode.in("Bottom L")) {
						FontUtil.drawStringWithShadow(mod, 2, scaled_height - (modCount * 10), generateColor(color));
					} else if (mode.in("Bottom R")) {
						FontUtil.drawStringWithShadow(mod, scaled_width - 2 - FontUtil.getFontWidth(mod), scaled_height - (modCount * 10), generateColor(color));
					}
				}

				modCount++;
				counter[0]++;
			}
		}
	}

	public void updateResolution() {
		this.scaled_width = mc.displayWidth;
		this.scaled_height = mc.displayHeight;
		this.scale_factor = 1;
		final boolean flag = mc.isUnicode();
		int i = mc.gameSettings.guiScale;
		if (i == 0) {
			i = 1000;
		}
		while (this.scale_factor < i && this.scaled_width / (this.scale_factor + 1) >= 320 && this.scaled_height / (this.scale_factor + 1) >= 240) {
			++this.scale_factor;
		}
		if (flag && this.scale_factor % 2 != 0 && this.scale_factor != 1) {
			--this.scale_factor;
		}
		final double scaledWidthD = this.scaled_width / (double) this.scale_factor;
		final double scaledHeightD = this.scaled_height / (double) this.scale_factor;
		this.scaled_width = MathHelper.ceil(scaledWidthD);
		this.scaled_height = MathHelper.ceil(scaledHeightD);
	}

	public int generateColor(Setting mod) {
		Setting red = Ozark.get_setting_manager().get_setting_with_tag("Arraylist", "Red");
		Setting green = Ozark.get_setting_manager().get_setting_with_tag("Arraylist", "Green");
		Setting blue = Ozark.get_setting_manager().get_setting_with_tag("Arraylist", "Blue");
		switch (mod.get_current_value()) {
			case "Alpha Step":
				return RainbowUtil.alphaStep(new Color(red.get_value(1),blue.get_value(1),green.get_value(1)), 50, (modCount * 2) + 15).getRGB();
			case "Rainbow":
				return RainbowUtil.rainbow(modCount * 100);
			case "Static":
				new Color(red.get_value(1),green.get_value(1),blue.get_value(1)).getRGB();
		}
	return -1;
	}
}
