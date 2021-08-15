package me.trambled.ozark.ozarkclient.module.misc;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.SoundUtil;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class HitSound extends Module {

    public HitSound() {
        super(Category.MISC);

        this.name = "HitSound";
        this.tag = "HitSound";
        this.description = "CSGO client's hitsound";
    }
    Setting sound = create("Sound", "Sound", "skeet", combobox("skeet", "neverlose"));

    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent event) {
        if (!event.getEntity().equals( mc.player )) {
            return;
        }
        final String value = this.sound.get_current_value();
        switch (value) {
            case "neverlose": {
                SoundUtil.playSound(SoundUtil.INSTANCE.neverlose);
                break;
            }
            case "skeet": {
                SoundUtil.playSound(SoundUtil.INSTANCE.skeet);
                break;
            }
        }
    }
}

