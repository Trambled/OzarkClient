package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.player.PlayerUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * Made by @Trambled on 1/27/2021
 * Trap detect taken from Salhack playerutil
 */

//not much code
public class AntiTrap extends Module
{
    public AntiTrap() {

        super(Category.COMBAT);

        this.name = "AntiTrap";
        this.tag = "AntiTrap";
        this.description = "Choruses out of a trap.";

    }

    Setting toggle = create("Toggle", "Toggle", false);
//  Setting switch_back = create("Switch Back", "SwitchBack", true);
//  Setting chat_msg = create("Chat Msg", "Chat Msg", true);

    public static boolean is_trapped; // so autocrystal can detect that its antitrapping

    @Override
    public void update() {

        if (full_null_check()) return;

        if (find_chorus_hotbar() == -1) {
            this.set_disable();
        }

        if (is_entity_trapped()) {
            mc.player.inventory.currentItem = find_chorus_hotbar();
            is_trapped = true;
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
        }

        if (!is_entity_trapped()) {
            is_trapped = false;
            if (mc.player.inventory.currentItem == find_chorus_hotbar()) {
                mc.gameSettings.keyBindUseItem.pressed = false;
            }

            if (toggle.get_value(true)) {
                this.set_disable();
            }
			
			
        }
    }

    @Override
    protected void enable() {
        mc.gameSettings.keyBindUseItem.pressed = false;
    }

    //salhack
    public static boolean is_entity_trapped()
    {
        BlockPos l_PlayerPos = PlayerUtil.getLocalPlayerPosFloored();

        final BlockPos[] l_TrapPositions = {
                l_PlayerPos.down(),
                l_PlayerPos.up().up(),
                l_PlayerPos.north(),
                l_PlayerPos.south(),
                l_PlayerPos.east(),
                l_PlayerPos.west(),
                l_PlayerPos.north().up(),
                l_PlayerPos.south().up(),
                l_PlayerPos.east().up(),
                l_PlayerPos.west().up(),
        };

        for (BlockPos l_Pos : l_TrapPositions)
        {
            IBlockState l_State = mc.world.getBlockState(l_Pos);

            if (l_State.getBlock() != Blocks.OBSIDIAN && mc.world.getBlockState(l_Pos).getBlock() != Blocks.BEDROCK)
                return false;
        }

        return true;
    }


    private int find_chorus_hotbar() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.CHORUS_FRUIT) {
                return i;
            }
        }
        return -1;
    }
	
}