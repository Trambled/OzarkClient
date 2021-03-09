package me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.items;


import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.WurstplusDraw;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.Component;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.font.FontUtil;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import net.minecraft.client.gui.Gui;

public class InfoComponent extends Component {
    private final WurstplusSetting op;
    private final ModuleButton parent;
    private int offset;

    public InfoComponent(WurstplusSetting op, ModuleButton parent, int offset) {
        this.op = op;
        this.parent = parent;
        this.offset = offset;

    }

    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }

    @Override
    public void renderComponent() {
        WurstplusDraw.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + 15 + offset, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
        WurstplusDraw.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + 15 + offset, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
        WurstplusDraw.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + offset + 16, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));

        Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, 0xFF111111);

        WurstplusDraw.draw_rect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 1, parent.parent.getY() + offset + 15, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
        WurstplusDraw.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() - 1, parent.parent.getY() + offset + 15, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));

        FontUtil.drawText(this.op.get_value("me"), parent.parent.getX() + 4, parent.parent.getY() + this.offset + 4, -1);
    }

}
