package me.travis.wurstplus.mixins;

import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.util.WurstplusCapeUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(value={AbstractClientPlayer.class})
public abstract class WurstplusMixinAbstractClientPlayer {
    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method={"getLocationCape"}, at={@At(value="HEAD")}, cancellable=true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {

        if (Wurstplus.get_hack_manager().get_module_with_tag("Capes").is_active()) {
            NetworkPlayerInfo info = this.getPlayerInfo();
            assert info != null;
            if (!WurstplusCapeUtil.is_uuid_valid(info.getGameProfile().getId())) {
                return;
            }
            ResourceLocation r;
            if (Wurstplus.get_setting_manager().get_setting_with_tag("Capes", "CapeCape").in("OG")) {
                r = new ResourceLocation("custom/cape-old.png");
            } else if (Wurstplus.get_setting_manager().get_setting_with_tag("Capes", "CapeCape").in("Space Weed")) {
                r = new ResourceLocation("custom/spaceweed.png");
            } else if (Wurstplus.get_setting_manager().get_setting_with_tag("Capes", "CapeCape").in("2010")) {
                r = new ResourceLocation("custom/2010.png");
            } else if (Wurstplus.get_setting_manager().get_setting_with_tag("Capes", "CapeCape").in("2012")) {
                r = new ResourceLocation("custom/2012.png");
            } else if (Wurstplus.get_setting_manager().get_setting_with_tag("Capes", "CapeCape").in("Magma")) {
                r = new ResourceLocation("custom/magma.png");
            } else if (Wurstplus.get_setting_manager().get_setting_with_tag("Capes", "CapeCape").in("Ocean")) {
                r = new ResourceLocation("custom/ocean.png");
            } else if (Wurstplus.get_setting_manager().get_setting_with_tag("Capes", "CapeCape").in("Retro")) {
                r = new ResourceLocation("custom/retro.png");
            } else if (Wurstplus.get_setting_manager().get_setting_with_tag("Capes", "CapeCape").in("Corona")) {
                r = new ResourceLocation("custom/corona.png");
            } else if (Wurstplus.get_setting_manager().get_setting_with_tag("Capes", "CapeCape").in("Mojang")) {
                r = new ResourceLocation("custom/mojang.png");
            } else if (Wurstplus.get_setting_manager().get_setting_with_tag("Capes", "CapeCape").in("Amazon")) {
                r = new ResourceLocation("custom/amazon.png");
            } else if (Wurstplus.get_setting_manager().get_setting_with_tag("Capes", "CapeCape").in("Lightning")) {
                r = new ResourceLocation("custom/lightning.gif");
            } else if (Wurstplus.get_setting_manager().get_setting_with_tag("Capes", "CapeCape").in("Agraesthetic")) {
                r = new ResourceLocation("custom/aesthetic.gif");
            } else {
                r = new ResourceLocation("custom/ahegao.png"); //must have else or the variable might not be initialized 
            }

            callbackInfoReturnable.setReturnValue(r);
        }


    }

}