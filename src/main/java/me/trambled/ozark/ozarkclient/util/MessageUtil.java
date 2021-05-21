package me.trambled.ozark.ozarkclient.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {
	public final static Minecraft mc = Minecraft.getMinecraft();

	public static ChatFormatting g = ChatFormatting.DARK_RED;
	public static ChatFormatting b = ChatFormatting.BLUE;
	public static ChatFormatting a = ChatFormatting.DARK_AQUA;
	public static ChatFormatting r = ChatFormatting.RESET;
	public static ChatFormatting f = ChatFormatting.BOLD;

	public static String opener = g + "[" + Ozark.DISPLAY_NAME + "]" + "  " +  r;

	public static void toggle_message(Module module) {
		opener = g + "[" + Ozark.DISPLAY_NAME + "]" + "  " +  r;
		if (module.is_active()) {
			if (module.get_tag().equals("AutoCrystal")) {
				client_message_simple(opener + "we do a little " + ChatFormatting.DARK_GREEN + "trolling");
			} else {
				client_message_simple(opener + r + module.get_name() + r + ChatFormatting.DARK_GREEN + " enabled");
			}			
		} else {
			if (module.get_tag().equals("AutoCrystal")) {
				client_message_simple(opener + "we aint" + ChatFormatting.RED + " trolling " + r + "no more");
			} else {
				client_message_simple(opener + r + module.get_name() + r + ChatFormatting.RED + " disabled");
			}
		}
	}

	public static void client_message_simple(String message) {
		opener = g + "[" + Ozark.DISPLAY_NAME + "]" + "  " +  r;
		if (mc.player != null) {
			final ITextComponent itc = new TextComponentString(message).setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("frank alachi"))));
			mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(itc, 5936);
		}
	}

        public static void send_client_message_simple(String message) {
		opener = g + "[" + Ozark.DISPLAY_NAME + "]" + "  " +  r;
		if (mc.player != null) {
			client_message_simple(opener + message);
		}
	}

	public static void client_message(String message) {
		opener = g + "[" + Ozark.DISPLAY_NAME + "]" + "  " +  r;
		if (mc.player != null) {
			mc.player.sendMessage(new ChatMessage(message));
		}
	}

	public static void send_client_message(String message) {
		opener = g + "[" + Ozark.DISPLAY_NAME + "]" + "  " +  r;
		if (mc.player != null) {
			client_message(opener + message);
		}
	}

	public static void send_client_error_message(String message) {
		opener = g + "[" + Ozark.DISPLAY_NAME + "]" + "  " +  r;
		client_message(opener + r + "Error: "  + message);
	}

	public static class ChatMessage extends TextComponentBase {
		String message_input;

		public ChatMessage(String message) {
			Pattern p       = Pattern.compile("&[0123456789abcdefrlosmk]");
			Matcher m       = p.matcher(message);
			StringBuffer sb = new StringBuffer();

			while (m.find()) {
				String replacement = "\u00A7" + m.group().substring(1);
				m.appendReplacement(sb, replacement);
			}

			m.appendTail(sb);
			this.message_input = sb.toString();
		}

		public String getUnformattedComponentText() {
			return this.message_input;
		}

		@Override
		public ITextComponent createCopy() {
			return new ChatMessage(this.message_input);
		}
	}

}
