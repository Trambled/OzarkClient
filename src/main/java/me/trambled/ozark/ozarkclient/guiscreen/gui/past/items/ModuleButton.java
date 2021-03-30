package me.trambled.ozark.ozarkclient.guiscreen.gui.past.items;

import me.trambled.ozark.ozarkclient.util.DrawUtil;
import me.trambled.ozark.ozarkclient.guiscreen.gui.past.Component;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.gui.past.Panel;
import me.trambled.ozark.ozarkclient.guiscreen.gui.past.font.FontUtil;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.SoundEvents;

import java.util.ArrayList;

public class ModuleButton extends Component {
    private ArrayList<Component> subcomponents;
    public Module mod;
    public Panel parent;
    public int offset;
    private boolean open;
    private boolean hovered;

    public ModuleButton(Module mod, Panel parent, int offset) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList<>();
        this.open = false;
        int opY = offset + 15;

            for (Setting settings : Ozark.get_setting_manager().get_settings_with_module(mod)) {
                if (settings.get_type().equals("button")) {
                    this.subcomponents.add(new BooleanComponent(settings, this, opY));
                    opY += 15;
                } else if (settings.get_type().equals("integerslider")) {
                    this.subcomponents.add(new IntegerComponent(settings, this, opY));
                    opY += 15;
                } else if (settings.get_type().equals("doubleslider")) {
                    this.subcomponents.add(new DoubleComponent(settings, this, opY));
                    opY += 15;
                } else if (settings.get_type().equals("combobox")) {
                    this.subcomponents.add(new ModeComponent(settings, this, opY));
                    opY += 15;
                } else if (settings.get_type().equals("label")) {
                    this.subcomponents.add(new InfoComponent(settings, this, opY));
                    opY += 15;
                } else if (settings.get_type().equals("bind")) {
                    this.subcomponents.add(new KeybindSettingComponent(settings, this, opY));
                    opY += 15;
                }
            }
        this.subcomponents.add(new KeybindComponent(this, opY));
    }

    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
        int opY = this.offset + 15;
        for (final Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 15;
        }
    }

    @Override
    public void renderComponent() {
        if (this.mod.is_active() || mod.get_tag().equalsIgnoreCase("PastGUI")) {
            DrawUtil.draw_rect(parent.getX() - 1, parent.getY() + offset, parent.getX() + parent.getWidth() + 1, parent.getY() + 15 + offset + 1, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
            DrawUtil.draw_rect(parent.getX() - 1, parent.getY() + offset, parent.getX(), parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
            DrawUtil.draw_rect(parent.getX() + parent.getWidth(), parent.getY() + offset, parent.getX() + parent.getWidth() + 1, parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
            DrawUtil.draw_rect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));

            Gui.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + 15 + offset, 0x75101010);
        } else {
            DrawUtil.draw_rect(parent.getX() - 1, parent.getY() + offset, parent.getX() + parent.getWidth() + 1, parent.getY() + 15 + offset + 1, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
            DrawUtil.draw_rect(parent.getX() - 1, parent.getY() + offset, parent.getX(), parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));
            DrawUtil.draw_rect(parent.getX() + parent.getWidth(), parent.getY() + offset, parent.getX() + parent.getWidth() + 1, parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1));

            Gui.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + 15 + offset, 0xFF111111);
        }

        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIHoverChange").get_value(true) && hovered == true) {
            FontUtil.drawText(this.mod.get_name(), parent.getX() + 6, parent.getY() + offset + 4, -1);
        } else {
            FontUtil.drawText(this.mod.get_name(), parent.getX() + 4, parent.getY() + offset + 4, -1);
        }

        if (this.subcomponents.size() > 1) {
            FontUtil.drawText("...", parent.getX() + parent.getWidth() - 10, (parent.getY() + offset + 4), -1);
        }

        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIDescriptions").get_value(true) && hovered == true) {
            FontUtil.drawText(mod.get_description(), 2, (new ScaledResolution(mc).getScaledHeight() - FontUtil.getFontHeight() - 2), -1);
        }

        if (this.open && !this.subcomponents.isEmpty()) {
            for (Component comp : this.subcomponents) {
                comp.renderComponent();
            }
        }
    }

    @Override
    public void closeAllSub() {
        this.open = false;
    }

    @Override
    public int getHeight() {
        if (this.open) {
            return 15 * (this.subcomponents.size() + 1);
        }
        return 15;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = this.isMouseOnButton(mouseX, mouseY);

        if (!this.subcomponents.isEmpty()) {
            for (Component comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();

            if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUISound").get_value(true)) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
        }

        if (isMouseOnButton(mouseX, mouseY) && button == 1) {
            if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUISound").get_value(true)) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }

            if (!this.isOpen()) {
                parent.closeAllSetting();
                this.setOpen(true);
            } else {
                this.setOpen(false);
            }
            this.parent.refresh();
        }

        for (Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        for (Component comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Component comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > parent.getX() && x < parent.getX() + 100 && y > this.parent.getY() + this.offset && y < this.parent.getY() + 15 + this.offset;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}