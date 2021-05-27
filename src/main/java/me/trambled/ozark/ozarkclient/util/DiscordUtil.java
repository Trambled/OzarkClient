package me.trambled.ozark.ozarkclient.util;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;
import me.trambled.ozark.Ozark;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import static me.trambled.ozark.ozarkclient.util.WrapperUtil.mc;

// originally from emphack but most of that has been removed
public class DiscordUtil
{
    private static final DiscordRPC rpc;
    public static DiscordRichPresence presence;
    private static String details;
    private static String state;
    private static int index = 0;
    
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
                	} else if (Ozark.get_setting_manager().get_setting_with_tag("DiscordRPC", "RPCMode").in("lempity")) {
                		DiscordUtil.presence.largeImageKey = "lempity";
					} else if (Ozark.get_setting_manager().get_setting_with_tag("DiscordRPC", "RPCMode").in("kambing")) {
						DiscordUtil.presence.largeImageKey = "kambing";
                	} else if (Ozark.get_setting_manager().get_setting_with_tag("DiscordRPC", "RPCMode").in("Tudou")) {
                		index++;
                		if (index == 8) {
                			index = 1;
						}
                		DiscordUtil.presence.largeImageKey = "tudou" + index;
					}
					if (mc.world == null) {
						DiscordUtil.details = "In the menus";
						if (Ozark.get_setting_manager().get_setting_with_tag("DiscordRPC", "RPCSmallImage").in("Tudou")) {
							DiscordUtil.presence.smallImageKey = "tudousmall";
						} else {
							DiscordUtil.presence.smallImageKey = "troll";
						}
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
							DiscordUtil.state = "Configuring options";
						}
					} else {
						if (mc.player != null) {
							int health = Math.round(mc.player.getHealth() + mc.player.getAbsorptionAmount());

							// STATE
							if (get_state().equals("Health")) {
								DiscordUtil.state = health + " HP";
							} else if (get_state().equals("Server")) {
								if (mc.isIntegratedServerRunning()) {
									DiscordUtil.state = "Playing Singleplayer";
								} else {
									DiscordUtil.state = "Playing " + mc.getCurrentServerData().serverIP;
								}
							} else if (get_state().equals("Target")) {
								if (!Ozark.TARGET_NAME.equals("NULL")) {
									DiscordUtil.state = "Ezing " + Ozark.TARGET_NAME;
								} else {
									DiscordUtil.state = "Chilling";
								}
							} else if (get_state().equals("User")) {
								DiscordUtil.state = "Playing with " + Ozark.get_actual_user();
							} else if (get_state().equals("Speed")) {
								if (Double.parseDouble(PlayerUtil.speed()) > 15) {
									DiscordUtil.state = "Zoomin at " + PlayerUtil.speed() + " KPH";
								} else {
									DiscordUtil.state = "Chilling at " + PlayerUtil.speed() + " KPH";
								}
							}

							// DETAILS
							if (get_details().equals("Health")) {
								DiscordUtil.details = health + " HP";
							} else if (get_details().equals("Server")) {
								if (mc.isIntegratedServerRunning()) {
									DiscordUtil.details = "Playing Singleplayer";
								} else {
									DiscordUtil.details = "Playing " + mc.getCurrentServerData().serverIP;
								}
							} else if (get_details().equals("Target")) {
								if (!Ozark.TARGET_NAME.equals("NULL")) {
									DiscordUtil.details = "Ezing " + Ozark.TARGET_NAME;
								} else {
									DiscordUtil.details = "Chilling";
								}
							} else if (get_details().equals("User")) {
								DiscordUtil.details = "Playing with " + Ozark.get_actual_user();
							} else if (get_details().equals("Speed")) {
								if (Double.parseDouble(PlayerUtil.speed()) > 0) {
									DiscordUtil.details = "Zoomin at " + PlayerUtil.speed() + " KPH";
								} else {
									DiscordUtil.details = "Not moving/AFK";
								}
							}

							// SMALL IMAGE
							if (mc.isIntegratedServerRunning()) {
								if (Ozark.get_setting_manager().get_setting_with_tag("DiscordRPC", "RPCSmallImage").in("Tudou")) {
									DiscordUtil.presence.smallImageKey = "tudousmall";
								} else {
									DiscordUtil.presence.smallImageKey = "troll";
								}
							} else {
								if (Ozark.get_setting_manager().get_setting_with_tag("DiscordRPC", "RPCSmallImage").in("Tudou")) {
									DiscordUtil.presence.smallImageKey = "tudousmall";
								} else {
									if (mc.getCurrentServerData().serverIP.equals("aurorapvp.club") || mc.getCurrentServerData().serverIP.equals("auroraanarchy.org")) {
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
										DiscordUtil.presence.smallImageKey = "troll";
									}
								}
							}
						}
					}
					DiscordUtil.presence.details = DiscordUtil.details;
					DiscordUtil.presence.state = DiscordUtil.state;
					DiscordUtil.rpc.Discord_UpdatePresence(DiscordUtil.presence);
					try {
						Thread.sleep(2000L);
					}
					catch (InterruptedException e3) {
						e3.printStackTrace();
					}
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }, "Discord-RPC-Callback-Handler").start();
    }

    public static void stop() {
        DiscordRPC.INSTANCE.Discord_ClearPresence();
        DiscordRPC.INSTANCE.Discord_Shutdown();
    }

    private static String get_state() {
    	return Ozark.get_setting_manager().get_setting_with_tag("DiscordRPC", "RPCState").get_current_value();
	}

	private static String get_details() {
		return Ozark.get_setting_manager().get_setting_with_tag("DiscordRPC", "RPCDetails").get_current_value();
	}
    
    static {
        rpc = DiscordRPC.INSTANCE;
        DiscordUtil.presence = new DiscordRichPresence();
    }
}
