package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.util.WurstplusCrystalUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.entity.Entity;
import me.travis.wurstplus.wurstplustwo.util.BlockUtils;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
    WurstplusSetting max_self_damage = create("Max Self Damage", "MaxSelfDamage", 6, 0, 20);
    WurstplusSetting required_health = create("Required Health", "RequiredHealth", 12f, 1f, 36f);
	WurstplusSetting delay = create("Delay", "Delay", 2, 1, 20);
	WurstplusSetting range = create("Range", "Range", 4, 0, 10);
	WurstplusSetting rotate = create("Rotate", "Rotate", true);
    
    @Override
    public void update() {
        if (this.index > 2000) {
            this.index = 0;
        }

        if (find_in_hotbar() == -1) {
            WurstplusMessageUtil.send_client_message("No materials!");
            this.set_disable();
        }

        //do it below a certain health
        if (mc.player.getHealth()+mc.player.getAbsorptionAmount() > required_health.get_value(1)) {
            return;
        }

        for (final EntityEnderCrystal cry : this.getExclusions()) {
            if (this.index % this.delay.get_value(1) == 0) {
                if (switch_mode.in("Normal")) {
                    InventoryUtils.switchToSlot(find_in_hotbar());
                }
                else if (switch_mode.in("Ghost")) {
                    InventoryUtils.switchToSlotGhost(find_in_hotbar());
                }
                if (mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Item.getItemFromBlock(Blocks.WOODEN_PRESSURE_PLATE)) {
                    BlockUtils.placeBlock(cry.getPosition(), this.rotate.get_value(true));
                    return;
                }
            }
        }
    }
    
    public ArrayList<EntityEnderCrystal> getCrystals() {
        final ArrayList<EntityEnderCrystal> crystals = new ArrayList<EntityEnderCrystal>();
        for (final Entity ent : mc.world.getLoadedEntityList()) {
            if (ent instanceof EntityEnderCrystal) {
                crystals.add((EntityEnderCrystal)ent);
            }
        }
        return crystals;
    }
    
    public ArrayList<EntityEnderCrystal> getInRange() {
        final ArrayList<EntityEnderCrystal> inRange = new ArrayList<EntityEnderCrystal>();
        for (final EntityEnderCrystal crystal : this.getCrystals()) {
            if (mc.player.getDistance((Entity)crystal) <= this.range.get_value(1)) {
                inRange.add(crystal);
            }
        }
        return inRange;
    }
    
    public ArrayList<EntityEnderCrystal> getExclusions() {
        final ArrayList<EntityEnderCrystal> returnOutput = new ArrayList<EntityEnderCrystal>();
        for (final EntityEnderCrystal crystal : this.getInRange()) {
            if (mc.world.getBlockState(crystal.getPosition()).getBlock() == Blocks.WOODEN_PRESSURE_PLATE || mc.world.getBlockState(crystal.getPosition()).getBlock() == Blocks.STONE_PRESSURE_PLATE || mc.world.getBlockState(crystal.getPosition()).getBlock() == Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE || mc.world.getBlockState(crystal.getPosition()).getBlock() == Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE) {
                continue;
            }

            final double self_damage = WurstplusCrystalUtil.calculateDamage(crystal, mc.player);

            if (self_damage < max_self_damage.get_value(1)) {
                continue;
            }

            returnOutput.add(crystal);
        }
        return returnOutput;
    }

    private int find_in_hotbar() {

        for (int i = 0; i < 9; ++i) {

            final ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {

                final Block block = ((ItemBlock) stack.getItem()).getBlock();

                if (block instanceof BlockPressurePlate)
                    return i;

            }
        }
        return -1;
    }
}
