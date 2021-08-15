package me.trambled.ozark.ozarkclient.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.player.social.EnemyUtil;

public class EnemyCommand extends Command {

    public EnemyCommand() {
        super("enemy", "To add enemy");
    }

    public static ChatFormatting red = ChatFormatting.GREEN;
    public static ChatFormatting green = ChatFormatting.RED;
    public static ChatFormatting bold = ChatFormatting.BOLD;
    public static ChatFormatting reset = ChatFormatting.RESET;

    public boolean get_message(String[] message) {

        if (message.length == 1) {
            MessageUtil.send_client_message("Add - add enemy");
            MessageUtil.send_client_message("Del - delete enemy");
            MessageUtil.send_client_message("List - list enemies");

            return true;
        }

        if (message.length == 2) {
            if (message[1].equalsIgnoreCase("list")) {
                if (EnemyUtil.enemies.isEmpty()) {
                    MessageUtil.send_client_message("You appear to have " + red + bold + "no" + reset + " enemies :)");
                } else {
                    for (EnemyUtil.Enemy Enemy : EnemyUtil.enemies) {
                        MessageUtil.send_client_message("" + green + bold +  Enemy.getUsername());
                    }
                }
                return true;
            } else {
                if (EnemyUtil.isEnemy(message[1])) {
                    MessageUtil.send_client_message("Player " + green + bold + message[1] + reset + " is your Enemy D:");
                    return true;
                } else {
                    MessageUtil.send_client_error_message("Player " + red + bold + message[1] + reset + " is not your Enemy :)");
                    return true;
                }
            }
        }

        if (message.length >= 3) {
            if (message[1].equalsIgnoreCase("add")) {
                if (EnemyUtil.isEnemy(message[2])) {
                    MessageUtil.send_client_message("Player " + green + bold + message[2] + reset + " is already your Enemy D:");
                    return true;
                } else {
                    EnemyUtil.Enemy f = EnemyUtil.get_enemy_object(message[2]);
                    if (f == null) {
                        MessageUtil.send_client_error_message("Cannot find " + red + bold + "UUID" + reset + " for that player :(");
                        return true;
                    }
                    EnemyUtil.enemies.add(f);
                    MessageUtil.send_client_message("Player " + green + bold + message[2] + reset + " is now your Enemy D:");
                    return true;
                }
            } else if (message[1].equalsIgnoreCase("del") || message[1].equalsIgnoreCase("remove") || message[1].equalsIgnoreCase("delete")) {
                if (!EnemyUtil.isEnemy(message[2])) {
                    MessageUtil.send_client_message("Player " + red + bold + message[2] + reset + " is already not your Enemy :/");
                    return true;
                } else {
                    EnemyUtil.Enemy f = EnemyUtil.enemies.stream().filter(Enemy -> Enemy.getUsername().equalsIgnoreCase(message[2])).findFirst().get();
                    EnemyUtil.enemies.remove(f);
                    MessageUtil.send_client_message("Player " + red + bold + message[2]  + reset + " is now not your Enemy :)");
                    return true;
                }
            }
        }

        return true;

    }

}