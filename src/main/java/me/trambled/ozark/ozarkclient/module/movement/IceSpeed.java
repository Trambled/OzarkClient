package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import net.minecraft.init.Blocks;

public class IceSpeed extends Module
{
    
    public IceSpeed() {
        super(Category.MOVEMENT);
        this.name = "IceSpeed";
        this.tag = "IceSpeed";
        this.description = "Speed on ice!";
    }

    Setting slipperiness = create("Slipperiness", "Slipperiness", 1.0, 1, 10);

    
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
