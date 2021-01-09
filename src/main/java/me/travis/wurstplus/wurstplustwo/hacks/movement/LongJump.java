package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusEntityUtil;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//xenon
public class LongJump extends WurstplusHack {
	
    float boostSpeed;
    int boostSpeedInt;

    boolean jumped;
    boolean boostable;

    public LongJump() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);

		this.name        = "LongJump";
		this.tag         = "LongJump";
		this.description = "Makes you jump far";
    }
	
	WurstplusSetting bypassMode = create("Bypass Mode", "Bypass Mode", "Packet", combobox("Packet", "Packet 2"));
	WurstplusSetting boostMode = create("Boost Mode", "boostMode", "Only ground", combobox("Only ground", "Always", "Jump Event"));
	WurstplusSetting calcMode = create("Calc Mode", "calcMode", "Constant", combobox("Dissolve", "Constant"));
	WurstplusSetting bypass = create("Bypass", "Bypass", true);
	WurstplusSetting boost = create("Boost", "Boost", 37, 1, 100);

    @Override
    public void update() {
        boostSpeedInt = (boost.get_value(1));
        boostSpeed = ((float) boostSpeedInt) / 20;
		
        if (mc.player.onGround) {
            jumped = false;
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }

        double yaw = WurstplusEntityUtil.getDirection();

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
        else if (mc.player.moveForward == 0 && mc.player.moveStrafing == 0) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
        //TODO: fix packet
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

}