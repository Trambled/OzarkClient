package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Module;

public class SmallHands extends Module {
    
    public SmallHands() {
        super(Category.RENDER);
        
        this.name = "SmallHands";
        this.tag = "SmallHands";
        this.description = "Makes ur hands smaller.";
    }
    
    Setting offset = create("Offset", "Offset", 90, 0, 360);
    
    @Override
    public void update() {
        mc.player.renderArmPitch = (float)this.offset.get_value(1);
    }
}
