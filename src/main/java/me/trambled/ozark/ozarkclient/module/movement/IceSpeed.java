package me.trambled.ozark.ozarkclient.module.movement;

import net.minecraft.init.Blocks;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Module;

public class IceSpeed extends Module
{
    
    public IceSpeed() {
        super(Category.MOVEMENT);
        this.name = "Ice Speed";
        this.tag = "IceSpeed";
        this.description = "speed on ice!";
    }

    Setting slipperiness = create("slipperiness", "slipperiness", 0.2, 1, 10);

    
    @Override
    public void update() {
        Blocks.ICE.setDefaultSlipperiness(slipperiness.get_value(1));
        Blocks.PACKED_ICE.setDefaultSlipperiness(slipperiness.get_value(1));
        Blocks.FROSTED_ICE.setDefaultSlipperiness(slipperiness.get_value(1));
    }

    @Override
    protected void disable() {
        Blocks.ICE.setDefaultSlipperiness(0.98f);
        Blocks.PACKED_ICE.setDefaultSlipperiness(0.98f);
        Blocks.FROSTED_ICE.setDefaultSlipperiness(0.98f);
    }
}
