package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

public
class SmallHands extends Module {

    Setting offset = create ( "Offset" , "Offset" , 90 , 0 , 360 );

    public
    SmallHands ( ) {
        super ( Category.RENDER );

        this.name = "Small Hands";
        this.tag = "SmallHands";
        this.description = "makes ur hands smaller";
    }

    @Override
    public
    void update ( ) {
        mc.player.renderArmPitch = (float) this.offset.get_value ( 1 );
    }
}
