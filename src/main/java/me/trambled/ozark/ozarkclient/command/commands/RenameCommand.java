package me.trambled.ozark.ozarkclient.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;

public class RenameCommand extends Command {

    public RenameCommand() {
        super("rename", "renames the client");
    }

    public boolean get_message(String[] message) {

        if (message.length == 1) {
            MessageUtil.send_client_error_message("message needed");
            return true;
        }

        if (message.length >= 2) {
            StringBuilder name = new StringBuilder();
            boolean flag = true;
            for (String word : message) {
                if (flag) {
                    flag = false;
                    continue;
                }
                name.append(word).append(" ");
            }
            String da_name = name.toString();
            da_name = da_name.replace(" ", "");
            Ozark.DISPLAY_NAME = da_name;
            MessageUtil.send_client_message("Display name changed to " + ChatFormatting.BOLD + name );
            Ozark.get_config_manager().save_settings();
            return true;
        }

        return false;

    }

}
