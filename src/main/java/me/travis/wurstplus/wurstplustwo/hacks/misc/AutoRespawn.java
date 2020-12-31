package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.client.gui.GuiGameOver;

public class AutoRespawn extends WurstplusHack {

    public AutoRespawn() {
        super(WurstplusCategory.WURSTPLUS_MISC);

        this.name = "AutoRespawn";
        this.tag = "AutoRespawn";
        this.description = "auto fart";
    }

    @Override
    public void update() {
        if (mc.player.isDead && mc.currentScreen instanceof GuiGameOver) {
            mc.player.respawnPlayer();
        }
    }
}
