package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.util.WurstplusPlayerUtil;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class Flight extends WurstplusHack
{   
    public Flight() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);

        this.name = "Flight";
        this.tag = "Flight";
        this.description = "flight go brrr";
    }
    
    @Override
    protected void enable() {
        mc.player.capabilities.isFlying = true;
    }

    @Override
    protected void disable() {
        if (mc.player != null) {
            mc.player.capabilities.isFlying = false;
        }
    }

    @Override
    public void update() {
        mc.player.capabilities.isFlying = true;
    }

}
