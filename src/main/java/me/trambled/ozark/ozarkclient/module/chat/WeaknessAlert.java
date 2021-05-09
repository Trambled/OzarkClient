package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.util.MessageUtil;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import net.minecraft.init.MobEffects;

//bloodhack
public class WeaknessAlert extends Module {

    public WeaknessAlert() {
        super(Category.CHAT);

        this.name = "Weakness Alert";
        this.tag = "WeaknessAlert";
        this.description = "weakness alert more like fag alert";
    }

    private boolean hasAnnounced = false;
 
    @Override
    public void update() {
        if (mc.world != null && mc.player != null) {
            if (mc.player.isPotionActive(MobEffects.WEAKNESS) && !hasAnnounced) {
                hasAnnounced = true;
                MessageUtil.send_client_message("You have weakness!");
            }
            if (!mc.player.isPotionActive(MobEffects.WEAKNESS) && hasAnnounced) {
                hasAnnounced = false;
                MessageUtil.send_client_message("No more weakness!");
            }
        }
    }
}
