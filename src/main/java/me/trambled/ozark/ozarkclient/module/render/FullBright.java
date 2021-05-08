package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import net.minecraft.init.MobEffects;

public
class FullBright extends Module {

    private float prior_gamma;

    public
    FullBright ( ) {
        super ( Category.RENDER );

        this.name = "Full Bright";
        this.tag = "FullBright";
        this.description = "best hack";
    }

    @Override
    protected
    void enable ( ) {
        prior_gamma = mc.gameSettings.gammaSetting;
    }

    @Override
    protected
    void disable ( ) {
        mc.gameSettings.gammaSetting = prior_gamma;
    }

    @Override
    public
    void update ( ) {
        mc.gameSettings.gammaSetting = 1000;
        mc.player.removePotionEffect ( MobEffects.NIGHT_VISION );
    }
}