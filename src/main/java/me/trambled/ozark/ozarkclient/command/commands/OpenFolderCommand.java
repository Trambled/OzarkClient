package me.trambled.ozark.ozarkclient.command.commands;


import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;


public class OpenFolderCommand extends Command {
    public OpenFolderCommand() {
        super("folder", "open ozark folder");
    }

    public boolean get_message(String[] message) {
        try {
            Desktop.getDesktop().open(new File(Ozark.get_config_manager().FILE_DIRECTORY.getPath()));
           MessageUtil.send_client_message("Opened config folder!");
        } catch (IOException e) {
            MessageUtil.send_client_message("Could not open config folder!");
            e.printStackTrace();
        return true;}return true;}}