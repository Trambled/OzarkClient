package me.trambled.ozark.ozarkclient.module.misc;

import me.trambled.ozark.ozarkclient.event.events.EventEntityRemoved;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.init.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class DonkeyAlert extends Module
{
    public DonkeyAlert() {
        super(Category.MISC);

        this.name = "Donkey Alert";
        this.tag = "DonkeyAlert";
        this.description = "alerts u when donkeys apear in render distance";
    }

    Setting donkeys = create("Donkeys", "AlertDonkeys", true);
    Setting llamas = create("Llamas", "AlertLlamas", true);
    Setting sound = create("Sound", "AlertSound", true);

    List<Entity> entities = new ArrayList<>();

    @Override
    protected void disable() {
        entities.clear();
    }

    @Override
    public void update(){
        if (full_null_check()) return;

        if (donkeys.get_value(true)) {
            for (final Entity e : mc.world.loadedEntityList) {
                if (e instanceof EntityDonkey && !entities.contains(e)) {
                    if (sound.get_value(true)) {
                        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f));
                    }
                    MessageUtil.send_client_message("Donkey found at " +  Math.round(e.posX) + ", " + Math.round(e.posZ) + "!");
                    entities.add(e);
                }
            }
        }
        if (llamas.get_value(true)) {
            for (final Entity e : mc.world.loadedEntityList) {
                if (e instanceof EntityLlama && !entities.contains(e)) {
                    if (sound.get_value(true)) {
                        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f));
                    }
                    MessageUtil.send_client_message("Donkey found at " + Math.round(e.posX) + ", " + Math.round(e.posZ) + "!");
                    entities.add(e);
                }
            }
        }
    }

    @EventHandler
    private final Listener<EventEntityRemoved> on_entity_removed = new Listener<>(event -> {
        if ((event.get_entity() instanceof EntityDonkey && donkeys.get_value(true)) || (event.get_entity() instanceof EntityLlama && llamas.get_value(true))) {
            entities.remove(event.get_entity());
        }
    });



}
