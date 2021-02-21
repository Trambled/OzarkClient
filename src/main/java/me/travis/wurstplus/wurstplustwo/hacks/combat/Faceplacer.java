package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;

public class Faceplacer extends WurstplusHack
{
    public Faceplacer() {

        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name = "Faceplacer";
        this.tag = "Faceplacer";
        this.description = "Faceplaces an opponent";
    }

    @Override
    public void update() {
        if (!Wurstplus.get_hack_manager().get_module_with_tag("AutoCrystal").is_active()) {
            WurstplusMessageUtil.send_client_error_message("AutoCrystal is not on!");
            this.set_disable();
        }
    }
}