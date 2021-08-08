package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;

import com.google.common.collect.Lists;
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
	public Arraylist() {
		super("Arraylist", "Arraylist", 1, 0, 0);
	}

	int modCount;
	public boolean flag;
	private int scaled_width;
	private int scaled_height;
	private int scale_factor;

	public void render() {
		Setting mode = Ozark.get_setting_manager().get_setting_with_tag("Arraylist", "Mode");
		Setting rainbow = Ozark.get_setting_manager().get_setting_with_tag("Arraylist", "Rainbow");
		Setting red = Ozark.get_setting_manager().get_setting_with_tag("Arraylist", "Red");
		Setting green = Ozark.get_setting_manager().get_setting_with_tag("Arraylist", "Green");
		Setting blue= Ozark.get_setting_manager().get_setting_with_tag("Arraylist", "Blue");

		updateResolution();
		modCount = 0;
		int position_update_y = 2;
		int[] counter = {1};
		final ScaledResolution resolution = new ScaledResolution(mc);
		List<Module> pretty_modules = Ozark.get_module_manager().get_array_active_modules().stream()
                .sorted(Comparator.comparing(module -> FontUtil.getFontWidth(module.array_detail() == null ? module.get_name() : module.get_name() + " [" + Ozark.w + module.array_detail() + Ozark.r  + "]") * (-1)))
				.collect(Collectors.toList());

		if (Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDArrayList").in("Bottom R") || Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDArrayList").in("Bottom L") ) {
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
				String mod = module.array_detail() == null ? module.get_name() : module.get_name() + " [" + Ozark.w + module.array_detail() + Ozark.r + "]";
				int x = resolution.getScaledWidth();
				if (mode.in("Free")) {
					FontUtil.drawStringWithShadow(mod, get_x() + this.docking(2, module.get_name()), get_y() + position_update_y, rainbow.get_value(true) ? RainbowUtil.rainbow(counter[0] * 100) : new Color(red.get_value(1), green.get_value(1), blue.get_value(1), 255).getRGB());

					position_update_y += getCFONT(mod, "height") + 2;

					if (getCFONT(module.get_name(), "width") > this.get_width()) {
						this.set_width(getCFONT(mod, "width") + 2);
					}

					this.set_height(position_update_y);
				} else {
					if (mode.in("Top R")) {
						FontUtil.drawStringWithShadow(mod, x - 2 - FontUtil.getFontWidth(mod), 1 + (modCount * 10), rainbow.get_value(true) ? RainbowUtil.rainbow(counter[0] * 100) : new Color(red.get_value(1), green.get_value(1), blue.get_value(1), 255).getRGB());
					} else if (mode.in("Top L")){
						FontUtil.drawStringWithShadow(mod, 2, 3 + modCount * 10, rainbow.get_value(true) ? RainbowUtil.rainbow(counter[0] * 100) : new Color(red.get_value(1), green.get_value(1), blue.get_value(1), 255).getRGB());
					} else if (mode.in("Bottom L")) {
						FontUtil.drawStringWithShadow(mod, 2, scaled_height - (modCount * 10),rainbow.get_value(true) ? RainbowUtil.rainbow(counter[0] * 100) : new Color(red.get_value(1), green.get_value(1), blue.get_value(1), 255).getRGB());
					} else if (mode.in("Bottom R")) {
						FontUtil.drawStringWithShadow(mod, scaled_width - 2 - FontUtil.getFontWidth(module.array_detail() == null ? module.get_name() : module.get_name() + " [" + Ozark.w + module.array_detail() + Ozark.r  + "]"), scaled_height - (modCount * 10), rainbow.get_value(true) ? RainbowUtil.rainbow(counter[0] * 100) : new Color(red.get_value(1), green.get_value(1), blue.get_value(1), 255).getRGB());
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
		final double scaledWidthD = this.scaled_width / (double)this.scale_factor;
		final double scaledHeightD = this.scaled_height / (double)this.scale_factor;
		this.scaled_width = MathHelper.ceil(scaledWidthD);
		this.scaled_height = MathHelper.ceil(scaledHeightD);
	}

}
