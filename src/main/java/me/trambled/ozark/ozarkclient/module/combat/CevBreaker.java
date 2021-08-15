package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.event.events.EventDamageBlock;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.player.EntityUtil;
import me.trambled.ozark.ozarkclient.util.player.PlayerUtil;
import me.trambled.ozark.ozarkclient.util.world.BlockInteractionHelper;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Objects;

//gamesense
public class CevBreaker extends Module
{
    public CevBreaker() {
        super(Category.COMBAT);

        this.name        = "CevBreaker";
        this.tag         = "CevBreaker";
        this.description = "Crystals an opponent from above their heads.";
    }

	Setting breakCrystal = create("Break Crystal", "CivBreakerBreakMode", "Packet", combobox("Vanilla", "Packet", "None"));
    Setting target = create("Target", "CivBreakerTarget", "Nearest", combobox("Nearest", "Looking"));
    Setting enemyRange = create("Range", "CivBreakerEnemyRange", 4.9, 1, 6);
    Setting supDelay = create("Sup Delay", "CivBreakerSupDelay", 1, 0, 4);
    Setting endDelay = create("End Delay", "CivBreakerEndDelay", 1, 0, 4);
    Setting hitDelay = create("Hit Delay", "CivBreakerHitDelay", 2, 0, 20);
    Setting confirmBreak = create("Confirm Break", "CivBreakerConfirmBreak", true);
    Setting confirmPlace=  create("Confirm Place", "CivBreakerConfirmPlace", true);
    Setting crystalDelay = create("Crystal Delay", "CivBreakerCrystalDelay", 2, 0, 20);
    Setting antiWeakness = create("Anti Weakness", "CivBreakerAntiWeakness", false);
    Setting placeCrystal = create("Place Crystal", "CivBreakerPlaceCrystal", true);
    Setting antiStep = create("Anti Step", "CivBreakerAntiStep", false);
    Setting trapPlayer = create("Trap Player", "CivBreakerTrapPlayer", false);
    Setting fastPlace = create("Fast Place", "CivBreakerFastPlace", false);
    Setting blocksPerTick = create("Blocks Per Tick", "CivBreakerBPS", 4, 2, 6);
    Setting pickSwitchTick = create("PickSwitchTick", "CivBreakerPickSwitchTick", 100, 0, 500);
    Setting switchSword = create("Switch Sword", "CivBreakerSwitchSword", false);
	Setting rotate = create("Rotate", "CivBreakerRotate", true);
    Setting midHitDelay = create("Mid HitDelay", "CivBreakerMidHitDelay", 1, 0, 5);
    Setting chatMsg = create("Chat Messages", "CivBreakerMessages", true);

    private boolean noMaterials = false,
            hasMoved = false,
            isSneaking = false,
            isHole = true,
            enoughSpace = true,
            broken,
            stoppedCa,
            deadPl,
            rotationPlayerMoved,
            prevBreak;

    private int oldSlot = -1,
            stage,
            delayTimeTicks,
            hitTryTick,
            tickPick;
    private final int[][] model = new int[][] {
            {1,1,0},
            {-1,1,0},
            {0,1,1},
            {0,1,-1}
    };

    private int[]   slot_mat,
            delayTable,
            enemyCoordsInt;

    private double[] enemyCoordsDouble;

    private structureTemp toPlace;


    Double[][] sur_block = new Double[4][3];

    private EntityPlayer aimTarget;

    @EventHandler
    private final Listener<EventDamageBlock> listener2 = new Listener<>(event -> {

        if (enemyCoordsInt != null && event.getPos().getX() + (event.getPos().getX() < 0 ? 1 : 0) == enemyCoordsInt[0] && event.getPos().getZ() + (event.getPos().getZ() < 0 ? 1 : 0) == enemyCoordsInt[2]) {
            destroyCrystalAlgo();
        }
    });


