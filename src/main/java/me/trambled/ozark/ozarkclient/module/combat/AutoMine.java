package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.BreakUtil;
import me.trambled.ozark.ozarkclient.util.EntityUtil;
import me.trambled.ozark.ozarkclient.util.FriendUtil;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public
class AutoMine extends Module {

    Setting end_crystal = create ( "End Crystal" , "MineEndCrystal" , false );
    Setting range = create ( "Range" , "MineRange" , 4 , 0 , 6 );
    public
    AutoMine ( ) {
        super ( Category.COMBAT );

        this.name = "Auto City";
        this.tag = "CityBoss";
        this.description = "mines out player's surround";
    }

    @Override
    protected
    void enable ( ) {

        BlockPos target_block = null;

        for (EntityPlayer player : mc.world.playerEntities) {
            if ( mc.player.getDistance ( player ) > range.get_value ( 1 ) ) continue;

            if ( FriendUtil.isFriend ( player.getName ( ) ) ) continue;

            BlockPos p = EntityUtil.is_cityable ( player , end_crystal.get_value ( true ) );

            if ( p != null ) {
                target_block = p;
            }
        }

        if ( target_block == null ) {
            MessageUtil.send_client_message ( "cannot find block" );
            this.set_disable ( );
        }

        BreakUtil.set_current_block ( target_block );

    }

    @Override
    protected
    void disable ( ) {
        BreakUtil.set_current_block ( null );
    }
}
