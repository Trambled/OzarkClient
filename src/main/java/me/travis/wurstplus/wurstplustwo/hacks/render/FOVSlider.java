package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;

public class FOVSlider extends WurstplusHack {

    public FOVSlider() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        this.name = "FOVSliderXulu";
        this.tag = "FOVSlider";
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


