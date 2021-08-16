package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.world.BlockInteractionHelper;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

//gamesense
public class Blocker extends Module
{

    public Blocker() {
        super(Category.COMBAT);

        this.name        = "Blocker";
        this.tag         = "Blocker";
        this.description = "Blocks anvils and pistons.";
    }

    Setting rotate = create("Rotate", "BlockerRotate", true);
    Setting anvilBlocker = create("Anvils", "BlockerAnvilBlocker", true);
    Setting pistonBlocker = create("Pistons", "BlockerPistonBlocker", true);
	Setting tickDelay = create("Delay", "BlockerTickDelay", 5, 0, 10);
	Setting chatMsg = create("Chat", "BlockerChatMSGS", true);

    private int delayTimeTicks = 0;
    private boolean noObby;
    private boolean noActive;

    @Override
    protected void enable() {
        if (mc.player == null) {
            disable();
            return;
        }

        if (chatMsg.get_value(true)) {

            String output = "";

            if (anvilBlocker.get_value(true))
                output += "Anvil ";
            if (pistonBlocker.get_value(true))
                output += " Piston ";

            if (!output.equals("")) {
                noActive = false;
                MessageUtil.send_client_message(output +" turned ON!");
            }else {
                noActive = true;
                disable();
            }
        }
        noObby = false;
    }

    @Override
    protected void disable() {
        if (mc.player == null) {
            return;
        }
        if (chatMsg.get_value(true)) {
            if (noActive) {
                MessageUtil.send_client_error_message("Nothing is active... Blocker turned OFF!");
            }else if(noObby) {
                MessageUtil.send_client_error_message("Obsidian not found... Blocker turned OFF!");
            }
        }

    }

    @Override
    public void update() {
        if (mc.player == null) {
            disable();
            return;
        }

        if (noObby) {
            disable();
            return;
        }

        if (delayTimeTicks < tickDelay.get_value(1)) {
            delayTimeTicks++;
        }
        else {
            delayTimeTicks = 0;

            if (anvilBlocker.get_value(true)) {
                blockAnvil();
            }
            if (pistonBlocker.get_value(true)) {
                blockPiston();
            }

        }

    }

    private void blockAnvil() {
        // Iterate for everything
        for (Entity t : mc.world.loadedEntityList) {
            // If it's a falling block
            if (t instanceof EntityFallingBlock) {
                Block ex = ((EntityFallingBlock) t).fallTile.getBlock();
                // If it's anvil
                if (ex instanceof BlockAnvil
                        // If coords are the same as us
                        && (int) t.posX == (int) mc.player.posX && (int) t.posZ == (int) mc.player.posZ
                        && get_block(mc.player.posX, mc.player.posY + 2, mc.player.posZ) instanceof BlockAir) {
                    // Place the block
                    placeBlock(new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ));
                    MessageUtil.send_client_message("AutoAnvil detected... Anvil Blocked!");
                }
            }
        }
    }

    private Block get_block(double x, double y, double z) {
        return mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    private void blockPiston() {
        // Iterate for everything
        for (Entity t : mc.world.loadedEntityList) {
            // If it's an ecrystal and it's near us
            if (t instanceof EntityEnderCrystal
                    && t.posX >= mc.player.posX - 1.5 && t.posX <= mc.player.posX + 1.5
                    && t.posZ >= mc.player.posZ - 1.5 && t.posZ <= mc.player.posZ + 1.5) {
                // Check if it's near
                for(int i = -2; i < 3; i++) {
                    for(int j = -2; j < 3; j++) {
                        if (i == 0 || j == 0) {
                            // If it's a piston
                            if (get_block(t.posX + i, t.posY, t.posZ + j) instanceof BlockPistonBase) {
                                // Break
                                breakCrystalPiston(t);
                                MessageUtil.send_client_message("PistonCrystal detected... Destroyed crystal!");
                            }
                        }
                    }
                }
            }
        }
    }

    private int findObsidianSlot() {
        int slot = -1;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) {
                continue;
            }

            Block block = ((ItemBlock) stack.getItem()).getBlock();
            if (block instanceof BlockObsidian) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private boolean placeBlock(BlockPos pos) {
        Block block = mc.world.getBlockState(pos).getBlock();

        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }

        EnumFacing side = BlockInteractionHelper.getPlaceableSide(pos);

        if (side == null) {
            return false;
        }

        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();

        if (!BlockInteractionHelper.canBeClicked(neighbour)) {
            return false;
        }

        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();

        int obsidianSlot = findObsidianSlot();

        if (mc.player.inventory.currentItem != obsidianSlot && obsidianSlot != -1) {
            mc.player.inventory.currentItem = obsidianSlot;
        }

        if (obsidianSlot == -1) {
            noObby = true;
            return false;
        }


        if (rotate.get_value(true)) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }

        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.rightClickDelayTimer = 4;

        return true;
    }


    private void breakCrystalPiston (Entity crystal) {
        // If rotate
        if (rotate.get_value(true)) {
            PistonCrystal.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, mc.player);
        }
        PistonCrystal.breakCrystal(crystal);
        // Rotate
        if (rotate.get_value(true))
            PistonCrystal.resetRotation();
    }

    /// AutoCrystal break things ///
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;

    @EventHandler
    private final Listener<EventPacket.SendPacket> packetSendListener = new Listener<>(event -> {
        Packet packet = event.get_packet();
        if (packet instanceof CPacketPlayer) {
            if (isSpoofingAngles) {
                ((CPacketPlayer) packet).yaw = (float) yaw;
                ((CPacketPlayer) packet).pitch = (float) pitch;
            }
        }
    });
}