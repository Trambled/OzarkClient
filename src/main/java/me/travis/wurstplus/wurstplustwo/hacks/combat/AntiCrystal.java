package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.util.WurstplusBlockInteractHelper;
import me.travis.wurstplus.wurstplustwo.util.WurstplusCrystalUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.ArrayList;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

//xenon
public class AntiCrystal extends WurstplusHack
{
    public AntiCrystal() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name        = "Anti Crystal";
        this.tag         = "AntiCrystal";
        this.description = "Places a pressure plate below crystals to remove crystal damage";
    }
	
	WurstplusSetting switch_mode = create("Mode", "Mode", "Normal", combobox("Normal", "Ghost", "None"));
    WurstplusSetting max_self_damage = create("Max Self Damage", "MaxSelfDamage", 6, 0, 20);
    WurstplusSetting required_health = create("Required Health", "RequiredHealth", 12f, 1f, 36f);
	WurstplusSetting delay = create("Delay", "Delay", 2, 1, 20);
	WurstplusSetting range = create("Range", "Range", 4, 0, 10);
	WurstplusSetting rotate = create("Rotate", "Rotate", true);

	private int index;
    
    @Override
    public void update() {
        if (this.index > 2000) {
            this.index = 0;
        }

        if (find_in_hotbar() == -1 && find_string_hotbar() == -1) {
            WurstplusMessageUtil.send_client_message("No materials!");
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
                    WurstplusBlockInteractHelper.placeBlock(cry.getPosition(), this.rotate.get_value(true));
                    return;
                }
            }
        }
    }

    public ArrayList<EntityEnderCrystal> get_crystals() {
        final ArrayList<EntityEnderCrystal> crystals = new ArrayList<EntityEnderCrystal>();
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

            final double self_damage = WurstplusCrystalUtil.calculateDamage(cry, mc.player);

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
