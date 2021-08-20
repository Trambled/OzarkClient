package me.trambled.ozark.ozarkclient.module.gui;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

public class Arraylist extends Module {
    public Arraylist() {
        super(Category.GUI);
        this.name = "ArrayList";
        this.tag = "ArrayList";
        this.description = "Working custom font arraylist with rainbow rolling";

    }
    Setting color = create("ColorMode", "ColorMode", "Rainbow", combobox("Rainbow", "Alpha Step", "Static"));
    Setting mode = create("Mode", "Mode", "Free", combobox("Free", "Top R", "Top L", "Bottom R", "Bottom L"));
    Setting red = create("Red", "Red", 255, 0, 255);
    Setting green = create("Green", "Green",  255, 0, 255);
    Setting blue = create("Blue", "Blue", 255, 0, 255);
    Setting retard_mode = create("Reverse Mode", "ArrayListRetardMode", false);

}