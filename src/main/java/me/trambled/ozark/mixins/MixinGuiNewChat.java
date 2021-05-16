package me.trambled.ozark.mixins;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import me.trambled.ozark.ozarkclient.util.RainbowUtil;
import net.minecraft.client.gui.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;
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
        if (text.contains(MessageUtil.opener) && Ozark.get_module_manager().get_module_with_tag("RainbowChat").is_active()) {
            RainbowUtil.drawRainbowStringChat(text, x, y, RainbowUtil.getMultiColour().getRGB(), 100.0f);
        } else {
            return fontRenderer.drawStringWithShadow(text, x, y, color);
        }
        // does this really matter ?
        return color;
    }


}
