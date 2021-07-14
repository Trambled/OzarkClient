package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.player.EntityUtil;
import me.trambled.ozark.ozarkclient.util.player.PlayerUtil;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import me.trambled.ozark.ozarkclient.util.world.BlockInteractionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AutoWeb extends Module {

    public AutoWeb() {
        super(Category.COMBAT);

        this.name        = "AutoSelfWeb";
        this.tag         = "AutoSelfWeb";
        this.description = "Places fuckin webs at ur feet.";
    }

    Setting always_on = create("Always On", "AlwaysOn", false);
    Setting rotate = create("Rotate", "AutoWebRotate", true);
    Setting range = create("Enemy Range", "AutoWebRange", 3, 0, 8);

    int new_slot = -1;

    boolean sneak = false;

    @Override
    public void enable() {

        if (mc.player != null) {

            new_slot = find_in_hotbar();

            if (new_slot == -1) {
                MessageUtil.send_client_error_message("cannot find webs in hotbar");
                set_active(false);
            }

        }

    }

    @Override
    public void disable() {
        if (mc.player != null) {
            if (sneak) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                sneak = false;
            }
        }
    }

    @Override
    public void update() {

        if (mc.player == null) return;

        if (always_on.get_value(true)) {

            EntityPlayer target = find_closest_target();
            if (target == null) return;

            if (mc.player.getDistance(target) < range.get_value(1) && is_surround()) {
                int last_slot = mc.player.inventory.currentItem;
                mc.player.inventory.currentItem = new_slot;
                mc.playerController.updateController();
                place_blocks(PlayerUtil.getLocalPlayerPosFloored());
                mc.player.inventory.currentItem = last_slot;
            }

        } else {
            int last_slot = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = new_slot;
            mc.playerController.updateController();
            place_blocks(PlayerUtil.getLocalPlayerPosFloored());
            mc.player.inventory.currentItem = last_slot;
            this.set_disable();
        }

    }

    public EntityPlayer find_closest_target()  {

        if (mc.world.playerEntities.isEmpty())
            return null;

        EntityPlayer closestTarget = null;

        for (final EntityPlayer target : mc.world.playerEntities)
        {
            if (target == mc.player)
                continue;

            if (FriendUtil.isFriend(target.getName()))
                continue;

            if (!EntityUtil.isLiving(target))
                continue;

            if (target.getHealth() <= 0.0f)
                continue;

            if (closestTarget != null)
                if (mc.player.getDistance(target) > mc.player.getDistance(closestTarget))
                    continue;

            closestTarget = target;
        }

        return closestTarget;
    }

    private int find_in_hotbar() {

        for (int i = 0; i < 9; ++i) {

            final ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack.getItem() == Item.getItemById(30)) {
                return i;
            }

        }
        return -1;
    }

    private boolean is_surround() {

        BlockPos player_block = PlayerUtil.getLocalPlayerPosFloored();
        return mc.world.getBlockState(player_block.east()).getBlock() != Blocks.AIR
            && mc.world.getBlockState(player_block.west()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(player_block.north()).getBlock() != Blocks.AIR
                    && mc.world.getBlockState(player_block.south()).getBlock() != Blocks.AIR
                        && mc.world.getBlockState(player_block).getBlock() == Blocks.AIR;

    }

    private void place_blocks(BlockPos pos) {

        if (!mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            return;
        }

        if (!BlockInteractionHelper.checkForNeighbours(pos)) {
            return;
        }

        for (EnumFacing side : EnumFacing.values()) {

            BlockPos neighbor = pos.offset(side);

            EnumFacing side2 = side.getOpposite();

            if (!BlockInteractionHelper.canBeClicked(neighbor)) continue;

            if (BlockInteractionHelper.blackList.contains(mc.world.getBlockState(neighbor).getBlock())) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                sneak = true;
            }

            Vec3d hitVec = new Vec3d(neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));

            if (rotate.get_value(true)) {
                BlockInteractionHelper.faceVectorPacketInstant(hitVec);
            }

            mc.playerController.processRightClickBlock(mc.player, mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
            mc.player.swingArm(EnumHand.MAIN_HAND);

            return;
        }

    }

}
