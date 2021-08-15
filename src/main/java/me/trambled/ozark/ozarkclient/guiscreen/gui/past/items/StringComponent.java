package me.trambled.ozark.ozarkclient.guiscreen.gui.past.items;


import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.gui.past.Component;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.font.FontUtil;
import me.trambled.ozark.ozarkclient.util.render.GuiUtil;
import me.trambled.ozark.ozarkclient.util.world.TimerUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;

// phobos
public class StringComponent extends Component {
    private final Setting op;
    private final ModuleButton parent;
    private int offset;
    private final int x;
    private final int y;
    public boolean listening = false;
    private boolean idling;
    private CurrentString current_string = new CurrentString("");
    private final TimerUtil timer = new TimerUtil();

    public StringComponent(Setting op, ModuleButton parent, int offset) {
        this.op = op;
        this.parent = parent;
        this.offset = offset;
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
    }

    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }

    @Override
    public void renderComponent() {
        GuiUtil.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
        GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
        GuiUtil.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + offset + 16, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));

        Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, 0xFF111111);

        GuiUtil.draw_rect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 1, parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
        GuiUtil.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() - 1, parent.parent.getY() + offset + 15, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));

        if (listening) {
            FontUtil.drawText(this.current_string.getString() + getIdleSign(), parent.parent.getX() + 4, parent.parent.getY() + this.offset + 4, -1);
        } else {
            FontUtil.drawText(this.op.get_name(), parent.parent.getX() + 4, parent.parent.getY() + this.offset + 4, -1);
        }
    }

    @Override
    public boolean is_shown() {
        return op.is_shown();
    }

    public static String removeLastChar(String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isMouseOnButton(mouseX, mouseY)) {
            listening = !listening;
        } else {
            listening = false;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (this.listening) {
            if (keyCode == 1) {
                return;
            }
            if (keyCode == 28) {
                this.enterString();
            } else if (keyCode == 14) {
                this.setString(removeLastChar(this.current_string.getString()));
            } else if (keyCode == 47 && (Keyboard.isKeyDown(157) || Keyboard.isKeyDown(29))) {
                try {
                    this.setString(this.current_string.getString() + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                this.setString(this.current_string.getString() + typedChar);
            }
        }
    }

    private void enterString() {
        if (!this.current_string.getString().isEmpty()) {
            this.op.set_message(this.current_string.getString());
        } else {
            this.op.set_message(this.op.get_preset_message(""));
        }
        this.op.get_master().on_message(op.get_tag(), op.get_message(""));

        this.setString("");
        listening = false;
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 100 && y > this.y && y < this.y + 15;
    }

    public void setString(String newString) {
        this.current_string = new CurrentString(newString);
    }

    public static class CurrentString {
        private final String string;

        public CurrentString(String string) {
            this.string = string;
        }

        public String getString() {
            return this.string;
        }
    }

    public String getIdleSign() {
        if (this.timer.passed(500L)) {
            this.idling = !this.idling;
            this.timer.reset();
        }
        if (this.idling) {
            return "_";
        }
        return "";
    }
}
