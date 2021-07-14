package me.trambled.ozark.ozarkclient.module.misc;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.world.TimerUtil;

public class AntiAFK extends Module {

    public AntiAFK() {
        super(Category.MISC);

        this.name = "AntiAFK";
        this.tag = "AntiAFK";
        this.description = "Prevents being kicked for afk-ing.";
    }
    Setting tickDelay = create("Delay","Delay",50.0D, 0.0D, 100.0D);
    Setting jump = create("Jump", "Jump", true);
    Setting chat = create("Chat", "Chat", true);
    TimerUtil afkTimer = new TimerUtil();

    @Override
    public void update() {
        if (afkTimer.hasPassed(tickDelay.get_value(1))) {
            if (jump.get_value(true))
                mc.player.jump();

            if (chat.get_value(true))
                mc.player.sendChatMessage("!pt");
        }
    }
}
