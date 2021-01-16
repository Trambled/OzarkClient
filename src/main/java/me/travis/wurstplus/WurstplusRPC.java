package me.travis.wurstplus;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;
import net.minecraft.client.Minecraft;
import me.travis.wurstplus.Wurstplus;

public class WurstplusRPC 
{
    private static final String ClientId = "785682576110518293";

    private static final Minecraft mc;

    private static final DiscordRPC rpc;

    public static DiscordRichPresence presence;

    private static String details;

    private static String state;
    
    public static void init() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + String.valueOf(var1) + ", var2: " + var2));
        WurstplusRPC.rpc.Discord_Initialize("785682576110518293", handlers, true, "");
        WurstplusRPC.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        WurstplusRPC.presence.details = mc.player.getHealth()+mc.player.getAbsorptionAmount() + " HP";
        WurstplusRPC.presence.largeImageKey = "ozark_2";
        WurstplusRPC.presence.largeImageText = "OzarkClient " + Wurstplus.WURSTPLUS_VERSION;
        WurstplusRPC.presence.smallImageKey = "troll";
        WurstplusRPC.presence.smallImageText = "ez";
        WurstplusRPC.rpc.Discord_UpdatePresence(WurstplusRPC.presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    WurstplusRPC.rpc.Discord_RunCallbacks();
                    WurstplusRPC.details = mc.player.getHealth()+mc.player.getAbsorptionAmount() + " HP";
                    WurstplusRPC.state = "";
                    if (WurstplusRPC.mc.isIntegratedServerRunning()) {
                        WurstplusRPC.state = "Playing Singleplayer";
                    }
                    else if (WurstplusRPC.mc.getCurrentServerData() != null) {
                        if (!WurstplusRPC.mc.getCurrentServerData().serverIP.equals("")) {
                            WurstplusRPC.state = "Playing " + WurstplusRPC.mc.getCurrentServerData().serverIP;
                        }
                    }
                    else {
                        WurstplusRPC.state = "Main Menu";
                    }
                    if (!WurstplusRPC.details.equals(WurstplusRPC.presence.details) || !WurstplusRPC.state.equals(WurstplusRPC.presence.state)) {
                        WurstplusRPC.presence.startTimestamp = System.currentTimeMillis() / 1000L;
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
    /**@Floppinqq
      @12/29/2020
      @FloppaHack-b1.1
     **/
    
    static {
        mc = Minecraft.getMinecraft();
        rpc = DiscordRPC.INSTANCE;
        WurstplusRPC.presence = new DiscordRichPresence();
    }
}
