package me.trambled.ozark.ozarkclient.module.misc;

import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.player.AutoKitUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.SPacketRespawn;

public class AutoKit extends Module {

    public AutoKit() {
        super(Category.MISC);

        this.name = "AutoKit";
        this.tag = "AutoKit";
        this.description = "Automatically selects a kit.";
    }

	Setting aurora = create("Aurora", "AutoKitAurora", false);

	@EventHandler
    private final Listener<EventPacket.ReceivePacket> receiveListener = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketRespawn && mc.player.isDead) {
            new Thread( ()->{
                try {
                    Thread.sleep(4000);
					if (aurora.get_value(true)) {
						mc.player.sendChatMessage("/pvpkit " + AutoKitUtil.get_message());
					} else {
						mc.player.sendChatMessage("/kit " + AutoKitUtil.get_message());
					}
                } catch (InterruptedException e) {
                e.printStackTrace();
                }
            }).start();
        }
    });
 }