    @Override
    protected void enable() {
        // Init values
        initValues();
        // Get Target
        if (getAimTarget())
            return;
        // Make checks
        playerChecks();
    }

    // Get target function
    private boolean getAimTarget() {
        /// Get aimTarget
        // If nearest, get it

        if (target.in("Nearest"))
            aimTarget = EntityUtil.findClosestTarget(enemyRange.get_value(1), aimTarget);
            // if looking
        else
            aimTarget = PlayerUtil.findLookingPlayer(enemyRange.get_value(1));

        // If we didnt found a target
        if (aimTarget == null || !target.in("Looking")){
            // if it's not looking and we didnt found a target
            if (!target.in("Looking") && aimTarget == null)
                this.set_disable();
            // If not found a target
            return aimTarget == null;
        }
        return false;
    }

    // Make some checks for startup
    private void playerChecks() {
        // Get all the materials
        if (getMaterialsSlot()) {
            // check if the enemy is in a hole
            if (is_in_hole()) {
                // Get enemy coordinates
                enemyCoordsDouble = new double[] {aimTarget.posX, aimTarget.posY, aimTarget.posZ};
                enemyCoordsInt = new int[] {(int) enemyCoordsDouble[0], (int) enemyCoordsDouble[1], (int) enemyCoordsDouble[2]};
                // Start choosing where to place what
                enoughSpace = createStructure();
                // Is not in a hoke
            } else {
                isHole = false;
            }
            // No materials
        }else noMaterials = true;
    }

    // Init some values
    private void initValues() {
        // Reset aimtarget
        aimTarget = null;
        // Create new delay table
        delayTable = new int[] {
                supDelay.get_value(1),
                crystalDelay.get_value(1),
                hitDelay.get_value(1),
                endDelay.get_value(1)
        };
        // Default values reset
        toPlace = new structureTemp(0, 0, new ArrayList<>());
        isHole = true;
        hasMoved = rotationPlayerMoved = deadPl = broken = false;
        slot_mat = new int[]{-1, -1, -1, -1};
        stage = delayTimeTicks = 0;

        if (mc.player == null){
            this.set_disable();
            return;
        }

        if (chatMsg.get_value(true)){
            MessageUtil.send_client_message("CevBreaker enabled");
        }

        oldSlot = mc.player.inventory.currentItem;

        stoppedCa = false;


    }

    // On disable of the module
    @Override
    protected void disable() {
        if (mc.player == null){
            return;
        }
        // If output
        if (chatMsg.get_value(true)){
            String output = "";
            String materialsNeeded = "";
            // No target found
            if (aimTarget == null) {
                output = "Error: No target found";
            }else
                // H distance not avaible
                if (noMaterials){
                    output = "Error: No Materials Detected";
                    materialsNeeded = getMissingMaterials();
                    // No Hole
                }else if (!isHole) {
                    output = "Error: Enemy is not in a hole!";
                    // No Space
                }else if(!enoughSpace) {
                    output = "Error: too tight, need more space!";
                    // Has Moved
                }else if(hasMoved) {
                    output = "Error: Enemy is too far!";
                }else if(deadPl) {
                    output = "Enemy is dead, ezzz ";
                }
            // Output in chat
            MessageUtil.send_client_message(output + "CevBreaker disabled");
            if (!materialsNeeded.equals(""))
                MessageUtil.send_client_error_message("Materials missing:" + materialsNeeded);

        }

        if (isSneaking){
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;
        }

        if (oldSlot != mc.player.inventory.currentItem && oldSlot != -1){
            mc.player.inventory.currentItem = oldSlot;
            oldSlot = -1;
        }

        noMaterials = false;
    }

