package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.mixins.accessor.ItemRenderer;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import net.minecraft.util.*;


public class FastSwitch extends Module {

    public FastSwitch() {
        super(Category.RENDER);

        this.name = "FastSwitch";
        this.tag = "FastSwitch";
        this.description = "mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm";
//kambing made this to properly understand mixins and stuff
    }
    @Override
    public void update() {
        if (((ItemRenderer)FastSwitch.mc.entityRenderer.itemRenderer).getPrevEquippedProgressMainHand() >= 0.9) {
            ((ItemRenderer)FastSwitch.mc.entityRenderer.itemRenderer).setEquippedProgressMainHand(1.0f);
            ((ItemRenderer)FastSwitch.mc.entityRenderer.itemRenderer).setItemStackMainHand(FastSwitch.mc.player.getHeldItem(EnumHand.MAIN_HAND));
        }
        if (((ItemRenderer)FastSwitch.mc.entityRenderer.itemRenderer).getPrevEquippedProgressOffHand() >= 0.9) {
            ((ItemRenderer)FastSwitch.mc.entityRenderer.itemRenderer).setEquippedProgressOffHand(1.0f);
            ((ItemRenderer)FastSwitch.mc.entityRenderer.itemRenderer).setItemStackOffHand(FastSwitch.mc.player.getHeldItem(EnumHand.OFF_HAND));
        }
    }
}
