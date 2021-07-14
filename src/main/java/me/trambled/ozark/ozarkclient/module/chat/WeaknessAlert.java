package me.trambled.ozark.ozarkclient.module.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import net.minecraft.init.MobEffects;

//bloodhack
public class WeaknessAlert extends Module {

    public WeaknessAlert() {
        super(Category.CHAT);

        this.name = "WeaknessAlert";
        this.tag = "WeaknessAlert";
        this.description = "Weakness alert more like fag alert.";
    }
    public static ChatFormatting red = ChatFormatting.DARK_RED;
    public static ChatFormatting green = ChatFormatting.GREEN;

    private boolean hasAnnounced = false;
 
    @Override
    public void update() {
        if (mc.world != null && mc.player != null) {
            if (mc.player.isPotionActive(MobEffects.WEAKNESS) && !hasAnnounced) {
                hasAnnounced = true;
                MessageUtil.send_client_message( red + "You have weakness!");
            }
            if (!mc.player.isPotionActive(MobEffects.WEAKNESS) && hasAnnounced) {
                hasAnnounced = false;
                MessageUtil.send_client_message( green + "No more weakness!");
            }
        }
    }
}
