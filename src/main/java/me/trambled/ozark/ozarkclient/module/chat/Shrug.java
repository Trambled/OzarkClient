package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;

public class Shrug extends Module {

    public Shrug() {
        super(Category.CHAT);
      
        this.name = "Shrug";
        this.tag = "Shrug";
        this.description = "¯\_(ツ)_/¯";
      
    }
  
    @Override
    protected void enable() {
        mc.player.sendChatMessage("¯\_(ツ)_/¯");
        this.toggle();
    }
}
