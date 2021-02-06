package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

/**
 * Made by @Trambled on 2/5/2020
 * Bypass mode from WurstplusStep.java
 */

public class Fucker extends WurstplusHack{

	
    public Fucker() {

        super(WurstplusCategory.WURSTPLUS_MISC);

        this.name = "Fucker";
        this.tag = "Fucker";
        this.description = "get out of a hole";
    }

    WurstplusSetting mode = create("Mode", "Mode", "Bypass", combobox("Bypass", "Jump", "TP"));
    WurstplusSetting disable = create("Disable", "Disable", true);

	@Override
	public void update() {
	    //make sures that the head block is air and the block above the head block is also air (as to not get stuck in a self trap, and ofc make sure that the player is burrowed
		if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)).getBlock().equals(Blocks.AIR) && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ)).getBlock().equals(Blocks.AIR)) {
            if (mode.in("Jump")) {
                mc.player.jump();
            }
            if (mode.in("Bypass")) {
                //this is in w+2 step
                // a really good way of bypassing anticheat
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805212, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
            }
            if (mode.in("TP")) {
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
            }
        } else {
		    if (disable.get_value(true)) {
		        this.set_disable();
            }
        }
    }
 }