package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.player.PlayerUtil;
import me.trambled.ozark.ozarkclient.util.world.CrystalUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public class Offhand extends Module {

    public Offhand() {
        super(Category.COMBAT);

        this.name        = "Offhand";
        this.tag         = "Offhand";
        this.description = "Switches shit to ur offhand.";
    }

    Setting switch_mode = create("Offhand", "OffhandOffhand", "Totem", combobox("Totem", "Crystal", "Gapple", "Pressure Plate"));
    Setting totem_switch = create("Totem HP", "OffhandTotemHP", 16, 0, 36);
    Setting module_check = create("ModuleCheck", "OffhandModuleCheck", false);
    Setting gapple_in_hole = create("Gapple In Hole", "OffhandGapple", false);
    Setting gapple_hole_hp = create("Gapple Hole HP", "OffhandGappleHP", 8, 0, 36);
    Setting only_when_right_click = create("Right Click", "OffhandRightClick", false);
    Setting sword_gap = create("Sword Gap", "OffhandSwordGap", true);
    Setting step = create("Step", "OffhandStep", false);
    Setting crystal_check = create("Crystal Check", "OffhandCrystalCheck", true);
    Setting damage_multiplier = create("Damage Multiplier", "OffhandDamageMultiplier", 1f, 1f, 3f);
    Setting gapclick = create("Gap Right Click", "OffhandGRC", false);

    private boolean switching = false;
    private int last_slot;

    @Override
    public void update() {

        if ((mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {

            if (Ozark.get_module_manager().get_module_with_tag("AutoTotem").is_active()) {
                MessageUtil.send_client_error_message("AutoTotem is not compatible with offhand anymore");
                Ozark.get_module_manager().get_module_with_tag("AutoTotem").set_disable();
            }

            if (switching) {
                swap_items(last_slot, 2);
                return;
            }

            float hp = mc.player.getHealth() + mc.player.getAbsorptionAmount();

            if (mc.gameSettings.keyBindUseItem.pressed || !only_when_right_click.get_value(true)) {
                if (hp > totem_switch.get_value(1)) {
                    if (lethal_crystal() && crystal_check.get_value(true)) {
                        swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), 0);
                        return;
                    }
                    if (sword_gap.get_value(true) &&  mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD && (mc.gameSettings.keyBindUseItem.isKeyDown() || !gapclick.get_value(true))) {
                        swap_items(get_item_slot(Items.GOLDEN_APPLE), step.get_value(true) ? 1 : 0);
                        return;
                    }
                    if (switch_mode.in("Crystal") && (Ozark.get_module_manager().get_module_with_tag("AutoCrystal").is_active() || !module_check.get_value(true))) {
                        swap_items(get_item_slot(Items.END_CRYSTAL), 0);
                        return;
                    }
                    if (gapple_in_hole.get_value(true) && hp > gapple_hole_hp.get_value(1) && is_in_hole()) {
                        swap_items(get_item_slot(Items.GOLDEN_APPLE), step.get_value(true) ? 1 : 0);
                        return;
                    }
                    if (switch_mode.in("Totem")) {
                        swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), step.get_value(true) ? 1 : 0);
                        return;
                    }
                    if (switch_mode.in("Gapple")) {
                        swap_items(get_item_slot(Items.GOLDEN_APPLE), step.get_value(true) ? 1 : 0);
                        return;
                    }
                    if (switch_mode.in("Crystal") && !Ozark.get_module_manager().get_module_with_tag("AutoCrystal").is_active() && module_check.get_value(true)) {
                        swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), 0);
                        return;
                    }
                } else {
                    swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), step.get_value(true) ? 1 : 0);
                    return;
                }

                if (mc.player.getHeldItemOffhand().getItem() == Items.AIR) {
                    swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), step.get_value(true) ? 1 : 0);
                }
            } else {
                swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), step.get_value(true) ? 1 : 0);
            }
        }

    }

    public void swap_items(int slot, int step) {
        if (slot == -1) return;
        if (step == 0) {
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
        }
        if (step == 1) {
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            switching = true;
            last_slot = slot;
        }
        if (step == 2) {
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            switching = false;
        }

        mc.playerController.updateController();
    }

    private boolean is_in_hole() {

        BlockPos player_block = PlayerUtil.getLocalPlayerPosFloored();

        return mc.world.getBlockState(player_block.east()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(player_block.west()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(player_block.north()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(player_block.south()).getBlock() != Blocks.AIR;
    }


    private int get_item_slot(Item input) {
        if (input == mc.player.getHeldItemOffhand().getItem()) return -1;
        for(int i = 36; i >= 0; i--) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if(item == input) {
                if (i < 9) {
                    if (input == Items.GOLDEN_APPLE) {
                        return -1;
                    }
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }

    @Override
    public void update_always() {
        totem_switch.set_shown(!switch_mode.in("Totem"));
        module_check.set_shown(switch_mode.in("Crystal"));
        gapple_in_hole.set_shown(!switch_mode.in("Gapple"));
        gapple_hole_hp.set_shown(!switch_mode.in("Gapple") && gapple_in_hole.get_value(true));
        only_when_right_click.set_shown(!switch_mode.in("Totem"));
        step.set_shown(!switch_mode.in("Totem"));
        sword_gap.set_shown(!switch_mode.in("Gapple"));
        gapclick.set_shown(!switch_mode.in("Gapple") && sword_gap.get_value(true));
        crystal_check.set_shown(!switch_mode.in("Totem"));
        damage_multiplier.set_shown(!switch_mode.in("Totem") && crystal_check.get_value(true));
    }
    
    private boolean lethal_crystal() {
        for (Entity t : mc.world.loadedEntityList) {
            if (t instanceof EntityEnderCrystal && mc.player.getDistance(t) <= 12) {
                if (CrystalUtil.calculateDamage(t.posX, t.posY, t.posZ, mc.player) * damage_multiplier.get_value(1d) >= mc.player.getHealth()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String array_detail() {
        if (switch_mode.in("Crystal" )) {
            return "Crystal";
        }else if (switch_mode.in("Totem" )) {
            return "Totem";
        }else if (switch_mode.in("Gapple" )) {
            return "Gapple";
        }else {
            return "Plate";

        }
    }}
