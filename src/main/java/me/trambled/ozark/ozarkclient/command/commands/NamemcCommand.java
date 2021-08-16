package me.trambled.ozark.ozarkclient.command.commands;


import me.trambled.ozark.ozarkclient.command.Command;

import java.awt.*;
import java.net.URI;

//kambing
public class NamemcCommand extends Command {
    public NamemcCommand() {
        super("namemc", "to search whos this nn");
    }

    public boolean get_message(String[] message) {
        if (message.length == 1) {
            try {
                Desktop.getDesktop().browse(URI.create("https://namemc.com/profile/" + mc.player.getName()));
            } catch (Exception ignored ) {}
        }else if (message.length == 2) {
            try {
                Desktop.getDesktop().browse(URI.create("https://namemc.com/profile/" + message[1]));
            } catch (Exception ignored ) {}

        return true;
    }return true;}
}
