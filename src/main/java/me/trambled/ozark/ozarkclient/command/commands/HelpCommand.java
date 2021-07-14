package me.trambled.ozark.ozarkclient.command.commands;


import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.command.Commands;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;


public class HelpCommand extends Command {
	public HelpCommand() {
		super("help", "helps people");
	}

	public boolean get_message(String[] message) {
		String type = "null";

		if (message.length == 1) {
			type = "list";
		}

		if (message.length > 1) {
			type = message[1];
		}

		if (message.length > 2) {
			MessageUtil.send_client_error_message(current_prefix() + "help <List/NameCommand>");

			return true;
		}

		if (type.equals("null")) {
			MessageUtil.send_client_error_message(current_prefix() + "help <List/NameCommand>");

			return true;
		}

		if (type.equalsIgnoreCase("list")) {

			for (Command commands : Commands.get_pure_command_list()) {
				MessageUtil.send_client_message_without_notif(commands.get_name());

			}

			return true;
		}

		Command command_requested = Commands.get_command_with_name(type);

		if (command_requested == null) {
			MessageUtil.send_client_error_message("This command does not exist.");

			return true;
		}

		MessageUtil.send_client_message(command_requested.get_name() + " - " + command_requested.get_description());

		return true;
	}
}