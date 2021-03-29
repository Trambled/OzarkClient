package me.trambled.ozark.ozarkclient.module.movement;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.Ozark;

//from kami
public class OldElytraFly extends Module {
	
	Setting mode = create("Mode", "Mode", "Boost", combobox("Boost", "Fly"));
        Setting speed = create("Speed", "Speed", 0.925f, 0.1f, 5f);
      
	
	public OldElytraFly() {
        super(Category.MOVEMENT);

        this.name        = "Old ElytraFly";
        this.tag         = "OldElytraFly";
        this.description = "Kami Elytra Flight";
    }
    
    @Override
    public void update() {
		
		Ozark.get_hack_manager().get_module_with_tag("NoFall").set_active(false);
		
        if (mc.player.capabilities.isFlying) {
            mc.player.setVelocity(0.0, -0.003, 0.0);
            mc.player.capabilities.setFlySpeed(speed.get_value(1));
        }

        if (mc.player.onGround) {
            mc.player.capabilities.allowFlying = false;
        }

        if (!mc.player.isElytraFlying()) {
            return;
        }

        if (mode.in("Boost")) {
            if (mc.player.isInWater()) {
                mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                return;
            }

            if (mc.gameSettings.keyBindJump.isKeyDown()) {

                mc.player.motionY += 0.08;

			} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {

                mc.player.motionY -= 0.04;
			}

            if (mc.gameSettings.keyBindForward.isKeyDown()) {
                float yaw = (float) Math.toRadians(mc.player.rotationYaw);

                mc.player.motionX -= MathHelper.sin(yaw) * 0.05f;

                    
                mc.player.motionZ += MathHelper.cos(yaw) * 0.05f;
            }

            if (mc.gameSettings.keyBindBack.isKeyDown()) {
                float yaw = (float) Math.toRadians(mc.player.rotationYaw);

                EntityPlayerSP player11;
                EntityPlayerSP player5 = player11 = mc.player;

                player11.motionX += MathHelper.sin(yaw) * 0.05f;

                    EntityPlayerSP player12;
                    EntityPlayerSP player6 = player12 = mc.player;

                player12.motionZ -= MathHelper.cos(yaw) * 0.05f;
            }

            if (mode.in("Fly")) {
                mc.player.capabilities.setFlySpeed(speed.get_value(1));
                mc.player.capabilities.isFlying = true;

                if (mc.player.capabilities.isCreativeMode) {
                    return;
                }

                mc.player.capabilities.allowFlying = true;
			}
        }
    }
    
    @Override
    protected void disable() {
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.setFlySpeed(0.05f);

        if (mc.player.capabilities.isCreativeMode) {
            return;
        }

        mc.player.capabilities.allowFlying = false;
    }
}