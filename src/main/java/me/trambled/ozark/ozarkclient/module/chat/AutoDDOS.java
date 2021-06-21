package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.util.MessageUtil;

public class AutoDDOS extends Module {
    public AutoDDOS() {
        super(Category.CHAT);
        
        this.name        = "AutoDDOS";
	this.tag         = "AutoDDOS";
	this.description = "Hacker pro.";

    }
    
    @Override 
    protected void enable() {
        MessageUtil.send_client_message("Successfully pinged IP address [REDACTED] with 32 bytes of data.");
    }

    @Override 
    public void update() {
        MessageUtil.send_client_message("");
    }
	
}
