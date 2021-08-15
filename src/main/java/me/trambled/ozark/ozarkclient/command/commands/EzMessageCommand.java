package me.trambled.ozark.ozarkclient.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.util.misc.EzMessageUtil;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;

public class EzMessageCommand extends Command {

    public EzMessageCommand() {
        super("ezmessage", "Set ez mode");
    }

    public boolean get_message(String[] message) {

        if (message.length == 1) {
            MessageUtil.send_client_error_message("message needed");
            return true;
        }

        if (message.length >= 2) {
            StringBuilder ez = new StringBuilder();
            boolean flag = true;
            for (String word : message) {
                if (flag) {
                    flag = false;
                    continue;
                }
                ez.append(word).append(" ");
            }
            EzMessageUtil.set_message(ez.toString());
            MessageUtil.send_client_message("ez message changed to " + ChatFormatting.BOLD + ez );
            Ozark.get_config_manager().save_settings();
            return true;
        }

        return false;

    }

}
