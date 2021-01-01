package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

public class Timechanger extends WurstplusHack {

    public Timechanger() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        this.name = "Time Changer";
        this.tag = "TimeChanger";
        this.description = "changes time";
    }
    
    WurstplusSetting time = create("Time", "Time", 18000, 0, 24000);
    
    @EventHandler
    private Listener<WurstplusEventRender> on_render = new Listener<>(event -> {
        if (mc.world == null) return;
        mc.world.setWorldTime(time.get_value(1));
    });

    @Override
    public void update() {
        if (mc.world == null) return;
        mc.world.setWorldTime(time.get_value(1));
    }
}
