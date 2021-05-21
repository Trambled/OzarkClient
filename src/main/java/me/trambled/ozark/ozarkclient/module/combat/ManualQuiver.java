package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;

public class ManualQuiver extends Module
{

    public ManualQuiver() {

        super(Category.COMBAT);

        this.name = "ManualQuiver";
        this.tag = "ManualQuiver";
        this.description = "Shoots arrows over you."; //works like half of the time

    }

    @EventHandler
    public Listener<EventPacket.SendPacket> listener = new Listener<>(event -> {
        if (event.get_packet() instanceof CPacketPlayerTryUseItem && mc.player.getHeldItemMainhand().getItem() instanceof ItemBow) {
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, -90.0f, mc.player.onGround));
        }
    });
}