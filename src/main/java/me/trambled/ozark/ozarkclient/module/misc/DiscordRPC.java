package me.trambled.ozark.ozarkclient.module.misc;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.DiscordUtil;

public class DiscordRPC extends Module
{
    public DiscordRPC() {
        super(Category.MISC);
        this.name = "DiscordRPC";
        this.tag = "DiscordRPC";
        this.description = "Show people how cool you are (discord edition).";
    }

    Setting large_image = create("Large Image", "RPCMode", "Normal", combobox("Normal", "Lempity", "kambing", "Tudou"));
    Setting small_image = create("Small Image", "RPCSmallImage", "Server", combobox("Server", "Tudou"));
    Setting state = create("State", "RPCState", "Health", combobox("Server", "Health", "Speed", "Target", "User"));
    Setting details = create("Details", "RPCDetails", "Server", combobox("Server", "Health", "Speed", "Target", "User"));



    @Override
    protected void enable() {
        DiscordUtil.init();
    }

    @Override
    protected void disable() {
        DiscordUtil.stop();
    }
}
