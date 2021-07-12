package me.trambled.ozark.mixins;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerControllerMP.class)
public interface AccessorPlayerControllerMP {

    @Accessor("curBlockDamageMP")
    float getCurBlockDamageMP();

    @Accessor("isHittingBlock")
    void setIsHittingBlock(boolean hitting);

}