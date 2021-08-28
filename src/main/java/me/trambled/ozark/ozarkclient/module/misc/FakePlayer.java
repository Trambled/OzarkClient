package me.trambled.ozark.ozarkclient.module.misc;

import com.mojang.authlib.GameProfile;
import me.trambled.ozark.ozarkclient.event.Eventbus;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.event.events.EventTotemPop;
import me.trambled.ozark.ozarkclient.manager.TotempopManager;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.world.CrystalUtil;
import me.trambled.ozark.ozarkclient.util.world.DamageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.GameType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.UUID;

public class FakePlayer extends Module {
    
    public FakePlayer() {
        super(Category.MISC);

		this.name        = "FakePlayer";
		this.tag         = "FakePlayer";
		this.description = "Spawns a client-side player to config on.";
    }

    Setting pop = create("Pop","poop",true);

    private EntityOtherPlayerMP fake_player;

    @Override
    protected void enable() {
        
        fake_player = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("bcc17b86-e657-4c1b-bedd-aaf28e8b1240"), "Trambled"));
        fake_player.setGameType(GameType.SURVIVAL);
        fake_player.copyLocationAndAnglesFrom(mc.player);
        fake_player.rotationYawHead = mc.player.rotationYawHead;
        fake_player.inventory.copyInventory(FakePlayer.mc.player.inventory);
        fake_player.setHealth(FakePlayer.mc.player.getHealth() + FakePlayer.mc.player.getAbsorptionAmount());
        fake_player.setSneaking(mc.player.isSneaking());
        mc.world.addEntityToWorld(-100, fake_player);
    }

    @Override
    protected void disable() {
        try {
            mc.world.removeEntity(fake_player);
        } catch (Exception ignored) {}
    }

    @Override
    public void log_out() {
        this.set_disable();
    }

    @EventHandler
    private final Listener<EntityJoinWorldEvent> on_world_event = new Listener<>(p_Event ->
    {
        if (p_Event.getEntity() == mc.player)
        {
            this.set_disable();
        }
    });

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> packetRecieveListener = new Listener<>(event -> {
        if(pop.get_value(true)) {
            if(event.get_packet() instanceof SPacketDestroyEntities) {
                final SPacketDestroyEntities packet = (SPacketDestroyEntities) event.get_packet();

                for(int id : packet.getEntityIDs()) {
                    final Entity entity = mc.world.getEntityByID(id);

                    if(entity instanceof EntityEnderCrystal) {
                        if(entity.getDistanceSq(fake_player) < 144) {
                            final float rawDamage = CrystalUtil.calculateDamage(entity.getPositionVector().x,entity.getPositionVector().y,entity.getPositionVector().z ,fake_player);
                            final float absorption = fake_player.getAbsorptionAmount() - rawDamage;
                            final boolean hasHealthDmg = absorption < 0;
                            final float health = fake_player.getHealth() + absorption;

                            if(hasHealthDmg && health > 0) {
                                fake_player.setHealth(health);
                                fake_player.setAbsorptionAmount(0);
                            } else if(health > 0) {
                                fake_player.setAbsorptionAmount(absorption);
                            } else {
                                fake_player.setHealth(2);
                                fake_player.setAbsorptionAmount(8);
                                fake_player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 5));
                                fake_player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 1));

                                try {
                                    mc.player.connection.handleEntityStatus(new SPacketEntityStatus(fake_player, (byte) 35));
                                } catch (Exception e) {
                                }

                                if(TotempopManager.totemMap.containsKey(fake_player)) {
                                    int times = TotempopManager.totemMap.get(fake_player) + 1;
                                    Eventbus.EVENT_BUS.post(new EventTotemPop(fake_player, times));
                                    TotempopManager.totemMap.remove(fake_player);
                                    TotempopManager.totemMap.put(fake_player, times);
                                } else {
                                    Eventbus.EVENT_BUS.post(new EventTotemPop(fake_player, 1));
                                    TotempopManager.totemMap.put(fake_player, 1);
                                }
                            }

                            fake_player.hurtTime = 5;
                        }
                    }
                }
            }
        }
    });
}