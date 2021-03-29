package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;
import net.minecraft.network.Packet;

// External.


public class EventPacket extends Event {
	private final Packet packet;

	public EventPacket(Packet packet) {
		super();

		this.packet = packet;
	}

	public Packet get_packet() {
		return this.packet;
	}

	public static class ReceivePacket extends EventPacket {
		public ReceivePacket(Packet packet) {
			super(packet);
		}
	}

	public static class SendPacket extends EventPacket {
		public SendPacket(Packet packet) {
			super(packet);
		}
	}
}