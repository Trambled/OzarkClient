package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.event.events.*;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.boss.EntityWither;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;

//mostly from salhack and creepy salhack
public class NoRender extends WurstplusHack {
	
	WurstplusSetting items = create("Items", "Items", false);
	WurstplusSetting withers = create("Withers", "Withers", false);
	WurstplusSetting wither_skulls = create("Wither Skulls", "WitherSkulls", true);
	WurstplusSetting sand = create("Sand", "Sand", true);
	WurstplusSetting fire = create("Fire", "Fire", true);
    WurstplusSetting water = create("Water", "Water", true);
    WurstplusSetting pumpkin = create("Pumpkin", "Pumpkin", true);
	WurstplusSetting boss_health = create("Boss Health", "BossHealth", true);
    WurstplusSetting firework_rocket = create("Firework Rocket", "FireworksRockets", false);
    WurstplusSetting hurt_cam = create("Hurt Cam", "HurtCam", true);
    WurstplusSetting skylight = create("Skylight", "Skylight", false);
    WurstplusSetting armor = create("Armor", "Armor", true);
    WurstplusSetting enchanting_table = create("Enchanting Table", "EnchantingTable", true);
    WurstplusSetting beacon = create("Beacon", "Beacon", false);

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
        if (firework_rocket.get_value(true)) {
            for (final Entity e : mc.world.loadedEntityList) {
                if (e instanceof EntityFireworkRocket) {
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
        if (water.get_value(true) && p_Event.getOverlayType() == OverlayType.WATER)
            p_Event.setCanceled(true);
    });
    @EventHandler
    private Listener<EventRenderBossHealth> renderbosshealth = new Listener<>(p_Event ->
    {
        if (boss_health.get_value(true))
            p_Event.cancel();
    });
    @EventHandler
    private Listener<EventRenderHurtCameraEffect> renderhurtcam= new Listener<>(p_Event ->
    {
        if (hurt_cam.get_value(true))
            p_Event.cancel();
    });

    @EventHandler
    private Listener<EventRenderUpdateLightMap>  white_power = new Listener<>(p_Event ->
    {
        if (skylight.get_value(true)) {
            p_Event.cancel();
        }
    });

    @EventHandler
    private final Listener<EventRenderArmorLayer> on_render_armor = new Listener<>(p_Event ->
    {
        if (armor.get_value(true)) {
            p_Event.cancel();
        }
    });

    @EventHandler
    private final Listener<EventRenderBeacon> on_render_beacon = new Listener<>(p_Event ->
    {
        if (beacon.get_value(true)) {
            p_Event.cancel();
        }
    });

    @EventHandler
    private final Listener<EventRenderEnchantingTable> on_render_enchanting_table = new Listener<>(p_Event ->
    {
        if (enchanting_table.get_value(true)) {
            p_Event.cancel();
        }
    });


}