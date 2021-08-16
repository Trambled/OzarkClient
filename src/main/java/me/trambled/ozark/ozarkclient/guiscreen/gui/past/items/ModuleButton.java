package me.trambled.ozark.ozarkclient.guiscreen.gui.past.items;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.gui.past.Component;
import me.trambled.ozark.ozarkclient.guiscreen.gui.past.Panel;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.font.FontUtil;
import me.trambled.ozark.ozarkclient.util.render.GuiUtil;
import me.trambled.turok.draw.RenderHelp;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.SoundEvents;

import java.util.ArrayList;

public class ModuleButton extends Component {
    public ArrayList<Component> subcomponents;
    public Module mod;
    public Panel parent;
    public int offset;
    private boolean open;
    private boolean hovered;
    private final int all_offset;
    private int rainbowOff;


    public ModuleButton(Module mod, Panel parent, int offset) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.all_offset = offset;
        this.subcomponents = new ArrayList<>();
        this.open = false;
        int opY = offset + 15;
            for (Setting settings : Ozark.get_setting_manager().get_settings_with_module(mod)) {
                if (settings.is_shown()) {
                    if (settings.get_type().equals("button")) {
                        this.subcomponents.add(new BooleanComponent(settings, this, opY));
                    } else if (settings.get_type().equals("integerslider")) {
                        this.subcomponents.add(new IntegerComponent(settings, this, opY));
                    } else if (settings.get_type().equals("doubleslider")) {
                        this.subcomponents.add(new DoubleComponent(settings, this, opY));
                    } else if (settings.get_type().equals("combobox")) {
                        this.subcomponents.add(new ModeComponent(settings, this, opY));
                    } else if (settings.get_type().equals("label")) {
                        this.subcomponents.add(new InfoComponent(settings, this, opY));
                    } else if (settings.get_type().equals("bind")) {
                        this.subcomponents.add(new KeybindSettingComponent(settings, this, opY));
                    } else if (settings.get_type().equals("string")) {
                        this.subcomponents.add(new StringComponent(settings, this, opY));
                    }
                }
            }
        this.subcomponents.add(new KeybindComponent(this, opY));
    }

    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }

    @Override
    public void renderComponent() {
        // glitchy in updateComponent
        int new_offset = all_offset;
        for (Component comp : subcomponents) {
            new_offset += 15;
            if (!comp.is_shown()) {
                new_offset -= 15;
            }
            comp.setOff(new_offset);

        }
        boolean trambled_mode = Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUITrampled").get_value(true);
        if (this.mod.is_active()) {
            if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1) == 255) {
                GuiUtil.draw_rect(parent.getX() - 1, parent.getY() + offset, parent.getX() + parent.getWidth() + 1, parent.getY() + 15 + offset + 1, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
                GuiUtil.draw_rect(parent.getX() - 1, parent.getY() + offset, parent.getX(), parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
                GuiUtil.draw_rect(parent.getX() + parent.getWidth(), parent.getY() + offset, parent.getX() + parent.getWidth() + 1, parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
            }
            GuiUtil.draw_rect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR2").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG2").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB2").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA2").get_value(1), rainbowOff);
            if (trambled_mode) GuiUtil.draw_rect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA4").get_value(1));
            if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIModuleOutline").get_value(true) && Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1) != 255) {
                GuiUtil.draw_rect(parent.getX() - 1, parent.getY() + offset, parent.getX(), parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
                GuiUtil.draw_rect(parent.getX() + parent.getWidth(), parent.getY() + offset, parent.getX() + parent.getWidth() + 1, parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
                if (trambled_mode) {
                    GuiUtil.draw_rect(parent.getX() - 1, parent.getY() + offset, parent.getX(), parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA4").get_value(1));
                    GuiUtil.draw_rect(parent.getX() + parent.getWidth(), parent.getY() + offset, parent.getX() + parent.getWidth() + 1, parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB4").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA4").get_value(1));
                }
            }
        } else {
            if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIModuleOutline").get_value(true) && Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1) != 255) {
                GuiUtil.draw_rect(parent.getX() - 1, parent.getY() + offset, parent.getX() + parent.getWidth() + 1, parent.getY() + 15 + offset + 1, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1));
                GuiUtil.draw_rect(parent.getX() - 1, parent.getY() + offset, parent.getX(), parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1));
                GuiUtil.draw_rect(parent.getX() + parent.getWidth(), parent.getY() + offset, parent.getX() + parent.getWidth() + 1, parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1));
            } else if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1) == 255) {
                GuiUtil.draw_rect(parent.getX() - 1, parent.getY() + offset, parent.getX() + parent.getWidth() + 1, parent.getY() + 15 + offset + 1, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
                GuiUtil.draw_rect(parent.getX() - 1, parent.getY() + offset, parent.getX(), parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
                GuiUtil.draw_rect(parent.getX() + parent.getWidth(), parent.getY() + offset, parent.getX() + parent.getWidth() + 1, parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA").get_value(1), rainbowOff);
            }
            GuiUtil.draw_rect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + 15 + offset, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIR3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIG3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIB3").get_value(1), Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIA3").get_value(1));
        }



        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIHoverChange").get_value(true) && hovered) {
            FontUtil.drawText(this.mod.get_name(), parent.getX() + 6, parent.getY() + offset + 4, -1);
        } else {
            FontUtil.drawText(this.mod.get_name(), parent.getX() + 4, parent.getY() + offset + 4, -1);
        }

        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIKambing").get_value(true)) {
            draw_module_suffix(Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUISuffix"));
        } else if (!Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIKambing").get_value(true)) {
            if(this.subcomponents.size() > 1) {
                draw_module_suffix(Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUISuffix"));
            }
        }
        

        if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIDescriptions").get_value(true) && hovered) {
            FontUtil.drawText(mod.get_description(), 2, (new ScaledResolution(mc).getScaledHeight() - FontUtil.getFontHeight() - 2), -1);
        }

        rainbowOff = parent.rainbowOff;


        if (this.open && !this.subcomponents.isEmpty()) {
            for (Component comp : this.subcomponents) {
                if (comp.is_shown()) {
                    comp.renderComponent();
                    parent.rainbowOff++;
                }
            }
        }
        this.parent.refresh();

    }

    @Override
    public void closeAllSub() {
        this.open = false;
    }

    @Override
    public int getHeight() {
        if (this.open) {
            int hidden_off = 0;
            for (Component hidden : subcomponents) {
                if (!hidden.is_shown()) {
                    hidden_off++;
                }
            }
            return (15 * (this.subcomponents.size() + 1)) - (15 * hidden_off);
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
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
        }

        if (isMouseOnButton(mouseX, mouseY) && button == 1) {
            if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUISound").get_value(true)) {
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }

            if (!this.isOpen()) {
                parent.closeAllSetting();
                this.setOpen(true);
            } else {
                this.setOpen(false);
            }
        }

        for (Component comp : this.subcomponents) {
            if (comp.is_shown()) {
                comp.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        for (Component comp : this.subcomponents) {
            if (comp.is_shown()) {
                comp.keyTyped(typedChar, key);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Component comp : this.subcomponents) {
            if (comp.is_shown()) {
                comp.mouseReleased(mouseX, mouseY, mouseButton);
            }
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

    private void draw_module_suffix(Setting setting) {
        if (setting.in("Dots")) {
            FontUtil.drawText("...", parent.getX() + parent.getWidth() - 10, (parent.getY() + offset + 4), -1);
        } else if (setting.in("Angle Bracket")) {
            FontUtil.drawText("<", parent.getX() + parent.getWidth() - 8, (parent.getY() + offset + 4), -1);
        } else if (setting.in("Triangle")) {
            RenderHelp.drawTriangleOutline(parent.getX() + 90f, parent.getY() + offset + 12f, 5f, 2, 1, 1,0XFFFFFF);
        }
    }

}
