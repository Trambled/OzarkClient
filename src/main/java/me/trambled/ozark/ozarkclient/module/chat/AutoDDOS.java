package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.util.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import net.minecraft.client.Minecraft;

public class AutoDDOS extends Module {
    public AutoDDOS() {
        super(Category.CHAT);
        
        this.name        = "Auto DDOS";
	this.tag         = "AutoDDOS";
	this.description = "hacker pro";

    }
    
    @Override 
    protected void enable() {
        MessageUtil.send_client_message("Successfully pinged IP address [REDACTED] with 32 bytes of data.");
	MessageUtil.send_client_message("");  
	MessageUtil.send_client_message("ass larp");    
	MessageUtil.send_client_message("Yo i think we ddos the wrong person");   
	MessageUtil.send_client_message(mc.player.getName + "is this ign urs?");
	MessageUtil.send_client_message("Error: Ping is 1827, closing Minecraft");
	set_disable();
    }		    
}
