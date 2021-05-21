package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall extends Module {
    
    public NoFall() {
        super(Category.MOVEMENT);

		this.name        = "NoFall";
		this.tag         = "NoFall";
		this.description = "Prevents fall damage.";
    }


    @Override
    protected void enable() {
        if (mc.player != null) {
            mc.player.fallDistance = 0;
        }
    }

    @Override
    protected void disable() {
        if (mc.player != null) {
            mc.player.fallDistance = 0;
        }
    }

    @Override
    public void update() {
        if (mc.player.fallDistance != 0) {
            mc.player.connection.sendPacket(new CPacketPlayer(true));
        }
    }
}
