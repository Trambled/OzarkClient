package me.trambled.ozark.ozarkclient.guiscreen.gui.past.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.gui.past.Component;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.font.FontUtil;
import me.trambled.ozark.ozarkclient.util.render.GuiUtil;

public class ModeComponent extends Component {
    private final Setting op;
    private final ModuleButton parent;
    private int offset;
    private int x;
    private int y;
    private int modeIndex;
    private int rainbowOff;


    public ModeComponent(Setting op, ModuleButton parent, int offset) {
        this.op = op;
        this.parent = parent;
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
        this.offset = offset;
        this.modeIndex = 0;
    }

    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }


    @Override
    public void renderComponent() {
        rainbowOff = parent.parent.rainbowOff;
        boolean dark_outline = !Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIBrightOutline").get_value(true);
        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIModuleOutline").get_value(true)) {
            GuiUtil.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
            GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
        }
        GuiUtil.draw_rect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1));
        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIModuleOutline").get_value(true)) {
            GuiUtil.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1));
            GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1));
            if (dark_outline) {
                GuiUtil.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA4").get_value(1));
                GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA4").get_value(1));
            }
        }
        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIModuleOutline").get_value(true) && Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1) == 255) {
            GuiUtil.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
            GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
            if (dark_outline) {
                GuiUtil.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA4").get_value(1));
                GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA4").get_value(1));
            }
        }
        GuiUtil.draw_rect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 1, parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
        GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() - 1, parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
        if (dark_outline) GuiUtil.draw_rect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 1, parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA4").get_value(1));
        if (dark_outline) GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() - 1, parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA4").get_value(1));

        FontUtil.drawText(this.op.get_name() + " " + ChatFormatting.GRAY + this.op.get_current_value().toUpperCase(), parent.parent.getX() + 4, parent.parent.getY() + offset + 4, -1);
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.y = parent.parent.getY() + this.offset;
        this.x = parent.parent.getX();
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen()) {
            final int maxIndex = this.op.get_values().size() - 1;
            this.modeIndex++;
            if (this.modeIndex > maxIndex) {
                this.modeIndex = 0;
            }
            this.op.set_current_value(this.op.get_values().get(this.modeIndex));
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 100 && y > this.y && y < this.y + 15;
    }

    @Override
    public boolean is_shown() {
        return op.is_shown();
    }
}
