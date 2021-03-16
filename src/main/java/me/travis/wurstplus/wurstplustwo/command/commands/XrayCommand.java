package me.travis.wurstplus.wurstplustwo.command.commands;

import me.travis.wurstplus.wurstplustwo.command.WurstplusCommand;
import me.travis.wurstplus.wurstplustwo.hacks.render.Xray;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import net.minecraft.block.Block;

public class XrayCommand extends WurstplusCommand
{
    public XrayCommand() {
        super("xray", "add or delete blocks from xray");
    }

    public boolean get_message(final String[] message) {
        if (message[1].equalsIgnoreCase("help")) {
            WurstplusMessageUtil.send_client_message("<add/del/list>");
            return true;
        }
        if (message.length == 1) {
            WurstplusMessageUtil.send_client_message("Specify an option. Try doing .xray help to see command options");
            return true;
        } else {
            if (message[1].equalsIgnoreCase("add")) {
                if (message.length < 3) {
                    WurstplusMessageUtil.send_client_message("Please specify a block.");
                } else {
                    if (Xray.addBlock(message[2])) {
                        WurstplusMessageUtil.send_client_message("Added " + message[2] + " to XRAY!");
                    }
                    else {
                        WurstplusMessageUtil.send_client_message("Unknown block!");
                    }
                }
            }
            else if (message[1].equalsIgnoreCase("remove")) {
                if (message.length < 3) {
                    WurstplusMessageUtil.send_client_message("Please specify a block.");
                } else {
                    if (Xray.delBlock(message[2])) {
                        WurstplusMessageUtil.send_client_message("Removed " + message[2] + " from XRAY!");
                    } else {
                        WurstplusMessageUtil.send_client_message("Unknown block!");
                    }
                }
            }
            else if (message[1].equalsIgnoreCase("list")) {
                WurstplusMessageUtil.send_client_message("Xray blocks &7(" + Xray.getBLOCKS().size() + ")&r: ");
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
                WurstplusMessageUtil.send_client_message(out);
            }
            else {
                WurstplusMessageUtil.send_client_message("Unknown arguments! (do .xray help)");
            }
            return true;
        }
    }
}
