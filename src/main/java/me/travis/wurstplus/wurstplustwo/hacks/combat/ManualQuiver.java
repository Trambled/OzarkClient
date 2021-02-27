package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;

public class ManualQuiver extends WurstplusHack
{

    public ManualQuiver() {

        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name = "Manual Quiver";
        this.tag = "ManualQuiver";
        this.description = "shoots arrows over you"; //works like half of the time

    }

    @EventHandler
    public Listener<WurstplusEventPacket.SendPacket> listener = new Listener<>(event -> {
        if (event.get_packet() instanceof CPacketPlayerTryUseItem && mc.player.getHeldItemMainhand().getItem() instanceof ItemBow) {
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, -90.0f, mc.player.onGround));
        }
    });
}