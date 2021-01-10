package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusTextureHelper;
import me.zero.alpine.fork.listener.Listener;
import me.zero.alpine.fork.listener.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

import java.util.function.Predicate;

public class OffhandBypass extends WurstplusHack {
    private final Listener<PlayerDestroyItemEvent> sendListener;

    public OffhandBypass() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "OffhandBypass";
        this.tag = "OffhandBypass";
        this.description = "cum";
        
        Listener<PlayerDestroyItemEvent> sendListener;

        this.sendListener = new Listener<PlayerDestroyItemEvent>(e -> {
            if (e.getHand() == EnumHand.MAIN_HAND && OffhandBypass.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
                e.setCanceled(true);
            }
        }, (Predicate<PlayerDestroyItemEvent>[])new Predicate[0]);
        
    }
    //idfk if this shit will work now



}