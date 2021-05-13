package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;

public class AutoSuicide extends Module {

    public AutoSuicide() {
        super(Category.CHAT);
      
        this.name = "AutoSuicide";
        this.tag = "AutoSuicide";
        this.description = "allahu akbar";
      
    }
  
    @Override
    protected void enable() {
        mc.player.sendChatMessage("/kill");
        this.toggle();
    }
}

