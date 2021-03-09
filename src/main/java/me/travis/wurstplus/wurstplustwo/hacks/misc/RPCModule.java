package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.RPC;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;

public class RPCModule extends WurstplusHack
{
    public RPCModule() {
        super(WurstplusCategory.WURSTPLUS_MISC);
        this.name = "DiscordRPC";
        this.tag = "DiscordRPC";
        this.description = "show people how cool you are (discord edition)";
    }

    //lempity why
    WurstplusSetting mode = create("Mode", "RPCMode", "Normal", combobox("Normal", "Lempity"));
    
    @Override
    protected void enable() {
        RPC.init();
    }

    @Override
    protected void disable() {
        RPC.stop();
    }	
	
}
