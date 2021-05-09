package me.trambled.ozark.ozarkclient.module.misc;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.DiscordUtil;

public class DiscordRPC extends Module
{
    public DiscordRPC() {
        super(Category.MISC);
        this.name = "DiscordRPC";
        this.tag = "DiscordRPC";
        this.description = "show people how cool you are (discord edition)";
    }

    Setting mode = create("Mode", "RPCMode", "Normal", combobox("Normal", "Lempity", "kambing"));
    
    @Override
    protected void enable() {
        DiscordUtil.init();
    }

    @Override
    protected void disable() {
        DiscordUtil.stop();
    }
}
