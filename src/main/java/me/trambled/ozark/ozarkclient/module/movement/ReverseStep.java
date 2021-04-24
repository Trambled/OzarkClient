package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

public class ReverseStep extends Module
{   
    public ReverseStep() {
        super(Category.MOVEMENT);

        this.name = "ReverseStep";
        this.tag = "ReverseStep";
        this.description = "go down";
    }
    Setting height = create("Height", "reverstepheight", 2.5, 0.5, 10);

    public void update() {
        if (mc.world == null || mc.player == null || mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder()
                || mc.gameSettings.keyBindJump.isKeyDown()) {
        }
        if (mc.player != null && mc.player.onGround && !mc.player.isInWater() && !mc.player.isOnLadder()) {
            for (double y = 0.0; y < this.height.get_value(1) + 0.5; y += 0.01) {
                if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                    mc.player.motionY = -10.0;
                    break;
                }
            }
        }
    }
}