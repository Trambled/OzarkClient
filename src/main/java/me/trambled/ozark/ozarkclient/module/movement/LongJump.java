package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.player.EntityUtil;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//xenon
public class LongJump extends Module {
	
    float boostSpeed;
    int boostSpeedInt;

    boolean jumped;
    boolean boostable;

    public LongJump() {
        super(Category.MOVEMENT);

		this.name        = "LongJump";
		this.tag         = "LongJump";
		this.description = "Makes you jump far.";
    }

    Setting bypass = create("Bypass", "Bypass", true);
    Setting bypassMode = create("Bypass Mode", "Bypass Mode", "Packet", combobox("Packet", "Packet2"));
	Setting boostMode = create("Boost Mode", "boostMode", "Only ground", combobox("Only ground", "Always", "Jump Event"));
	Setting calcMode = create("Calc Mode", "calcMode", "Constant", combobox("Dissolve", "Constant"));
	Setting boost = create("Boost", "Boost", 37, 1, 100);

    @Override
    public void update() {
        boostSpeedInt = (boost.get_value(1));
        boostSpeed = ((float) boostSpeedInt) / 20;
		
        if (mc.player.onGround) {
            jumped = false;
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }

        double yaw = EntityUtil.getDirection();

        if (boostMode.in("Always")) {
            setMotion(yaw, boostSpeed);
        }
        else if (boostMode.in("Only ground")) {
            if (mc.player.onGround && mc.player.moveForward != 0) {
                if (calcMode.in("Constant")) {
                    setMotion(yaw, boostSpeed);
                }
                else {
                    mc.timer.tickLength = 50f;

                    mc.player.motionX = -Math.sin(yaw) * (((float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ)) * (mc.player.onGround ? boostSpeed : 1f));
                    mc.player.motionZ = Math.cos(yaw) * (((float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ)) * (mc.player.onGround ? boostSpeed : 1f));
                }
            }
            else {
                mc.timer.tickLength = 50f;

                mc.player.motionX = -Math.sin(yaw) * (((float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ)) * (mc.player.onGround ? boostSpeed : 1f));
                mc.player.motionZ = Math.cos(yaw) * (((float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ)) * (mc.player.onGround ? boostSpeed : 1f));
            }
        }
        else {
            mc.timer.tickLength = 50f;

            mc.player.motionX = -Math.sin(yaw) * (((float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ)) * (boostable ? boostSpeed : 1f));
            mc.player.motionZ = Math.cos(yaw) * (((float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ)) * (boostable ? boostSpeed : 1f));

            boostable = false;
        }

        if (mc.player.moveForward != 0) {
            if (mc.player.onGround) {
                mc.player.jump();
            }

            if (bypass.get_value(true)) {
                if (bypassMode.in("Packet")) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, mc.player.posY + mc.player.motionY, mc.player.posZ + mc.player.motionZ, true));
                }
                else {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, mc.player.posY + mc.player.motionY, mc.player.posZ + mc.player.motionZ, true));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, mc.player.posY + mc.player.motionY, mc.player.posZ + mc.player.motionZ, true));
                }
            }

        }
        else if ( mc.player.moveStrafing == 0 ) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
    }

    @Override
    protected void disable() {
        mc.player.motionX = 0;
        mc.player.motionZ = 0;
    }

    public void setMotion(double yaw, float sped) {

        mc.player.motionX = -Math.sin(yaw) * sped;
        mc.player.motionZ = Math.cos(yaw) * sped;
    }

    @SubscribeEvent
    public void onJump(final LivingEvent.LivingJumpEvent event) {
        if ((mc.player != null && mc.world != null) && event.getEntity() == mc.player && (mc.player.movementInput.moveForward != 0f || mc.player.movementInput.moveStrafe != 0f)) {

            jumped = true;
            boostable = true;
        }
    }

    @Override
    public void update_always() {
        bypassMode.set_shown(bypass.get_value(true));
    }

}