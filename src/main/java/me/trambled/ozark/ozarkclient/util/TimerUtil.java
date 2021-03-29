package me.trambled.ozark.ozarkclient.util;

public class TimerUtil {

    private long time;

    public TimerUtil() {
        this.time = -1L;
    }

    public boolean passed(final long ms) {
        return this.getTime(System.nanoTime() - this.time) >= ms;
    }

    public void resetTimeSkipTo(final long ms)
    {
        this.time = System.nanoTime() + ms;
    }

    public void reset() {
        this.time = System.nanoTime();
    }

    public long getTime(final long time) {
        return time / 1000000L;
    }

}