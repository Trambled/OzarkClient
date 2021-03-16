package me.travis.wurstplus.wurstplustwo.hacks.movement;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.Wurstplus;

//from kami
public class OldElytraFly extends WurstplusHack {
	
	WurstplusSetting mode = create("Mode", "Mode", "Boost", combobox("Boost", "Fly"));
        WurstplusSetting speed = create("Speed", "Speed", 0.925f, 0.1f, 5f);
      
	
	public OldElytraFly() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);

        this.name        = "Old ElytraFly";
        this.tag         = "OldElytraFly";
        this.description = "Kami Elytra Flight";
    }
    
    @Override
    public void update() {
		
		Wurstplus.get_hack_manager().get_module_with_tag("NoFall").set_active(false);
		
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
                EntityPlayerSP player7;
                EntityPlayerSP player = player7 = mc.player;

                player7.motionY += 0.08;

			} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                EntityPlayerSP player8;
                EntityPlayerSP player2 = player8 = mc.player;

                player8.motionY -= 0.04;
			}

            if (mc.gameSettings.keyBindForward.isKeyDown()) {
                float yaw = (float) Math.toRadians(mc.player.rotationYaw);
                    
                EntityPlayerSP player9;                    
                EntityPlayerSP player3 = player9 = mc.player;
                    
                player9.motionX -= MathHelper.sin(yaw) * 0.05f;
                    
                EntityPlayerSP player10;
                EntityPlayerSP player4 = player10 = mc.player;
                    
                player10.motionZ += MathHelper.cos(yaw) * 0.05f;
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