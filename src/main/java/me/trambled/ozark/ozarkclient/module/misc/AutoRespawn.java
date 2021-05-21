package me.trambled.ozark.ozarkclient.module.misc;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import net.minecraft.client.gui.GuiGameOver;

public class AutoRespawn extends Module {

    public AutoRespawn() {
        super(Category.MISC);

        this.name = "AutoRespawn";
        this.tag = "AutoRespawn";
        this.description = "Automatically respawns.";
    }

    @Override
    public void update() {
        if (mc.player.isDead && mc.currentScreen instanceof GuiGameOver) {
            mc.player.respawnPlayer();
        }
    }
}
