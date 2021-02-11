package me.travis.wurstplus.wurstplustwo.hacks.combat;

import net.minecraft.entity.Entity;
import java.util.Iterator;
import me.travis.wurstplus.wurstplustwo.util.BlockUtils;
import net.minecraft.util.EnumHand;
import me.travis.wurstplus.wurstplustwo.util.InventoryUtils;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.ArrayList;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

//xenon
public class AntiCrystal extends WurstplusHack
{
    int index;
    
    public AntiCrystal() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name        = "Anti Crystal";
        this.tag         = "AntiCrystal";
        this.description = "based";
        this.index = 0;
    }
	
	WurstplusSetting switch_mode = create("Mode", "Mode", "Normal", combobox("Normal", "Ghost", "None")); 
	WurstplusSetting delay = create("Delay", "Delay", 2, 1, 20);
	WurstplusSetting range = create("Range", "Range", 4, 0, 10);
	WurstplusSetting rotate = create("Rotate", "Rotate", true);
    
    @Override
    public void update() {
        if (this.index > 2000) {
            this.index = 0;
        }
        for (final EntityEnderCrystal cry : this.getNonPlaced()) {
            if (this.index % this.delay.get_value(1) == 0) {
                if (switch_mode.in("Normal")) {
                    InventoryUtils.switchToSlot(Item.getItemFromBlock(Blocks.WOODEN_PRESSURE_PLATE));
                }
                else if (switch_mode.in("Ghost")) {
                    InventoryUtils.switchToSlotGhost(Item.getItemFromBlock(Blocks.WOODEN_PRESSURE_PLATE));
                }
                if (this.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Item.getItemFromBlock(Blocks.WOODEN_PRESSURE_PLATE)) {
                    BlockUtils.placeBlock(cry.getPosition(), this.rotate.get_value(true));
                    return;
                }
                continue;
            }
        }
    }
    
    public ArrayList<EntityEnderCrystal> getCrystals() {
        final ArrayList<EntityEnderCrystal> crystals = new ArrayList<EntityEnderCrystal>();
        for (final Entity ent : this.mc.world.getLoadedEntityList()) {
            if (ent instanceof EntityEnderCrystal) {
                crystals.add((EntityEnderCrystal)ent);
            }
        }
        return crystals;
    }
    
    public ArrayList<EntityEnderCrystal> getInRange() {
        final ArrayList<EntityEnderCrystal> inRange = new ArrayList<EntityEnderCrystal>();
        for (final EntityEnderCrystal crystal : this.getCrystals()) {
            if (this.mc.player.getDistance((Entity)crystal) <= this.range.get_value(1)) {
                inRange.add(crystal);
            }
        }
        return inRange;
    }
    
    public ArrayList<EntityEnderCrystal> getNonPlaced() {
        final ArrayList<EntityEnderCrystal> returnOutput = new ArrayList<EntityEnderCrystal>();
        for (final EntityEnderCrystal crystal : this.getInRange()) {
            if (this.mc.world.getBlockState(crystal.getPosition()).getBlock() == Blocks.WOODEN_PRESSURE_PLATE) {
                continue;
            }
            returnOutput.add(crystal);
        }
        return returnOutput;
    }
}
