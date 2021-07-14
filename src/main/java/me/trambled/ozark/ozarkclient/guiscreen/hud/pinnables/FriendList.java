package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;
import me.trambled.ozark.ozarkclient.util.player.social.OnlineFriendsUtil;
import net.minecraft.entity.Entity;

public class FriendList extends Pinnable {
    
    public FriendList() {
        super("Friends", "Friends", 1, 0, 0);
    }

    int passes;

    public static ChatFormatting bold = ChatFormatting.BOLD;

    @Override
	public void render() {
		int nl_r = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
		int nl_g = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
        int nl_b = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);
        int nl_a = Ozark.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(1);

        String line1 = bold + "the trollas:";
        
        passes = 0;

        create_line(line1, this.docking(1, line1), 2, nl_r, nl_g, nl_b, nl_a);
        
        if (!OnlineFriendsUtil.getFriends().isEmpty()) {
            for (Entity e : OnlineFriendsUtil.getFriends()) {
                passes++;
                create_line(e.getName(), this.docking(1, e.getName()), this.get(line1, "height")*passes, nl_r, nl_g, nl_b, nl_a);
            }
        }

		this.set_width(this.get(line1, "width") + 2);
		this.set_height(this.get(line1, "height") + 2);
	}

}
