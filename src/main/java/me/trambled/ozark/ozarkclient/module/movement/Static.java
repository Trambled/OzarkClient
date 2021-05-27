package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

// phobos
public class Static extends Module {
    
    public Static() {
        super(Category.MOVEMENT);

		this.name        = "Static";
		this.tag         = "Static";
		this.description = "Stops movement.";
    }

    Setting mode = create("Mode", "Mode", "AntiVoid", combobox("Static", "AntiVoid", "Roof"));
	Setting disabler = create("Disable", "Disable", false);
	Setting ySpeed = create("YSpeed", "YSpeed", false);
	Setting speed = create("Speed", "Speed", 0.1, 0f, 10f);
	Setting height = create("Height", "Height", 3f, 0f, 256f);


    @Override
    public void update() {
		if (full_null_check()) return;
		if (mode.in("Static")) {
			Static.mc.player.capabilities.isFlying = false;
            Static.mc.player.motionX = 0.0;
            Static.mc.player.motionY = 0.0;
            Static.mc.player.motionZ = 0.0;
            if (!this.ySpeed.get_value(true)) return;
            Static.mc.player.jumpMovementFactor = (float) this.speed.get_value(1d);
            if (Static.mc.gameSettings.keyBindJump.isKeyDown()) {
                Static.mc.player.motionY += this.speed.get_value(1d);
            }
            if (!Static.mc.gameSettings.keyBindSneak.isKeyDown()) return;
            Static.mc.player.motionY -= this.speed.get_value(1d);
		} else if (mode.in("AntiVoid")) {
			if (Static.mc.player.noClip || !(Static.mc.player.posY <= (double) this.height.get_value(1)))
                return;
            RayTraceResult trace = Static.mc.world.rayTraceBlocks(Static.mc.player.getPositionVector(), new Vec3d(Static.mc.player.posX, 0.0, Static.mc.player.posZ), false, false, false);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                return;
            }
            Static.mc.player.setVelocity(0.0, 0.0, 0.0);
            if (Static.mc.player.getRidingEntity() == null) return;
			Static.mc.player.getRidingEntity().setVelocity(0.0, 0.0, 0.0);
		} else if (mode.in("Roof")) {
			Static.mc.player.connection.sendPacket(new CPacketPlayer.Position(Static.mc.player.posX, 10000.0, Static.mc.player.posZ, Static.mc.player.onGround));
            if (!this.disabler.get_value(true)) return;
            this.set_disable();
		}
    }

    @Override
    public void update_always() {
        disabler.set_shown(mode.in("Roof"));
        ySpeed.set_shown(mode.in("Static"));
        speed.set_shown(mode.in("Static") && ySpeed.get_value(true));
        height.set_shown(mode.in("AntiVoid"));
    }

}