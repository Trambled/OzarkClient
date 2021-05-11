package me.trambled.ozark.ozarkclient.module.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.util.FriendUtil;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class VisualRange extends Module {

	private List<String> people;

	public VisualRange() {
		super(Category.CHAT);

		this.name        = "Visual Range";
		this.tag         = "VisualRange";
		this.description = "bc using ur eyes is overrated";
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
						MessageUtil.send_client_message("I see a friend named " + ChatFormatting.RESET + ChatFormatting.GREEN + name + ChatFormatting.RESET + ". Say Hi!");
					} else {
						MessageUtil.send_client_message("I see an enemy named " + ChatFormatting.RESET + ChatFormatting.RED + name + ChatFormatting.RESET + "");
					}
					people.add(name);
				}
			}
		}

	}
}