package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;
import org.lwjgl.input.Keyboard;

//TODO: add timer to noweb like in konas
public class NoSlowDown extends Module {
    
    public NoSlowDown() {
        super(Category.MOVEMENT);

		this.name        = "NoSlow";
		this.tag         = "NoSlow";
		this.description = "Doesn't slow you down in certain shit.";
    }
	
	Setting webs = create("Webs", "Webs", true);
	Setting gui_move = create("GuiMove", "GuiMove", true);
	Setting strict = create("Strict", "Strict", false);
    Setting h_web = create("Horizontal Web", "HorizontalWeb", 2.00, 0, 100);
	Setting v_web = create("Vertical Web", "VerticalWeb", 2.00, 0, 100);
	
	private final static KeyBinding[] keys = new KeyBinding[]{NoSlowDown.mc.gameSettings.keyBindForward, NoSlowDown.mc.gameSettings.keyBindBack, NoSlowDown.mc.gameSettings.keyBindLeft, NoSlowDown.mc.gameSettings.keyBindRight, NoSlowDown.mc.gameSettings.keyBindJump, NoSlowDown.mc.gameSettings.keyBindSprint};
	
	@EventHandler //bope
	private final Listener<InputUpdateEvent> listener = new Listener<>(event -> {
		if (mc.player.isHandActive() && !mc.player.isRiding()) {
			event.getMovementInput().moveStrafe  *= 5;
			event.getMovementInput().moveForward *= 5;
		}
	});
	
	@Override //phobos
    public void update() {
        if (gui_move.get_value(true)) {
            if (NoSlowDown.mc.currentScreen instanceof GuiOptions || NoSlowDown.mc.currentScreen instanceof GuiVideoSettings || NoSlowDown.mc.currentScreen instanceof GuiScreenOptionsSounds || NoSlowDown.mc.currentScreen instanceof GuiContainer || NoSlowDown.mc.currentScreen instanceof GuiIngameMenu) {
                for (KeyBinding bind : keys) {
                    KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
                }
            } else if (NoSlowDown.mc.currentScreen == null) {
                for (KeyBinding bind : keys) {
                    if (Keyboard.isKeyDown(bind.getKeyCode())) continue;
                    KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                }
            }
        }
        if (this.webs.get_value(true) && NoSlowDown.mc.player.isInWeb) {
            NoSlowDown.mc.player.motionX *= this.h_web.get_value(1);
            NoSlowDown.mc.player.motionZ *= this.h_web.get_value(1);
            NoSlowDown.mc.player.motionY *= this.v_web.get_value(1);
        }
    }
	
	@EventHandler
    private final Listener<EventPacket.SendPacket> dostrict = new Listener<>(event -> {
        if (event.get_packet() instanceof CPacketPlayer && this.strict.get_value(true) && NoSlowDown.mc.player.isHandActive() && !NoSlowDown.mc.player.isRiding()) {
            NoSlowDown.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(Math.floor(NoSlowDown.mc.player.posX), Math.floor(NoSlowDown.mc.player.posY), Math.floor(NoSlowDown.mc.player.posZ)), EnumFacing.DOWN));
        }
    });
}
