package me.trambled.ozark.mixins;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import me.trambled.ozark.ozarkclient.util.RainbowUtil;
import net.minecraft.client.gui.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

/**
 * Phobos
 */
@Mixin(GuiNewChat.class)
public class MixinGuiNewChat extends Gui {

    @Redirect(method={"setChatLine"}, at=@At(value="INVOKE", target="Ljava/util/List;size()I", ordinal=0, remap=false))
    public int drawnChatLinesSize(List<ChatLine> list) {
        return Ozark.get_module_manager().get_module_with_tag("ChatModifications").is_active() && Ozark.get_setting_manager().get_setting_with_tag("ChatModifications", "ChatModsInfiniteChat").get_value(true) ? -2147483647 : list.size();
    }

    @Redirect(method={"setChatLine"}, at=@At(value="INVOKE", target="Ljava/util/List;size()I", ordinal=2, remap=false))
    public int chatLinesSize(List<ChatLine> list) {
        return Ozark.get_module_manager().get_module_with_tag("ChatModifications").is_active() && Ozark.get_setting_manager().get_setting_with_tag("ChatModifications", "ChatModsInfiniteChat").get_value(true) ? -2147483647 : list.size();
    }

    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawStringWithShadowMaybe(FontRenderer fontRenderer, String text, float x, float y, int color) {
        if (text.contains(MessageUtil.opener) && Ozark.get_setting_manager().get_setting_with_tag("ChatModifications", "rainbowozark").get_value(true)) {
            if (Ozark.get_setting_manager().get_setting_with_tag("ChatModifications", "customfont").get_value(true)) {
                RainbowUtil.drawRainbowStringChatCustomFont(text, x, y, RainbowUtil.getMultiColour().getRGB(), 100.0F);
            } else {
                RainbowUtil.drawRainbowStringChat(text, x, y, RainbowUtil.getMultiColour().getRGB(), 100.0F);
            }
        } else {
            return fontRenderer.drawStringWithShadow(text, x, y, color);
        }
        // does this really matter ?
        return color;
    }


}
