package me.trambled.ozark.ozarkclient.module.render;


import com.mojang.authlib.GameProfile;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.event.events.EventTotemPop;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.player.EntityUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopChams extends Module {

    public PopChams() {
        super(Category.RENDER);

        this.name = "PopChams";
        this.tag = "PopChams";
        this.description = "combination of pasted shits";
    }
    Setting alivetick = create("Alive tick", "AliveTick",20,0,100);
    Setting ytravel = create("yfly", "ytravel",0,-10,10);
    public Setting r = create("R", "popChamsR", 255, 0, 255);
    public Setting g = create("G", "popChamsG", 255, 0, 255);
    public Setting b = create("B", "popChamsB", 255, 0, 255);
    public Setting a = create("A", "popChamsA", 100, 0, 255);

    public HashMap<EntityPlayer, Integer> pop = new HashMap<>();

    Color color = new Color(r.get_value(1),g.get_value(1),b.get_value(1),a.get_value(1));
    /*
    so this code is actually from wp3 private repo
    but i manage to get it lol
    skid
     */

    @EventHandler
    private final Listener<EventTotemPop> tot = new Listener<>(event -> {
        if(event.getEntity() == mc.player) return;
        pop.put(new EntityPlayer(mc.world, new GameProfile(event.getEntity().getUniqueID(),event.getEntity().getName())) {
            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return false;
            }
        }, color.getAlpha());
            });

    @Override
    public void render(EventRender event) {
        List<EntityPlayer> playersToRemove = new ArrayList<>();
        for (Map.Entry<EntityPlayer, Integer> player : pop.entrySet()) {
            if (player.getValue() > alivetick.get_value(1)) {
                playersToRemove.add(player.getKey());
            }
            player.getKey().posY += ytravel.get_value(1);
            mc.getRenderManager().renderEntityStatic(player.getKey(), event.get_partial_ticks(), false);
            player.setValue(player.getValue() + 1);
        }
        for (EntityPlayer player : playersToRemove) {
            pop.remove(player);
        }
    }
}