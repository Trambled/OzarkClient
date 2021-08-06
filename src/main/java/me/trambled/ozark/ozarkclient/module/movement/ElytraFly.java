package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.event.events.EventPlayerTravel;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.world.MathUtil;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.world.TimerUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;

public final class ElytraFly extends Module
{
    public ElytraFly()
    {
        super(Category.MOVEMENT);

        this.name = "ElytraFly";
        this.tag = "ElytraFly";
        this.description = "Makes you fly with a elytra.";
    }

    Setting mode = create("Mode", "Mode", "Normal", combobox("Normal", "Tarzan", "Superior", "Packet", "Control"));
    Setting speed = create("Speed", "SpeedEly", 1.82, 0.0, 10.0);
    Setting DownSpeed = create("DownSpeed", "DownSpeed", 1.82, 0.0, 10.0);
    Setting GlideSpeed = create("GlideSpeed", "GlideSpeed", 1, 0, 10);
    Setting Accelerate = create("Accelerate", "Accelerate", true);
    Setting vAccelerationTimer = create("Timer", "Timer", 1000, 0, 10000);
    Setting RotationPitch = create("RotationPitch", "RotationPitch", 0.0, -90.0, 90.0);
    Setting CancelInWater = create("CancelInWater", "CancelInWater", true);
    Setting CancelAtHeight = create("CancelAtHeight", "CancelAtHeight", 5, 0, 10);
    Setting InstantFly = create("InstantFly", "InstantFly", false);
    Setting EquipElytra = create("EquipElytra", "EquipElytra", false);
    Setting PitchSpoof = create("PitchSpoof", "PitchSpoof", false);
    Setting use_timer = create("Timerontakeoff", "Timerontakeoff", false);

    private TimerUtil AccelerationTimer = new TimerUtil();
    private TimerUtil AccelerationResetTimer = new TimerUtil();
    private TimerUtil InstantFlyTimer = new TimerUtil();
    private boolean SendMessage = false;

    private int ElytraSlot = -1;

    @Override
    public void update() {
        if (Ozark.get_module_manager().get_module_with_tag("NoFall").is_active()) {
            Ozark.get_module_manager().get_module_with_tag("NoFall").set_active(false);
            MessageUtil.send_client_message("Nofall turned off because it does not work with elytrafly");
            MessageUtil.send_client_message("Make sure you dont have nofall or antihunger on on any other clients");
        }

        if (use_timer.get_value(true)  && !mc.player.isElytraFlying() && (mc.player.getHealth() > 0) && mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
            mc.timer.tickLength = 500.0f;
        } else {
            mc.timer.tickLength = 50.0f;
        }
    }

    @Override
    public void enable()
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
    public void disable()
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
    private Listener<EventPlayerTravel> OnTravel = new Listener<>(p_Event ->
    {
        if (mc.player == null)
            return;

        /// Player must be wearing an elytra.
        if (mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA)
            return;

        if (!mc.player.isElytraFlying())
        {
            if (!mc.player.onGround && InstantFly.get_value(true))
            {
                if (!InstantFlyTimer.passed(1000))
                    return;

                InstantFlyTimer.reset();

                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }

            return;
        }

        if(mode.in("Normal") || mode.in("Tarzan") || mode.in("Packet")) {
            HandleNormalModeElytra(p_Event);
        } else if(mode.in("Superior")) {
            HandleImmediateModeElytra(p_Event);
        } else if(mode.in("Control")) {
            HandleControlMode(p_Event);
        }
    });

    public void HandleNormalModeElytra(EventPlayerTravel p_Travel)
    {
        double l_YHeight = mc.player.posY;

        if (l_YHeight <= CancelAtHeight.get_value(1))
        {
            if (!SendMessage)
            {
                SendMessage = true;
            }

            return;
        }

        boolean l_IsMoveKeyDown = mc.player.movementInput.moveForward > 0 || mc.player.movementInput.moveStrafe > 0;

        boolean l_CancelInWater = !mc.player.isInWater() && !mc.player.isInLava() && CancelInWater.get_value(true);

        if (mc.player.movementInput.jump)
        {
            p_Travel.cancel();
            Accelerate();
            return;
        }

        if (!l_IsMoveKeyDown)
        {
            AccelerationTimer.resetTimeSkipTo(-vAccelerationTimer.get_max(1));
        }
        else if ((mc.player.rotationPitch <= RotationPitch.get_value(1) || mode.in("Tarzan")) && l_CancelInWater)
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

    public void HandleImmediateModeElytra(EventPlayerTravel p_Travel)
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
                double[] dir = MathUtil.directionSpeedNoForward(speed.get_value(1));

                mc.player.motionX = dir[0];
                mc.player.motionY = -(GlideSpeed.get_value(1) / 10000f);
                mc.player.motionZ = dir[1];
            }

            p_Travel.cancel();
            return;
        }

        mc.player.setVelocity(0, 0, 0);

        p_Travel.cancel();

        double[] dir = MathUtil.directionSpeed(speed.get_value(1));

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
            SendMessage = false;
        }

        float l_Speed = (float)this.speed.get_value(1);

        final double[] dir = MathUtil.directionSpeed(l_Speed);

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


    private void HandleControlMode(EventPlayerTravel p_Event)
    {
        final double[] dir = MathUtil.directionSpeed(speed.get_value(1));

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

        mc.player.motionY = (-MathUtil.degToRad(mc.player.rotationPitch)) * mc.player.movementInput.moveForward;


        mc.player.prevLimbSwingAmount = 0;
        mc.player.limbSwingAmount = 0;
        mc.player.limbSwing = 0;
        p_Event.cancel();
    }

    @EventHandler
    private Listener<EventPacket> PacketEvent = new Listener<>(p_Event ->
    {
        if (p_Event.get_packet() instanceof CPacketPlayer && PitchSpoof.get_value(true))
        {
            if (!mc.player.isElytraFlying())
                return;

            if (p_Event.get_packet() instanceof CPacketPlayer.PositionRotation && PitchSpoof.get_value(true))
            {
                CPacketPlayer.PositionRotation rotation = (CPacketPlayer.PositionRotation) p_Event.get_packet();

                mc.getConnection().sendPacket(new CPacketPlayer.Position(rotation.x, rotation.y, rotation.z, rotation.onGround));
                p_Event.cancel();
            }
            else if (p_Event.get_packet() instanceof CPacketPlayer.Rotation && PitchSpoof.get_value(true))
            {
                p_Event.cancel();
            }
        }
    });
}