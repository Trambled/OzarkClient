package me.trambled.ozark.mixins;

import me.trambled.ozark.Ozark;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { RenderGlobal.class }, priority = 9999)
public class MixinRenderGlobal
{
    @Shadow
    public ShaderGroup entityOutlineShader;
    @Shadow
    public boolean entityOutlinesRendered;
    @Shadow
    public WorldClient world;

    @Redirect(method = { "broadcastSound" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;playSound(DDDLnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FFZ)V"))
    private void playWitherSpawn(final WorldClient worldClient, final double x, final double y, final double z, final SoundEvent soundIn, final SoundCategory category, final float volume, final float pitch, final boolean distanceDelay) {
        if (!Ozark.get_module_manager().get_module_with_tag("AntiSound").is_active() || !Ozark.get_setting_manager().get_setting_with_tag("AntiSound", "WitherSpawn").get_value(true)) {
            this.world.playSound(x, y, z, soundIn, category, volume, pitch, distanceDelay);
        }
    }

    @Redirect(method = { "playEvent" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;playSound(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FFZ)V", ordinal = 22))
    private void playWitherShoot(final WorldClient worldClient, final BlockPos pos, final SoundEvent soundIn, final SoundCategory category, final float volume, final float pitch, final boolean distanceDelay) {
        if (!Ozark.get_module_manager().get_module_with_tag("AntiSound").is_active() || !Ozark.get_setting_manager().get_setting_with_tag("AntiSound", "Wither").get_value(true)) {
            this.world.playSound(pos, soundIn, category, volume, pitch, distanceDelay);
        }
    }
}
