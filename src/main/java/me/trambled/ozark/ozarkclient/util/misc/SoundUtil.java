package me.trambled.ozark.ozarkclient.util.misc;

import java.io.InputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class SoundUtil
{
    public static final SoundUtil INSTANCE;
    public ResourceLocation skeet;
    public ResourceLocation neverlose;
    public ResourceLocation ez4ence;

    public SoundUtil() {
        this.skeet = new ResourceLocation("audio/skeet.wav");
        this.neverlose = new ResourceLocation("audio/neverlose.wav");
        this.ez4ence = new ResourceLocation("audio/ez4ence.wav");

    }

    public static void playSound(final ResourceLocation rl) {
        try {
            final InputStream sound = Minecraft.getMinecraft().getResourceManager().getResource(rl).getInputStream();
            final AudioStream as = new AudioStream(sound);
            AudioPlayer.player.start((InputStream)as);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        INSTANCE = new SoundUtil();
    }
}

