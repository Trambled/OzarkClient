package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;

// rte was here >:^)


public class ReverseStep extends Module {
    public ReverseStep() {
        super(Category.MOVEMENT);
        this.name = "Reverse Step";
        this.tag = "reversestep";
        this.description = "Makes you go down cuz you a fat ass nigga";
    }

    @Override
    public void update() {
        if (!mc.player.onGround || mc.player.isOnLadder() || mc.player.isInWater() || mc.player.isInLava() || mc.player.movementInput.jump || mc.player.noClip) return;
        if (mc.player.moveForward == 0 && mc.player.moveStrafing == 0) return;

        mc.player.motionY = -1;
    }
}
