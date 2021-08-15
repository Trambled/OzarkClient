package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.Random;

// Zero alpine manager.


public class ChatSuffix extends Module {
	public ChatSuffix() {
		super(Category.CHAT);

		this.name        = "ChatSuffix";
		this.tag         = "ChatSuffix";
		this.description = "Show off how cool u are.";
	}

	Setting ignore = create("Ignore", "ChatSuffixIgnore", true);
	Setting type = create("Type", "ChatSuffixType", "Default", combobox("Default", "Random"));
	Setting version = create("Version", "ChatSuffixVersion", true);

	boolean accept_suffix;
	boolean suffix_default;
	boolean suffix_random;

	StringBuilder suffix;

	String[] random_client_name = {
		"OzarkClient",
		"KKKHack",
		"Trambhack",
		"Chimpware",
		"Naziware",
		"Jihadhack",
		"Allahhack"	
	};

	String[] random_client_finish = {
		" sponsored by trambled",
		" sponsored by chimpware",
		" sponsored by allah",
		" sponsored by the kkk",
		" sponsored by hitler",
		" sponsored by ozark",
		" version " + Ozark.VERSION
	};

	@EventHandler
	private final Listener<EventPacket.SendPacket> listener = new Listener<>(event -> {
		// If not be the CPacketChatMessage return.
		if (!(event.get_packet() instanceof CPacketChatMessage)) {
			return;
		}

		// Start event suffix.
		accept_suffix = true;

		// Get value.
		boolean ignore_prefix = ignore.get_value(true);

		String message = ((CPacketChatMessage) event.get_packet()).getMessage();

		// If is with some characther.
		if (message.startsWith("/")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("\\") && ignore_prefix) accept_suffix = false;
		if (message.startsWith("!")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(":")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(";")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(".")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(",")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("@")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("&")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("*")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("$")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("#")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("(")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(")")  && ignore_prefix) accept_suffix = false;

		// Compare the values type.
		if (type.in("Default")) {
			suffix_default = true;
			suffix_random  = false;
		}

		if (type.in("Random")) {
			suffix_default = false;
			suffix_random  = true;
		}

		// If accept.
		if (accept_suffix) {
			if (suffix_default) {
				// Just default.
				message += Ozark.SIGN + convert_base(Ozark.DISPLAY_NAME + Ozark.SIGN);
				if (version.get_value(true)) {
					message += Ozark.VERSION;
				}
			}

			if (suffix_random) {
				// Create first the string builder.
				StringBuilder suffix_with_randoms = new StringBuilder();

				// Convert the base using the TravisFont.
				suffix_with_randoms.append(convert_base(random_string(random_client_name)));
				suffix_with_randoms.append(convert_base(random_string(random_client_finish)));

				message += Ozark.SIGN + suffix_with_randoms;
			}

			// If message 256 string length substring.
			if (message.length() >= 256) {
				message.substring(0, 256);
			}
		}

		// Send the message.
		((CPacketChatMessage) event.get_packet()).message = message;
	});

	// Get the random values string.
	public String random_string(String[] list) {
		return list[new Random().nextInt(list.length)];
	}

	// Convert the base using the TravisFont.
	public String convert_base(String base) {
		return Ozark.smoth(base);
	}

	@Override
	public String array_detail() {
		// Update the detail.
		return this.type.get_current_value();
	}
}
