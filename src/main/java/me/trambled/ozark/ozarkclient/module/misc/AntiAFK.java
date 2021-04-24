package me.trambled.ozark.ozarkclient.misc;

import java.util.Timer;
import java.util.TimerTask;

import me.trambled.ozark.ozarkclient.module.Module;

public class AntiAFK extends Module
{
    public AntiAFK() {
        super(Category.MISC);

        this.name = "Anti AFK";
        this.tag = "AntiAFK";
        this.description = "prevents being kicked for afk-ing";
    }

    private Timer timer = new Timer();

    @Override
    public void onEnable() {
        super.onEnable();

        if (mc.player == null)
        {
            toggle();
            return;
        }

        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                mc.player.sendChatMessage("/stats");
            }
        }, 0, 120000);
    }

    @Override
    public void onDisable()
    {
        super.onDisable();

        if (timer != null)
            timer.cancel();
    }
}
