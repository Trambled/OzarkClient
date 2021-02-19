package me.travis.wurstplus.wurstplustwo.hacks.chat;

import java.util.Random;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class AutoExcuse extends WurstplusHack
{
    int diedTime;
    
    public AutoExcuse() {
        super(WurstplusCategory.WURSTPLUS_CHAT);
        this.diedTime = 0;
        this.name = "Auto Excuse";
        this.tag = "AutoExcuse";
        this.description = "tell people why you died";
    }
    
    @Override
    public void update() {
        if (this.diedTime > 0) {
            --this.diedTime;
        }
        if (AutoExcuse.mc.player.isDead) {
            this.diedTime = 500;
        }
        if (!AutoExcuse.mc.player.isDead && this.diedTime > 0) {
            int randomNum = (int )(Math.random() * 50 + 1);

            if (randomNum == 1) {
                AutoExcuse.mc.player.sendChatMessage("your ping is so good :(((( why are you targeting me");
            }
            if (randomNum == 2) {
                AutoExcuse.mc.player.sendChatMessage("i was in my inventoryyyyyyy");
            }
            if (randomNum == 3) {
                AutoExcuse.mc.player.sendChatMessage("i was configuring my settings bro im not ez i promise");
            }
            if (randomNum == 4) {
                AutoExcuse.mc.player.sendChatMessage("I was tabbed out of minecraft dude");
            }
            if (randomNum == 5) {
                AutoExcuse.mc.player.sendChatMessage("i was was deSynced :(");
            }
            if (randomNum == 6) {
                AutoExcuse.mc.player.sendChatMessage("imagine hacking nub");
            }
            this.diedTime = 0;
        }
    }
}
