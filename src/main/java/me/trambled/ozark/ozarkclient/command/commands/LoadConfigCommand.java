package me.trambled.ozark.ozarkclient.command.commands;


import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.manager.NotificationManager;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import me.trambled.ozark.ozarkclient.util.TimerUtil;


public class LoadConfigCommand extends Command {
    public LoadConfigCommand() {
        super("load", "load ur config");
    }

    public boolean get_message(String[] message) {
        Ozark.get_config_manager().load_settings();
        MessageUtil.send_client_message("Config loaded!");
        return true;}}