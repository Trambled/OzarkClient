package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.entity.item.EntityEnderCrystal;

public class EventRenderEntityCrystal extends Event {
    public final ModelEnderCrystal modelBase;
    public EntityEnderCrystal entityIn;
    public float limbSwing;
    public float limbSwingAmount;
    public float ageInTicks;
    public float netHeadYaw;
    public float headPitch;
    public float scale;

    public EventRenderEntityCrystal( EntityEnderCrystal crystal, ModelEnderCrystal modelBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.modelBase = modelBase;
        this.entityIn = crystal;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scale = scale;
    }
}
