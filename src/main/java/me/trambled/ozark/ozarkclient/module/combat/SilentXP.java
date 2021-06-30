package me.trambled.ozark.ozarkclient.module.combat;


import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

//w+3
public class SilentXP extends Module {

    public SilentXP() {
        super(Category.COMBAT);

        this.name        = "SilentXP";
        this.tag         = "SilentXP";
        this.description = "Mends with your fourth hand.";
    }
    Setting takeOffVal = create("Armor HP", "SXPAmorHP", 15, 1, 90);
    Setting allowTakeOff = create("Remove Armor", "SXPRA", true);
    Setting lookPitch = create("Pitch", "SXPP", 90, 10, 360);
    Setting delay = create("Delay", "SXPDelay", 1, 0, 4);
    Setting rightclick = create("Hold Right Click", "SXPRC", false);

//kambing made rightclick option
    private int delay_count;
    int prvSlot;

    @Override
    public void enable(){
        delay_count = 0;
    }


    private int findExpInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    public void update() {
        if (!rightclick.get_value(true) || mc.gameSettings.keyBindUseItem.isKeyDown()) {
        doXp();
    }}

    public void doXp(){
        int oldPitch = (int)mc.player.rotationPitch;
        prvSlot = mc.player.inventory.currentItem; //TODO add better rotations
        mc.player.connection.sendPacket(new CPacketHeldItemChange(findExpInHotbar()));
        mc.player.rotationPitch = lookPitch.get_value(1);
        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, lookPitch.get_value(1), true));
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        mc.player.rotationPitch = oldPitch;
        mc.player.inventory.currentItem = prvSlot;
        mc.player.connection.sendPacket(new CPacketHeldItemChange(prvSlot));
        if (allowTakeOff.get_value(true)) {
            takeArmorOff(); //TODO add the ArmourMend take off thing
        }
    }

    private ItemStack getArmor(int first) {
        return mc.player.inventoryContainer.getInventory().get(first);
    }

    private void takeArmorOff() {
        int slot = 5;
        while (slot <= 8) {
            ItemStack item;
            item = getArmor(slot);
            double max_dam = item.getMaxDamage();
            double dam_left = item.getMaxDamage() - item.getItemDamage();
            double percent = (dam_left / max_dam) * 100;

            if (percent >= takeOffVal.get_value(1) && !item.equals(Items.AIR)) {
                if (!notInInv(Items.AIR)) {
                    return;
                }
                if (delay_count < delay.get_value(1)) {
                    delay_count++;
                    return;
                }
                delay_count = 0;

                mc.playerController.windowClick(0, slot, 0, ClickType.QUICK_MOVE, mc.player);

            }
            slot++;
        }
    }

    public Boolean notInInv(Item itemOfChoice) {
        int n;
        n = 0;
        if (itemOfChoice == mc.player.getHeldItemOffhand().getItem()) return true;

        for (int i = 35; i >= 0; i--) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == itemOfChoice) {
                return true;

            } else if (item != itemOfChoice) {
                n++;
            }
        }
        if (n >= 35) {

            return false;
        }
        return true;
    }
}

