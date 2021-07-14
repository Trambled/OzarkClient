package me.trambled.ozark.ozarkclient.module.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;

public class OffhandPlus extends Module {

    public OffhandPlus() {
        super(Category.COMBAT);

        this.name        = "Offhand+";
        this.tag         = "OffhandPlus";
        this.description = "Offhand with binds.";
    }

    Setting totem_switch = create("Totem HP", "OffhandPlusTotemHP", 16, 0, 36);
    Setting module_check = create("ModuleCheck", "OffhandPlusModuleCheck", false);
    Setting disable_else = create("Disable Else", "OffhandPlusDisableElse", true);
    Setting crystal_bind = create("Crystal", "OffhandPlusCrystal", 0);
    Setting gapple_bind = create("Gapple", "OffhandPlusGapple", 0);
    Setting totem_bind = create("Totem", "OffhandPlusTotem", 0);
    Setting pressure_bind = create("Pressure Plate", "OffhandPlusPressurePlate", 0);
    Setting step = create("Step", "OffhandPlusStep", false);
    Setting chat_msg = create("ChatMsg", "OffhandPlusChatMsgs", true);

    private boolean switching = false;
    private boolean crystal = false;
    private boolean gapple = false;
    private boolean pressure_plate = false;
    private boolean totem = false;
    private int last_slot;

    @Override
    protected void enable() {
        crystal = false;
        gapple = false;
        pressure_plate = false;
        totem = false;
    }

    @Override
    public void on_bind(String tag) {
        if (tag.equals("OffhandPlusCrystal")) {
            crystal = !crystal;
            if (disable_else.get_value(true)) {
                gapple = false;
                pressure_plate = false;
                totem = false;
            }
            if (chat_msg.get_value(true)) {
                if (crystal) {
                    MessageUtil.send_client_message("Crystal offhand " + ChatFormatting.DARK_GREEN + "enabled");
                } else {
                    MessageUtil.send_client_message("Crystal offhand " + ChatFormatting.RED + "disabled");
                }
            }
        }
        if (tag.equals("OffhandPlusGapple")) {
            gapple = !gapple;
            if (disable_else.get_value(true)) {
                crystal = false;
                pressure_plate = false;
                totem = false;
            }
            if (chat_msg.get_value(true)) {
                if (gapple) {
                    MessageUtil.send_client_message("Gapple offhand enabled " + ChatFormatting.DARK_GREEN + "enabled");
                } else {
                    MessageUtil.send_client_message("Gapple offhand " + ChatFormatting.RED + "disabled");
                }
            }
        }
        if (tag.equals("OffhandPlusPressurePlate")) {
            pressure_plate = !pressure_plate;
            if (disable_else.get_value(true)) {
                gapple = false;
                crystal = false;
                totem = false;
            }
            if (chat_msg.get_value(true)) {
                if (pressure_plate) {
                    MessageUtil.send_client_message("Pressure plate offhand" + ChatFormatting.DARK_GREEN + "enabled");
                } else {
                    MessageUtil.send_client_message("Pressure plate offhand " + ChatFormatting.RED + "disabled");
                }
            }
        }
        if (tag.equals("OffhandPlusTotem")) {
            totem = !totem;
            if (disable_else.get_value(true)) {
                gapple = false;
                crystal = false;
                pressure_plate = false;
            }
            if (chat_msg.get_value(true)) {
                if (totem) {
                    MessageUtil.send_client_message("Totem offhand " + ChatFormatting.DARK_GREEN + "enabled");
                } else {
                    MessageUtil.send_client_message("Totem offhand " + ChatFormatting.RED + "disabled");
                }
            }
        }
    }

    @Override
    public void update() {
        if (crystal && crystal_bind.get_bind(1) == 0) {
            crystal = false;
        }
        if (gapple && gapple_bind.get_bind(1) == 0) {
            gapple = false;
        }
        if (pressure_plate && pressure_bind.get_bind(1) == 0) {
            pressure_plate = false;
        }
        if (totem && totem_bind.get_bind(1) == 0) {
            totem = false;
        }

        if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory) {

            if (Ozark.get_module_manager().get_module_with_tag("AutoTotem").is_active()) {
                MessageUtil.send_client_error_message("AutoTotem is not compatible with offhand plus");
                Ozark.get_module_manager().get_module_with_tag("AutoTotem").set_disable();
            }

            if (switching) {
                swap_items(last_slot, 2);
                return;
            }

            float hp = mc.player.getHealth() + mc.player.getAbsorptionAmount();

            if (hp > totem_switch.get_value(1)) {
                if (module_check.get_value(true)) {
                    if (crystal && Ozark.get_module_manager().get_module_with_tag("AutoCrystal").is_active()) {
                        swap_items(get_item_slot(Items.END_CRYSTAL),0);
                        return;
                    }
                } else if (crystal && !module_check.get_value(true)){
                    swap_items(get_item_slot(Items.END_CRYSTAL),0);
                    return;
                }
                if (gapple) {
                    swap_items(get_item_slot(Items.GOLDEN_APPLE), step.get_value(true) ? 1 : 0);
                    return;
                }
                if (crystal && !Ozark.get_module_manager().get_module_with_tag("AutoCrystal").is_active() && module_check.get_value(true)) {
                    swap_items(get_item_slot(Items.TOTEM_OF_UNDYING),0);
                    return;
                }
                swap_items(get_item_slot(Items.TOTEM_OF_UNDYING),step.get_value(true) ? 1 : 0);
            } else {
                swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), step.get_value(true) ? 1 : 0);
                return;
            }

            if (!(mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)) {
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

}
