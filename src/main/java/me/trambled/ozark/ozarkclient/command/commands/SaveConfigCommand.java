package me.trambled.ozark.ozarkclient.command.commands;


import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.util.MessageUtil;


public class SaveConfigCommand extends Command {
    public SaveConfigCommand() {
        super("save", "save ur bad config");
    }

    public boolean get_message(String[] message) {
      Ozark.get_config_manager().save_settings();
        MessageUtil.send_client_message("Config saved!");
    return true;}}