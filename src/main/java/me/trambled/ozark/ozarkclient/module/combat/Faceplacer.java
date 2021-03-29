package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.util.MessageUtil;

public class Faceplacer extends Module
{
    public Faceplacer() {

        super(Category.COMBAT);

        this.name = "Faceplacer";
        this.tag = "Faceplacer";
        this.description = "Faceplaces an opponent";
    }

    @Override
    public void update() {
        if (!Ozark.get_hack_manager().get_module_with_tag("AutoCrystal").is_active()) {
            MessageUtil.send_client_error_message("AutoCrystal is not on!");
            this.set_disable();
        }
    }
}