package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;

public class FovSlider extends Module {

    public FOVSlider() {
        super(Category.RENDER);

        this.name = "FovSlider";
        this.tag = "FovSlider";
        this.description = "Changes FOV";
    }
	
	private float fov;

    WurstplusSetting FOV = create("FOV", "FOV", 110, 90, 200);
    WurstplusSetting mode = create("Mode", "FOVSliderMode", "Hand Changer", combobox("FOV Changer", "Hand Changer"));

    @SubscribeEvent
    public void fovOn(final EntityViewRenderEvent.FOVModifier e) {
        if (mode.in("Hand Changer")) {
            e.setFOV((float)FOV.get_value(1));
        }
    }
    
    @Override
    public void enable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.fov = mc.gameSettings.fovSetting;
    }
    
    @Override
    public void disable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        mc.gameSettings.fovSetting = fov;
    }
    
    @Override
    public void update() {
        if (mc.world == null) {
            return;
        }
        if (mode.in("FOV Changer")){
            mc.gameSettings.fovSetting = FOV.get_value(1);
        }
    }
}
