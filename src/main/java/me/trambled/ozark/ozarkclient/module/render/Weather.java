package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

public class Weather extends Module {

    public Weather() {
        super(Category.RENDER);

        this.name = "Weather";
        this.tag = "Weather";
        this.description = "Changes weather.";
    }
    
    Setting weather = create("Weather", "Weather", "Clear", combobox("Clear", "Rain", "Thunder Storm"));

    private int prev_weather;
    
    @EventHandler
    private final Listener<EventRender> on_render = new Listener<>( event -> {
        if (mc.world == null) return;
        if (weather.in("Clear")) {
            mc.world.setRainStrength(0);
        }
        if (weather.in("Rain")) {
            mc.world.setRainStrength(1);
        }
        if (weather.in("Thunder Storm")) {
            mc.world.setRainStrength(2);
        }
    });

    @Override
    public void update() {
        if (mc.world == null) return;
        if (weather.in("Clear")) {
            mc.world.setRainStrength(0);
        }
        if (weather.in("Rain")) {
            mc.world.setRainStrength(1);
        }
        if (weather.in("Thunder Storm")) {
            mc.world.setRainStrength(2);
        }
    }

    @Override
    protected void enable() {
        prev_weather = (int) mc.world.getRainStrength(1);
    }

    @Override
    protected void disable() {
        mc.world.setRainStrength(prev_weather);
    }
}
