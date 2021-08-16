package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.Event;
import me.trambled.ozark.ozarkclient.event.events.EventMove;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.event.events.EventPlayerJump;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.world.TimerUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;

public class Strafe extends Module {

	public Strafe() {
		super(Category.MOVEMENT);

		this.name        = "Strafe";
		this.tag         = "Strafe";
		this.description = "Its like running, but faster.";
	}

	Setting speed_mode = create("Mode", "StrafeMode", "Strafe", combobox("Strafe", "On Ground", "None"));
	Setting speed = create("Speed", "StrafeSpeed", 5.4f, 0f, 10f);
	Setting y_offset = create("Y Offset", "StrafeYOffset", 4f, 0f, 10f);
	Setting auto_sprint = create("Auto Sprint", "StrafeSprint", true);
	Setting on_water = create("On Water", "StrafeOnWater", true);
	Setting auto_jump = create("Auto Jump", "StrafeAutoJump", true);
	Setting backward = create("Backwards", "StrafeBackwards", true);
	Setting timer = create("Timer", "StrafeTimer", false);
	Setting crystal_boost = create("Crystal Boost", "StrafeCrystalBoost", false); // cookie client
	Setting knockback_time = create("Knockback Time", "StrafeKnockBackTime", 850, 0, 3000);

	TimerUtil knockback_timer = new TimerUtil();
	public double boosted_x;
	public double boosted_z;

	@Override
	public void update() {

		if (full_null_check()) return;

		if (!Ozark.get_module_manager().get_module_with_tag("Timer").is_active() && timer.get_value(true)) {
			Ozark.get_module_manager().get_module_with_tag("Timer").set_active(true);
		}

		if (speed_mode.in("None")) {
			return;
		}

		if (mc.player.isRiding()) return;

		if (mc.player.isInWater() || mc.player.isInLava()) {
			if (!on_water.get_value(true)) return;
		}

		BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
		if (mc.world.getBlockState(pos).getBlock().equals(Blocks.WEB)) {
			return;
		}

		if (mc.player.moveForward != 0 || mc.player.moveStrafing != 0) {

			if (mc.player.moveForward < 0 && !backward.get_value(true)) return;

			if (auto_sprint.get_value(true)) {
				mc.player.setSprinting(true);
			}

			if (mc.player.onGround && speed_mode.in("Strafe")) {

				if (auto_jump.get_value(true)) {
					mc.player.motionY = y_offset.get_value(1) * 0.1;
				}

				final float yaw = get_rotation_yaw() * 0.017453292F;
				mc.player.motionX -= MathHelper.sin(yaw) * 0.2f;
				mc.player.motionZ += MathHelper.cos(yaw) * 0.2f;

			} else if (mc.player.onGround && speed_mode.in("On Ground")) {

				final float yaw = get_rotation_yaw();
				mc.player.motionX -= MathHelper.sin(yaw) * 0.2f;
				mc.player.motionZ += MathHelper.cos(yaw) * 0.2f;
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY+ (y_offset.get_value(1) * 0.1), mc.player.posZ, false));

			}

		}

		if (mc.gameSettings.keyBindJump.isKeyDown() && mc.player.onGround) {
			mc.player.motionY = y_offset.get_value(1) * 0.1f;
		}

	}

	@EventHandler
	private final Listener<EventPlayerJump> on_jump = new Listener<>( event -> {
		if (speed_mode.in("Strafe")) {
			event.cancel();
		}
	});


	@EventHandler
	private final Listener<EventMove> player_move = new Listener<>( event -> {

		if (speed_mode.in("On Ground")) return;

		if (mc.player.isInWater() || mc.player.isInLava()) {
			if (!speed_mode.get_value(true)) return;
		}

		if (mc.player.isSneaking() || mc.player.isOnLadder() || mc.player.isInWeb || mc.player.isInLava() || mc.player.isInWater() || mc.player.capabilities.isFlying) return;

		float player_speed = 0.2873f;
		float move_forward = mc.player.movementInput.moveForward;
		float move_strafe = mc.player.movementInput.moveStrafe;
		float rotation_yaw = mc.player.rotationYaw;

		if (mc.player.isPotionActive(MobEffects.SPEED)) {
			final int amp = Objects.requireNonNull ( mc.player.getActivePotionEffect ( MobEffects.SPEED ) ).getAmplifier();
			player_speed *= (1.2f * (amp+1));
		}

		player_speed *= speed.get_value(1) * 0.2f;

		if (move_forward == 0 && move_strafe == 0) {
			event.set_x(0.0d);
			event.set_z(0.0d);
		} else {
			if (move_forward != 0.0f) {
				if (move_strafe > 0.0f) {
					rotation_yaw += ((move_forward > 0.0f) ? -45 : 45);
				} else if (move_strafe < 0.0f) {
					rotation_yaw += ((move_forward > 0.0f) ? 45 : -45);
				}
				move_strafe = 0.0f;
				if (move_forward > 0.0f) {
					move_forward = 1.0f;
				} else if (move_forward < 0.0f) {
					move_forward = -1.0f;
				}
			}


			//1pop stuff
			if (crystal_boost.get_value(true) && !knockback_timer.passed(knockback_time.get_value(1))) {
				double velocityX = this.boosted_x;
				double velocityZ = this.boosted_z;

				player_speed += (float) Math.sqrt(velocityX * velocityX + velocityZ * velocityZ);

			}

			event.set_x(((move_forward * player_speed) * Math.cos(Math.toRadians((rotation_yaw + 90.0f))) + (move_strafe * player_speed) * Math.sin(Math.toRadians((rotation_yaw + 90.0f)))));
			event.set_z(((move_forward * player_speed) * Math.sin(Math.toRadians((rotation_yaw + 90.0f))) - (move_strafe * player_speed) * Math.cos(Math.toRadians((rotation_yaw + 90.0f)))));

		}

		event.cancel();

	});

	private float get_rotation_yaw() {
		float rotation_yaw = mc.player.rotationYaw;
		if (mc.player.moveForward < 0.0f) {
			rotation_yaw += 180.0f;
		}
		float n = 1.0f;
		if (mc.player.moveForward < 0.0f) {
			n = -0.5f;
		}
		else if (mc.player.moveForward > 0.0f) {
			n = 0.5f;
		}
		if (mc.player.moveStrafing > 0.0f) {
			rotation_yaw -= 90.0f * n;
		}
		if (mc.player.moveStrafing < 0.0f) {
			rotation_yaw += 90.0f * n;
		}
		return rotation_yaw * 0.017453292f;
	}

	@Override
	protected void disable() {
		if (Ozark.get_module_manager().get_module_with_tag("Timer").is_active() && timer.get_value(true)) {
			Ozark.get_module_manager().get_module_with_tag("Timer").set_active(false);
		}
	}

	@EventHandler
	private final Listener<EventPacket.ReceivePacket> packetEvent = new Listener<>( event -> {
		if (event.get_packet() instanceof SPacketExplosion && event.get_era() == Event.Era.EVENT_PRE) {
			SPacketExplosion packet = (SPacketExplosion)event.get_packet();

			boosted_x = packet.motionX;
			boosted_z = packet.motionZ;
			knockback_timer.reset();
		}
	});

	@Override
	public void update_always() {
		knockback_time.set_shown(crystal_boost.get_value(true));
	}
}
