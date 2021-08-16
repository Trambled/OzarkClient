package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.event.events.*;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;

//mostly from salhack and creepy salhack
public class NoRender extends Module {
    public static NoRender INSTANCE = new NoRender();
	public NoRender() {
		super(Category.RENDER);

		this.name        = "NoRender";
		this.tag         = "NoRender";
		this.description = "Doesn't render certain shit.";
	}

    public static NoRender getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new NoRender();
        }
        return INSTANCE;
    }

    public void setInstance() {
        INSTANCE = this;
    }

    Setting items = create("Items", "Items", false);
    Setting withers = create("Withers", "Withers", false);
    Setting wither_skulls = create("Wither Skulls", "WitherSkulls", true);
    Setting sand = create("Sand", "Sand", false);
    Setting fire = create("Fire", "Fire", true);
    Setting water = create("Water", "Water", true);
    Setting pumpkin = create("Pumpkin", "Pumpkin", true);
    Setting boss_health = create("Boss Health", "BossHealth", false);
    Setting firework_rocket = create("Firework Rocket", "FireworksRockets", false);
    Setting hurt_cam = create("Hurt Cam", "HurtCam", true);
    Setting skylight = create("Skylight", "Skylight", false);
    Setting armor = create("Armor", "Armor", false);
    Setting advancements = create("Advancements", "Advancements", false);
    Setting enchanting_table = create("Enchanting Table", "EnchantingTable", false);
    Setting beacon = create("Beacon", "Beacon", false);
    Setting nametags = create("Nametags", "Nametags", false);
    public Setting totemPops = create("Totem", "Totem", false);
	
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
    private final Listener<RenderBlockOverlayEvent> on_block_overlay = new Listener<>(p_Event ->
    {
        if (fire.get_value(true) && p_Event.getOverlayType() == OverlayType.FIRE)
            p_Event.setCanceled(true);
        if (pumpkin.get_value(true) && p_Event.getOverlayType() == OverlayType.BLOCK)
            p_Event.setCanceled(true);
        if (water.get_value(true) && p_Event.getOverlayType() == OverlayType.WATER)
            p_Event.setCanceled(true);
    });
    @EventHandler
    private final Listener<EventRenderBossHealth> renderbosshealth = new Listener<>(p_Event ->
    {
        if (boss_health.get_value(true))
            p_Event.cancel();
    });
    @EventHandler
    private final Listener<EventRenderHurtCameraEffect> on_render_hurt_cam= new Listener<>(p_Event ->
    {
        if (hurt_cam.get_value(true))
            p_Event.cancel();
    });

    @EventHandler
    private final Listener<EventRenderUpdateLightMap>  on_update_light_map = new Listener<>(p_Event ->
    {
        if (skylight.get_value(true))
            p_Event.cancel();

    });

    @EventHandler
    private final Listener<EventRenderArmorLayer> on_render_armor = new Listener<>(p_Event ->
    {
        if (armor.get_value(true))
            p_Event.cancel();

    });

    @EventHandler
    private final Listener<EventRenderBeacon> on_render_beacon = new Listener<>(p_Event ->
    {
        if (beacon.get_value(true))
            p_Event.cancel();

    });

    @EventHandler
    private final Listener<EventRenderEnchantingTable> on_render_enchanting_table = new Listener<>(p_Event ->
    {
        if (enchanting_table.get_value(true))
            p_Event.cancel();

    });

    @EventHandler
    private final Listener<EventRenderName> on_render_name = new Listener<>(event -> {
        if (nametags.get_value(true)) {
            event.cancel();
        }
    });

    @EventHandler
    private final Listener<EventRenderToast> on_render_toast = new Listener<>(event -> {
       if (advancements.get_value(true)) {
           event.cancel();
       }
    });


}
