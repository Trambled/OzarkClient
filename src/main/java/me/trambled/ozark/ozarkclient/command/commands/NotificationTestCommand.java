package me.trambled.ozark.ozarkclient.command.commands;


import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.manager.NotificationManager;
import me.trambled.ozark.ozarkclient.util.world.TimerUtil;


public class NotificationTestCommand extends Command {
    public NotificationTestCommand() {
        super("notif", "testing purposes");
    }

    public boolean get_message(String[] message) {
        if (message.length == 1) {
            Ozark.get_notification_manager().add_notification(new NotificationManager.Notification("Ozark play 2b2t snine tudou classic 2017 (test)", new TimerUtil()));


        }else if (message.length == 2) {
            Ozark.get_notification_manager().add_notification(new NotificationManager.Notification(message[1], new TimerUtil()));
        }
    return true;
    }}
