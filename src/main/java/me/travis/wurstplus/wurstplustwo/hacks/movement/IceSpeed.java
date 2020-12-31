// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.wurstplustwo.hacks.movement;

import net.minecraft.init.Blocks;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class IceSpeed extends WurstplusHack
{
    WurstplusSetting slipperiness;
    
    public IceSpeed() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.slipperiness = this.create("Slipperiness", "Slipperiness", 0.20000000298023224, 0.0, 10.0);
        this.name = "IceSpeed";
        this.tag = "IceSpeed";
        this.description = "IceSpeed";
    }
    
    @Override
    public void update() {
        Blocks.ICE.slipperiness = (float)this.slipperiness.get_value(1);
        Blocks.PACKED_ICE.slipperiness = (float)this.slipperiness.get_value(1);
        Blocks.FROSTED_ICE.slipperiness = (float)this.slipperiness.get_value(1);
    }
    
    public void disable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }
}
