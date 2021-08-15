package me.trambled.ozark.ozarkclient.guiscreen.gui.past.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.gui.past.Component;
import me.trambled.ozark.ozarkclient.util.font.FontUtil;
import me.trambled.ozark.ozarkclient.util.render.GuiUtil;
import org.lwjgl.input.Keyboard;

public class KeybindComponent extends Component {
    private boolean isBinding;
    private final ModuleButton parent;
    private int offset;
    private int x;
    private int y;
    private String points;
    private float tick;
    private int rainbowOff;


    public KeybindComponent(ModuleButton parent, int offset) {
        this.parent = parent;
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
        this.offset = offset;
        this.points = ".";
        this.tick = 0;
    }

    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }


    @Override
    public void renderComponent() {
        boolean dark_outline = !Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIBrightOutline").get_value(true);
        rainbowOff = parent.parent.rainbowOff;

        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIModuleOutline").get_value(true)) {
            GuiUtil.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
            GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
        }
        GuiUtil.draw_rect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1));
        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIModuleOutline").get_value(true)) {
            GuiUtil.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1));
            GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1));
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

        if (isBinding) {
            tick += 0.5f;

            FontUtil.drawText("Listening" + ChatFormatting.GRAY + " " + points, parent.parent.getX() + 4, parent.parent.getY() + offset + 4, -1);
        } else {
            FontUtil.drawText("Bind" + ChatFormatting.GRAY + " " + parent.mod.get_bind("string"), parent.parent.getX() + 4, parent.parent.getY() + offset + 4, -1);
        }

        if (isBinding) {
            if (tick >= 15) {
                points = "..";
            }
            if (tick >= 30) {
                points = "...";
            }
            if (tick >= 45) {
                points = ".";
                tick = 0.0f;
            }
        }
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.y = parent.parent.getY() + this.offset;
        this.x = parent.parent.getX();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen()) {
            this.isBinding = !this.isBinding;
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        if (this.isBinding) {
            if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
                this.parent.mod.set_bind(0);
                this.isBinding = false;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_BACK)) {
                this.parent.mod.set_bind(0);
                this.isBinding = false;
            } else {
                this.parent.mod.set_bind(key);
                this.isBinding = false;
            }
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 100 && y > this.y && y < this.y + 15;
    }
}
