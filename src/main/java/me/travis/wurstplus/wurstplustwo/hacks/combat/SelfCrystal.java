package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class SelfCrystal extends WurstplusHack
{
    public SelfCrystal() {

        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name = "Self Crystal";
        this.tag = "SelfCrystal";
        this.description = "aurora cant do a /kill command so we have to";
    }
}