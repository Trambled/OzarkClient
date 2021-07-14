package me.trambled.ozark.ozarkclient.command.commands;


import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.manager.CommandManager;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;


public class PrefixCommand extends Command {
	public PrefixCommand() {
		super("prefix", "Change prefix.");
	}

	public boolean get_message(String[] message) {
		String prefix = "null";

		if (message.length > 1) {
			prefix = message[1];
		}

		if (message.length > 2) {
			MessageUtil.send_client_error_message(current_prefix() + "prefix <character>");

			return true;
		}

		if (prefix.equals("null")) {
			MessageUtil.send_client_error_message(current_prefix() + "prefix <character>");

			return true;
		}

		CommandManager.set_prefix(prefix);

		MessageUtil.send_client_message("The new prefix is " + prefix);

		return true;
	}
}