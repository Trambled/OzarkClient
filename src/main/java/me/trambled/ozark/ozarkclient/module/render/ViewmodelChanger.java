package me.trambled.ozark.ozarkclient.module.render;


import me.trambled.ozark.ozarkclient.event.events.EventTransformSideFirstPerson;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @Author GL_DONT_CARE (Viewmodel Transformations)
 * @Author NekoPvP (Item FOV)
 * @Author ollie (yaw and pitchs stuff)
 */

public class ViewmodelChanger extends Module {
    public ViewmodelChanger() {
        super(Category.RENDER);

        this.name = "Custom Viewmodel";
        this.tag = "CustomViewmodel";
        this.description = "a combo of gamesense & ferox viewmodel";
    }

    Setting type = create("Type", "Type", "Value", combobox("FOV", "Both", "Value"));
    Setting fov = create("FOV", "FOV", 110, 110, 200);
    Setting right_x = create("Right X", "FOVRightX", 0.0, -2.0, 2.0);
    Setting right_y = create("Right Y", "FOVRightY", 0.0, -2.0, 2.0);
    Setting right_z = create("Right Z", "FOVRightZ", 0.0, -2.0, 2.0);
    Setting left_x = create("Left X", "FOVLeftX", 0.0, -2.0, 2.0);
    Setting left_y = create("Left Y", "FOVLeftY", 0.0, -2.0, 2.0);
    Setting left_z = create("Left Z", "FOVLeftZ", 0.0, -2.0, 2.0);
    Setting left_yaw = create("Left Yaw", "FOVLeftYaw", 0, -100, 100);
    Setting left_pitch = create("Left Pitch", "FOVLeftPitch", 0, -100, 100);
    Setting left_roll = create("Left Roll", "FOVLeftRoll", 0, -100, 100);
    Setting right_yaw = create("Right Yaw", "FOVRightYaw", 0, -100, 100);
    Setting right_pitch = create("Right Pitch", "FOVRightPitch", 0, -100, 100);
    Setting right_roll = create("Right Roll", "FOVRightRoll", 0, -100, 100);
    Setting cancel_eating = create("NoEat", "FOVCancelEating", false);

    private float fov_previous;

    @Override
    protected void enable() {
        fov_previous = mc.gameSettings.fovSetting;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    protected void disable() {
        mc.gameSettings.fovSetting = fov_previous;
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @EventHandler
    private final Listener<EventTransformSideFirstPerson> eventListener = new Listener<>(event -> {
        if (type.in("Value") || type.in("Both")) {
            if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
                GlStateManager.translate(right_x.get_value((double)1), right_y.get_value((double)1), right_z.get_value((double)1));
                GlStateManager.rotate(right_yaw.get_value(1),0,1,0);
                GlStateManager.rotate(right_pitch.get_value(1),1,0,0);
                GlStateManager.rotate(right_roll.get_value(1),0,0,1);
            } else if (event.getEnumHandSide() == EnumHandSide.LEFT) {
                GlStateManager.translate(left_x.get_value((double)1), left_y.get_value((double)1), left_z.get_value((double)1));
                GlStateManager.rotate(left_yaw.get_value(1),0,1,0);
                GlStateManager.rotate(left_pitch.get_value(1),1,0,0);
                GlStateManager.rotate(left_roll.get_value(1),0,0,1);
            }
        }
    });

    @SubscribeEvent
    public void onFov(EntityViewRenderEvent.FOVModifier event) {
        if (type.in("FOV") || type.in("Both")) {
            event.setFOV((float) fov.get_value(1));
        }
    }
}
