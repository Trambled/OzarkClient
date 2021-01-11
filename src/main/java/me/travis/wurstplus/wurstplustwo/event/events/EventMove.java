package me.travis.wurstplus.wurstplustwo.event.events;

import me.travis.wurstplus.wurstplustwo.event.MinecraftEvent;
import net.minecraft.entity.MoverType;

public class EventMove extends MinecraftEvent
{
    public MoverType Type;
    public double X;
    public double Y;
    public double Z;

    public EventMove(MoverType p_Type, double p_X, double p_Y, double p_Z)
    {
        Type = p_Type;
        X = p_X;
        Y = p_Y;
        Z = p_Z;
    }
}
