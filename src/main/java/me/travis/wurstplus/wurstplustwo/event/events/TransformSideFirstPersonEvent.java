package me.travis.wurstplus.wurstplustwo.event.events;

import me.travis.wurstplus.wurstplustwo.event.WurstplusEventCancellable;
import net.minecraft.util.EnumHandSide;

public class TransformSideFirstPersonEvent extends WurstplusEventCancellable {

    private final EnumHandSide enumHandSide;

    public TransformSideFirstPersonEvent(EnumHandSide enumHandSide){
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide(){
        return this.enumHandSide;
    }
}
