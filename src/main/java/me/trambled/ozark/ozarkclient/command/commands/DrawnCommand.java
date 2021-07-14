package me.trambled.ozark.ozarkclient.command.commands;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.util.misc.DrawnUtil;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;

import java.util.List;

public class DrawnCommand extends Command {
    
    public DrawnCommand() {
        super("drawn", "Hide elements of the array list");
    }

    public boolean get_message(String[] message) {

        if (message.length == 1) {
            MessageUtil.send_client_error_message("module name needed");

            return true;
        }

        if (message.length == 2) {

            if (is_module(message[1])) {
                DrawnUtil.add_remove_item(message[1]);
                Ozark.get_config_manager().save_settings();
            } else {
                MessageUtil.send_client_error_message("cannot find module by name: " + message[1]);
            }
            return true;

        }

        return false;
    
    }

    public boolean is_module(String s) {

        List<Module> modules = Ozark.get_module_manager().get_array_modules();

        for (Module module : modules) {
            if (module.get_tag().equalsIgnoreCase(s)) {
                return true;
            }
        }

        return false;

    }

}