package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.player.PlayerUtil;

public class FastSwim extends Module
{
    public FastSwim() {
        super(Category.MOVEMENT);
        this.name = "FastSwim";
        this.tag = "FastSwim";
        this.description = "Swimming go brrrrrrrrr.";
    }

    Setting up = create("Up", "Up", true);
    Setting down = create("Down", "Down", true);
    Setting water = create("Water", "Water", true);
    Setting lava = create("Lava", "Lava", true);

    PlayerUtil util = new PlayerUtil();
    
    @Override
    public void update() {
        int divider = 5;
        if (full_null_check()) return;
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
