package me.trambled.ozark.ozarkclient.command.commands;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;

public class ConfigCommand extends Command {

    public ConfigCommand() {
        super("config", "changes which config is loaded");
    }

    public boolean get_message(String[] message) {

        if (message.length == 1) {
            MessageUtil.send_client_error_message("config needed");
            return true;
        } else if (message.length == 2) {
            String config = message[1];
            if (Ozark.get_config_manager().set_active_config_folder(config+"/")) {
                MessageUtil.send_client_message("new config folder set as " + config);
            } else {
                MessageUtil.send_client_error_message("cannot set folder to " + config);
            }
            return true;
        } else {
            MessageUtil.send_client_error_message("config path may only be one word");
            return true;
        }
    }

}
