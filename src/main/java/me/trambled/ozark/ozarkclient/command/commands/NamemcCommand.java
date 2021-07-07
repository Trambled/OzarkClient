package me.trambled.ozark.ozarkclient.command.commands;


import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.manager.NotificationManager;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import me.trambled.ozark.ozarkclient.util.TimerUtil;

import java.awt.*;
import java.net.URI;

//kambing
public class NamemcCommand extends Command {
    public NamemcCommand() {
        super("namemc", "to search whos this nn");
    }

    public boolean get_message(String[] message) {
        if (message.length == 1) {
MessageUtil.send_client_message("Nigga you forgot the name (expected since ur fkin dumb)");
        }else if (message.length == 2) {
            MessageUtil.send_client_message("Sending you to namemc.com/profile/" + message[1]);
            try {
                Desktop.getDesktop().browse(URI.create("https://namemc.com/profile/" + message[1]));
            } catch (Exception ex) {
            }

        return true;
    }return true;}}