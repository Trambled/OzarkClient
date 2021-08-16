package me.trambled.ozark.ozarkclient.event;

import me.zero.alpine.fork.event.type.Cancellable;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;


public class Event extends Cancellable {
	private final Era era_switch = Era.EVENT_PRE;
	private final float partial_ticks;

	public Event() {
		partial_ticks = mc.getRenderPartialTicks();
	}

	public Era get_era() {
		return era_switch;
	}

	public float get_partial_ticks() {
		return partial_ticks;
	}

	public enum Era {
		EVENT_PRE,
		EVENT_PERI,
		EVENT_POST
	}
}