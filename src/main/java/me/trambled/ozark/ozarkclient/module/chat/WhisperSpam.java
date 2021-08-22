package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class WhisperSpam extends Module {
    
    public WhisperSpam() {
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
            chants.add("/w <player> PICK MY COTTON NIGGER");
            chants.add("/w <player> BLACK NIGGER COON");
            chants.add("/w <player> KIKE");
            chants.add("/w <player> I <3 THE CONFEDERACY");
            chants.add("/w <player> NIGGERS BELONG ON THE BACK OF THE BUS");
            chants.add("/w <player> KICK THAT NIGGER BITCH OFF THE PLANE!!");
            chants.add("/w <player> DEATH TO THE KIKES");
            chants.add("/w <player> HEIL HITLER");
            chants.add("/w <player> I <3 THE KKK");
            chants.add("/w <player> #FREEDEREKCHAUVIN");
            chants.add("/w <player> I HATE JEWS, FAGGOTS, NIGGERS, AND SPICS"); // hating jews is nott racist wtf is this bruh

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
            chants.add("/w <player> you fucking racist");
            chants.add("/w <player> RIP GEORGE FLOYD");
            chants.add("/w <player> #BLM");
            chants.add("/w <player> #ICANTBREATHE");
            chants.add("/w <player> #NOJUSTICENOPEACE");
            chants.add("/w <player> IM NOT BLACK BUT I STAND WITH YOU");
            chants.add("/w <player> END RACISM, JOIN EMPERIUM");
            chants.add("/w <player> DEFUND THE POLICE");
            chants.add("/w <player> I HOPE YOU POSTED YOUR BLACK SQUARE");
            chants.add("/w <player> RESPECT BLM");
            chants.add("/w <player> IF YOURE NOT WITH US, YOURE AGAINST US");
            chants.add("/w <player> DEREK CHAUVIN WAS A RACIST");

        }else if (mode.in("snine mode")) {
            chants.clear();
            chants.add("/w <player> ALLAHU AKBAR");
            chants.add("/w <player> HARAM PIGGIES DIE FOR ALLAH");
            chants.add("/w <player> #FREEPALESTINE");
            chants.add("/w <player> BISMILLAH AL RAHMAN AL RAHIM");
            chants.add("/w <player> MUHAMMAD PEACE BE UPON HIM");
            chants.add("/w <player> I PRAY TO ALLAH 5 TIMES A DAY");
            chants.add("/w <player> DEATH TO STINKY HARAM INFIDELS");
            chants.add("/w <player> PORK = HARAM");

        }
    }
    @Override
    public void update() {
        
        tick_delay++;
        if (tick_delay < delay.get_value(1)*10) return;

        String s = chants.get(r.nextInt(chants.size()));
        String name =  get_random_name();
        
        if (name.equals(mc.player.getName())) return;
        
        for (EntityPlayer friend : Objects.requireNonNull ( FriendUtil.get_friends ( ) )) {
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
