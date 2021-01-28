package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

//xulu
public class NoSoundLag extends WurstplusHack{

	
    public NoSoundLag() {

        super(WurstplusCategory.WURSTPLUS_MISC);

        this.name = "AntiSound";
        this.tag = "AntiSound";
        this.description = "remove certain annoying sounds";
    }
	WurstplusSetting wither = create("Wither", "Wither", true);
    WurstplusSetting wither_hurt = create("Wither Hurt", "WitherHurt", true);
    WurstplusSetting wither_death = create("Wither Death", "WitherDeath", false);
    WurstplusSetting wither_spawn = create("Wither Spawn", "WitherSpawn", false);
    WurstplusSetting punches = create("Punches", "Punches", true);
    WurstplusSetting punches_weak = create("Punches Weak", "PunchesWeak", true);
    WurstplusSetting punches_kb = create("Punches Knockback", "PunchesKB", true);
    WurstplusSetting explosion = create("Explosion", "Explosion", true);
    WurstplusSetting totem = create("Totem", "Totem", false);
    WurstplusSetting portal = create("Portal", "Portal", true);
    WurstplusSetting elytras = create("Elytra", "Elytra", true);
	
	@EventHandler
    private Listener<WurstplusEventPacket.ReceivePacket> receiveListener = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect)event.get_packet();
            if (this.shouldCancelSound(packet.getSound())) {
                event.cancel();
            }
        }
    });

    @Override
    protected void enable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    protected void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    private boolean shouldCancelSound(final SoundEvent soundEvent) {
        return (soundEvent == SoundEvents.ENTITY_WITHER_AMBIENT && this.wither.get_value(true)) || (soundEvent == SoundEvents.ENTITY_WITHER_SPAWN && this.wither_spawn.get_value(true)) || (soundEvent == SoundEvents.ENTITY_WITHER_HURT && this.wither_hurt.get_value(true)) || (soundEvent == SoundEvents.ENTITY_WITHER_DEATH && this.wither_death.get_value(true)) || (soundEvent == SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE && this.punches.get_value(true)) || (soundEvent == SoundEvents.ENTITY_PLAYER_ATTACK_WEAK && this.punches_weak.get_value(true)) || (soundEvent == SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK && this.punches_kb.get_value(true)) || (soundEvent == SoundEvents.ENTITY_GENERIC_EXPLODE && this.explosion.get_value(true));
    }
 }