package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundEvent;

//xulu
public class AntiSound extends WurstplusHack {

    public AntiSound() {
		super(WurstplusCategory.WURSTPLUS_MISC);

		this.name        = "Anti Sound";
		this.tag         = "AntiSound";
		this.description = "cancels certain sounds";
    }

    WurstplusSetting wither = create("Wither", "Wither", false);
    WurstplusSetting wither_spawn = create("Wither Spawn", "WitherSpawn", false);
    WurstplusSetting wither_hurt = create("Wither Hurt", "WitherHurt", false);
    WurstplusSetting wither_death = create("Wither Death", "WitherDeath", false);
    WurstplusSetting punches = create("Punches", "Punches", true);
    WurstplusSetting punches_weakness = create("Weakness Punch", "PunchesWeakness", true);
    WurstplusSetting punches_knockback = create("Knockback Punch", "PunchesKnockback", true);
    WurstplusSetting explosion = create("Explosions", "Explosions", true);
    WurstplusSetting totem = create("Totem", "Totem", false);
    WurstplusSetting elytra = create("Elytra", "Elytra", true);
    WurstplusSetting portal = create("Portals", "Portals", false);

    @EventHandler
    private final Listener<WurstplusEventPacket.ReceivePacket> packet_event = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect)event.get_packet();
            if (this.shouldCancelSound(packet.getSound())) {
                event.cancel();
            }
        }
    });

    private boolean shouldCancelSound(final SoundEvent soundEvent) {
        return (soundEvent == SoundEvents.ENTITY_WITHER_AMBIENT && this.wither.get_value(true)) || (soundEvent == SoundEvents.ENTITY_WITHER_SPAWN && this.wither_spawn.get_value(true)) || (soundEvent == SoundEvents.ENTITY_WITHER_HURT && this.wither_hurt.get_value(true)) || (soundEvent == SoundEvents.ENTITY_WITHER_DEATH && this.wither_death.get_value(true)) || (soundEvent == SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE && this.punches.get_value(true)) || (soundEvent == SoundEvents.ENTITY_PLAYER_ATTACK_WEAK && this.punches_weakness.get_value(true)) || (soundEvent == SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK && this.punches_knockback.get_value(true)) || (soundEvent == SoundEvents.ENTITY_GENERIC_EXPLODE && this.explosion.get_value(true));
    }
}