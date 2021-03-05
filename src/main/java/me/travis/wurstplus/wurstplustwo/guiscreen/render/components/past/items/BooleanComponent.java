package me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.items;


import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.WurstplusDraw;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.font.FontUtil;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.items.ModuleButton;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.Component;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.PastGUIHack;
import net.minecraft.client.gui.Gui;

public class BooleanComponent extends Component {
    private WurstplusSetting op;
    private ModuleButton parent;
    private int offset;
    private int x;
    private int y;

    public BooleanComponent(WurstplusSetting op, ModuleButton parent, int offset) {
        this.op = op;
        this.parent = parent;
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
        this.offset = offset;
    }

    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }

    private final int past_gui_r = Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1);
    private final int past_gui_g = Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1);
    private final int past_gui_b = Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1);
    private final int past_gui_a = Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1);

    @Override
    public void renderComponent() {
        WurstplusDraw.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + 15 + offset, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
        WurstplusDraw.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + 15 + offset, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
        WurstplusDraw.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + offset + 16, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));

        if (op.get_value(true)) {
            WurstplusDraw.draw_rect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
            Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, 0x75101010);
        } else {
            Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, 0xFF111111);
        }

        WurstplusDraw.draw_rect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 1, parent.parent.getY() + offset + 15, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
        WurstplusDraw.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() - 1, parent.parent.getY() + offset + 15, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));

        FontUtil.drawText(this.op.get_name(), parent.parent.getX() + 4, parent.parent.getY() + offset + 4, -1);
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.y = parent.parent.getY() + this.offset;
        this.x = parent.parent.getX();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen()) {
            this.op.set_value(!op.get_value(true));
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        if (x > this.x && x < this.x + 100 && y > this.y && y < this.y + 15) {
            return true;
        } else {
            return false;
        }
    }
}
