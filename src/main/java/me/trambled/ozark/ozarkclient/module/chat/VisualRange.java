package me.trambled.ozark.ozarkclient.module.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class VisualRange extends Module {

	private List<String> people;

	public VisualRange() {
		super(Category.CHAT);

		this.name        = "VisualRange";
		this.tag         = "VisualRange";
		this.description = "Bc using ur eyes is overrated.";
	}

	@Override
	public void enable() {
		people = new ArrayList<>();
	}

	@Override
	public void update() {
		if (mc.world == null | mc.player == null) return;

		List<String> peoplenew = new ArrayList<>();
		List<EntityPlayer> playerEntities = mc.world.playerEntities;

		for (Entity e : playerEntities) {
			if (e.getName().equals(mc.player.getName())) continue;
			peoplenew.add(e.getName());
		}

		if (peoplenew.size() > 0) {
			for (String name : peoplenew) {
				if (!people.contains(name)) {
					if (FriendUtil.isFriend(name)) {
						MessageUtil.send_client_message( ChatFormatting.AQUA + name + ChatFormatting.RESET + " has entered visual range!");
					} else {
						MessageUtil.send_client_message( ChatFormatting.DARK_RED + name + ChatFormatting.RESET + " has entered visual range!");
					}
					people.add(name);
				}
			}
		}

	}
}
