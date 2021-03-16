package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.EnumHand;

public class LowHands extends WurstplusHack {
    public LowHands() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        this.name = "Low Hand";
        this.tag = "LowHands";
        this.description = "someone cut off me hand";
    }
	
	WurstplusSetting offset = create("Offset", "SmallHandOffset", 90, 0, 360);
	

    @Override
    public void update() {
		mc.player.renderArmPitch = offset.get_value(1);
    }
}
