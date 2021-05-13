package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import com.mojang.realmsclient.gui.ChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class BurrowAlert extends Module {

    public BurrowAlert() {
        super(Category.CHAT);

        this.name = "BurrowAlert";
        this.tag = "Burrow Alert";
        this.description = "tells who is burrowed";
    }

    List<EntityPlayer> players = new ArrayList<>();

    @Override
    protected void enable() {
        players.clear();
    }

    @Override
    public void update() {
        for (EntityPlayer player : mc.world.playerEntities) {
            BlockPos pos = new BlockPos(player.posX, player.posY + 0.2, player.posZ);
            if (mc.world.getBlockState(pos).getBlock().equals(Blocks.OBSIDIAN)) {
                if (players.contains(player)) continue;
                MessageUtil.send_client_message(player.getName() + ChatFormatting.DARK_RED + " has burrowed");
                players.add(player);
            } else {
                if (players.contains(player)) {
                    MessageUtil.send_client_message(player.getName() + ChatFormatting.GREEN + " is no longer burrowed");
                    players.remove(player);
                }
            }
        }
    }
}
