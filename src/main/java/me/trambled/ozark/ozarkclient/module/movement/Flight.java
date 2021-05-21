package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;

public class Flight extends Module
{   
    public Flight() {
        super(Category.MOVEMENT);

        this.name = "Flight";
        this.tag = "Flight";
        this.description = "Flight go brrr.";
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
