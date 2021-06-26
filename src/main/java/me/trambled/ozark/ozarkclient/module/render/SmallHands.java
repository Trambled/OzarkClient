package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

public class SmallHands extends Module {

    public SmallHands() {
        super(Category.RENDER);

        this.name = "SmallHands";
        this.tag = "SmallHands";
        this.description = "Makes ur hands smaller.";
    }

    Setting offset = create("Offset", "Offset", 90, 0, 360);

    @Override
    public void update() {
        try {
            mc.player.renderArmPitch = (float)this.offset.get_value(1);
        } catch (Exception ignored) {}
    }
    @Override
    public String array_detail() {
        if (offset.get_value(1) > 90 ) {
            return "Big";
        }else{
            return "Small";
        }
    }}
