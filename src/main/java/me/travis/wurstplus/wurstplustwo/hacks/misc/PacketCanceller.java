package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.event.events.EventNetworkPacketEvent;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

import java.util.ArrayList;

public class PacketCanceller extends WurstplusHack {

	public PacketCanceller() {
		super(WurstplusCategory.WURSTPLUS_MISC);

		this.name        = "Fast Util"; 
		this.tag         = "FastUtil";
		this.description = "use things faster";
	}

	WurstplusSetting CancelCPacketInput = create("CancelCPacketInput", "CancelCPacketInput", true);
	WurstplusSetting CancelPosition = create("CancelPosition", "CancelPosition", true);
	WurstplusSetting CancelPositionRotation = create("CancelPositionRotation", "CancelPositionRotation", true);
        WurstplusSetting CancelRotation = create("CancelRotation", "CancelRotation", true);
	WurstplusSetting CancelCPacketPlayerAbilities = create("CancelCPacketPlayerAbilities", "CancelCPacketPlayerAbilities", true);
	WurstplusSetting CancelCPacketPlayerDigging = create("CancelCPacketPlayerDigging", "CancelCPacketPlayerDigging", true);
	WurstplusSetting CancelCPacketPlayerTryUseItem = create("CancelCPacketPlayerTryUseItem", "CancelCPacketPlayerTryUseItem", true);
	WurstplusSetting CancelCPacketPlayerTryUseItemOnBlock = create("CancelCPacketPlayerTryUseItemOnBlock", "CancelCPacketPlayerTryUseItemOnBlock", true);
	WurstplusSetting CancelCPacketEntityAction = create("CancelCPacketEntityAction", "CancelCPacketEntityAction", true);
	WurstplusSetting CancelCPacketUseEntity = create("CancelCPacketUseEntity", "CancelCPacketUseEntity", true);
	WurstplusSetting CancelCPacketVehicleMove = create("CancelCPacketVehicleMove", "CancelCPacketVehicleMove", true);

    private int PacketsCanelled = 0;
    private ArrayList<Packet> PacketsToIgnore = new ArrayList<Packet>();

    @Override
    protected void disable()
    {
        PacketsCanelled = 0;
    } //TODO: add arraylist shit

    @EventHandler
    private Listener<EventNetworkPacketEvent> PacketEvent = new Listener<>(p_Event ->
    {
        if ((p_Event.getPacket() instanceof CPacketInput && CancelCPacketInput.get_value(true))//
            || (p_Event.getPacket() instanceof CPacketPlayer.Position && CancelPosition.get_value(true))//
            || (p_Event.getPacket() instanceof CPacketPlayer.PositionRotation && CancelPositionRotation.get_value(true))//
            || (p_Event.getPacket() instanceof CPacketPlayer.Rotation && CancelRotation.get_value(true)) //
            || (p_Event.getPacket() instanceof CPacketPlayerAbilities && CancelCPacketPlayerAbilities.get_value(true)) //
            || (p_Event.getPacket() instanceof CPacketPlayerDigging && CancelCPacketPlayerDigging.get_value(true)) //
            || (p_Event.getPacket() instanceof CPacketPlayerTryUseItem && CancelCPacketPlayerTryUseItem.get_value(true)) //
            || (p_Event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && CancelCPacketPlayerTryUseItemOnBlock.get_value(true)) //
            || (p_Event.getPacket() instanceof CPacketEntityAction && CancelCPacketEntityAction.get_value(true))//
            || (p_Event.getPacket() instanceof CPacketUseEntity && CancelCPacketUseEntity.get_value(true))//
            || (p_Event.getPacket() instanceof CPacketVehicleMove && CancelCPacketVehicleMove.get_value(true)))//
        {
            if (PacketsToIgnore.contains(p_Event.getPacket()))
            {
                PacketsToIgnore.remove(p_Event.getPacket());
                return;
            }
            
            ++PacketsCanelled;
            p_Event.cancel();
            return;
        }
    });
    
    public void AddIgnorePacket(Packet p_Packet)
    {
        PacketsToIgnore.add(p_Packet);
    }
}