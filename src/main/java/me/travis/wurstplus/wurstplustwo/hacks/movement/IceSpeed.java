package me.travis.wurstplus.wurstplustwo.hacks.movement;

import net.minecraft.init.Blocks;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class IceSpeed extends WurstplusHack
{
    
    public IceSpeed() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "Ice Speed";
        this.tag = "IceSpeed";
        this.description = "speed on ice!";
    }

    WurstplusSetting slipperiness = create("slipperiness", "slipperiness", 0.2, 1, 10);

    
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
