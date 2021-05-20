package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

public final class RainbowChat extends Module {

    public RainbowChat() {
        super(Category.CHAT);

        this.name = "RainbowChat";
        this.tag = "RainbowChat";
        this.description = "chat opener is rainbow";
    }

    Setting custom_font = create("Custom Font", "RainbowChatCustomFont", true);

}