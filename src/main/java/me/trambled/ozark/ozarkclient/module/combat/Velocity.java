package me.trambled.ozark.ozarkclient.module.combat;


import me.trambled.ozark.ozarkclient.event.Event;
import me.trambled.ozark.ozarkclient.event.events.EventEntity;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;


public class Velocity extends Module {
	public Velocity() {
		super(Category.COMBAT);

		this.name        = "Velocity";
		this.tag         = "Velocity";
		this.description = "No knockback.";
	}



	@EventHandler
	private final Listener<EventPacket.ReceivePacket> damage = new Listener<>(event -> {
		if (event.get_era() == Event.Era.EVENT_PRE) {
			if (event.get_packet() instanceof SPacketEntityVelocity) {
				SPacketEntityVelocity knockback = (SPacketEntityVelocity) event.get_packet();

				if (knockback.getEntityID() == mc.player.getEntityId()) {
					event.cancel();

					knockback.motionX *= 0.0f;
					knockback.motionY *= 0.0f;
					knockback.motionZ *= 0.0f;
				}
			} else if (event.get_packet() instanceof SPacketExplosion) {
				event.cancel();

				SPacketExplosion knockback = (SPacketExplosion) event.get_packet();


				knockback.motionX *= 0.0f;
				knockback.motionY *= 0.0f;
				knockback.motionZ *= 0.0f;
			}
		}
	});

	@EventHandler
	private final Listener<EventEntity.EventColision> explosion = new Listener<>(event -> {
		if (event.get_entity() == mc.player) {
			event.cancel();

		}
	});
}
