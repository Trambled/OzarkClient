package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class WurstplusCapes extends WurstplusHack {

    public WurstplusCapes() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        this.name = "Capes";
        this.tag = "Capes";
        this.description = "see epic capes behind epic dudes";
    }

    WurstplusSetting cape = create("Cape", "CapeCape", "Space Weed", combobox("OG", "Space Weed", "2010", "2012", "Ahegao", "Magma", "Ocean", "Amazon", "Retro", "Corona", "Mojang", "ComDoge"));

}
