package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public
class ChatSpammer extends Module {

    Setting delay = create ( "Delay" , "SpammerDelay" , 10 , 0 , 100 );
    List < String > chants = new ArrayList <> ( );
    Random r = new Random ( );
    int tick_delay;

    public
    ChatSpammer ( ) {
        super ( Category.CHAT );

        this.name = "Chat Spammer";
        this.tag = "ChatSpammer";
        this.description = "be annoying";
    }

    @Override
    protected
    void enable ( ) {

        tick_delay = 0;

        chants.add ( "I LOVE OZARK CLIENT" );
        chants.add ( "OZARK OWNS ME" );
        chants.add ( "CANT GET ENOUGH OF THAT OZARK CLIENT" );
        chants.add ( "OZARK PICKS MY COTTON FOR ME" );
        chants.add ( "OZARK CLIENT OWNS ME AND ALL" );
        chants.add ( "CURBSTOMPING CHILDREN WITH OZARK CLIENT" );
        chants.add ( "NIGGERS BEG IN MY DM'S FOR OZARK CLIENT" );
        chants.add ( "I BET YOU WISH YOU HAD OZARK CLIENT" );
        chants.add ( "OZARK USERS NEVER DIE" );
        chants.add ( "sn0w client more like TRASH" );
        chants.add ( "I KILLED FITMC WITH THE POWER OF OZARK CLIENT" );
        chants.add ( "YOU CANT BEAT OZARK CLIENT" );

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
