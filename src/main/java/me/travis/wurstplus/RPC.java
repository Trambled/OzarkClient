package me.travis.wurstplus;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;

public class RPC
{
    private static final Minecraft mc;

    private static final DiscordRPC rpc;

    public static DiscordRichPresence presence;

    private static String details;

    private static String state;
    
    public static void init() {
        //TODO: makes it so that it shows the server image on the small image
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + String.valueOf(var1) + ", var2: " + var2));
        RPC.rpc.Discord_Initialize("785682576110518293", handlers, true, "");
        RPC.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        RPC.presence.details = mc.player.getHealth()+mc.player.getAbsorptionAmount() + " HP";
        RPC.presence.largeImageKey = "ozark_2";
        RPC.presence.largeImageText = "OzarkClient " + Wurstplus.WURSTPLUS_VERSION;
        RPC.presence.smallImageKey = "troll";
        RPC.presence.smallImageText = "ez";
        RPC.rpc.Discord_UpdatePresence(RPC.presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    RPC.rpc.Discord_RunCallbacks();
                    RPC.details = "";
                    RPC.state = "";
					
					if (mc.world == null) {
						RPC.details = "In the menus";
                        RPC.presence.smallImageKey = "troll";
						if (mc.currentScreen instanceof GuiWorldSelection) {
							RPC.state = "Selecting a world to play on";
						} else if (mc.currentScreen instanceof GuiMainMenu) {
                            RPC.state = "In the Main Menu";
                        } else if (mc.currentScreen instanceof GuiOptions) {
                            RPC.state = "Configuring options";
						} else if (mc.currentScreen instanceof GuiMultiplayer){
							RPC.state = "Selecting a server to play on";
						} else if (mc.currentScreen instanceof GuiScreenResourcePacks) {
						    RPC.state = "Selecting a resource pack";
                        } else {
						    RPC.state = "Doing something";
                        }
					} else {
						if (mc.player != null) {
							RPC.state = mc.player.getHealth()+mc.player.getAbsorptionAmount() + " HP";
							if (mc.isIntegratedServerRunning()) {
								RPC.details = "Playing Singleplayer";
							} else {
								RPC.details = "Playing " + mc.getCurrentServerData().serverIP;

								if (mc.getCurrentServerData().serverIP.equals("aurorapvp.club") || mc.getCurrentServerData().serverIP.equals("auroraanarchy.org")) {
                                    RPC.presence.smallImageKey = "aurora";
                                } else if (mc.getCurrentServerData().serverIP.equals("8b8t.xyz")) {
                                    RPC.presence.smallImageKey = "8b8t";
                                } else if (mc.getCurrentServerData().serverIP.equals("0b0t.org")) {
                                    RPC.presence.smallImageKey = "0b0t";
                                } else if (mc.getCurrentServerData().serverIP.equals("5b5t.org")) {
                                    RPC.presence.smallImageKey = "5b5t";
                                } else if (mc.getCurrentServerData().serverIP.equals("9b9t.com") || mc.getCurrentServerData().serverIP.equals("9b9t.org")) {
                                    RPC.presence.smallImageKey = "9b9t";
                                } else if (mc.getCurrentServerData().serverIP.equals("bedtrap.org")) {
                                    RPC.presence.smallImageKey = "bedtrap";
                                } else if (mc.getCurrentServerData().serverIP.equals("constantiam.net")) {
                                    RPC.presence.smallImageKey = "const";
                                } else if (mc.getCurrentServerData().serverIP.equals("l2x9.org")) {
                                    RPC.presence.smallImageKey = "l2";
                                } else if (mc.getCurrentServerData().serverIP.equals("matrixanarchy.net")) {
                                    RPC.presence.smallImageKey = "matrix";
                                } else if (mc.getCurrentServerData().serverIP.equals("cpe2.ign.gg")) {
                                    RPC.presence.smallImageKey = "cpe";
                                } else if (mc.getCurrentServerData().serverIP.equals("oldfag.org")) {
                                    RPC.presence.smallImageKey = "oldfag";
                                } else if (mc.getCurrentServerData().serverIP.equals("openanarchy.org")) {
                                    RPC.presence.smallImageKey = "oa";
                                } else if (mc.getCurrentServerData().serverIP.equals("eliteanarchy.org")) {
                                    RPC.presence.smallImageKey = "elite";
                                } else if (mc.getCurrentServerData().serverIP.equals("6b6t.co")) {
                                    RPC.presence.smallImageKey = "6b6t";
                                } else if (mc.getCurrentServerData().serverIP.equals("2b2t.org")) {
                                    RPC.presence.smallImageKey = "2b2t";
                                } else if (mc.getCurrentServerData().serverIP.equals("2b2tpvp.net")) {
                                    RPC.presence.smallImageKey = "2bpvp";
                                } else if (mc.getCurrentServerData().serverIP.equals("us.crystalpvp.cc") || mc.getCurrentServerData().serverIP.equals("tcp.crystalpvp.cc") || mc.getCurrentServerData().serverIP.equals("crystalpvp.cc")) {
                                    RPC.presence.smallImageKey = "cc";
                                } else {
                                    RPC.presence.smallImageKey = "troll";
                                }
							}
						}
					}

                    RPC.presence.details = RPC.details;
                    RPC.presence.state = RPC.state;
                    RPC.rpc.Discord_UpdatePresence(RPC.presence);
                    
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
        rpc = DiscordRPC.INSTANCE;
        RPC.presence = new DiscordRichPresence();
    }
}
