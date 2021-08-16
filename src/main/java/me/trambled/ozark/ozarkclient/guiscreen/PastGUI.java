package me.trambled.ozark.ozarkclient.guiscreen;


import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.gui.past.Component;
import me.trambled.ozark.ozarkclient.guiscreen.gui.past.Panel;
import me.trambled.ozark.ozarkclient.guiscreen.gui.past.items.Snow;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.util.render.ParticleUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.init.SoundEvents;

import java.util.ArrayList;
import java.util.Random;

public class PastGUI extends GuiScreen {
    private final ParticleUtil particleUtil = new ParticleUtil(100);
    public static ArrayList<Panel> panels;
    public static ArrayList<Component> components;
    private final ArrayList<Snow> snow_list = new ArrayList <> ( );

    public PastGUI() {
        panels = new ArrayList<>();

        int panelX = 5;
        int panelY = 5;
        int panelWidth = 100;
        int panelHeight = 15;

        for (Category c : Category.values()) {

            if (c.is_hidden()) {
                continue;
            }

            PastGUI.panels.add(new Panel(c.get_name(), panelX, panelY, panelWidth, panelHeight, c));
            panelX += 105;
        }

        Random random = new Random();

        for (int i = 0; i < 100; ++i)
        {
            for (int y = 0; y < 3; ++y)
            {
                Snow snow = new Snow(25 * i, y * -50, random.nextInt(3) + 1, random.nextInt(2)+1);
                snow_list.add(snow);
            }
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        drawDefaultBackground();

        final ScaledResolution res = new ScaledResolution(mc);
        particleUtil.tick(10);
        particleUtil.render();

        if (!snow_list.isEmpty() && Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUISnow").get_value(true))
        {
            snow_list.forEach(snow -> snow.Update(res));
        }


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
                if (Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUISound").get_value(true)) {
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
        if (Ozark.get_module_manager().get_module_with_tag("PastGUI").is_active()) {
            Ozark.get_module_manager().get_module_with_tag("PastGUI").set_active(false);
        }

        if (OpenGlHelper.shadersSupported) {
            try {
                mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            } catch (Exception ignored) {}
        }
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
        return Ozark.get_setting_manager ( ).get_setting_with_tag ( "PastGUI" , "PastGUIPauseGame" ).get_value ( true );
    }

    public final ArrayList<Component> get_components() {
        return components;
    }
}
