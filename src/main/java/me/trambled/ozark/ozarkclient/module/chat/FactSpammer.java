package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.FactUtil;

// cb+
public class FactSpammer extends Module {

    public FactSpammer() {
        super(Category.CHAT);

        this.name = "Fact Spammer";
        this.tag = "FactSpammer";
        this.description = "spams snapple facts";
    }

    Setting delay = create("Delay", "SpammerDelay", 7.0, 7.0, 60.0);

    private long start_time = 0L;

    @Override
    public void update() {
        if (System.currentTimeMillis() - start_time >= (long)(delay.get_value(0) * 1000)) {
            mc.player.sendChatMessage(FactUtil.random_fact());
            start_time = System.currentTimeMillis();
        }
    }

    @Override
    public void enable() {
        start_time = System.currentTimeMillis();
    }

}
