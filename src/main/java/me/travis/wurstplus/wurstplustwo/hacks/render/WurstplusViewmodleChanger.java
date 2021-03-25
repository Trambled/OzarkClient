package me.travis.wurstplus.wurstplustwo.hacks.render;


import me.travis.wurstplus.wurstplustwo.event.events.TransformSideFirstPersonEvent;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
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
 */

public class WurstplusViewmodleChanger extends WurstplusHack {
    public WurstplusViewmodleChanger() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        this.name = "Custom Viewmodel";
        this.tag = "CustomViewmodel";
        this.description = "this is pretty bad lmao";
    }

    WurstplusSetting type = create("Type", "Type", "Value", combobox("FOV", "Both", "Value"));
    WurstplusSetting right_x = create("Right X", "FOVRightX", 0.0, -2.0, 2.0);
    WurstplusSetting right_y = create("Right Y", "FOVRightY", 0.0, -2.0, 2.0);
    WurstplusSetting right_z = create("Right Z", "FOVRightZ", 0.0, -2.0, 2.0);
    WurstplusSetting left_x = create("Left X", "FOVLeftX", 0.0, -2.0, 2.0);
    WurstplusSetting left_y = create("Left Y", "FOVLeftY", 0.0, -2.0, 2.0);
    WurstplusSetting left_z = create("Left Z", "FOVLeftZ", 0.0, -2.0, 2.0);
    WurstplusSetting fov = create("FOV", "FOV", 70, 130, 200);
    WurstplusSetting cancel_eating = create("NoEat", "FOVCancelEating", false);

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
    private final Listener<TransformSideFirstPersonEvent> eventListener = new Listener<>(event -> {
        if (type.in("Value") || type.in("Both")) {
            if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
                GlStateManager.translate(right_x.get_value(1), right_y.get_value(1), right_z.get_value(1));
            } else if (event.getEnumHandSide() == EnumHandSide.LEFT) {
                GlStateManager.translate(left_x.get_value(1), left_y.get_value(1), left_z.get_value(1));
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