package me.travis.wurstplus.wurstplustwo.command.commands;

import me.travis.wurstplus.wurstplustwo.command.WurstplusCommand;
import me.travis.wurstplus.wurstplustwo.command.WurstplusCommands;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import net.minecraft.client.Minecraft;

public class Server extends WurstplusCommand {

        private static final Minecraft mc;

        public String serverip;

	public Server() {
		super("ip", "shows server ip");
	}

	public boolean get_message(String[] message) {
		String type = "null";
                
                serverip = mc.getCurrentServerData().serverIP;

                WurstplusMessageUtil.send_client_message("You are playing on " + serverip);

                return true;
	}

    static {
        mc = Minecraft.getMinecraft();
    }
}