package me.trambled.ozark.ozarkclient.module.misc;

import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundEvent;

//xulu
public class AntiSound extends Module {

    public AntiSound() {
		super(Category.MISC);

		this.name        = "Anti Sound";
		this.tag         = "AntiSound";
		this.description = "cancels certain sounds";
    }

    Setting wither = create("Wither", "AntiSoundWither", false);
    Setting wither_spawn = create("Wither Spawn", "AntiSoundWitherSpawn", false);
    Setting wither_hurt = create("Wither Hurt", "AntiSoundWitherHurt", false);
    Setting wither_death = create("Wither Death", "AntiSoundWitherDeath", false);
    Setting punches = create("Punches", "AntiSoundPunches", true);
    Setting punches_weakness = create("Weakness Punch", "AntiSoundPunchesWeakness", true);
    Setting punches_knockback = create("Knockback Punch", "AntiSoundPunchesKnockback", true);
    Setting explosion = create("Explosions", "AntiSoundExplosions", true);
    Setting totem = create("Totem", "AntiSoundTotem", false);
    Setting elytra = create("Elytra", "AntiSoundElytra", true);
    Setting portal = create("Portals", "AntiSoundPortals", false);

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> packet_event = new Listener<>(event -> {
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