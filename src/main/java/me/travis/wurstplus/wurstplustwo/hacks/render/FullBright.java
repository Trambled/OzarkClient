package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventSetupFog;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.init.MobEffects;

public class FullBright extends WurstplusHack {
	
	private float prior_gamma;
    
    public FullBright() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        this.name = "Full Bright";
        this.tag = "FullBright";
        this.description = "best hack";
    }
	
	@Override
	protected void enable() {
		prior_gamma = mc.gameSettings.gammaSetting;
	}
	
	@Override
	protected void disable() {
		mc.gameSettings.gammaSetting = prior_gamma;
	}
	
	@Override
	public void update() {
		mc.gameSettings.gammaSetting = 1000;
        mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
	}
}