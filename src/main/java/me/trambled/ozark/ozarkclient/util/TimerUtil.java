package me.trambled.ozark.ozarkclient.util;

import net.minecraft.client.Minecraft;

public class TimerUtil {

    private long time;

    public TimerUtil() {
        this.time = -1L;
    }

    public boolean passed(final long ms) {
        return this.getTime(System.nanoTime() - this.time) >= ms;
    }

    public void resetTimeSkipTo(final long ms) {
        this.time = System.nanoTime() + ms;
    }

    public void reset() {
        this.time = System.nanoTime();
    }

    public long getTime(final long time) {
        return time / 1000000L;
    }

    public boolean hasPassed(long time) {
        return Minecraft.getMinecraft().player.ticksExisted % (int) time == 0;
    }
}
