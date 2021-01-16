package me.travis.wurstplus.wurstplustwo.hacks.combat;

import java.util.function.Predicate;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import me.zero.alpine.fork.listener.EventHandler;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.Listener;

public class ManualQuiver extends WurstplusHack
{
   
	
	@EventHandler
    public Listener<WurstplusEventPacket.SendPacket> listener;
	
    
    public ManualQuiver() {

        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name = "Manual Quiver";
        this.tag = "ManualQuiver";
        this.description = "quiver but manual"; //works like half of the time
        
    
    
        this.listener = new Listener<WurstplusEventPacket.SendPacket>(event -> {
            if (event.get_packet() instanceof CPacketPlayerTryUseItem && mc.player.getHeldItemMainhand().getItem() instanceof ItemBow) {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, -90.0f, mc.player.onGround));
            }
        }, new Predicate[0]);
    }
}