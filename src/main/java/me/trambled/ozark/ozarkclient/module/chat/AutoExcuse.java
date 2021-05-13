package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;

public class AutoExcuse extends Module
{
    int diedTime;
    
    public AutoExcuse() {
        super(Category.CHAT);
        this.diedTime = 0;
        this.name = "AutoExcuse";
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
            if (randomNum == 7) {
                AutoExcuse.mc.player.sendChatMessage("i was in my impact gui");
            }
            if (randomNum == 8) {
                AutoExcuse.mc.player.sendChatMessage("i died cause i was using the leaked 1.8.3.5 ozark leak");
            }
            if (randomNum == 9) {
                AutoExcuse.mc.player.sendChatMessage("i was using wurst+1");
            }
            this.diedTime = 0;
        }
    }
}
