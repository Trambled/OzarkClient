package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.world.BlockInteractionHelper;
import me.trambled.ozark.ozarkclient.util.world.CrystalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

import java.util.ArrayList;

//xenon
public class AntiCrystal extends Module
{
    public AntiCrystal() {
        super(Category.COMBAT);

        this.name        = "AntiCrystal";
        this.tag         = "AntiCrystal";
        this.description = "Places a pressure plate below crystals to remove crystal damage.";
    }
	
	Setting switch_mode = create("Mode", "Mode", "Normal", combobox("Normal", "Ghost", "None"));
    Setting max_self_damage = create("Max Self Damage", "MaxSelfDamage", 6, 0, 20);
    Setting required_health = create("Required Health", "RequiredHealth", 12f, 1f, 36f);
	Setting delay = create("Delay", "Delay", 1, 0, 20);
	Setting range = create("Range", "Range", 4, 0, 10);
	Setting rotate = create("Rotate", "Rotate", true);

	private int index;
    
    @Override
    public void update() {
        if (this.index > 2000) {
            this.index = 0;
        }

        if (find_in_hotbar() == -1 && find_string_hotbar() == -1) {
            MessageUtil.send_client_message("No materials!");
            this.set_disable();
        }

        //do it below a certain health
        if (mc.player.getHealth()+mc.player.getAbsorptionAmount() > required_health.get_value(1)) {
            return;
        }

        for (final EntityEnderCrystal cry : get_crystals()) {
            if (this.index % this.delay.get_value(1) == 0) {
                if (switch_mode.in("Normal")) {
                    if (find_in_hotbar() != -1) {
                        mc.player.inventory.currentItem = find_in_hotbar();
                    } else {
                        mc.player.inventory.currentItem = find_string_hotbar();
                    }
                } else if (switch_mode.in("Ghost")) {
                    if (find_in_hotbar() != -1) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(find_in_hotbar()));
                    } else {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(find_string_hotbar()));
                    }
                }

                if (mc.player.inventory.currentItem == find_in_hotbar() || mc.player.inventory.currentItem == find_string_hotbar()) {
                    BlockInteractionHelper.placeBlock(cry.getPosition(), this.rotate.get_value(true));
                    return;
                }
            }
        }
    }

    public ArrayList<EntityEnderCrystal> get_crystals() {
        final ArrayList<EntityEnderCrystal> crystals = new ArrayList <> ( );
        for (final Entity crystal : mc.world.getLoadedEntityList()) {
            if (!(crystal instanceof EntityEnderCrystal)) {
                continue;
            }
            if (!(mc.player.getDistance(crystal) <= this.range.get_value(1))) {
                continue;
            }
            if (mc.world.getBlockState(crystal.getPosition()).getBlock() == Blocks.WOODEN_PRESSURE_PLATE || mc.world.getBlockState(crystal.getPosition()).getBlock() == Blocks.STONE_PRESSURE_PLATE || mc.world.getBlockState(crystal.getPosition()).getBlock() == Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE || mc.world.getBlockState(crystal.getPosition()).getBlock() == Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE) {
                continue;
            }

            EntityEnderCrystal cry = (EntityEnderCrystal) crystal;

            final double self_damage = CrystalUtil.calculateDamage(cry, mc.player);

            if (self_damage < max_self_damage.get_value(1)) {
                continue;
            }

            crystals.add(cry);
        }
        return crystals;
    }

    private int find_in_hotbar() {

        for (int i = 0; i < 9; ++i) {

            final ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {

                final Block block = ((ItemBlock) stack.getItem()).getBlock();

                if (block instanceof BlockPressurePlate)
                    return i;

                else if (block instanceof BlockWeb)
                    return i;

            }
        }
        return -1;
    }

    private int find_string_hotbar() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.STRING) {
                return i;
            }
        }
        return -1;
    }
}
