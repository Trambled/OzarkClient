package me.travis.wurstplus.wurstplustwo.hacks.chat;

import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.init.MobEffects;

//bloodhack
public class WeaknessAlert extends WurstplusHack {

    public WeaknessAlert() {
        super(WurstplusCategory.WURSTPLUS_CHAT);

        this.name = "Weakness Alert";
        this.tag = "WeaknessAlert";
        this.description = "gives you weakness alerts";
    }

    private boolean hasAnnounced = false;
 
    @Override
    public void update() {
        if (mc.world != null && mc.player != null) {
            if (mc.player.isPotionActive(MobEffects.WEAKNESS) && !hasAnnounced) {
                hasAnnounced = true;
                WurstplusMessageUtil.send_client_message("You currently have weakness! damn weakness fags");
            }
            if (!mc.player.isPotionActive(MobEffects.WEAKNESS) && hasAnnounced) {
                hasAnnounced = false;
                WurstplusMessageUtil.send_client_message("You lost your weakness!");
            }
        }
    }
}
