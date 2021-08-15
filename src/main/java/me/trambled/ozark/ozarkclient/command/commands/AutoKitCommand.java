package me.trambled.ozark.ozarkclient.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.player.AutoKitUtil;

public class AutoKitCommand extends Command {

    public AutoKitCommand() {
        super("autokit", "chooses a custom kit");
    }

    public boolean get_message(String[] message) {

        if (message.length == 1) {
            MessageUtil.send_client_error_message("message needed");
            return true;
        }

        if (message.length >= 2) {
            StringBuilder kit = new StringBuilder();
            boolean flag = true;
            for (String word : message) {
                if (flag) {
                    flag = false;
                    continue;
                }
                kit.append(word).append(" ");
            }
            AutoKitUtil.set_message(kit.toString());
            MessageUtil.send_client_message("kit changed to " + ChatFormatting.BOLD + kit );
            Ozark.get_config_manager().save_settings();
            return true;
        }

        return false;

    }

}
