package me.trambled.ozark.mixins;

import me.trambled.ozark.Ozark;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
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


}
