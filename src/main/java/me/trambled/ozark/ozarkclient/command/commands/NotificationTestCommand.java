package me.trambled.ozark.ozarkclient.command.commands;


import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.manager.NotificationManager;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import me.trambled.ozark.ozarkclient.util.TimerUtil;


public class NotificationTestCommand extends Command {
    public NotificationTestCommand() {
        super("notif", "testing purposes");
    }

    public boolean get_message(String[] message) {
        Ozark.get_notification_manager().add_notification(new NotificationManager.Notification("Ozark play 2b2t snine tudou classic 2017 (test)", new TimerUtil()));
    return true;
    }}