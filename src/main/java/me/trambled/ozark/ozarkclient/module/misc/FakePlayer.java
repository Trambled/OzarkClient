package me.trambled.ozark.ozarkclient.module.misc;

import com.mojang.authlib.GameProfile;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.UUID;

public class FakePlayer extends Module {
    
    public FakePlayer() {
        super(Category.MISC);

		this.name        = "FakePlayer";
		this.tag         = "FakePlayer";
		this.description = "Spawns a client-side player to config on.";
    }

    private EntityOtherPlayerMP fake_player;

    @Override
    protected void enable() {
        
        fake_player = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("bcc17b86-e657-4c1b-bedd-aaf28e8b1240"), "Trambled"));
        fake_player.copyLocationAndAnglesFrom(mc.player);
        fake_player.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-100, fake_player);
    }

    @Override
    protected void disable() {
        try {
            mc.world.removeEntity(fake_player);
        } catch (Exception ignored) {}
    }

    @EventHandler
    private final Listener<EntityJoinWorldEvent> on_world_event = new Listener<>(p_Event ->
    {
        if (p_Event.getEntity() == mc.player)
        {
            this.set_disable();
        }
    });

}