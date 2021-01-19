package me.travis.wurstplus.wurstplustwo.event.events;

import net.minecraft.network.Packet;
import me.travis.wurstplus.wurstplustwo.event.EventStage;

public class EventPacket extends EventStage {
	private static Packet packet2;
	private final Packet packet;

	public EventPacket(Packet packet) {
		this.packet = (Packet) packet2;
	}

	public static Object getPacket() {
		return packet2;
	}

	public Packet get_packet() {
		return this.packet;
	}

	public static class SendPacket extends EventPacket {
		public SendPacket(Packet packet) {
			super(packet);
		}
	}

	public static class ReceivePacket extends EventPacket {
		public ReceivePacket(Packet packet) {
			super(packet);
		}
	}
}