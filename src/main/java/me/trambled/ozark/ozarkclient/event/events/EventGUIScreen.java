package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;
import net.minecraft.client.gui.GuiScreen;

// External.


public class EventGUIScreen extends Event {
	private final GuiScreen guiscreen;

	public EventGUIScreen(GuiScreen screen) {
		super();

		guiscreen = screen;
	}

	public GuiScreen get_guiscreen() {
		return guiscreen;
	}
}