package me.trambled.ozark.ozarkclient.guiscreen.gui.past.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.gui.past.Component;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.font.FontUtil;
import me.trambled.ozark.ozarkclient.util.render.GuiUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class IntegerComponent extends Component {
    private final Setting set;
    private final ModuleButton parent;
    private int offset;
    private int x;
    private int y;
    private boolean dragging;
    private double sliderWidth;
    private int rainbowOff;


    public IntegerComponent(Setting value, ModuleButton button, int offset) {
        this.dragging = false;
        this.set = value;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }


    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }

    @Override
    public void renderComponent() {
        if (sliderWidth < 0) sliderWidth = 0;

        if (sliderWidth == 0 || sliderWidth == 1) sliderWidth = 2;

        if (sliderWidth > parent.parent.getWidth()) sliderWidth = parent.parent.getWidth();
        boolean trambled_mode = Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUITrampled").get_value(true);
        boolean dark_outline = !Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIBrightOutline").get_value(true);
        rainbowOff = parent.parent.rainbowOff;

        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIModuleOutline").get_value(true) && Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1) == 255) {
            GuiUtil.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
            GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
            if (dark_outline) {
                GuiUtil.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA4").get_value(1));
                GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA4").get_value(1));
            }
        }
        GuiUtil.draw_rect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1));

        GuiUtil.draw_rect(parent.parent.getX() + 1, parent.parent.getY() + offset, parent.parent.getX() + (int) sliderWidth - 1, parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
        if (trambled_mode) GuiUtil.draw_rect(parent.parent.getX() + 1, parent.parent.getY() + offset, parent.parent.getX() + (int) sliderWidth - 1, parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA4").get_value(1));

        GuiUtil.draw_rect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 1, parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
        GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() - 1, parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
        if (dark_outline) GuiUtil.draw_rect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 1, parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA4").get_value(1));
        if (dark_outline) GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() - 1, parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA4").get_value(1));

        FontUtil.drawText(this.set.get_name() + ChatFormatting.GRAY + " " + this.set.get_value(1), parent.parent.getX() + 4, parent.parent.getY() + offset + 3, -1);
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.y = parent.parent.getY() + this.offset;
        this.x = parent.parent.getX();

        double diff = Math.min(100, Math.max(0, mouseX - this.x));
        int min = this.set.get_min(1);
        int max = this.set.get_max(1);
        this.sliderWidth = 100 * (this.set.get_value(1) - min) / (max - min);

        if (this.dragging) {
            if (diff == 0) {
                this.set.set_value(this.set.get_min(1));
            } else {
                int newValue = (int) roundToPlace(diff / 100 * (max - min) + min, 2);
                this.set.set_value(newValue);
            }
        }
    }

    private static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen()) {
            this.dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        this.dragging = false;
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 100 && y > this.y && y < this.y + 15;
    }

    @Override
    public boolean is_shown() {
        return set.is_shown();
    }
}
