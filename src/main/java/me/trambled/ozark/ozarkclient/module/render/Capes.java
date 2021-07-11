package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

public class Capes extends Module {

    public Capes() {
        super(Category.RENDER);

        this.name = "Capes";
        this.tag = "Capes";
        this.description = "See epic capes behind epic dudes.";
    }

    Setting cape = create("Cape", "CapeCape", "Ozark", combobox("Ozark", "Space Weed", "2010", "2012", "Ahegao", "Magma", "Ocean", "Amazon", "Retro", "Corona", "Mojang", "ComDoge", "FrootLoop"));

}
