package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutoGroom extends Module {
    
    public AutoGroom() {
        super(Category.CHAT);

        this.name = "Auto Groom";
        this.tag = "AutoGroom";
        this.description = "EDP445, james charles, jefferey epstein, etc";
    }

    Setting delay = create("Delay", "Delay", 10, 0, 100);

    List<String> chants = new ArrayList<>();

    Random r = new Random();

    int tick_delay;

    @Override
    protected void enable() {
        
        tick_delay = 0;

        chants.add("/w <player> heyyyyy");
        chants.add("/w <player> how old are you??");
        chants.add("/w <player> you look like such a big boy...");
        chants.add("/w <player> hi cutie");
        chants.add("/w <player> can i fart in your mouth");
        chants.add("/w <player> can i be your sussy little baka??");
        chants.add("/w <player> whats my kitten doing talking to another man?? so sussy...");
        chants.add("/w <player> ill touch you inappropriately with the power of Ozark Client");
        chants.add("/w <player> we can play nekopara in the bachi's office together my kitten ");
        chants.add("/w <player> age is just a number my kitten");
        
    }

    @Override
    public void update() {
        
        tick_delay++;
        if (tick_delay < delay.get_value(1)*10) return;

        String s = chants.get(r.nextInt(chants.size()));
        String name =  get_random_name();
        
        if (name.equals(mc.player.getName())) return;

        mc.player.sendChatMessage(s.replace("<player>", name));

        tick_delay = 0;

    }

    public String get_random_name() {

        List<EntityPlayer> players = mc.world.playerEntities;

        return players.get(r.nextInt(players.size())).getName();

    }

}
