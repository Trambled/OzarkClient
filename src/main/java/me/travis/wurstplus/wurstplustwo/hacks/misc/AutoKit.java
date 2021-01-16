package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.util.AutoKitUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.world.GameType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoKit extends WurstplusHack{

	
    public AutoKit() {

        super(WurstplusCategory.WURSTPLUS_MISC);

        this.name = "Auto Kit";
        this.tag = "AutoKit";
        this.description = "automatically selects a kit";
	}
	
	WurstplusSetting delay = create("Delay", "Delay", 1250, 0, 5000);
	
	@EventHandler
    private Listener<WurstplusEventPacket.ReceivePacket> receiveListener = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketRespawn && mc.player.isDead) {
            new Thread( ()->{
                try {
                    Thread.sleep(delay.get_value(1));
                    mc.player.sendChatMessage("/kit " + AutoKitUtil.get_message());
                } catch (InterruptedException e) {
                e.printStackTrace();
                }
            }).start();
        }
    });

    @Override
    protected void enable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    protected void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
 }