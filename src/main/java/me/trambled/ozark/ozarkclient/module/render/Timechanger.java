package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

public
class Timechanger extends Module {

    Setting time = create ( "Time" , "Time" , 18000 , 0 , 24000 );
    @EventHandler
    private final Listener < EventRender > on_render = new Listener <> ( event -> {
        if ( mc.world == null ) return;
        mc.world.setWorldTime ( time.get_value ( 1 ) );
    } );

    public
    Timechanger ( ) {
        super ( Category.RENDER );

        this.name = "Time Changer";
        this.tag = "TimeChanger";
        this.description = "changes time";
    }

    @Override
    public
    void update ( ) {
        if ( mc.world == null ) return;
        mc.world.setWorldTime ( time.get_value ( 1 ) );
    }
}
