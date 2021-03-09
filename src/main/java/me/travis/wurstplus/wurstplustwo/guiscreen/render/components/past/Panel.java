package me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past;

import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.WurstplusDraw;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.font.FontUtil;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past.items.ModuleButton;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class Panel {
    protected Minecraft mc = Minecraft.getMinecraft();

    public ArrayList<Component> components;
    public String title;
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean isSettingOpen;
    private boolean isDragging;
    private boolean open;
    public int dragX;
    public int dragY;
    public WurstplusCategory cat;
    public int tY;

    public Panel(String title, int x, int y, int width, int height, WurstplusCategory cat) {
        this.components = new ArrayList<>();
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dragX = 0;
        this.isSettingOpen = true;
        this.isDragging = false;
        this.open = true;
        this.cat = cat;
        this.tY = this.height;

        for (WurstplusHack modules : Wurstplus.get_hack_manager().get_modules_with_category(cat)) {
            if (modules.get_category() == cat) {
                ModuleButton modButton = new ModuleButton(modules, this, tY);
                this.components.add(modButton);
                tY += 15;
            }
        }
        this.refresh();
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        WurstplusDraw.draw_rect(x - 1, y - 1, x + width + 1, y + height + 1, Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
        Gui.drawRect(x, y, x + width, y + height, 0x75101010);

        if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Lato")) {
            FontUtil.drawText(title, x + 4 , y + height / 2 - FontUtil.getFontHeight() / 2, -1);
        } else if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Verdana")) {
            FontUtil.drawText(title, x + 4, y + height / 2 - FontUtil.getFontHeight() / 2, -1);
        } else if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIFont").in("Arial")) {
            FontUtil.drawText(title, x + 4, y + height / 2 - FontUtil.getFontHeight() / 2, -1);
        } else {
            FontUtil.drawText(title, x + 4, y + height / 2 - FontUtil.getFontHeight() / 2, -1);
        }

        if (this.open && !this.components.isEmpty()) {
            for (Component component : components) {
                component.renderComponent();
            }
        }
    }

    public void refresh() {
        int off = this.height;
        for (Component comp : components) {
            comp.setOff(off);
            off += comp.getHeight();
        }
    }

    public boolean isWithinHeader(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

    public void updatePosition(int mouseX, int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - dragX);
            this.setY(mouseY - dragY);
        }
        scroll();
    }

    public void scroll() {
        int scrollWheel = Mouse.getDWheel();

        for (Panel panels : Wurstplus.past_gui.panels) {
            if (scrollWheel < 0) {
                panels.setY((int) (panels.getY() - Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIScrollSpeed").get_value(1)));
                continue;
            }
            if (scrollWheel <= 0) continue;
            panels.setY((panels.getY() + Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIScrollSpeed").get_value(1)));
        }
    }

    public void closeAllSetting() {
        for (Component component : components) {
            component.closeAllSub();
        }
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setDragging(boolean drag) {
        this.isDragging = drag;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public WurstplusCategory getCategory() {
        return cat;
    }
}
