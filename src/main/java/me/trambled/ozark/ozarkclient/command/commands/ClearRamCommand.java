package me.trambled.ozark.ozarkclient.command.commands;

import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import org.lwjgl.Sys;

import java.lang.management.ManagementFactory;

//kambing
public class ClearRamCommand extends Command {
    long memorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
    public ClearRamCommand() {
        super("ram",  "clear ur ram");
    }

    public boolean get_message(String[] message) {
        MessageUtil.send_client_message("You have " + memorySize + " ram (buy more ram pooron)");
        MessageUtil.send_client_message("Clearing ram...");
        System.gc();
        MessageUtil.send_client_message("Ram Cleared!");
        return true;
    }}