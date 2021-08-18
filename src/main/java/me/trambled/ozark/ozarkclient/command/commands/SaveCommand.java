package me.trambled.ozark.ozarkclient.command.commands;


import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.command.Command;



public class SaveCommand extends Command {
    public SaveCommand() {
        super("save", "save config");
    }

    public boolean get_message(String[] message) {
        Ozark.get_config_manager().save_settings();


        return true;
    }}
