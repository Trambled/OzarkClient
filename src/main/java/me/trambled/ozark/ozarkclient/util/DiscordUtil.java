package me.trambled.ozark.ozarkclient.util;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;
import me.trambled.ozark.Ozark;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;

// originally from emphack but most of that has been removed
public class DiscordUtil
{
    private static final Minecraft mc;

    private static final DiscordRPC rpc;

    public static DiscordRichPresence presence;

    private static String details;

    private static String state;
    
    public static void init() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + String.valueOf(var1) + ", var2: " + var2));
        DiscordUtil.rpc.Discord_Initialize("785682576110518293", handlers, true, "");
        DiscordUtil.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        DiscordUtil.presence.largeImageKey = "ozark_2";
        DiscordUtil.presence.largeImageText = "OzarkClient " + Ozark.VERSION;
        DiscordUtil.presence.smallImageKey = "troll";
        DiscordUtil.presence.smallImageText = "ozark client on top!";
        DiscordUtil.rpc.Discord_UpdatePresence(DiscordUtil.presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    	DiscordUtil.rpc.Discord_RunCallbacks();
                    	DiscordUtil.details = "";
                    	DiscordUtil.state = "";
                    	if (Ozark.get_setting_manager().get_setting_with_tag("DiscordRPC", "RPCMode").in("Normal")) {
                        	DiscordUtil.presence.largeImageKey = "ozark_2";
                    	} else {
                        	DiscordUtil.presence.largeImageKey = "lempity";
                    	}
					
		    	if (mc.world == null) {
				DiscordUtil.details = "In the menus";
                        	DiscordUtil.presence.smallImageKey = "troll";
				//idk i just wanted it to do everything lol
		    		if (mc.currentScreen instanceof GuiWorldSelection) {
					DiscordUtil.state = "Selecting a world to play on";
		 		} else if (mc.currentScreen instanceof GuiMainMenu) {
                            		DiscordUtil.state = "In the Main Menu";
                        	} else if (mc.currentScreen instanceof GuiOptions) {
                            		DiscordUtil.state = "Configuring options";
				} else if (mc.currentScreen instanceof GuiMultiplayer || mc.currentScreen instanceof GuiScreenAddServer || mc.currentScreen instanceof GuiScreenServerList) {
					DiscordUtil.state = "Selecting a server to play on";
				} else if (mc.currentScreen instanceof GuiScreenResourcePacks) {
                            		DiscordUtil.state = "Selecting a resource pack";
                        	} else if (mc.currentScreen instanceof GuiDisconnected) {
                            		DiscordUtil.state = "Disconnected from server :c";
                        	} else if (mc.currentScreen instanceof GuiConnecting) {
                            		DiscordUtil.state = "Connecting to a server";
                        	} else if (mc.currentScreen instanceof GuiCreateFlatWorld || mc.currentScreen instanceof GuiCreateWorld) {
                            		DiscordUtil.state = "Creating world";
                       		} else {
					// im p sure i covered everything except the exact options
                            		// so ig this is heere
					DiscordUtil.state = "Configuring options";
				}
                        } else {
				if (mc.player != null) {
					int health = Math.round(mc.player.getHealth()+mc.player.getAbsorptionAmount());
					DiscordUtil.state = health + " HP";
					if (mc.isIntegratedServerRunning()) {
						DiscordUtil.details = "Playing Singleplayer";
					} else if (!mc.isIntegratedServerRunning()) {
						DiscordUtil.details = "Playing " + mc.getCurrentServerData().serverIP;
					}

					if (mc.getCurrentServerData().serverIP.equals("aurorapvp.club") || mc.getCurrentServerData().serverIP.equals("auroraanarchy.org")) {
                                    		// doesnt like aurora and aurora anarchy have a slightly different logo?
                                    		// either way idc lol
						DiscordUtil.presence.smallImageKey = "aurora";
                                	} else if (mc.getCurrentServerData().serverIP.equals("8b8t.xyz")) {
                                    		DiscordUtil.presence.smallImageKey = "8b8t";
                                	} else if (mc.getCurrentServerData().serverIP.equals("0b0t.org")) {
                                    		DiscordUtil.presence.smallImageKey = "0b0t";
                                	} else if (mc.getCurrentServerData().serverIP.equals("5b5t.org")) {
                                    		DiscordUtil.presence.smallImageKey = "5b5t";
                                	} else if (mc.getCurrentServerData().serverIP.equals("9b9t.com") || mc.getCurrentServerData().serverIP.equals("9b9t.org")) {
                                    		DiscordUtil.presence.smallImageKey = "9b9t";
                                	} else if (mc.getCurrentServerData().serverIP.equals("bedtrap.org")) {
                                    		DiscordUtil.presence.smallImageKey = "bedtrap";
                                	} else if (mc.getCurrentServerData().serverIP.equals("constantiam.net")) {
                                    		DiscordUtil.presence.smallImageKey = "const";
                                	} else if (mc.getCurrentServerData().serverIP.equals("l2x9.org")) {
                                    		DiscordUtil.presence.smallImageKey = "l2";
                                	} else if (mc.getCurrentServerData().serverIP.equals("matrixanarchy.net")) {
                                    		DiscordUtil.presence.smallImageKey = "matrix";
                                	} else if (mc.getCurrentServerData().serverIP.equals("cpe2.ign.gg")) {
                                    		DiscordUtil.presence.smallImageKey = "cpe";
                                	} else if (mc.getCurrentServerData().serverIP.equals("oldfag.org")) {
                                   		DiscordUtil.presence.smallImageKey = "oldfag";
                                	} else if (mc.getCurrentServerData().serverIP.equals("openanarchy.org")) {
                                    		DiscordUtil.presence.smallImageKey = "oa";
                                	} else if (mc.getCurrentServerData().serverIP.equals("eliteanarchy.org")) {
                                    		DiscordUtil.presence.smallImageKey = "elite";
                                	} else if (mc.getCurrentServerData().serverIP.equals("6b6t.co")) {
                                    		DiscordUtil.presence.smallImageKey = "6b6t";
                                	} else if (mc.getCurrentServerData().serverIP.equals("2b2t.org")) {
                                    		DiscordUtil.presence.smallImageKey = "2b2t";
                                	} else if (mc.getCurrentServerData().serverIP.equals("2b2tpvp.net")) {
                                    		DiscordUtil.presence.smallImageKey = "2bpvp";
                                	} else if (mc.getCurrentServerData().serverIP.equals("us.crystalpvp.cc") || mc.getCurrentServerData().serverIP.equals("crystalpvp.cc")) {
                                    		DiscordUtil.presence.smallImageKey = "cc";
                                	} else {
						//trol
                                    		DiscordUtil.presence.smallImageKey = "troll";
                                	}
				}
			}
						

                    	DiscordUtil.presence.details = DiscordUtil.details;
                    	DiscordUtil.presence.state = DiscordUtil.state;
                    	DiscordUtil.rpc.Discord_UpdatePresence(DiscordUtil.presence);
                    
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
        DiscordUtil.presence = new DiscordRichPresence();
    }
}
