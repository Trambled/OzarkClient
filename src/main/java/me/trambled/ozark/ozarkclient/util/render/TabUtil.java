package me.trambled.ozark.ozarkclient.util.render;

import me.trambled.ozark.ozarkclient.util.player.social.EnemyUtil;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;

public class TabUtil {

    public static String get_player_name(final NetworkPlayerInfo info) {
        final String name = (info.getDisplayName() != null) ? info.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(info.getPlayerTeam(),
                info.getGameProfile().getName());
        if (FriendUtil.isFriend(name)) {
            return section_sign() + "6" + name;
        }
        if (EnemyUtil.isEnemy(name)) {
            return section_sign() + "c" + name;
        }
        return name;
    }

    public static String section_sign() {
        return "\u00A7";
    }

}
