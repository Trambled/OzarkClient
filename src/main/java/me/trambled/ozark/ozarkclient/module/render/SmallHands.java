package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Module;

public class SmallHands extends Module {
    
    public SmallHands() {
        super(Category.RENDER);
        
        this.name = "Small Hand";
        this.tag = "SmallHand";
        this.description = "Small hand in game.";
    }
    
    Setting offset = create("Offset", "Offset", 90, 0, 360);
    
    @Override
    public void update() {
        SmallHand.mc.field_71439_g.field_71155_g = (float)this.offset.get_value(1);
    }
}
