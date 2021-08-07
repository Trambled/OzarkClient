package me.trambled.ozark.mixins;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.events.EventRenderEntityModel;
import me.trambled.ozark.ozarkclient.module.chat.TotemPopCounter;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@Mixin(RenderLivingBase.class)


public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T> {

    @Shadow
    protected ModelBase mainModel;

    protected MixinRenderLivingBase(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn);
    }

    @Redirect(method = {"renderModel"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void renderModelHook(final ModelBase modelBase, final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (Ozark.get_module_manager().get_module_with_tag("Chams").is_active()) {
            final EventRenderEntityModel event = new EventRenderEntityModel(0, modelBase, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Ozark.get_module_manager().get_module_with_tag("Chams").on_render_model(event);
            if (event.isCancelled()) {
                return;
            }
        }
        modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }


    @Inject(method = {"renderModel"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V")}, cancellable = true)
    private void renderModel(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo info) {
        if (TotemPopCounter.pops.containsKey(entity.getEntityId())) {
            if (TotemPopCounter.pops.get(entity.getEntityId()) == 0) {
                MessageUtil.client_message("removing");
                Minecraft.getMinecraft().world.removeEntityFromWorld(entity.getEntityId());
            } else if (TotemPopCounter.pops.get(entity.getEntityId()) < 0) {
                //this is retarted but it doesnt instantly stop rendering sorry for messy code dont remove this
                if (TotemPopCounter.pops.get(entity.getEntityId()) < -5)
                    TotemPopCounter.pops.remove(entity.getEntityId());
                return;
            }
            MessageUtil.client_message("rendering");
            GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            glPushMatrix();
            glPushAttrib(GL_ALL_ATTRIB_BITS);
            glDisable(GL_TEXTURE_2D);
            glEnable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
            GL11.glDisable((int) 2929);
            GL11.glEnable((int) 10754);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            GL11.glColor4f((float)((float)TotemPopCounter.INSTANCE.r.get_value(1) / 255.0f), (float)((float)TotemPopCounter.INSTANCE.g.get_value(1) / 255.0f), (float)((float)TotemPopCounter.INSTANCE.b.get_value(1) / 255.0f), (float)((float)TotemPopCounter.INSTANCE.a.get_value(1) / 255.0f));
            new Color(TotemPopCounter.INSTANCE.r.get_value(1), TotemPopCounter.INSTANCE.g.get_value(1), TotemPopCounter.INSTANCE.b.get_value(1), TotemPopCounter.pops.get(entity.getEntityId()));
            mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GL11.glEnable((int) 2929);
            glEnable(GL_TEXTURE_2D);
            glPopAttrib();
            glPopMatrix();
            TotemPopCounter.pops.computeIfPresent(entity.getEntityId(), (key, oldValue) -> oldValue - 1);
        }
    }
}


