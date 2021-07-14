package me.trambled.ozark.ozarkclient.command.commands;

import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.module.render.Xray;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import net.minecraft.block.Block;

public class XrayCommand extends Command
{
    public XrayCommand() {
        super("xray", "add or delete blocks from xray");
    }

    public boolean get_message(final String[] message) {
        if (message[1].equalsIgnoreCase("help")) {
            MessageUtil.send_client_message("<add/del/list>");
            return true;
        }
        if (message.length == 1) {
            MessageUtil.send_client_error_message("Specify an option. Try doing .xray help to see command options");
            return true;
        } else {
            if (message[1].equalsIgnoreCase("add")) {
                if (message.length < 3) {
                    MessageUtil.send_client_error_message("Please specify a block.");
                } else {
                    if (Xray.addBlock(message[2])) {
                        MessageUtil.send_client_message("Added " + message[2] + " to XRAY!");
                    }
                    else {
                        MessageUtil.send_client_error_message("Unknown block!");
                    }
                }
            }
            else if (message[1].equalsIgnoreCase("remove")) {
                if (message.length < 3) {
                    MessageUtil.send_client_error_message("Please specify a block.");
                } else {
                    if (Xray.delBlock(message[2])) {
                        MessageUtil.send_client_message("Removed " + message[2] + " from XRAY!");
                    } else {
                        MessageUtil.send_client_error_message("Unknown block!");
                    }
                }
            }
            else if (message[1].equalsIgnoreCase("list")) {
                MessageUtil.send_client_message("Xray blocks &7(" + Xray.getBLOCKS().size() + ")&r: ");
                String out = "";
                boolean start = true;
                for (final Block b : Xray.getBLOCKS()) {
                    if (start) {
                        out = b.getLocalizedName();
                    }
                    else {
                        out = out + ", " + b.getLocalizedName();
                    }
                    start = false;
                }
                MessageUtil.send_client_message(out);
            }
            else {
                MessageUtil.send_client_error_message("Unknown arguments! (do .xray help)");
            }
            return true;
        }
    }
}
