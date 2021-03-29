package me.trambled.ozark.ozarkclient.manager;

import me.trambled.turok.draw.RenderHelp;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.command.Commands;
import me.trambled.ozark.ozarkclient.event.EventBus;
import me.trambled.ozark.ozarkclient.event.events.EventGameOverlay;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

// External.
// Travis.


public class EventManager {

	private final Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.isCanceled()) {
			return;
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (mc.player == null) {
			return;
		}

		Ozark.get_hack_manager().update();
	}

	@SubscribeEvent
	public void onWorldRender(RenderWorldLastEvent event) {
		if (event.isCanceled()) {
			return;
		}

		Ozark.get_hack_manager().render(event);
	}

	@SubscribeEvent
	public void onRender(RenderGameOverlayEvent.Post event) {

		if (event.isCanceled()) {
			return;
		}

		EventBus.EVENT_BUS.post(new EventGameOverlay(event.getPartialTicks(), new ScaledResolution(mc)));

		RenderGameOverlayEvent.ElementType target = RenderGameOverlayEvent.ElementType.EXPERIENCE;

		if (!mc.player.isCreative() && mc.player.getRidingEntity() instanceof AbstractHorse) {
			target = RenderGameOverlayEvent.ElementType.HEALTHMOUNT;
		}

		if (event.getType() == target) {
			Ozark.get_hack_manager().render();

			if (!Ozark.get_hack_manager().get_module_with_tag("GUI").is_active()) {
				Ozark.get_hud_manager().render();
			}

			GL11.glPushMatrix();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);

			GlStateManager.enableBlend();

			GL11.glPopMatrix();

			RenderHelp.release_gl();
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (Keyboard.getEventKeyState()) {
			Ozark.get_hack_manager().bind(Keyboard.getEventKey());
			Ozark.get_setting_manager().bind(Keyboard.getEventKey());
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onChat(ClientChatEvent event) {
		String   message      = event.getMessage();
		String[] message_args = CommandManager.command_list.get_message(event.getMessage());

		boolean true_command = false;

		if (message_args.length > 0) {

			event.setCanceled(true);

			mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());

			for (Command command : Commands.get_pure_command_list()) {
				try {
					if (CommandManager.command_list.get_message(event.getMessage())[0].equalsIgnoreCase(command.get_name())) {
						true_command = command.get_message(CommandManager.command_list.get_message(event.getMessage()));
					}
				} catch (Exception ignored) {}
			}

			if (!true_command && CommandManager.command_list.has_prefix(event.getMessage())) {
				MessageUtil.send_client_message("Try using " + CommandManager.get_prefix() + "help list to see all commands");

				true_command = false;
			}
		}
	}

	@SubscribeEvent
	public void onInputUpdate(InputUpdateEvent event) {
		EventBus.EVENT_BUS.post(event);
	}

    @SubscribeEvent
    public void onRenderBlockOverlay(RenderBlockOverlayEvent event)
    {
        EventBus.EVENT_BUS.post(event);
    }


}