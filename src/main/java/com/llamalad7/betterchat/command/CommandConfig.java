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
package com.llamalad7.betterchat.command;

import com.llamalad7.betterchat.gui.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public
class CommandConfig extends CommandBase {

    @Override
    public
    String getName ( ) {
        return "betterchat";
    }

    @Override
    public
    String getUsage ( ICommandSender sender ) {
        return "/betterchat";
    }

    @Override
    public
    boolean checkPermission ( MinecraftServer server , ICommandSender sender ) {
        return true;
    }

    @Override
    public
    void execute ( MinecraftServer server , ICommandSender sender , String[] args ) throws CommandException {
        MinecraftForge.EVENT_BUS.register ( this );
    }

    @SubscribeEvent
    public
    void onClientTick ( TickEvent.ClientTickEvent event ) {
        MinecraftForge.EVENT_BUS.unregister ( this );
        Minecraft.getMinecraft ( ).displayGuiScreen ( new GuiConfig ( ) );
    }
}
