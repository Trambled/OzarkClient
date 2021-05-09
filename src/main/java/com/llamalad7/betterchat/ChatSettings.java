/*
 *       Copyright (C) 2020-present LlamaLad7 <https://github.com/lego3708>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.llamalad7.betterchat;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;


public class ChatSettings {
    private Configuration config;
    public boolean smooth;
    public boolean clear;
    public int xOffset;
    public int yOffset;

    public ChatSettings(Configuration config) {
        this.config = config;
    }

    public void saveConfig() {
        updateConfig(false);
        config.save();
    }

    public void loadConfig() {
        config.load();
        updateConfig(true);
    }

    public void resetConfig() {
        Property prop;

        prop = config.get("All", "Clear", false);
        prop.set(clear = false);

        prop = config.get("All", "Smooth", true);
        prop.set(smooth = true);

        prop = config.get("All", "xOffset", 0);
        prop.set(xOffset = 0);

        prop = config.get("All", "yOffset", 0);
        prop.set(yOffset = 0);
        Minecraft.getMinecraft().gameSettings.chatScale = 1.0f;
        Minecraft.getMinecraft().gameSettings.chatWidth = 1.0f;
        config.save();
    }

    private void updateConfig(boolean load) {
        Property prop;

        prop = config.get("All", "Clear", false);
        if (load) clear = prop.getBoolean();
        else prop.set(clear);

        prop = config.get("All", "Smooth", true);
        if (load) smooth = prop.getBoolean();
        else prop.set(smooth);

        prop = config.get("All", "xOffset", 0);
        if (load) xOffset = prop.getInt();
        else prop.set(xOffset);

        prop = config.get("All", "yOffset", 0);
        if (load) yOffset = prop.getInt();
        else prop.set(yOffset);
    }
}
