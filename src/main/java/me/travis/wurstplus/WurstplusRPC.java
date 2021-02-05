package me.travis.wurstplus;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import me.travis.wurstplus.Wurstplus;

// mostly from emphack and bope, i modified some shit tho :D
public class WurstplusRPC 
{
    private static final String ClientId = "785682576110518293";

    private static final Minecraft mc;

    private static DiscordRPC rpc;

    public static DiscordRichPresence presence;

    private static String details;

    private static String state;
	
	public WurstplusRPC(String tag) {
		presence = new DiscordRichPresence(); 
		rpc = DiscordRPC.INSTANCE;
	}
    
    public static void init() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + String.valueOf(var1) + ", var2: " + var2));
        WurstplusRPC.rpc.Discord_Initialize("785682576110518293", handlers, true, "");
        WurstplusRPC.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        WurstplusRPC.presence.details = mc.player.getHealth()+mc.player.getAbsorptionAmount() + " HP";
        WurstplusRPC.presence.largeImageKey = "ozark_2";
        WurstplusRPC.presence.largeImageText = "OzarkClient " + Wurstplus.WURSTPLUS_VERSION;
        WurstplusRPC.presence.smallImageText = "ez";
        WurstplusRPC.rpc.Discord_UpdatePresence(WurstplusRPC.presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    WurstplusRPC.rpc.Discord_RunCallbacks();
                    WurstplusRPC.details = "";
                    WurstplusRPC.state = "";
					
					if (mc.world == null) {
						
						WurstplusRPC.presence.smallImageKey = "troll";
						
						WurstplusRPC.details = "In the menus";
						if (mc.currentScreen instanceof GuiWorldSelection) {
							WurstplusRPC.state = "Selecting a world to play on";
						} else if (mc.currentScreen instanceof GuiMainMenu) {
                            WurstplusRPC.state = "In the Main Menu";
                        } else if (mc.currentScreen instanceof GuiOptions) {
                            WurstplusRPC.state = "Configuring options";
						} else if (mc.currentScreen instanceof GuiMultiplayer) {
							WurstplusRPC.state = "Selecting a server to play on";
						} else {
							WurstplusRPC.state = "idk what hes doing";
						}
					} else {
						if (mc.player != null) {
							WurstplusRPC.state = mc.player.getHealth()+mc.player.getAbsorptionAmount() + " HP";
							if (mc.isIntegratedServerRunning()) {
								WurstplusRPC.details = "Playing Singleplayer";
								WurstplusRPC.presence.smallImageKey = "troll";
							} else {
								WurstplusRPC.details = "Playing " + mc.getCurrentServerData().serverIP;
								WurstplusRPC.presence.smallImageKey = "troll";
								
								//rip small image didnt work cuz idk how to get server images :c
							}
						}
					}

                    WurstplusRPC.presence.details = WurstplusRPC.details;
                    WurstplusRPC.presence.state = WurstplusRPC.state;
                    WurstplusRPC.rpc.Discord_UpdatePresence(WurstplusRPC.presence);
                    
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    Thread.sleep(5000L);
                }
                catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
        }, "Discord-RPC-Callback-Handler").start();
    }

    public static void stop() {
        DiscordRPC.INSTANCE.Discord_ClearPresence();
        DiscordRPC.INSTANCE.Discord_Shutdown();
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
