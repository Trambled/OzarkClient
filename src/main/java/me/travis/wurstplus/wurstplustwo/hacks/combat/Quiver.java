package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

public class Quiver extends WurstplusHack {

    public Quiver() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name        = "Quiver";
        this.tag         = "Quiver";
        this.description = "shoots positive effects at u";
    }

    WurstplusSetting speed = create("Speed", "QuiverSpeed", true);
    WurstplusSetting strength = create("Strength", "QuiverStrength", true);

    int randomVariation;

    @Override
    public void update() {
        PotionEffect speedEffect = mc.player.getActivePotionEffect(Potion.getPotionById(1));
        PotionEffect strengthEffect = mc.player.getActivePotionEffect(Potion.getPotionById(5));

        boolean hasSpeed;
        boolean hasStrength;

        hasSpeed = speedEffect != null;

        hasStrength = strengthEffect != null;

        if (mc.player.inventory.currentItem == find_bow_hotbar())
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0, -90, true));

        if (strength.get_value(true) && !hasStrength)
            if (mc.player.inventory.getCurrentItem().getItem() == Items.BOW && isArrowInInventory("Arrow of Strength")) {
                if (mc.player.getItemInUseMaxCount() >= getBowCharge()) {
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                        mc.player.stopActiveHand();
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        mc.player.setActiveHand(EnumHand.MAIN_HAND);
                } else if (mc.player.getItemInUseMaxCount() == 0) {
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        mc.player.setActiveHand(EnumHand.MAIN_HAND);
                }
            }
        if (speed.get_value(true) && !hasSpeed) {
            if (mc.player.inventory.getCurrentItem().getItem() == Items.BOW && isArrowInInventory("Arrow of Speed")) {
                if (mc.player.getItemInUseMaxCount() >= getBowCharge()) {
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                    mc.player.stopActiveHand();
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    mc.player.setActiveHand(EnumHand.MAIN_HAND);
                } else if (mc.player.getItemInUseMaxCount() == 0) {
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    mc.player.setActiveHand(EnumHand.MAIN_HAND);
                }
            }
        }
    }

    private int find_bow_hotbar() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                return i;
            }
        }
        return -1;
    }


    private boolean isArrowInInventory(String type) {
        boolean inInv = false;
        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() == Items.TIPPED_ARROW) {
                if (itemStack.getDisplayName().equalsIgnoreCase(type)) {
                    inInv = true;
                    switchArrow(i);
                    break;
                }
            }
        }

        return inInv;
    }

    private void switchArrow(int oldSlot) {
        WurstplusMessageUtil.send_client_message("Switching arrows!");
        int bowSlot = mc.player.inventory.currentItem;
        int placeSlot = bowSlot +1;

        if (placeSlot > 8)
            placeSlot = 1;

        if (placeSlot != oldSlot) {
            if (mc.currentScreen instanceof GuiContainer)
                return;

            mc.playerController.windowClick(0, oldSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, placeSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, oldSlot, 0, ClickType.PICKUP, mc.player);
        }
    }

    private int getBowCharge() {
        if (randomVariation == 0)
            randomVariation = 1;

        return 1 + randomVariation;
    }


}