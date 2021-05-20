package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;



public class NoSpeedFOV extends Module {

    public NoSpeedFOV() {
        super(Category.RENDER);

        this.name = "NoSpeedFOV";
        this.tag = "NoSpeedFOV";
        this.description = "cancel the fov given by speed";

        //i make this in mixins because i want to learn how mixins work
    }
}