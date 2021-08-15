package me.trambled.ozark.ozarkclient.util.misc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.util.player.PlayerUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;

import java.util.Objects;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;

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
        handlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));
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
                		if (index >= 8) {
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
							switch (get_state()) {
								case "Health":
									DiscordUtil.state = health + " HP";
									break;
								case "Server":
									if (mc.isIntegratedServerRunning()) {
										DiscordUtil.state = "Playing Singleplayer";
									} else {
										DiscordUtil.state = "Playing " + Objects.requireNonNull ( mc.getCurrentServerData ( ) ).serverIP;
									}
									break;
								case "Target":
									if (!Ozark.TARGET_NAME.equals("NULL")) {
										DiscordUtil.state = "Ezing " + Ozark.TARGET_NAME;
									} else {
										DiscordUtil.state = "Chilling";
									}
									break;
								case "User":
									DiscordUtil.state = "Playing with " + Ozark.get_actual_user();
									break;
								case "Speed":
									if (Double.parseDouble(PlayerUtil.speed()) > 15) {
										DiscordUtil.state = "Zoomin at " + PlayerUtil.speed() + " KPH";
									} else {
										DiscordUtil.state = "Chilling at " + PlayerUtil.speed() + " KPH";
									}
									break;
							}

							// DETAILS
							switch (get_details()) {
								case "Health":
									DiscordUtil.details = health + " HP";
									break;
								case "Server":
									if (mc.isIntegratedServerRunning()) {
										DiscordUtil.details = "Playing Singleplayer";
									} else {
										DiscordUtil.details = "Playing " + Objects.requireNonNull ( mc.getCurrentServerData ( ) ).serverIP;
									}
									break;
								case "Target":
									if (!Ozark.TARGET_NAME.equals("NULL")) {
										DiscordUtil.details = "Ezing " + Ozark.TARGET_NAME;
									} else {
										DiscordUtil.details = "Chilling";
									}
									break;
								case "User":
									DiscordUtil.details = "Playing with " + Ozark.get_actual_user();
									break;
								case "Speed":
									if (Double.parseDouble(PlayerUtil.speed()) > 0) {
										DiscordUtil.details = "Zoomin at " + PlayerUtil.speed() + " KPH";
									} else {
										DiscordUtil.details = "Not moving/AFK";
									}
									break;
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
									switch (Objects.requireNonNull ( mc.getCurrentServerData ( ) ).serverIP) {
										case "aurorapvp.club":
										case "auroraanarchy.org":
											DiscordUtil.presence.smallImageKey = "aurora";
											break;
										case "8b8t.xyz":
											DiscordUtil.presence.smallImageKey = "8b8t";
											break;
										case "0b0t.org":
											DiscordUtil.presence.smallImageKey = "0b0t";
											break;
										case "5b5t.org":
											DiscordUtil.presence.smallImageKey = "5b5t";
											break;
										case "9b9t.com":
										case "9b9t.org":
											DiscordUtil.presence.smallImageKey = "9b9t";
											break;
										case "bedtrap.org":
											DiscordUtil.presence.smallImageKey = "bedtrap";
											break;
										case "constantiam.net":
											DiscordUtil.presence.smallImageKey = "const";
											break;
										case "l2x9.org":
											DiscordUtil.presence.smallImageKey = "l2";
											break;
										case "matrixanarchy.net":
											DiscordUtil.presence.smallImageKey = "matrix";
											break;
										case "cpe2.ign.gg":
											DiscordUtil.presence.smallImageKey = "cpe";
											break;
										case "oldfag.org":
											DiscordUtil.presence.smallImageKey = "oldfag";
											break;
										case "openanarchy.org":
											DiscordUtil.presence.smallImageKey = "oa";
											break;
										case "eliteanarchy.org":
											DiscordUtil.presence.smallImageKey = "elite";
											break;
										case "6b6t.co":
											DiscordUtil.presence.smallImageKey = "6b6t";
											break;
										case "2b2t.org":
											DiscordUtil.presence.smallImageKey = "2b2t";
											break;
										case "2b2tpvp.net":
											DiscordUtil.presence.smallImageKey = "2bpvp";
											break;
										case "us.crystalpvp.cc":
										case "crystalpvp.cc":
											DiscordUtil.presence.smallImageKey = "cc";
											break;
										default:
											DiscordUtil.presence.smallImageKey = "troll";
											break;
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
