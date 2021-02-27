package me.travis.wurstplus.wurstplustwo.command.commands;

import me.travis.wurstplus.wurstplustwo.command.WurstplusCommand;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;

public class Server extends WurstplusCommand {


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
}