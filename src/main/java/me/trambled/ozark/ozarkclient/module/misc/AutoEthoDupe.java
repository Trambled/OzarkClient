package me.trambled.ozark.ozarkclient.module.misc;

import com.mojang.authlib.GameProfile;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.UUID;

public class AutoEthoDupe extends Module {
    
    public AutoEthoDupe() {
        super(Category.MISC);

		this.name        = "AutoEthoDupe";
		this.tag         = "AutoEthoDupe";
		this.description = "etho dupe snine19 tudou love 2017 classic meme";
    }

    private EntityOtherPlayerMP fake_player;

    @Override
    protected void enable() {
        
        fake_player = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("5045bb4a-184e-4bf4-a8fa-7f7c05dd3c77"), "x3th0_"));
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

}
