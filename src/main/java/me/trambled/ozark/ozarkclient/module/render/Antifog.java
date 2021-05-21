package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.event.events.EventSetupFog;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;

public class Antifog extends Module {
    
    public Antifog() {
        super(Category.RENDER);

        this.name = "AntiFog";
        this.tag = "AntiFog";
        this.description = "See even more.";
    }

    @EventHandler
    private final Listener<EventSetupFog> setup_fog = new Listener<> (event -> {

        event.cancel();

        mc.entityRenderer.setupFogColor(false);

        GlStateManager.glNormal3f(0.0F, -1.0F, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.colorMaterial(1028, 4608);

    });

}