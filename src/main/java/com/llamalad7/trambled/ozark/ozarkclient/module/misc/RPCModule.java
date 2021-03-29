package me.trambled.ozark.ozarkclient.module.misc;

import me.trambled.ozark.RPC;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

public class RPCModule extends Module
{
    public RPCModule() {
        super(Category.MISC);
        this.name = "DiscordRPC";
        this.tag = "DiscordRPC";
        this.description = "show people how cool you are (discord edition)";
    }

    //lempity why
    Setting mode = create("Mode", "RPCMode", "Normal", combobox("Normal", "Lempity"));
    
    @Override
    protected void enable() {
        RPC.init();
    }

    @Override
    protected void disable() {
        RPC.stop();
    }	
	
}
