package me.trambled.ozark.ozarkclient.command.commands;

import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;

import java.lang.management.ManagementFactory;

//kambing
public class ClearRamCommand extends Command {
    private static final long xd = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize() /1024/1024/1024;
    public ClearRamCommand() {
        super("ram",  "clear ur ram");
    }

    public boolean get_message(String[] message) {
        MessageUtil.send_client_message("You have " + xd + "GB of ram (buy more ram pooron)");
        MessageUtil.send_client_message("Clearing ram...");
        System.gc();
        MessageUtil.send_client_message("Ram Cleared!");
        return true;
    }}