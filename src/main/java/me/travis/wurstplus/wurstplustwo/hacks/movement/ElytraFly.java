package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.util.WurstplusMathUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusTimer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;

import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPlayerTravel;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

public final class ElytraFly extends WurstplusHack
{
	WurstplusSetting mode = create("Mode", "ElytraFlyMode", "Superior", combobox("Superior", "Packet", "Control", "None"));
    WurstplusSetting speed = create("Speed", "ElytraFlySpeed", 1.82f, 0f, 10f);
    WurstplusSetting DownSpeed = create("DownSpeed", "ElytraFlyDownSpeed", 1.82f, 0f, 10f);
    WurstplusSetting GlideSpeed = create("GlideSpeed", "ElytraFlyGlideSpeed", 1f, 0f, 10f);
	WurstplusSetting UpSpeed = create("UpSpeed", "ElytraFlyUpSpeed", 2f, 0f, 10f);
	WurstplusSetting Accelerate = create("Accelerate", "ElytraFlyAccelerate", true);
	WurstplusSetting vAccelerationTimer = create("Acceleration Timer", "ElytraFlyTimer", 1000, 0, 10000);
	WurstplusSetting RotationPitch = create("RotationPitch", "ElytraPitch", 0f, -90f, 90f);
	WurstplusSetting InstantFly = create("InstantFly", "ElytraFlyInstant", true);
    WurstplusSetting EquipElytra = create("EquipElytra", "ElytraFlyEquip", true);
	WurstplusSetting use_timer = create("Use Timer", "UseTimer", true);
	WurstplusSetting timer_speed = create("Timer Speed", "TimerSpeed", 0.01, 0, 4);

    private WurstplusTimer AccelerationTimer = new WurstplusTimer();
    private WurstplusTimer AccelerationResetTimer = new WurstplusTimer();
    private WurstplusTimer InstantFlyTimer = new WurstplusTimer();
    private boolean has_elytra;

    public ElytraFly()
    {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);

