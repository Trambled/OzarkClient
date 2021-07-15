package me.trambled.ozark.ozarkclient.util.player.social;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import static me.trambled.ozark.ozarkclient.util.player.social.FriendSyncUtil.addFriend;
import static me.trambled.ozark.ozarkclient.util.player.social.FriendSyncUtil.delFriend;

public class FriendSyncCommand extends CommandBase {
    @Override
    public String getName() {
        return "fsync";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "fsync <add | del | sync> <user> {alias}";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        switch (args[0]){
            case "add":{
                if(args.length == 3){
                    if(addFriend(args[1],args[2],null)){
                        sender.sendMessage(new TextComponentString("Player " + args[1] + " has been added to your friends list"));
                    }else{
                        sender.sendMessage(new TextComponentString("Player " + args[1] + " already exists on your friends list"));
                    }
                }else if(args.length == 2){
                    if(addFriend(args[1],args[1],null)){
                        sender.sendMessage(new TextComponentString("Player " + args[1] + " has been added to your friends list"));
                    }else{
                        sender.sendMessage(new TextComponentString("Player " + args[1] + " already exists on your friends list"));
                    }
                }else{
                    sender.sendMessage(new TextComponentString("Wrong usage, correct syntax: fsync <add | del | sync> <user>"));
                    break;
                }
            }

            case "del":{
                if(args.length == 2){
                    if(delFriend(args[1])){
                        sender.sendMessage(new TextComponentString("Player " + args[1] + " has been deleted from your friends list"));
                    }else{
                        sender.sendMessage(new TextComponentString("Player " + args[1] + " doesn't exist on your friends list"));
                    }
                }
                break;
            }

            case "sync":{
                FriendSync.read();
                FriendSync.write();
                sender.sendMessage(new TextComponentString("Friend lists have been synced"));
                break;
            }

            default:{
                sender.sendMessage(new TextComponentString("Wrong usage, correct syntax: fsync <add | del | sync> <user>"));
            }
        }

    }
}
