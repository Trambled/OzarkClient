package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.FriendUtil;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutoGroom extends Module {
    
    public AutoGroom() {
        super(Category.CHAT);

        this.name = "WhisperSpam";
        this.tag = "WhisperSpam";
        this.description = "ur mom";
    }

    Setting delay = create("Delay", "Delay", 10, 0, 100);
    Setting mode = create("Mode", "modeurmom", "Racist", combobox("Racist", "no racist", "snine mode", "groomer"));

    List<String> chants = new ArrayList<>();

    Random r = new Random();

    int tick_delay;

    @Override
    protected void enable() {
        tick_delay = 0;
        chants.clear();

        if (mode.in("Racist")) {
            chants.clear();
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
            chants.add("I HATE JEWS, FAGGOTS, NIGGERS, AND SPICS"); // hating jews is nott racist wtf is this bruh

        }else if (mode.in("groomer")) {
            chants.clear();
            chants.add("/w <player> heyyyyy");
            chants.add("/w <player> how old are you??");
            chants.add("/w <player> you look like such a big boy...");
            chants.add("/w <player> hi cutie");
            chants.add("/w <player> can i fart in your mouth");
            chants.add("/w <player> can i be your little sussy baka??");
            chants.add("/w <player> whats my kitten doing talking to another man?? so sussy...");
            chants.add("/w <player> ill touch you inappropriately with the power of Ozark Client");
            chants.add("/w <player> we can play nekopara in bachi's office together my kitten ");
            chants.add("/w <player> age is just a number my kitten");

        }else if (mode.in("no racist")) {
            chants.clear();
            chants.add("<player> you fucking racist");
            chants.add("RIP GEORGE FLOYD");
            chants.add("#BLM");
            chants.add("#ICANTBREATHE");
            chants.add("#NOJUSTICENOPEACE");
            chants.add("IM NOT BLACK BUT I STAND WITH YOU");
            chants.add("END RACISM, JOIN EMPERIUM");
            chants.add("DEFUND THE POLICE");
            chants.add("<player> I HOPE YOU POSTED YOUR BLACK SQUARE");
            chants.add("RESPECT BLM");
            chants.add("IF YOURE NOT WITH US, YOURE AGAINST US");
            chants.add("DEREK CHAUVIN WAS A RACIST");

        }else if (mode.in("snine mode")) {
            chants.clear();
            chants.add("ALLAHU AKBAR");
            chants.add("HARAM PIGGIES DIE FOR ALLAH");
            chants.add("#FREEPALESTINE");
            chants.add("BISMILLAH AL RAHMAN AL RAHIM");
            chants.add("MUHAMMAD PEACE BE UPON HIM");
            chants.add("I PRAY TO ALLAH 5 TIMES A DAY");
            chants.add("DEATH TO STINKY HARAM INFIDELS");
            chants.add("PORK = HARAM");

        }
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