        this.name = "NewElytraFly";
        this.tag = "NewElytraFly";
        this.description = "makes you fly with elytra";
    }
    
    private int ElytraSlot = -1;
	
	@Override
	public void update() {
		Wurstplus.get_hack_manager().get_module_with_tag("NoFall").set_active(false);
		
		if (mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
			has_elytra = true;
		} else {
		    has_elytra = false;
        }
		
		if (use_timer.get_value(true) && !mc.player.isElytraFlying() && (mc.player.getHealth() > 0) && has_elytra) {
			mc.timer.tickLength = 50.0f / ((timer_speed.get_value(1) == 0f) ? 0.1f : timer_speed.get_value(1));
		} else {
            mc.timer.tickLength = 50.0f;
		}
		
	}
	
    
    @Override
    protected void enable()
    {	
        ElytraSlot = -1;
        
        if (EquipElytra.get_value(true))
        {
            if (mc.player != null && mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA)
            {
				
                for (int l_I = 0; l_I < 44; ++l_I)
                {
                    ItemStack l_Stack = mc.player.inventory.getStackInSlot(l_I);
                    
                    if (l_Stack.isEmpty() || l_Stack.getItem() != Items.ELYTRA)
                        continue;
                    
                    ItemElytra l_Elytra = (ItemElytra)l_Stack.getItem();
                    
                    ElytraSlot = l_I;
                    break;
                }
                
                if (ElytraSlot != -1)
                {
                    boolean l_HasArmorAtChest = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.AIR;
                    
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, ElytraSlot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
                    
                    if (l_HasArmorAtChest)
                        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, ElytraSlot, 0, ClickType.PICKUP, mc.player);
                }
            }
        }
    }

    @Override
    protected void disable()
    {
		
		mc.timer.tickLength = 50.0f; // in case if the player disables elyfly during takeoff
		
        if (mc.player == null)
            return;
        
        if (ElytraSlot != -1)
        {
            boolean l_HasItem = !mc.player.inventory.getStackInSlot(ElytraSlot).isEmpty() || mc.player.inventory.getStackInSlot(ElytraSlot).getItem() != Items.AIR;
            
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, ElytraSlot, 0, ClickType.PICKUP, mc.player);
            
            if (l_HasItem)
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
        }
    }

    @EventHandler
    private Listener<WurstplusEventPlayerTravel> OnTravel = new Listener<>(p_Event ->
    {
        if (mc.player == null)
            return;

        if (mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA)
            return;

        if (!mc.player.isElytraFlying())
        {
            if (!mc.player.onGround && InstantFly.get_value(true))
            {
                if (!InstantFlyTimer.passed(1000))
                    return;

                InstantFlyTimer.reset();

                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_FALL_FLYING));
            }

            return;
        }
		if (mode.in("Packet")) {
			HandleNormalModeElytra(p_Event);
		}
		if (mode.in("Superior")) {
			HandleImmediateModeElytra(p_Event);
		}
		if (mode.in("Control")) {
			HandleControlMode(p_Event);
		}
    });

    public void HandleNormalModeElytra(WurstplusEventPlayerTravel p_Travel)
    {
        double l_YHeight = mc.player.posY;

        boolean l_IsMoveKeyDown = mc.player.movementInput.moveForward > 0 || mc.player.movementInput.moveStrafe > 0;

        
        if (mc.player.movementInput.jump)
        {
            p_Travel.cancel();
            Accelerate();
            return;
        }

        if (!l_IsMoveKeyDown)
        {
            AccelerationTimer.resetTimeSkipTo(-vAccelerationTimer.get_value(1));
        } 
        else if ((mc.player.rotationPitch <= RotationPitch.get_value(1)))
        {
            if (Accelerate.get_value(true))
            {
                if (AccelerationTimer.passed(vAccelerationTimer.get_value(1)))
                {
                    Accelerate();
                    return;
                }
            }
            return;
        }

        p_Travel.cancel();
        Accelerate();
    }

    public void HandleImmediateModeElytra(WurstplusEventPlayerTravel p_Travel)
    {
        if (mc.player.movementInput.jump)
        {
            double l_MotionSq = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
            
            if (l_MotionSq > 1.0)
            {
                return;
            }
            else
            {
                double[] dir = WurstplusMathUtil.directionSpeedNoForward(speed.get_value(1));
                
                mc.player.motionX = dir[0];
                mc.player.motionY = -(GlideSpeed.get_value(1) / 10000f);
                mc.player.motionZ = dir[1];
            }

            p_Travel.cancel();
            return;
        }
        
        mc.player.setVelocity(0, 0, 0);

        p_Travel.cancel();
       
        double[] dir = WurstplusMathUtil.directionSpeed(speed.get_value(1));

        if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0)
        {
            mc.player.motionX = dir[0];
            mc.player.motionY = -(GlideSpeed.get_value(1) / 10000f);
            mc.player.motionZ = dir[1];
        }

        if (mc.player.movementInput.sneak)
            mc.player.motionY = -DownSpeed.get_value(1);
        
        mc.player.prevLimbSwingAmount = 0;
        mc.player.limbSwingAmount = 0;
        mc.player.limbSwing = 0;
    }

    public void Accelerate()
    {
        if (AccelerationResetTimer.passed(vAccelerationTimer.get_value(1)))
        {
            AccelerationResetTimer.reset();
            AccelerationTimer.reset();
        }

        float l_Speed = this.speed.get_value(1);

        final double[] dir = WurstplusMathUtil.directionSpeed(l_Speed);

        mc.player.motionY = -(GlideSpeed.get_value(1) / 10000f);

        if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0)
        {
            mc.player.motionX = dir[0];
            mc.player.motionZ = dir[1];
        }
        else
        {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }

        if (mc.player.movementInput.sneak)
            mc.player.motionY = -DownSpeed.get_value(1);

        mc.player.prevLimbSwingAmount = 0;
        mc.player.limbSwingAmount = 0;
        mc.player.limbSwing = 0;
    }


    private void HandleControlMode(WurstplusEventPlayerTravel p_Event)
    {
        final double[] dir = WurstplusMathUtil.directionSpeed(speed.get_value(1));
        
        if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0)
        {
            mc.player.motionX = dir[0];
            mc.player.motionZ = dir[1];
            
            mc.player.motionX -= (mc.player.motionX*(Math.abs(mc.player.rotationPitch)+90)/90) - mc.player.motionX;
            mc.player.motionZ -= (mc.player.motionZ*(Math.abs(mc.player.rotationPitch)+90)/90) - mc.player.motionZ;
        }
        else
        {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
        
        mc.player.motionY = (-WurstplusMathUtil.degToRad(mc.player.rotationPitch)) * mc.player.movementInput.moveForward;
        

        mc.player.prevLimbSwingAmount = 0;
        mc.player.limbSwingAmount = 0;
        mc.player.limbSwing = 0;
        p_Event.cancel();
    }

}