package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.event.events.EventNetworkPacketEvent;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.SPacketSpawnMob;

public class DonkeyAlert extends WurstplusHack {

    public DonkeyAlert() {
        super(WurstplusCategory.WURSTPLUS_MISC);

        this.name = "Donkey Alert";
        this.tag = "DonkeyAlert";
        this.description = "i need sex dupe";
    }

    @EventHandler
    private Listener<EventNetworkPacketEvent> PacketEvent = new Listener<>(p_Event -> {
        if (p_Event.getPacket() instanceof SPacketSpawnMob) {

            final SPacketSpawnMob packet = (SPacketSpawnMob) p_Event.getPacket();

            if (packet.getEntityType() == 31) {
                WurstplusMessageUtil.client_message("Donkey spawned at " + packet.getX() + ", " + packet.getY() + ", " + packet.getZ() + ", ");
            }
        }
    });


}