    private String getMissingMaterials() {
        /*
			// I use this as a remind to which index refers to what
			0 => obsidian
			1 => Crystal
			2 => Pick
			3 => Sword
		 */
        StringBuilder output = new StringBuilder();

        if (slot_mat[0] == -1)
            output.append(" Obsidian");
        if (slot_mat[1] == -1)
            output.append(" Crystal");
        if ((antiWeakness.get_value(true) || switchSword.get_value(true)) && slot_mat[3] == -1)
            output.append(" Sword");
        if (slot_mat[2] == -1)
            output.append(" Pick");

        return output.toString();
    }

    @Override
    public void update() {
        // If no mc.player
        if (mc.player == null || mc.player.isDead){
            this.set_disable();
            return;
        }



        // Wait
        if (delayTimeTicks < delayTable[stage]){
            delayTimeTicks++;
            return;
        }
        // If the delay is finished
        else {
            delayTimeTicks = 0;
        }


        // Check if something is not ok
        if (enemyCoordsDouble == null || aimTarget == null) {
            if (aimTarget == null) {
                aimTarget = PlayerUtil.findLookingPlayer(enemyRange.get_value(1));
                if (aimTarget != null) {
                    playerChecks();
                }
            }else
                checkVariable();
            return;
        }

        // Check if he is dead
        if (aimTarget.isDead) {
            deadPl = true;
        }

        // Check if he is not in the hole
        if ((int) aimTarget.posX != (int) enemyCoordsDouble[0] || (int) aimTarget.posZ != (int) enemyCoordsDouble[2])
            hasMoved = true;

        // If we have to left
        if (checkVariable()){
            return;
        }

        /*
            This is how it works:
            a) Place obsidian
            b) Place Crystal
            c) Break
            and then, in C, check for destroying the crystal
         */

        /// Start Placing ///
        // A) Lets place all the supports blocks
        if (placeSupport()) {
            switch (stage) {
                // Place obsidian
                case 1:
                    placeBlockThings(stage);
                    if (fastPlace.get_value(true)) {
                        placeCrystal();
                    }
                    prevBreak = false;
                    tickPick = 0;
                    break;

                // Place crystal
                case 2:
                    // Confirm Place
                    if (confirmPlace.get_value(true))
                        if (!(BlockInteractionHelper.getBlock(compactBlockPos(0)) instanceof BlockObsidian)) {
                            stage--;
                            return;
                        }

                    placeCrystal();
                    break;

                // Break
                case 3:
                    // Confirm Place
                    if (confirmPlace.get_value(true))
                        if (getCrystal() == null) {
                            stage--;
                            return;
                        }

                    // Switch to pick / sword
                    int switchValue = 3;
                    if (!switchSword.get_value(true) || (tickPick == pickSwitchTick.get_value(1) || tickPick++ == 0))
                        switchValue = 2;

                    if (mc.player.inventory.currentItem != slot_mat[switchValue]) {
                        mc.player.inventory.currentItem = slot_mat[switchValue];
                    }

                    // Get block
                    BlockPos obbyBreak = new BlockPos(enemyCoordsDouble[0], enemyCoordsInt[1] + 2, enemyCoordsDouble[2]);
                    // If we have not break it yet
                    if (BlockInteractionHelper.getBlock(obbyBreak) instanceof BlockObsidian) {
                        // Get side
                        EnumFacing sideBreak = BlockInteractionHelper.getPlaceableSide(obbyBreak);
                        // If it's != null
                        if (sideBreak != null) {
                            // Switch break values
                            if (breakCrystal.in("Packet")) {
                                if (!prevBreak) {

                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                                    mc.player.connection.sendPacket(new CPacketPlayerDigging(
                                            CPacketPlayerDigging.Action.START_DESTROY_BLOCK, obbyBreak, sideBreak
                                    ));
                                    mc.player.connection.sendPacket(new CPacketPlayerDigging(
                                            CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, obbyBreak, sideBreak
                                    ));

                                    prevBreak = true;
                                }
                            } else if (breakCrystal.in("Normal")) {
                                mc.player.swingArm(EnumHand.MAIN_HAND);
                                mc.playerController.onPlayerDamageBlock(obbyBreak, sideBreak);
                            }
                        }
                    } else {
                        // Destroy crystal
                        destroyCrystalAlgo();
                    }

                    break;
            }
        }

    }

