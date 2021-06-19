package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class SkyColour extends Module {

    public SkyColour() {
        super(Category.RENDER);

        this.name = "SkyColour";
        this.tag = "SkyColour";
        this.description = "Changes the sky's colour.";
    }

    Setting r = create("R", "SkyColourR", 255, 0, 255);
    Setting g = create("G", "SkyColourG", 255, 0, 255);
    Setting b = create("B", "SkyColourB", 255, 0, 255);
    Setting rainbow_mode = create("Rainbow", "SkyColourRainbow", false);

    @SubscribeEvent
    public void fog_colour(final EntityViewRenderEvent.FogColors event) {
        event.setRed(r.get_value(1) / 255f);
        event.setGreen(g.get_value(1) / 255f);
        event.setBlue(b.get_value(1) / 255f);
    }

    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        event.setDensity(0.0f);
        event.setCanceled(true);
    }

    @Override
    public void update() {
        if (rainbow_mode.get_value(true)) {
            cycle_rainbow();
        }
    }

    public void cycle_rainbow() {

        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

        r.set_value((color_rgb_o >> 16) & 0xFF);
        g.set_value((color_rgb_o >> 8) & 0xFF);
        b.set_value(color_rgb_o & 0xFF);

    }

}
