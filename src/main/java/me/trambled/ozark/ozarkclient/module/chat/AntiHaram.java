package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public
class AntiHaram extends Module {

    Setting delay = create ( "Delay" , "Delay" , 10 , 0 , 100 );
    List < String > chants = new ArrayList <> ( );
    Random r = new Random ( );
    int tick_delay;

    public
    AntiHaram ( ) {
        super ( Category.CHAT );

        this.name = "Anti Haram";
        this.tag = "AntiHaram";
        this.description = "snine19";
    }

    @Override
    protected
    void enable ( ) {

        tick_delay = 0;

        chants.add ( "ALLAHU AKBAR" );
        chants.add ( "HARAM PIGGIES DIE FOR ALLAH" );
        chants.add ( "#FREEPALESTINE" );
        chants.add ( "BISMILLAH AL RAHMAN AL RAHIM" );
        chants.add ( "MUHAMMAD PEACE BE UPON HIM" );
        chants.add ( "I PRAY TO ALLAH 5 TIMES A DAY" );
        chants.add ( "DEATH TO STINKY HARAM INFIDELS" );
        chants.add ( "PORK = HARAM" );

    }

    @Override
    public
    void update ( ) {

        tick_delay++;
        if ( tick_delay < delay.get_value ( 1 ) * 10 ) return;

        String s = chants.get ( r.nextInt ( chants.size ( ) ) );
        String name = get_random_name ( );

        if ( name.equals ( mc.player.getName ( ) ) ) return;

        mc.player.sendChatMessage ( s.replace ( "<player>" , name ) );

        tick_delay = 0;

    }

    public
    String get_random_name ( ) {

        List < EntityPlayer > players = mc.world.playerEntities;

        return players.get ( r.nextInt ( players.size ( ) ) ).getName ( );

    }


}