    private void placeCrystal() {
        // Check pistonPlace if confirmPlace
        placeBlockThings(stage);
    }

    private Entity getCrystal() {
        // Check if the crystal exist
        for(Entity t : mc.world.loadedEntityList) {
            // If it's a crystal
            if (t instanceof EntityEnderCrystal) {
                /// Check if the crystal is in the enemy
                // One coordinate is going to be always the same, the other is going to change (because we are pushing it)
                // We have to check if that coordinate is the same as the enemy. Ww add "crystalDeltaBreak" so we can break the crystal before
                // It go to the hole, for a better speed (we find the frame perfect for every servers)
                if (  (int) t.posX == enemyCoordsInt[0] && (int) t.posZ == enemyCoordsInt[2] && t.posY - enemyCoordsInt[1] == 3  )
                    // If found, yoink
                    return t;
            }
        }
        return null;
    }

    // Algo for destroying the crystal
    public void destroyCrystalAlgo() {
        // Get the crystal
        Entity crystal = getCrystal();
        // If we have confirmBreak, we have found 0 crystal and we broke a crystal before
        if (confirmBreak.get_value(true) && broken && crystal == null) {
            /// That means the crystal was broken 100%
            // Reset
            stage = 0;
            broken = false;

        }
        // If found the crystal
        if (crystal != null) {
            // Break it
            breakCrystalPiston(crystal);
            // If we have to check
            if (confirmBreak.get_value(true))
                broken = true;
                // If not, left
            else {
                stage = 0;
            }
        }else stage = 0;
    }


    // Actual break crystal
    private void breakCrystalPiston (Entity crystal) {
        // HitDelay
        if (hitTryTick++ < midHitDelay.get_value(1))
            return;
        else
            hitTryTick = 0;
        // If weaknes
        if (antiWeakness.get_value(true))
            mc.player.inventory.currentItem = slot_mat[3];
        // If rotate
        /// Break type
        // Swing
        if (breakCrystal.in("Vanilla")) {
            EntityUtil.attackEntity(crystal);
            // Packet
        } else if (breakCrystal.in("Packet")) {
            try {
                mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
                mc.player.swingArm(EnumHand.MAIN_HAND);
            } catch (NullPointerException ignored ) {

            }
        }
    }

    // Place the supports blocks
    private boolean placeSupport() {
        // N^ blocks checked
        int checksDone = 0;
        // N^ blocks placed
        int blockDone = 0;
        // If we have to place
        if (toPlace.supportBlock > 0) {
            // Lets iterate and check
            // Lets finish
            do {
                BlockPos targetPos = getTargetPos(checksDone);

                // Try to place the block
                if (placeBlock(targetPos, 0)) {
                    // If we reached the limit
                    if (++blockDone == blocksPerTick.get_value(1))
                        // Return false
                        return false;
                }

                // If we reached the limit, exit
            } while (++checksDone != toPlace.supportBlock);
        }
        stage = stage == 0 ? 1 : stage;
        return true;
    }

