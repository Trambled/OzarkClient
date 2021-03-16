package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall extends WurstplusHack {
    
    public NoFall() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);

		this.name        = "NoFall";
		this.tag         = "NoFall";
		this.description = "prevents fall damage";
		this.toggle_message = false;
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