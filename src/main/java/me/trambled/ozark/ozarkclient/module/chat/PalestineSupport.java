package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.util.FriendUtil;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PalestineSupport extends Module {

    public PalestineSupport() {
        super(Category.CHAT);

        this.name = "PalestineSupport";
        this.tag = "PalestineSupport";
        this.description = "SAVE PALESTINE!!!!";
    }

    Setting delay = create("Delay", "Delay", 10, 0, 100);
    Setting test = create("Test", "Test", "big wall of text", "");

    List<String> chants = new ArrayList<>();

    Random r = new Random();

    int tick_delay;

    @Override
    protected void enable() {

        tick_delay = 0;

        chants.add("/w <player> SAVE PALESTINE!");
        chants.add("/w <player> Shame on Israel!");
        chants.add("/w <player> Israel isn't a state! Palestine is!");
        chants.add("/w <player> FREE PALESTINE!");
        chants.add("/w <player> God is with Palestine!");
        chants.add("/w <player> Israel is the real terrorist. Stop being ignorant!");
        chants.add("/w <player> I stand for Palestine!");
        chants.add("/w <player> Justice for Palestine!");
        chants.add("/w <player> You don't need to be a muslim to support Palestine, you just need to be a human!");
        chants.add("/w <player> Israel killed thousands of innocence, Israel is the real terrorist!");
        chants.add("/w <player> God is with Hamas!");
        chants.add("/w <player> Palestinians live matter!");
        chants.add("/w <player> Israel treat Palestinians as subhuman, just like what Hitler did to Jews!");

    }

    @Override
    public void update() {

        tick_delay++;
        if (tick_delay < delay.get_value(1)*10) return;

        String s = chants.get(r.nextInt(chants.size()));
        String name =  get_random_name();

        if (name.equals(mc.player.getName())) return;

        for (EntityPlayer friend : FriendUtil.get_friends()) {
            if (name.equals(friend.getName())) return;
        }

        mc.player.sendChatMessage(s.replace("<player>", name));

        tick_delay = 0;

    }

    public String get_random_name() {

        List<EntityPlayer> players = mc.world.playerEntities;

        return players.get(r.nextInt(players.size())).getName();

    }

}