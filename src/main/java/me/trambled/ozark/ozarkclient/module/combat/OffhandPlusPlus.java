package me.trambled.ozark.ozarkclient.module.combat;


import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import me.trambled.ozark.ozarkclient.util.InventoryUtil;
import org.lwjgl.input.Mouse;


public class OffhandPlusPlus extends Module {

    public OffhandPlusPlus() {
        super(Category.COMBAT);

        this.name = "Offhand++";
        this.tag = "OffhandPlusPlusPlusPlusPlusPlusPlusPlusPlusPlusPlusPlusPlusPlusPlusPlusPlusPlusPlusPlus";
        this.description = "Offhand with binds.";
    }

    Setting mode = create("mode", "mode", "Crystal", combobox("Crystal", "Gapple", "Bed", "Chorus", "Totem"));
    Setting fallbackMode = create("Fallback", "Fallback", "Totem", combobox("Crystal", "Gapple", "Bed", "Chorus", "Totem"));
    Setting totem_switch = create("Totem HP", "OffhandPlusTotemHP", 16, 0, 36);
    Setting checks = create("checks", "check", false);
    Setting elytra = create("elytracheck", "elytracheck", false);
    Setting fallCheck = create("falling", "fall", false);
    Setting swordGap = create("Swordgap", "sg", false);
    Setting forceGap = create("forcegap", "fg", false);
    Setting hotbar = create("hotbar", "hb", false);


    public void update() {
        if (mc.player == null || mc.world == null) {
            return;

 Item searching = Items.TOTEM_OF_UNDYING;

            if (mc.player.isElytraFlying() & elytra.get_value(true))
                return;

            if (mc.player.fallDistance > 5 && fallCheck.get_value(true))
                return;
            {


                if (mode.in("Crystal")) {
                    searching = Items.END_CRYSTAL;
                }

                if (mode.in("Gapple")) {
                    searching = Items.GOLDEN_APPLE;
                }
                if (mode.in("Bed")) {
                    searching = Items.BED;
                }
                if (mode.in("Chorus")) {
                    searching = Items.CHORUS_FRUIT;

                }

                if (totem_switch.get_value(1) > mc.player.getHealth() + mc.player.getAbsorptionAmount())
                    searching = Items.TOTEM_OF_UNDYING;

                else if (InventoryUtil.getHeldItem(Items.DIAMOND_SWORD) && swordGap.get_value(true))
                    searching = Items.GOLDEN_APPLE;

                else if (forceGap.get_value(true) && Mouse.isButtonDown(1))
                    searching = Items.GOLDEN_APPLE;

                if (mc.player.getHeldItemOffhand().getItem() == searching)
                    return;

                if (mc.currentScreen != null)
                    return;

                if (InventoryUtil.getInventoryItemSlot(searching, hotbar.get_value(false)) != -1)
                    InventoryUtil.moveItemToOffhand(InventoryUtil.getInventoryItemSlot(searching, hotbar.get_value(true)));
                return;
            }

            if (mode.in("Crystal")) {
                searching = Items.END_CRYSTAL;
            }

            if (mode.in("Gapple")) {
                searching = Items.GOLDEN_APPLE;
            }
            if (mode.in("Bed")) {
                searching = Items.BED;
            }
            if (mode.in("Chorus")) {
                searching = Items.CHORUS_FRUIT;
            }
                if (mode.in("Totem")) {
                    searching = Items.TOTEM_OF_UNDYING;

                }

                if (mc.player.getHeldItemOffhand().getItem() == searching)
                    return;

                if (InventoryUtil.getInventoryItemSlot(searching, hotbar.get_value(false)) != -1)
                    InventoryUtil.moveItemToOffhand(InventoryUtil.getInventoryItemSlot(searching, hotbar.get_value(true)));
            }


        }
    }







