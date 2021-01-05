package me.travis.wurstplus.wurstplustwo.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.command.WurstplusCommand;
import me.travis.wurstplus.wurstplustwo.util.AutoKitUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;

public class AutoKit extends WurstplusCommand {

    public AutoKit() {
        super("autokit", "chooses a custom kit");
    }

    public boolean get_message(String[] message) {

        if (message.length == 1) {
            WurstplusMessageUtil.send_client_error_message("message needed");
            return true;
        }

        if (message.length >= 2) {
            StringBuilder kit = new StringBuilder();
            boolean flag = true;
            for (String word : message) {
                if (flag) {
                    flag = false;
                    continue;
                }
                kit.append(word).append(" ");
            }
            AutoKitUtil.set_message(kit.toString());
            WurstplusMessageUtil.send_client_message("kit changed to " + ChatFormatting.BOLD + kit.toString());
            Wurstplus.get_config_manager().save_settings();
            return true;
        }

        return false;

    }

}
