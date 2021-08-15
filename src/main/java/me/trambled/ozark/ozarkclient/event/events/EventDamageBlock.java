package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class EventDamageBlock extends Event {

    private final BlockPos BlockPos;
    private EnumFacing Direction;

    public EventDamageBlock(BlockPos posBlock, EnumFacing directionFacing)
    {
        BlockPos = posBlock;
        setDirection(directionFacing);
    }

    public BlockPos getPos()
    {
        return BlockPos;
    }

    /**
     * @return the direction
     */
    public EnumFacing getDirection()
    {
        return Direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(EnumFacing direction)
    {
        Direction = direction;
    }
    
}