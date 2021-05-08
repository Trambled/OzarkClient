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
package com.llamalad7.betterchat.handlers;

import com.llamalad7.betterchat.gui.GuiBetterChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public
class InjectHandler {
    public static GuiBetterChat chatGUI;

    @SubscribeEvent
    public
    void onClientTick ( TickEvent.ClientTickEvent event ) {
        MinecraftForge.EVENT_BUS.unregister ( this );
        chatGUI = new GuiBetterChat ( Minecraft.getMinecraft ( ) );
        ObfuscationReflectionHelper.setPrivateValue ( GuiIngame.class , Minecraft.getMinecraft ( ).ingameGUI , chatGUI , "field_73840_e" );
    }
}
