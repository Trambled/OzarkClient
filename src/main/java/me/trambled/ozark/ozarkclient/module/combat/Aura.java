package me.trambled.ozark.ozarkclient.module.combat;


import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.stream.Collectors;


public class Aura extends Module {

	public Aura() {
		super(Category.COMBAT);

		this.name = "Aura";
		this.tag = "Aura";
		this.description = "Automatically hits enemies within a certain range.";
	}

	Setting mode = create("Mode", "KillAuraMode", "Normal", combobox("A32k", "Normal"));
	Setting player = create("Player", "KillAuraPlayer", true);
	Setting hostile = create("Hostile", "KillAuraHostile", false);
	Setting sync_tps = create("Sync TPS", "KillAuraSyncTps", false);
	Setting range = create("Range", "KillAuraRange", 5.0, 0.5, 6.0);
	Setting delay = create("Delay", "KillAuraDelay", 2, 0, 10);
	Setting only_sword = create("Sword", "AuraSword", true);
	boolean start_verify = true;

	EnumHand actual_hand = EnumHand.MAIN_HAND;
	public static final Aura INSTANCE;

	double tick = 0;

	@Override
	protected void enable() {
		tick = 0;
	}

	@Override
	public void update() {
		if (mc.player != null && mc.world != null) {

			tick++;

			if (mc.player.isDead | mc.player.getHealth() <= 0) {
				return;
			}

			if (mode.in("Normal")) {
				if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && only_sword.get_value(true)) {
					start_verify = false;
				} else if ((mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && only_sword.get_value(true)) {
					start_verify = true;
				} else if (!only_sword.get_value(true)) {
					start_verify = true;
				}

				Entity entity = find_entity();

				if (entity != null && start_verify) {
					// Tick.
					float tick_to_hit = 20.0f - Ozark.get_event_handler().get_tick_rate();

					// If possible hit or no.
					boolean is_possible_attack = mc.player.getCooledAttackStrength(sync_tps.get_value(true) ? -tick_to_hit : 0.0f) >= 1;

					// To hit if able.
					if (is_possible_attack) {
						attack_entity(entity);
					}
				}
			} else {

				if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) return;

				if (tick < delay.get_value(1)) return;

				tick = 0;

				Entity entity = find_entity();

				if (entity != null) {
					attack_entity(entity);
				}
			}

		}
	}

	public void attack_entity(Entity entity) {

		if (mode.in("A32k")) {

			int newSlot = -1;

			for (int i = 0; i < 9; i++) {
				ItemStack stack = mc.player.inventory.getStackInSlot(i);
				if (stack == ItemStack.EMPTY) {
					continue;
				}
				if (checkSharpness(stack)) {
					newSlot = i;
					break;
				}
			}

			if (newSlot != -1) {
				mc.player.inventory.currentItem = newSlot;
			}

		}

		// Get actual item off hand.
		ItemStack off_hand_item = mc.player.getHeldItemOffhand();

		// If off hand not null and is some SHIELD like use.
		if (off_hand_item.getItem() == Items.SHIELD) {
			// Ignore ant continue.
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
		}

		// Start hit on entity.
		mc.player.connection.sendPacket(new CPacketUseEntity(entity));
		mc.player.swingArm(actual_hand);
		mc.player.resetCooldown();
	}

	// For find a entity.
	public Entity find_entity() {
		// Create a request.
		Entity entity_requested = null;

		for (Entity player : mc.world.playerEntities.stream().filter(entityPlayer -> !FriendUtil.isFriend(entityPlayer.getName())).collect(Collectors.toList())) {
			// If entity is not null continue to next event.
			if (player != null) {
				// If is compatible.
				if (is_compatible(player)) {
					// If is possible to get.
					if (mc.player.getDistance(player) <= range.get_value(1.0)) {
						// Atribute the entity into entity_requested.
						entity_requested = player;
					}
				}
			}
		}

		// Return the entity requested.
		return entity_requested;
	}

	// Compatible or no.
	public boolean is_compatible(Entity entity) {
		// Instend entity with some type entity to continue or no.
		if (player.get_value(true) && entity instanceof EntityPlayer) {
			if (entity != mc.player && !(entity.getName().equals(mc.player.getName())) /* && FriendManager.is_friend(entity) == false */) {
				return true;
			}
		}

		// If is hostile.
		if (hostile.get_value(true) && entity instanceof IMob) {
			return true;
		}

		// If entity requested die.
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase entity_living_base = (EntityLivingBase) entity;

			if (entity_living_base.getHealth() <= 0) {
				return false;
			}
		}

		// Return false.
		return false;
	}

	private boolean checkSharpness(ItemStack stack) {

		if (stack.getTagCompound() == null) {
			return false;
		}

		NBTTagList enchants = (NBTTagList) stack.getTagCompound().getTag("ench");

		if (enchants == null) {
			return false;
		}

		for (int i = 0; i < enchants.tagCount(); i++) {
			NBTTagCompound enchant = enchants.getCompoundTagAt(i);
			if (enchant.getInteger("id") == 16) {
				int lvl = enchant.getInteger("lvl");
				if (lvl > 5) {
					return true;
				}
				break;
			}
		}

		return false;

	}

	static {
		INSTANCE = new Aura();
	}
}
