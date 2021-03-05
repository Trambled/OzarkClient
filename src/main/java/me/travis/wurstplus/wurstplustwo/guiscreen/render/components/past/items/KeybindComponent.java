package me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.WurstplusDraw;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.Component;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.font.FontUtil;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.items.ModuleButton;
import me.travis.wurstplus.wurstplustwo.hacks.PastGUIHack;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import sun.security.pkcs11.Secmod;

public class KeybindComponent extends Component {
    private boolean isBinding;
    private ModuleButton parent;
    private int offset;
    private int x;
    private int y;
    private String points;
    private float tick;

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

    private final int past_gui_r = Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1);
    private final int past_gui_g = Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1);
    private final int past_gui_b = Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1);
    private final int past_gui_a = Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1);

    @Override
    public void renderComponent() {
        WurstplusDraw.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + 15 + offset, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
        WurstplusDraw.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + 15 + offset, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
        WurstplusDraw.draw_rect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + offset + 16, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));

        Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, 0xFF111111);

        WurstplusDraw.draw_rect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 1, parent.parent.getY() + offset + 15, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
        WurstplusDraw.draw_rect(parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() - 1, parent.parent.getY() + offset + 15, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));

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
        if (x > this.x && x < this.x + 100 && y > this.y && y < this.y + 15) {
            return true;
        } else {
            return false;
        }
    }
}