    // Place a block
    private boolean placeBlock(BlockPos pos, int step){
        // Get the block
        Block block = mc.world.getBlockState(pos).getBlock();
        // Get all sides
        EnumFacing side = BlockInteractionHelper.getPlaceableSide(pos);

        // If there is a solid block
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)){
            return false;
        }
        // If we cannot find any side
        if (side == null){
            return false;
        }

        // Get position of the side
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();


        // If that block can be clicked
        if (!BlockInteractionHelper.canBeClicked(neighbour)){
            return false;
        }

        // Get the position where we are gonna click
        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();

        /*
			// I use this as a remind to which index refers to what
			0 => obsidian
			1 => Crystal
			2 => Pick
			3 => Sword
		 */
        // Get what slot we are going to select
        // If it's not empty
        if (slot_mat[step] == 11 || mc.player.inventory.getStackInSlot(slot_mat[step]) != ItemStack.EMPTY) {
            // Is it is correct
            if (mc.player.inventory.currentItem != slot_mat[step]) {
                // Change the hand's item (è qui l'errore)
                mc.player.inventory.currentItem = slot_mat[step] == 11 ? mc.player.inventory.currentItem : slot_mat[step];
            }
        } else {
            noMaterials = true;
            return false;
        }


        // Why?
        if (!isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)){
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }

        // For the rotation
        if (rotate.get_value(true)){
            // Look
            BlockInteractionHelper.faceVectorPacketInstant(hitVec, true);
        }
        // If we are placing with the main hand
        EnumHand handSwing = EnumHand.MAIN_HAND;
        // If we are placing with the offhand
        if (slot_mat[step] == 11)
            handSwing = EnumHand.OFF_HAND;

        // Place the block
        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, handSwing);
        mc.player.swingArm(handSwing);

        return true;
    }

    // Given a step, place the block
    public void placeBlockThings(int step) {
        if (step != 1 || placeCrystal.get_value(true)) {
            step--;
            // Get absolute position
            BlockPos targetPos = compactBlockPos(step);
            // Place 93 4 -29
            placeBlock(targetPos, step);
        }
        // Next step
        stage++;
    }

    // Given a step, return the absolute block position
    public BlockPos compactBlockPos(int step) {
        // Get enemy's relative position of the block
        BlockPos offsetPos = new BlockPos(toPlace.to_place.get(toPlace.supportBlock + step));
        // Get absolute position and return
        return new BlockPos(enemyCoordsDouble[0] + offsetPos.getX(), enemyCoordsDouble[1] + offsetPos.getY(), enemyCoordsDouble[2] + offsetPos.getZ());

    }

    // Given a index of a block, get the target position (this is used for support blocks)
    private BlockPos getTargetPos(int idx) {
        BlockPos offsetPos = new BlockPos(toPlace.to_place.get(idx));
        return new BlockPos(enemyCoordsDouble[0] + offsetPos.getX(), enemyCoordsDouble[1] + offsetPos.getY(), enemyCoordsDouble[2] + offsetPos.getZ());
    }

    // Check if we have to disable
    private boolean checkVariable() {
        // If something went wrong
        if (noMaterials || !isHole || !enoughSpace || hasMoved || deadPl || rotationPlayerMoved){
            this.set_disable();
            return true;
        }
        return false;
    }

    // Class for the structure
    private static class structureTemp {
        public double distance;
        public int supportBlock;
        public ArrayList<Vec3d> to_place;
        public int direction;

        public structureTemp(double distance, int supportBlock, ArrayList<Vec3d> to_place) {
            this.distance = distance;
            this.supportBlock = supportBlock;
            this.to_place = to_place;
            this.direction = -1;
        }

    }

    // Create the skeleton of the structure
    private boolean createStructure() {

        if ((Objects.requireNonNull(BlockInteractionHelper.getBlock(enemyCoordsDouble[0], enemyCoordsDouble[1] + 2, enemyCoordsDouble[2]).getRegistryName()).toString().toLowerCase().contains("bedrock"))
                || !(BlockInteractionHelper.getBlock(enemyCoordsDouble[0], enemyCoordsDouble[1] + 3, enemyCoordsDouble[2]) instanceof BlockAir)
                || !(BlockInteractionHelper.getBlock(enemyCoordsDouble[0], enemyCoordsDouble[1] + 4, enemyCoordsDouble[2]) instanceof BlockAir))
            return false;

        // Iterate for every blocks around, find the closest
        double distance_now;
        double min_found = Double.MAX_VALUE;
        int cor = 0;
        int i = 0;
        for(Double[] cord_b : sur_block) {
            if ((distance_now = mc.player.getDistanceSq(new BlockPos(cord_b[0], cord_b[1], cord_b[2]))) < min_found) {
                min_found = distance_now;
                cor = i;
            }
            i++;
        }

        int bias = enemyCoordsInt[0] == (int) mc.player.posX || enemyCoordsInt[2] == (int) mc.player.posZ ? -1 : 1;

        // Create support blocks
        toPlace.to_place.add(new Vec3d(model[cor][0] * bias, 1, model[cor][2] * bias));
        toPlace.to_place.add(new Vec3d(model[cor][0] * bias, 2, model[cor][2] * bias));
        toPlace.supportBlock = 2;

        // Create antitrap + antiStep
        if (trapPlayer.get_value(true) || antiStep.get_value(true)) {
            for(int high = 1; high < 3; high++ ) {
                if (high != 2 || antiStep.get_value(true))
                    for (int[] modelBas : model) {
                        Vec3d toAdd = new Vec3d(modelBas[0], high, modelBas[2]);
                        if (!toPlace.to_place.contains(toAdd)) {
                            toPlace.to_place.add(toAdd);
                            toPlace.supportBlock++;
                        }
                    }
            }
        }


        // Create structure
        toPlace.to_place.add(new Vec3d(0, 2, 0));
        toPlace.to_place.add(new Vec3d(0, 3, 0));
        return true;
    }

    // Get all the materials
    private boolean getMaterialsSlot() {
		/*
			// I use this as a remind to which index refers to what
			0 => obsidian
			1 => Crystal
			2 => Pick
			3 => Sword
		 */

        if (mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal) {
            slot_mat[1] = 11;
        }
        // Iterate for all the inventory
        for(int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            // If there is no block
            if (stack == ItemStack.EMPTY){
                continue;
            }
            // If endCrystal
            if (slot_mat[1] == -1 && stack.getItem() instanceof ItemEndCrystal) {
                slot_mat[1] = i;
                // If sword
            }else if ((antiWeakness.get_value(true) || switchSword.get_value(true)) && stack.getItem() instanceof ItemSword) {
                slot_mat[3] = i;
            }else
                // If Pick
                if (stack.getItem() instanceof ItemPickaxe) {
                    slot_mat[2] = i;
                }
            if (stack.getItem() instanceof ItemBlock){

                // If yes, get the block
                Block block = ((ItemBlock) stack.getItem()).getBlock();

                // Obsidian
                if (block instanceof BlockObsidian) {
                    slot_mat[0] = i;
                }
            }
        }
        // Count what we found
        int count = 0;
        for(int val : slot_mat) {
            if (val != -1)
                count++;
        }

        // If we have everything we need, return true
        return count >= 3 + ((antiWeakness.get_value(true) || switchSword.get_value(true)) ? 1 : 0) ;

    }

    private boolean is_in_hole() {
        sur_block = new Double[][] {
                {aimTarget.posX + 1, aimTarget.posY, aimTarget.posZ},
                {aimTarget.posX - 1, aimTarget.posY, aimTarget.posZ},
                {aimTarget.posX, aimTarget.posY, aimTarget.posZ + 1},
                {aimTarget.posX, aimTarget.posY, aimTarget.posZ - 1}
        };


        // Check if the guy is in a hole
        return !(get_block(sur_block[0][0], sur_block[0][1], sur_block[0][2]) instanceof BlockAir) &&
                !(get_block(sur_block[1][0], sur_block[1][1], sur_block[1][2]) instanceof BlockAir) &&
                !(get_block(sur_block[2][0], sur_block[2][1], sur_block[2][2]) instanceof BlockAir) &&
                !(get_block(sur_block[3][0], sur_block[3][1], sur_block[3][2]) instanceof BlockAir);
    }

    private Block get_block(double x, double y, double z) {
        return mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }


}
