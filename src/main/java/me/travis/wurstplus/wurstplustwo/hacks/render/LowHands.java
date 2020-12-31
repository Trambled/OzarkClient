package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.EnumHand;

public class LowHands extends WurstplusHack {

    public LowHands() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        this.name = "LowHands";
        this.tag = "LowHands";
        this.description = "someone cut off me hand ";
    }
	
	WurstplusSetting mainhand_height = create("Mainhand", "Mainhand", 5, 0, 10);
	WurstplusSetting offhand_height = create("Offhand", "Offhand", 5, 0, 10);
	

    @Override
    public void update() {
		mc.entityRenderer.itemRenderer.equippedProgressMainHand = mainhand_height.get_value(1) / 10;
        mc.entityRenderer.itemRenderer.equippedProgressOffHand = offhand_height.get_value(1) / 10;
		
        mc.entityRenderer.itemRenderer.itemStackMainHand = mc.player.getHeldItem(EnumHand.MAIN_HAND);
        mc.entityRenderer.itemRenderer.itemStackOffHand = mc.player.getHeldItem(EnumHand.OFF_HAND);
    }
}
