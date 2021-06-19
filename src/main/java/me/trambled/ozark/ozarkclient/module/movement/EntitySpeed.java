package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import net.minecraft.util.MovementInput;

//salhack
public class EntitySpeed extends Module
{   
    public EntitySpeed() {
        super(Category.MOVEMENT);

        this.name = "EntitySpeed";
        this.tag = "EntitySpeed";
        this.description = "Modify your speed on an rideable entity.";
    }
    
	Setting speed = create("Speed", "Speed", 1.43, 0, 10);

    @Override
    public void update() {
        if (mc.player.getRidingEntity() != null) {
            MovementInput movementInput = mc.player.movementInput;
            double forward = movementInput.moveForward;
            double strafe = movementInput.moveStrafe;
            float yaw = mc.player.rotationYaw;
            if ((forward == 0.0D) && (strafe == 0.0D)) {
                mc.player.getRidingEntity().motionX = 0.0D;
                 mc.player.getRidingEntity().motionZ = 0.0D;
            }
            else{
                if (forward != 0.0D) {
                    if (strafe > 0.0D) {
                        yaw += (forward > 0.0D ? -45 : 45);
                    }else if (strafe < 0.0D) {
                        yaw += (forward > 0.0D ? 45 : -45);
                    }
                    strafe = 0.0D;
                    if (forward > 0.0D) {
                        forward = 1.0D;
                    }else if (forward < 0.0D) {
                        forward = -1.0D;
                    }
                }
                double sin = Math.sin(Math.toRadians(yaw + 90.0F));
                double cos = Math.cos(Math.toRadians(yaw + 90.0F));
                mc.player.getRidingEntity().motionX = (forward * speed.get_value(1) * cos + strafe * speed.get_value(1) * sin);
                mc.player.getRidingEntity().motionZ = (forward * speed.get_value(1) * sin - strafe * speed.get_value(1) * cos);
            }
        }
	}
}
