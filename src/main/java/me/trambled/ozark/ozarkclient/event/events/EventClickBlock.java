package me.trambled.ozark.ozarkclient.event.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class EventClickBlock extends Event {

    private final BlockPos pos;
    private final EnumFacing side;
    private boolean damage = false;

    public EventClickBlock(BlockPos pos, EnumFacing side) {
        this.pos = pos;
        this.side = side;
    }

    public EventClickBlock(BlockPos pos, EnumFacing side, boolean damage) {
        this.pos = pos;
        this.side = side;
        this.damage = damage;
    }

    public boolean isDamage() {
        return damage;
    }

    public BlockPos getPos() {
        return pos;
    }

    public EnumFacing getSide() {
        return side;
    }


}
