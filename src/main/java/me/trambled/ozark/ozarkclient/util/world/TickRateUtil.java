package me.trambled.ozark.ozarkclient.util.world;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketTimeUpdate;

//Mmmmm ... Pepsimod
public class TickRateUtil {

    public static float TPS = 20.0f;
    public static long lastUpdate = -1;

    public static void update(final Packet packet) {
        if (!(packet instanceof SPacketTimeUpdate)) {
            return;
        }

        final long currentTime = System.currentTimeMillis();

        if (lastUpdate == -1) {
            lastUpdate = currentTime;
            return;
        }

        long timeDiff = currentTime - lastUpdate;
        float tickTime = timeDiff / 20F;
        if (tickTime == 0) {
            tickTime = 50;
        }
        float tps = 1000 / tickTime;
        if (tps > 20.0f) {
            tps = 20.0f;
        }
        TPS = tps;
        lastUpdate = currentTime;
    }

    public static void reset() {
        TPS = 20.0f;
    }

}
