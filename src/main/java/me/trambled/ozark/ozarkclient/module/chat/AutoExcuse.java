package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.event.EventHandler;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;

import java.util.Objects;

public class AutoExcuse extends Module {
    int diedTime;

    public AutoExcuse() {
        super(Category.CHAT);
        this.diedTime = 0;
        this.name = "AutoExcuse";
        this.tag = "AutoExcuse";
        this.description = "Tell people why you died.";
    }

    @Override
    public void update() {
        if (this.diedTime > 0) {
            --this.diedTime;
        }
        if (AutoExcuse.mc.player.isDead) {
            this.diedTime = 500;
        }
        if (!AutoExcuse.mc.player.isDead && this.diedTime > 0) {
            AutoExcuse.mc.player.sendChatMessage("I died due to the server was processing a latency of " + get_ping() + "ms and a tick of" + tps() + " per second. Therefore, it made my AutoTotem module fails.");
            this.diedTime = 0;
        }
    }

    public int get_ping() {
        try {
            return ( Objects.requireNonNull ( mc.getConnection ( ) ).getPlayerInfo(mc.player.getUniqueID()).getResponseTime() );
        } catch (Exception e) {
            return 0;
        }
    }

    public int tps() {
        try {
            return ( Math.round(EventHandler.INSTANCE.get_tick_rate()) );
        } catch (Exception e) {
            return 0;
        }
    }
}
