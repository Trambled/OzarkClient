package me.trambled.ozark.ozarkclient.module.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.world.CrystalUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;


public class Safety extends Module {
    public Safety() {
        super(Category.GUI);

        this.name = "Safety";
        this.tag = "Safety";
        this.description = "shows ur safety status in arraylist";
    }

    Setting rubberband = create("Notify rubberband", "Notifyrubberband", true);

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> receive_listener = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketPlayerPosLook && rubberband.get_value(true)) {
            MessageUtil.send_client_message(ChatFormatting.RED + "Rubberband detected!");
        }
    });


    @Override
    public String array_detail() {
        if (is_fucked(mc.player)) {
            return ChatFormatting.RED + "Unsafe";
        }else{
            return ChatFormatting.GREEN + "Safe";
        }
    }

    public boolean is_fucked(EntityPlayer player) {

        BlockPos pos = new BlockPos(player.posX, player.posY - 1, player.posZ);

        if (CrystalUtil.canPlaceCrystal(pos.south()) || (CrystalUtil.canPlaceCrystal(pos.south().south()) && mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR)) {
            return true;
        }

        if (CrystalUtil.canPlaceCrystal(pos.east()) || (CrystalUtil.canPlaceCrystal(pos.east().east()) && mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR)) {
            return true;
        }

        if (CrystalUtil.canPlaceCrystal(pos.west()) || (CrystalUtil.canPlaceCrystal(pos.west().west()) && mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR)) {
            return true;
        }

        if (CrystalUtil.canPlaceCrystal(pos.north()) || (CrystalUtil.canPlaceCrystal(pos.north().north()) && mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR)) {
            return true;
        }


        return false;

    }
}

