package me.trambled.ozark.ozarkclient.command.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import net.minecraft.item.ItemStack;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class AutoGearCommand extends Command {
	public AutoGearCommand() {
		super("gear", "autogear command stuff");
	}


	final static private String pathSave = "OZARKCLIENT/autogear.json";

	public boolean get_message(String[] message) {
		String msg = "null";

		if (message.length > 1) {
			msg = message[1];
		}

		if (msg.equals("null")) {
			MessageUtil.send_client_error_message(current_prefix() + "autogear <save/set/list>");

			return true;
		}

		ChatFormatting c = ChatFormatting.GRAY;

		if (msg.equalsIgnoreCase("save") || msg.equals("add") || msg.equals("create")) {
			if (message.length == 3) {
				save(message[2]);
			} else {
				MessageUtil.send_client_error_message("Not enough parameters!");
			}
		} else if (msg.equalsIgnoreCase("set")) {
			if (message.length == 3) {
				set(message[2]);
			} else {
				MessageUtil.send_client_error_message("Not enough parameters!");
			}
		} else if (msg.equalsIgnoreCase("list")) {
			if (message.length == 2) {
				listMessage();
			}else MessageUtil.send_client_error_message("Not enough parameters!");
//		} else if (msg.equalsIgnoreCase("del") || msg.equalsIgnoreCase("delete")) {
//			if (message.length == 3) {
//				delete(message[2]);
//			}	else MessageUtil.send_client_error_message("Not enough parameters!");
		} else {
			MessageUtil.send_client_error_message("AutoGear message is: gear set/save/del/list [name]");

			return true;
		}

		return true;
	}

	private void listMessage() {
		JsonObject completeJson = new JsonObject();
		try {
			// Read json
			completeJson = new JsonParser().parse(new FileReader(pathSave)).getAsJsonObject();
			int lenghtJson = completeJson.entrySet().size();
			for(int i = 0; i < lenghtJson; i++) {
				String item = new JsonParser().parse(new FileReader(pathSave)).getAsJsonObject().entrySet().toArray()[i].toString().split("=")[0];
				if (!item.equals("pointer"))
					MessageUtil.send_client_message("Kit available: " + item);
			}

		} catch (IOException e) {
			// Case not found, reset
			MessageUtil.send_client_error_message("Kit not found!");
		}
	}

	private void delete(String name) {
		JsonObject completeJson = new JsonObject();
		try {
			// Read json
			completeJson = new JsonParser().parse(new FileReader(pathSave)).getAsJsonObject();
			if (completeJson.get(name) != null && !name.equals("pointer")) {
				// Delete
				completeJson.remove(name);
				// Check if it's setter
				if (completeJson.get("pointer").getAsString().equals(name))
					completeJson.addProperty("pointer", "none");
				// Save
				saveFile(completeJson, name, "deleted");
			}else MessageUtil.send_client_error_message("Kit not found!");

		} catch (IOException e) {
			// Case not found, reset
			MessageUtil.send_client_error_message("Kit not found!");
		}
	}

	private void set(String name) {
		JsonObject completeJson = new JsonObject();
		try {
			// Read json
			completeJson = new JsonParser().parse(new FileReader(pathSave)).getAsJsonObject();
			if (completeJson.get(name) != null && !name.equals("pointer")) {
				// Change the value
				completeJson.addProperty("pointer", name);
				// Save
				saveFile(completeJson, name, "selected");
			}else MessageUtil.send_client_error_message("Kit not found!");
		} catch (IOException e) {
			// Case not found, reset
			MessageUtil.send_client_error_message("Kit not found!");
		}
	}

	private void save(String name) {
		JsonObject completeJson = new JsonObject();
		try {
			// Read json
			completeJson = new JsonParser().parse(new FileReader(pathSave)).getAsJsonObject();
			if (completeJson.get(name) != null && !name.equals("pointer")) {
				MessageUtil.send_client_error_message("This kit already exists!");
				return;
			}
			// We can continue

		} catch (IOException e) {
			// Case not found, reset
			completeJson.addProperty("pointer", "none");
		}

		// String that is going to be our inventory
		StringBuilder jsonInventory = new StringBuilder();
		for(ItemStack item : mc.player.inventory.mainInventory) {
			// Add everything
			jsonInventory.append( Objects.requireNonNull ( item.getItem ( ).getRegistryName ( ) ).toString() + item.getMetadata()).append(" ");
		}
		// Add to the json
		completeJson.addProperty(name, jsonInventory.toString());
		// Save json
		saveFile(completeJson, name, "saved");
	}

	private void saveFile(JsonObject completeJson, String name, String operation) {
		// Save the json
		try {
			// Open
			BufferedWriter bw = new BufferedWriter(new FileWriter(pathSave));
			// Write
			bw.write(completeJson.toString());
			// Save
			bw.close();
			// Chat message
			MessageUtil.send_client_message("Kit " + name + " " + operation);
		} catch (IOException e) {
			MessageUtil.send_client_error_message("Error saving the file!");
		}
	}

	public static String getCurrentSet() {

		JsonObject completeJson = new JsonObject();
		try {
			// Read json
			completeJson = new JsonParser().parse(new FileReader(pathSave)).getAsJsonObject();
			if (!completeJson.get("pointer").getAsString().equals("none"))
				return completeJson.get("pointer").getAsString();


		} catch (IOException e) {
			// Case not found, reset
		}
		MessageUtil.send_client_error_message("Kit not found!");
		return "";
	}

	public static String getInventoryKit(String kit) {
		JsonObject completeJson = new JsonObject();
		try {
			// Read json
			completeJson = new JsonParser().parse(new FileReader(pathSave)).getAsJsonObject();
			return completeJson.get(kit).getAsString();


		} catch (IOException e) {
			// Case not found, reset
		}
		MessageUtil.send_client_error_message("Kit not found!");
		return "";
	}
}