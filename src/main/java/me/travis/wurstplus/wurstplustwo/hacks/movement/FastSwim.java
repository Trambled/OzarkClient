package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.util.WurstplusPlayerUtil;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class FastSwim extends WurstplusHack
{
    public FastSwim() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "Fast Swim";
        this.tag = "FastSwim";
        this.description = "swimming go brrrrrrrrr";
    }

    WurstplusSetting up = create("Up", "Up", true);
    WurstplusSetting down = create("Down", "Down", true);
    WurstplusSetting water = create("Water", "Water", true);
    WurstplusSetting lava = create("Lava", "Lava", true);

    WurstplusPlayerUtil util = new WurstplusPlayerUtil();
    
    @Override
    public void update() {
        int divider = 5;
        if ((FastSwim.mc.player.isInWater() || FastSwim.mc.player.isInLava()) && FastSwim.mc.player.movementInput.jump && this.up.get_value(true)) {
            FastSwim.mc.player.motionY = 0.0725 / divider;
        }
        if (FastSwim.mc.player.isInWater() && this.water.get_value(true) && (FastSwim.mc.player.movementInput.moveForward != 0.0f || FastSwim.mc.player.movementInput.moveStrafe != 0.0f)) {
            this.util.addSpeed(0.02);
        }
        if (FastSwim.mc.player.isInLava() && this.lava.get_value(true) && !FastSwim.mc.player.onGround && (FastSwim.mc.player.movementInput.moveForward != 0.0f || FastSwim.mc.player.movementInput.moveStrafe != 0.0f)) {
            this.util.addSpeed(0.06999999999999999);
        }
        if (FastSwim.mc.player.isInWater() && this.down.get_value(true) && !FastSwim.mc.player.onGround && FastSwim.mc.player.movementInput.sneak) {
            final int divider2 = divider * -1;
            FastSwim.mc.player.motionY = 2.2 / divider2;
        }
        if (FastSwim.mc.player.isInLava() && this.down.get_value(true) && FastSwim.mc.player.movementInput.sneak) {
            final int divider2 = divider * -1;
            FastSwim.mc.player.motionY = 0.91 / divider2;
        }
    }
}
