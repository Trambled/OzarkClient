package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

// full credit goes to llamalad7
public final class BetterChat extends Module {

    public BetterChat() {
        super(Category.CHAT);

        this.name = "BetterChat";
        this.tag = "BetterChat";
        this.description = "a good chat mod made by llamalad7";
    }

    Setting smooth = create("Smooth", "Smooth", true);
    Setting clear = create("Clear", "Clear", true);

}
