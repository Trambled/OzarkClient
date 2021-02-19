package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

/**
 * Made by @Trambled on 1/27/2021
 * Trap detect taken from Salhack playerutil
 */

//not much code
public class AntiTrap extends WurstplusHack
{
    public AntiTrap() {

        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name = "Anti Trap (WIP)";
        this.tag = "AntiTrap";
        this.description = "here u go comdoge";

    }

    WurstplusSetting toggle = create("Toggle", "Toggle", false);
    //WurstplusSetting switch_back = create("Switch Back", "SwitchBack", true);
    //WurstplusSetting chat_msg = create("Chat Msg", "Chat Msg", true);

    public static boolean is_trapped; // so autocrystal can detect that its antitrapping
	private boolean was_eating = false;

    @Override
    public void update() {

        if (nullCheck()) return;

        if (find_chorus_hotbar() == -1) {
            this.set_disable();
        }

        if (is_entity_trapped(mc.player)) {
            mc.player.inventory.currentItem = find_chorus_hotbar();
            is_trapped = true;
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
		    was_eating = true;
        }

        if (!is_entity_trapped(mc.player)) {
            is_trapped = false;

            if (toggle.get_value(true)) {
                this.set_disable();
            }
			
			
        }
		
        if (was_eating)	{
            was_eating = false;
            mc.gameSettings.keyBindUseItem.pressed = false;
        }
		
    }

    //salhack
    public static boolean is_entity_trapped(EntityPlayer e)
    {
        BlockPos l_PlayerPos = EntityPosToFloorBlockPos(e);

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

    //salhack
    public static BlockPos EntityPosToFloorBlockPos(EntityPlayer e)
    {
        return new BlockPos(Math.floor(e.posX), Math.floor(e.posY), Math.floor(e.posZ));
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