package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.util.GS.*;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import me.trambled.ozark.ozarkclient.util.MessageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class OffhandGS extends Module {

    public OffhandGS() {
        super(Category.COMBAT);

        this.name = "OffhandGs";
        this.tag = "OffhandGs";
        this.description = "gamesense offhand (godhand)";
    }

    Setting defaultItem = create("Default", "Default", "Totem", combobox("Totem", "Crystal", "Gapple", "Plates", "Obby", "Pot", "Exp"));
    Setting nonDefaultItem = create("Non Default", "NonDefault", "Crystal", combobox("Totem", "Crystal", "Gapple", "Obby", "Pot", "Exp", "Plates", "String", "Skull"));
    Setting potionChoose = create("Potion", "Potion", "first", combobox("first", "strength", "swiftness"));
    Setting healthSwitch = create("Health Switch", "HealthSwitch", 15, 0, 36);
    Setting tickDelay = create("Switch Delay", "Delay", 0, 0, 20);
    Setting fallDistance = create("Fall Distance", "FallDistance", 12, 0, 30);
    Setting maxSwitchPerSecond = create("Max Switch", "MaxSwitch", 6, 2, 10);
    Setting biasDamage = create("Bias Damage", "BiasDMG", 1f, 0f, 3f);
    Setting pickObby = create("Pick Obby", "PickObby", true);
    Setting pickObbyShift = create("Pick Obby On Shift", "PickObbyOnShift", false);
    Setting crystObby = create("Crystal Shift Obby", "CrystalObby", false);
    Setting rightGap = create("Right click gap", "RGP", true);
    Setting shiftPot = create("Shift Pot", "ShiftPot", true);
    Setting swordCheck = create("Only Sword", "OnlySword", true);
    Setting swordCrystal = create("Sword Crystal", "SwordCrystal", false);
    Setting pickCrystal = create("Pick Crystal", "PickCrystal", true);
    Setting fallDistanceBol = create("Fall Distance", "FallDistance", true);
    Setting crystalCheck = create("Crystal Check", "CrystalCheck", true);
    Setting noHotBar = create("No Hotbar", "NoHotbar", true);
    Setting onlyHotbar = create("Only Hotbar", "OnlyHotbar", true);
    Setting antiWeakness = create("Antiweakness", "Antiweakness", true);
    Setting hotbarTotem = create("Hotbar Totem", "Hotbartotem", true);

    int prevSlot,
            tickWaited,
            totems;
    boolean returnBack,
            stepChanging,
            firstChange;
    private static boolean activeT = false;
    private static int forceObby;
    private static int forceSkull;
    private ArrayList<Long> switchDone = new ArrayList<>();
    private final ArrayList<Item> ignoreNoSword = new ArrayList<Item>() {
        {
            add(Items.GOLDEN_APPLE);
            add(Items.EXPERIENCE_BOTTLE);
            add(Items.BOW);
            add(Items.POTIONITEM);
        }
    };
    public static boolean isActive() {
        return activeT;
    }

    public static void requestObsidian() {
        forceObby++;
    }

    public static void requestSkull() { forceSkull = 1; }

    public static void removeSkull() { forceSkull = 0; }

    public static void removeObsidian() {
        if (forceObby != 0) forceObby--;
    }

    // Create maps of allowed items
    Map<String, Item> allowedItemsItem = new HashMap<String, Item>() {{
        put("Totem", Items.TOTEM_OF_UNDYING);
        put("Crystal", Items.END_CRYSTAL);
        put("Gapple", Items.GOLDEN_APPLE);
        put("Pot", Items.POTIONITEM);
        put("Exp", Items.EXPERIENCE_BOTTLE);
        put("String", Items.STRING);
    }};
    // Create maps of allowed blocks
    Map<String, net.minecraft.block.Block> allowedItemsBlock = new HashMap<String, net.minecraft.block.Block>() {
        {
            put("Plates", Blocks.WOODEN_PRESSURE_PLATE);
            put("Skull", Blocks.SKULL);
            put("Obby", Blocks.OBSIDIAN);
        }
    };

    @Override
    protected void enable() {
        // Enable it
        activeT = firstChange = true;
        // If they are gonna force us obby
        forceObby = 0;

        returnBack = false;
    }

    @Override
    protected void disable() {
        activeT = false;
        forceObby = forceSkull = 0;
    }

    @Override
    public void update() {
        if (mc.currentScreen instanceof GuiContainer) return;
        // If we are changing
        if (stepChanging)
            // Check if we have to wait
            if (tickWaited++ >= tickDelay.get_value()) {
                // If we are fine, finish
                tickWaited = 0;
                stepChanging = false;
                // Change
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                switchDone.add(System.currentTimeMillis());
                // If yes, return
            } else return;

        // Get number of totems
        totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();

        // If e had an item before that we have to return back
        if (returnBack) {
            // If we have to wait
            if (tickWaited++ >= tickDelay.get_value()) {
                changeBack();
            } else return;
        }

        String itemCheck = getItem();

        // If our offhand is okay
        if (offHandSame(itemCheck)) {

            // If the inventory is opened, close it
            boolean done = false;
            if (hotbarTotem.get_value(true) && itemCheck.equals("Totem") {
                done = switchItemTotemHot();
            }
            if (!done) {
                switchItemNormal(itemCheck);
            }

        }

    }

    private void changeBack() {
        /// Change
        // Check if the slot is not air
        if (prevSlot == -1 || !mc.player.inventory.getStackInSlot(prevSlot).isEmpty())
            prevSlot = findEmptySlot();
        // If it's air
        if (prevSlot != -1) {
            mc.playerController.windowClick(0, prevSlot < 9 ? prevSlot + 36 : prevSlot, 0, ClickType.PICKUP, mc.player);
        } else
            MessageUtil.send_client_message_simple("Your inventory is full. the item that was on your offhand is going to be dropped. Open your inventory and choose where to put it");
        // Set to false
        returnBack = false;
        tickWaited = 0;
    }

    private boolean switchItemTotemHot() {
        // Get totem
        int slot = InventoryUtil.findTotemSlot(0, 8);
        // If we have found one
        if (slot != -1) {
            // Switch
            if (mc.player.inventory.currentItem != slot)
                mc.player.inventory.currentItem = slot;
            return true;
        }
        return false;
    }

    private void switchItemNormal(String itemCheck) {
        // Get slot
        int t = getInventorySlot(itemCheck);
        // If nothing found
        if (t == -1) return;
        // Change
        if (!itemCheck.equals("Totem") && canSwitch())
            return;
        toOffHand(t);
    }

    private String getItem() {
        // This is going to contain the item
        String itemCheck = "";
        // If we have to check
        boolean normalOffHand = true;

        /*
            FallDistance and:
            1) his posY is changing (else probably he is packet flying)
            2) he has not an elytra
            or crystalCheck
         */
        if ((fallDistanceBol.get_value(true)) && mc.player.fallDistance >= fallDistance.get_value(true)) && mc.player.prevPosY != mc.player.posY && !mc.player.isElytraFlying())
                || (crystalCheck.get_value(true) && crystalDamage())) {
            normalOffHand = false;
            itemCheck = "Totem";
        }
        // If forceSkull
        if (forceSkull == 1) {
            itemCheck = "Skull";
            normalOffHand = false;
        }
        // If crystal obby
        Item mainHandItem = mc.player.getHeldItemMainhand().getItem();
        if (forceObby > 0
                || (normalOffHand && (
                (crystObby.get_value(true)) && mc.gameSettings.keyBindSneak.isKeyDown()
                        && mainHandItem == Items.END_CRYSTAL)
                        || (pickObby.get_value(true)) && mainHandItem == Items.DIAMOND_PICKAXE && (!pickObbyShift.get_value(true) || mc.gameSettings.keyBindSneak.isKeyDown()))))) {
            itemCheck = "Obby";
            normalOffHand = false;
        }
        // Sword Crystal
        if (swordCrystal.get_value(true) && (mainHandItem == Items.DIAMOND_SWORD)) {
            itemCheck = "Crystal";
            normalOffHand = false;
        }
        // Pick Crystal
        if (pickCrystal.getValue() && (mainHandItem == Items.DIAMOND_PICKAXE)) {
            itemCheck = "Crystal";
            normalOffHand = false;
        }

        // Gap + Pot
        if (normalOffHand && mc.gameSettings.keyBindUseItem.isKeyDown() && (!swordCheck.get_value(true)) || mainHandItem == Items.DIAMOND_SWORD)) {
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                if (shiftPot.get_value(true)) {
                    itemCheck = "Pot";
                    normalOffHand = false;
                }
            } else if (rightGap.getValue() && !ignoreNoSword.contains(mainHandItem)) {
                itemCheck = "Gapple";
                normalOffHand = false;
            }
        }

        // If weakness
        if (normalOffHand && antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
            normalOffHand = false;
            itemCheck = "Crystal";
        }

        // If no player
        if (normalOffHand && !nearPlayer()) {
            normalOffHand = false;
            itemCheck = noPlayerItem.getValue();
        }


        // Get item to check based from the health
        itemCheck = getItemToCheck(itemCheck);
        return itemCheck;
    }

    private boolean canSwitch() {
        long now = System.currentTimeMillis();

        // Lets remove every old one
        for (int i = 0; i < switchDone.size(); i++) {
            if (now - switchDone.get(i) > 1000)
                switchDone.remove(i);
            else
                break;

        }

        if (switchDone.size() / 2 >= maxSwitchPerSecond.getValue()) {
            return true;
        }
        switchDone.add(now);
        return false;
    }



    private boolean crystalDamage() {
        // Check if the crystal exist
        for (Entity t : mc.world.loadedEntityList) {
            // If it's a crystal
            if (t instanceof EntityEnderCrystal && mc.player.getDistance(t) <= 12) {
                if (DamageUtil.calculateDamage(t.posX, t.posY, t.posZ, mc.player) * biasDamage.getValue() >= mc.player.getHealth()) {
                    return true;
                }
            }
        }
        return false;
    }

    private int findEmptySlot() {
        // Iterate all the inv
        for (int i = 35; i > -1; i--) {
            // If empty, return
            if (mc.player.inventory.getStackInSlot(i).isEmpty())
                return i;
        }
        // If full, -1
        return -1;
    }

    private boolean offHandSame(String itemCheck) {
        // Check if e have arleady that item
        Item offHandItem = mc.player.getHeldItemOffhand().getItem();
        if (allowedItemsBlock.containsKey(itemCheck)) {
            Block item = allowedItemsBlock.get(itemCheck);
            if (offHandItem instanceof ItemBlock)
                // Check if it's the block we have
                return ((ItemBlock) offHandItem).getBlock() != item;
            else if(offHandItem instanceof ItemSkull && item == Blocks.SKULL)
                return true;
        } else {
            Item item = allowedItemsItem.get(itemCheck);
            return item != offHandItem;
        }
        return true;
    }

    private String getItemToCheck(String str) {


        return ( PlayerUtil.getHealth() > healthSwitch.getValue())
                ? (str.equals("")
                ? nonDefaultItem.getValue()
                : str
        )
                : defaultItem.getValue();

    }

    private int getInventorySlot(String itemName) {
        // Get if it's a block or an item
        Object item;
        boolean blockBool = false;
        if (allowedItemsItem.containsKey(itemName)) {
            item = allowedItemsItem.get(itemName);
        } else {
            item = allowedItemsBlock.get(itemName);
            blockBool = true;
        }
        int res;
        // Check if the item return is what we want
        if (!firstChange) {
            if (prevSlot != -1) {
                res = isCorrect(prevSlot, blockBool, item, itemName);
                if (res != -1)
                    return res;
            }
        }
        // Iterate
        for (int i = (onlyHotBar.getValue() ? 8 : 35); i > (this.noHotBar.getValue() ? 9 : -1); i--) {

            res = isCorrect(i, blockBool, item, itemName);
            if (res != -1)
                return res;

        }
        return -1;

    }

    private int isCorrect(int i, boolean blockBool, Object item, String itemName) {
        // Get item
        Item temp = mc.player.inventory.getStackInSlot(i).getItem();
        // If we have to check if it's a block
        if (blockBool) {
            // Check if it's what we want
            if (temp instanceof ItemBlock) {
                if (((ItemBlock) temp).getBlock() == item)
                    return i;
            } else if (temp instanceof ItemSkull && item == Blocks.SKULL) {
                return i;
            }

            // If we have to check if it's an item
        } else {
            // Check if it's what we want
            if (item == temp) {
                // If it's a potion
                if (itemName.equals("Pot") && !(potionChoose.get_value("first")) || mc.player.inventory.getStackInSlot(i).stackTagCompound.toString().split(":")[2].contains(potionChoose.get_value())))
                    return -1;
                return i;
            }
        }
        return -1;
    }



    private void toOffHand(int t) {

        // Lets check if we have arleady an item
        if (!mc.player.getHeldItemOffhand().isEmpty()) {
            // After we have to return this
            if (firstChange)
                prevSlot = t;
            returnBack = true;
            firstChange = !firstChange;
        } else prevSlot = -1;

        // Move the item
        mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
        stepChanging = true;
        tickWaited = 0;
    }


    }








