package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutoRacist extends Module {
    
    public AutoRacist() {
        super(Category.CHAT);

        this.name = "Auto Racist";
        this.tag = "AutoRacist";
        this.description = "edgy....";
    }

    Setting delay = create("Delay", "Delay", 10, 0, 100);

    List<String> chants = new ArrayList<>();

    Random r = new Random();

    int tick_delay;

    @Override
    protected void enable() {
        
        tick_delay = 0;

        chants.add("PICK MY COTTON NIGGER");
        chants.add("BLACK NIGGER COON");
        chants.add("KIKE");
        chants.add("I <3 THE CONFEDERACY");
        chants.add("NIGGERS BELONG ON THE BACK OF THE BUS");
        chants.add("KICK THAT NIGGER BITCH OFF THE PLANE!!");
        chants.add("DEATH TO THE KIKES");
        chants.add("HEIL HITLER");
        chants.add("I <3 THE KKK");
        chants.add("#FREEDEREKCHAUVIN");
        chants.add("I HATE JEWS, FAGGOTS, NIGGERS, AND SPICS");

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
