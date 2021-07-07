package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class FullBright extends Module {


	public FullBright() {
		super(Category.RENDER);

		this.name = "FullBright";
		this.tag = "FullBright";
		this.description = "Best hack.";
	}

	Setting mode = create("Mode", "FBMode", "Potion", combobox("Gamma", "Potion"));

	float oldBright;

	@Override
	public void update() {
		if (mc.player == null)
			return;

		if (mode.in("Potion"))
			mc.player.addPotionEffect(new PotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 1337, 1, false, false)));
	}

	@Override
	public void enable() {
		if (mc.player == null)
			return;

		oldBright = mc.gameSettings.gammaSetting;

		if (mode.in("Gamma"))
			mc.gameSettings.gammaSetting = +100;
	}

	@Override
	public void disable() {
		mc.player.removePotionEffect(MobEffects.NIGHT_VISION);

		if (mode.in("Gamma"))
			mc.gameSettings.gammaSetting = oldBright;
	}
}
