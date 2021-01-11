package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import java.util.Iterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.boss.EntityWither;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;

public class NoRender extends WurstplusHack {
	
	WurstplusSetting items = create("Items", "Items", false);
	WurstplusSetting withers = create("Withers", "Withers", false);
	WurstplusSetting wither_skulls = create("Wither Skulls", "WitherSkulls", true);
	WurstplusSetting sand = create("Sand", "Sand", true);
	WurstplusSetting fire = create("Fire", "Fire", true);
	WurstplusSetting pumpkin = create("Pumpkin", "Pumpkin", true);

	public NoRender() {
		super(WurstplusCategory.WURSTPLUS_RENDER);

		this.name        = "NoRender";
		this.tag         = "NoRender";
		this.description = "doesnt render certain shit";
	}
	
	@Override
    public void update() {
		if (items.get_value(true)) {
			for (final Entity e : mc.world.loadedEntityList) {
                if (e instanceof EntityItem) {
                    mc.world.removeEntity(e);
				}
            }
        }
	    if (withers.get_value(true)) {
			for (final Entity e : mc.world.loadedEntityList) {
                if (e instanceof EntityWither) {
                    mc.world.removeEntity(e);
				}
            }
        }
	    if (wither_skulls.get_value(true)) {
			for (final Entity e : mc.world.loadedEntityList) {
                if (e instanceof EntityWitherSkull) {
                    mc.world.removeEntity(e);
				}
            }
        }
		if (sand.get_value(true)) {
			for (final Entity e : mc.world.loadedEntityList) {
                if (e instanceof EntityFallingBlock) {
                    mc.world.removeEntity(e);
				}
			}
		}
	}
    @EventHandler
    private Listener<RenderBlockOverlayEvent> OnBlockOverlayEvent = new Listener<>(p_Event ->
    {
        if (fire.get_value(true) && p_Event.getOverlayType() == OverlayType.FIRE)
            p_Event.setCanceled(true);
        if (pumpkin.get_value(true) && p_Event.getOverlayType() == OverlayType.BLOCK)
            p_Event.setCanceled(true);
    });
}