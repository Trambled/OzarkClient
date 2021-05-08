package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

public
class Capes extends Module {

    Setting cape = create ( "Cape" , "CapeCape" , "Space Weed" , combobox ( "OG" , "Space Weed" , "2010" , "2012" , "Ahegao" , "Magma" , "Ocean" , "Amazon" , "Retro" , "Corona" , "Mojang" , "ComDoge" , "FrootLoop" ) );

    public
    Capes ( ) {
        super ( Category.RENDER );

        this.name = "Capes";
        this.tag = "Capes";
        this.description = "see epic capes behind epic dudes";
    }

}
