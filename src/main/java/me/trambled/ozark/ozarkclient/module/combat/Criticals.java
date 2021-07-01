package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;


public class Criticals extends Module {

	public Criticals() {
		super(Category.COMBAT);

		this.name        = "Criticals";
		this.tag         = "Criticals";
		this.description = "You can hit with criticals when attack.";
	}

	Setting mode = create("Mode", "CriticalsMode", "Packet", combobox("Packet", "Jump"));
	Setting only_when_ka = create("Only When KA", "CriticalsOnlyWhenKA", true);

	@EventHandler
	private final Listener<EventPacket.SendPacket> listener = new Listener<>(event -> {
		if (event.get_packet() instanceof CPacketUseEntity) {
			CPacketUseEntity event_entity = ((CPacketUseEntity) event.get_packet());
			if (event_entity.getAction() == CPacketUseEntity.Action.ATTACK && mc.player.onGround) {
				if (Ozark.get_module_manager().get_module_with_tag("Aura").is_active() || !only_when_ka.get_value(true)) {
					if (mode.in("Packet")) {
						mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1f, mc.player.posZ, false));
						mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
					} else if (mode.in("Jump")) {
						mc.player.jump();
					}
				}
			}
		}
	});

	@Override
	public String array_detail() {
		return mode.get_current_value();
	}
}