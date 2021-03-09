package me.travis.wurstplus.wurstplustwo.guiscreen.render.components.past;


import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.SoundEvents;

import java.util.ArrayList;

public class PastGUI extends GuiScreen {
    public static ArrayList<Panel> panels;

    public PastGUI() {
        panels = new ArrayList<>();

        int panelX = 5;
        int panelY = 5;
        int panelWidth = 100;
        int panelHeight = 15;

        for (WurstplusCategory c : WurstplusCategory.values()) {

            if (c.is_hidden()) {
                continue;
            }

            PastGUI.panels.add(new Panel(c.get_name(), panelX, panelY, panelWidth, panelHeight, c));
            panelX += 105;
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        drawDefaultBackground();

        for (Panel p : panels) {
            p.updatePosition(mouseX, mouseY);
            p.drawScreen(mouseX, mouseY, partialTicks);

            for (Component comp : p.getComponents()) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Panel p : panels) {
            if (p.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
                p.setDragging(true);
                p.dragX = mouseX - p.getX();
                p.dragY = mouseY - p.getY();
            } else if (p.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUISound").get_value(true)) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                }
                p.setOpen(!p.isOpen());
            } else if (p.isOpen() && !p.getComponents().isEmpty()) {
                for (Component component : p.getComponents()) {
                    component.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        for (Panel panel : panels) {
            if (panel.isOpen() && !panel.getComponents().isEmpty() && keyCode != 1) {
                for (Component component : panel.getComponents()) {
                    component.keyTyped(typedChar, keyCode);
                }
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public void onGuiClosed() {
        Wurstplus.get_hack_manager().get_module_with_tag("PastGUI").set_active(false);

        Wurstplus.get_config_manager().save_settings();
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Panel p : panels) {
            p.setDragging(false);

            if (p.isOpen() && !p.getComponents().isEmpty()) {
                for (Component component : p.getComponents()) {
                    component.mouseReleased(mouseX, mouseY, state);
                }
            }
        }
    }

    public static ArrayList<Panel> getPanels() {
        return panels;
    }

    public static Panel getPanelByName(String name) {
        Panel panel = null;
        for (Panel p : getPanels()) {
            if (p.title.equalsIgnoreCase(name)) {
                panel = p;
            }
        }
        return panel;
    }

    @Override
    public boolean doesGuiPauseGame() {
        if (Wurstplus.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIPauseGame").get_value(true)) {
            return true;
        } else {
            return false;
        }
    }
}