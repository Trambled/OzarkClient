package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import me.trambled.ozark.ozarkclient.util.world.BlockInteractionHelper;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

//gamesense
public class AutoAnvil extends Module
{
    
    public AutoAnvil() {
        super(Category.COMBAT);

        this.name        = "AutoAnvil";
        this.tag         = "AutoAnvil";
        this.description = "Automatically anvils an opponent.";
    }
	
	Setting break_mode = create("Break Mode", "BreakMode", "Feet", combobox("Pick", "Feet", "None"));
    Setting target = create("Target", "AutoAnvilTarget", "Nearest", combobox("Nearest", "Looking"));
    Setting tick_delay = create("Tick Delay", "AutoAnvilTickDelay", 5, 1, 10);
    Setting blocks_per_tick = create("Blocks Per Tick", "AutoAnvilBPS", 5, 1, 10);
    Setting range = create("Range", "AutoAnvilRange", 4, 0, 6);
    Setting decrease = create("Decrease", "AutoAnvilDecrease", 4, 0, 8);
    Setting h_distance = create("H Distance", "AutoAnvilHDistance", 7, 1, 10);
    Setting min_h = create("Min H", "AutoAnvilMinH", 3, 1, 10);
    Setting fail_stop = create("Fail Stop", "AutoAnvilFailStop", 2, 1, 10);
    Setting anti_crystal = create("AntiCrystal", "AutoAnvilAntiCrystal", false);
	Setting rotate = create("Rotate", "AutoAnvilRotate", true);
    Setting fast_anvil = create("FastAnvil", "AutoAnvilFastAnvil", true);
    Setting chatMsg = create("Chat Messages", "AutoAnvilMessages", true);

    private boolean isSneaking = false;
    private boolean firstRun = false;
    private boolean noMaterials = false;
    private boolean hasMoved = false;
    private boolean isHole = true;
    private boolean enoughSpace = true;
    private boolean blockUp = false;
    private int oldSlot = -1;
    private int[] slot_mat = {-1, -1, -1, -1};
    private double[] enemyCoords;
    Double[][] sur_block;
    private int noKick;
    int[][] model = new int[][] {
            {1,1,0},
            {-1,1,0},
            {0,1,1},
            {0,1,-1}
    };

    private int blocksPlaced = 0;
    private int delayTimeTicks = 0;
    private int offsetSteps = 0;
    private boolean pick_d = false;

    private EntityPlayer aimTarget;

    @Override
    protected void enable() {
        // Setup
        if (break_mode.in("Pick")) {
            pick_d = true;
        }
        blocksPlaced = 0;
        isHole = true;
        hasMoved = blockUp = false;
        firstRun = true;
        slot_mat = new int[]{-1, -1, -1, -1};
        to_place = new ArrayList<>();

        if (mc.player == null) {
            set_disable();
            return;
        }

        oldSlot = mc.player.inventory.currentItem;
    }

    @Override
    protected void disable() {
        if (mc.player == null) {
            return;
        }

        if (chatMsg.get_value(true)) {
            if (noMaterials) {
                MessageUtil.send_client_error_message("No Materials Detected!");
            }
            else if (!isHole) {
                MessageUtil.send_client_error_message("Enemy is not in a hole!");
            }
            else if(!enoughSpace) {
                MessageUtil.send_client_error_message("Not enough space!");
            }
            else if(hasMoved) {
                MessageUtil.send_client_error_message("Enemy moved away from the hole!");
            }
            else if(blockUp) {
                MessageUtil.send_client_error_message("Enemy head blocked!");
            }
        }

        if (isSneaking) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;
        }

        if (oldSlot != mc.player.inventory.currentItem && oldSlot != -1) {
            mc.player.inventory.currentItem = oldSlot;
            oldSlot = -1;
        }

        noMaterials = false;
        firstRun = true;
    }

    @Override
    public void update() {
        if (mc.player == null) {
            set_disable();
            return;
        }

        if (firstRun) {

            // All the setup
            if (target.in("Nearest"))
                aimTarget = findClosestTarget(range.get_value(1), aimTarget);
            else if(target.in("Looking"))
                aimTarget = findLookingPlayer(range.get_value(1));

            if (aimTarget == null) {
                return;
            }
            firstRun = false;
            if (getMaterialsSlot()) {
                // check if the enemy is in a hole
                if (is_in_hole()) {
                    // Get enemy coordinates
                    enemyCoords = new double[] {aimTarget.posX, aimTarget.posY, aimTarget.posZ};
                    // Start choosing where to place what
                    enoughSpace = createStructure();

                }
                else {
                    isHole = false;
                }
            }
            else noMaterials = true;


        }
        else {
            // Wait
            if (delayTimeTicks < tick_delay.get_value(1)) {
                delayTimeTicks++;
                return;
            }
            else {
                delayTimeTicks = 0;

                // Check if he has moved away
                if ((int) enemyCoords[0] != (int) aimTarget.posX || (int) enemyCoords[2] != (int) aimTarget.posZ)
                    hasMoved = true;

                // Check a block on the enemy's head
                if (!(get_block(enemyCoords[0], enemyCoords[1] + 2, enemyCoords[2]) instanceof BlockAir)
                        || !(get_block(enemyCoords[0], enemyCoords[1] + 3, enemyCoords[2]) instanceof BlockAir)) {
                    blockUp = true;
                }


            }
        }

        blocksPlaced = 0;

        // If we have to left
        if (noMaterials || !isHole || !enoughSpace || hasMoved || blockUp) {
            set_disable();
            return;
        }

        noKick = 0;
        while (blocksPlaced <= blocks_per_tick.get_value(1)) {

            // Max of blocks we have to place
            int maxSteps;
            maxSteps = AutoAnvil.to_place.size();

            // If we are at the end
            if (offsetSteps >= maxSteps) {
                offsetSteps = 0;
                break;
            }

            // Get position
            BlockPos offsetPos = new BlockPos(to_place.get(offsetSteps));
            BlockPos targetPos = new BlockPos(aimTarget.getPositionVector()).add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());

            boolean tryPlacing = true;

            // If there is an entity
            if(offsetSteps > 0 && offsetSteps < AutoAnvil.to_place.size() - 1)
                for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(targetPos))) {
                    if (entity instanceof EntityPlayer) {
                        tryPlacing = false;
                        break;
                    }
                }

            if (tryPlacing && placeBlock(targetPos, offsetSteps)) {
                blocksPlaced++;
            }

            offsetSteps++;
            // Why?
            if (isSneaking) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                isSneaking = false;
            }
            if (noKick == fail_stop.get_value(1)) {
                break;
            }
        }


    }
	
	public static boolean basicChecksEntity(Entity pl) {
        return pl == mc.player || FriendUtil.isFriend(pl.getName()) || pl.isDead;
    }
	
    // Find player you are looking
    public static EntityPlayer findLookingPlayer(double rangeMax) {
        // Get player
        ArrayList<EntityPlayer> listPlayer = new ArrayList<>();
        // Only who is in a distance of enemyRange
        for(EntityPlayer playerSin : mc.world.playerEntities) {
            if (basicChecksEntity(playerSin))
                continue;
            if (mc.player.getDistance(playerSin) <= rangeMax) {
                listPlayer.add(playerSin);
            }
        }

        EntityPlayer target = null;
        // Get coordinate eyes + rotation
        Vec3d positionEyes = mc.player.getPositionEyes(mc.getRenderPartialTicks());
        Vec3d rotationEyes = mc.player.getLook(mc.getRenderPartialTicks());
        // Precision
        int precision = 2;
        // Iterate for every blocks
        for(int i = 0; i < (int) rangeMax; i++) {
            // Iterate for the precision
            for(int j = precision; j > 0 ; j--) {
                // Iterate for all players
                for(EntityPlayer targetTemp : listPlayer) {
                    // Get box of the player
                    AxisAlignedBB playerBox = targetTemp.getEntityBoundingBox();
                    // Get coordinate of the vec3d
                    double xArray = positionEyes.x + (rotationEyes.x * i) + rotationEyes.x/j;
                    double yArray = positionEyes.y + (rotationEyes.y * i) + rotationEyes.y/j;
                    double zArray = positionEyes.z + (rotationEyes.z * i) + rotationEyes.z/j;
                    // If it's inside
                    if (   playerBox.maxY >= yArray && playerBox.minY <= yArray
                            && playerBox.maxX >= xArray && playerBox.minX <= xArray
                            && playerBox.maxZ >= zArray && playerBox.minZ <= zArray) {
                        // Get target
                        target = targetTemp;
                    }
                }
            }
        }

        return target;
    }

    // Find closest target
    public static EntityPlayer findClosestTarget(double rangeMax, EntityPlayer aimTarget){
        List<EntityPlayer> playerList = mc.world.playerEntities;

        EntityPlayer closestTarget_test = null;

        for (EntityPlayer entityPlayer : playerList){

            if (basicChecksEntity(entityPlayer))
                continue;

            if (aimTarget == null && mc.player.getDistance(entityPlayer) <= rangeMax){
                closestTarget_test = entityPlayer;
                continue;
            }
            if (aimTarget != null && mc.player.getDistance(entityPlayer) <= rangeMax && mc.player.getDistance(entityPlayer) < mc.player.getDistance(aimTarget)){
                closestTarget_test = entityPlayer;
            }
        }
        return closestTarget_test;
    }
	
    private boolean placeBlock(BlockPos pos, int step) {
        // Get the block
        Block block = mc.world.getBlockState(pos).getBlock();
        // Get all sides
        EnumFacing side = BlockInteractionHelper.getPlaceableSide(pos);
        // If it is a ghostblock
        if (step == to_place.size() - 1 && block instanceof BlockAnvil && side != null) {
            // UnGlitch it with a left click
            //mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, side));
            noKick++;
        }
        // If there is a solid block
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        // If we cannot find any side
        if (side == null) {
            return false;
        }

        // Get position of the side
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();

        // If that block can be clicked
        if (!BlockInteractionHelper.canBeClicked(neighbour)) {
            return false;
        }

        // Get the position where we are gonna click
        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();

        /*
			// I use this as a remind to which index refers to what
			0 => obsidian
			1 => anvil
			2 => pressure plate / button
			3 => pick
		 */
        // Get what slot we are going to select
        int utilSlot =
                (step == 0 && (break_mode.in("Feet")))
                        ? 2 :
                        (step == to_place.size() - 1) ? 1 : 0;
        // If it's not empty
        if (mc.player.inventory.getStackInSlot(slot_mat[utilSlot]) != ItemStack.EMPTY) {
            // Is it is correct
            if (mc.player.inventory.currentItem != slot_mat[utilSlot]) {
                // Change the hand's item
                mc.player.inventory.currentItem = slot_mat[utilSlot];
            }
        }
        else return false;

        // Why?
        if (!isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }

        // For the rotation
        if (rotate.get_value(true)) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }

        // FastAnvil
        int bef = mc.rightClickDelayTimer;
        // If we are at our last
        if (step == to_place.size() - 1) {

            // Get the name of the player
            EntityPlayer found = getPlayerFromName(aimTarget.getGameProfile().getName());
            // If that player moved
            if (found == null || (int) found.posX != (int) enemyCoords[0] || (int) found.posZ != (int) enemyCoords[2]) {
                hasMoved = true;
                return false;
            }
            // If fastAnvil
            if (fast_anvil.get_value(true))
                // FastPlace
                mc.rightClickDelayTimer = 0;
        }

        // Place the block
        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);

        // Disable fastplace
        if (fast_anvil.get_value(true) && step == to_place.size() - 1) {
            mc.rightClickDelayTimer = bef;
		}


        // Breaking the anvil
        if (pick_d && step == to_place.size() - 1) {
            EnumFacing prova = BlockInteractionHelper.getPlaceableSide(new BlockPos(enemyCoords[0], enemyCoords[1], enemyCoords[2]));
            if (prova != null) {
                mc.player.inventory.currentItem = slot_mat[3];
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(
                        CPacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(enemyCoords[0], enemyCoords[1], enemyCoords[2]), prova
                ));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(
                        CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(enemyCoords[0], enemyCoords[1], enemyCoords[2]), prova
                ));
            }
        }

        return true;
    }

    private EntityPlayer getPlayerFromName(String name) {
        // Iterate for every player
        List<EntityPlayer> playerList = mc.world.playerEntities;
        for (EntityPlayer entityPlayer : playerList) {
            // same
            if (entityPlayer.getGameProfile().getName().equals(name)) {
                return entityPlayer;
            }

        }
        return null;
    }

    private static ArrayList<Vec3d> to_place = new ArrayList <> ( );

    private boolean getMaterialsSlot() {
		/*
			// I use this as a remind to which index refers to what
			0 => obsidian
			1 => anvil
			2 => pressure plate
			3 => pick
		 */
        boolean feet = false;
        boolean pick = false;

        // If we have to search also for a button/pressure plate
        if (break_mode.in("Feet")) {
            feet = true;
        }
        if (break_mode.in("Pick")) {
            pick = true;
        }

        // Iterate for all the inventory
        for(int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            // If there is no block
            if (stack == ItemStack.EMPTY) {
                continue;
            }
            if (pick && stack.getItem() instanceof ItemPickaxe) {
                slot_mat[3] = i;
            }
            if (stack.getItem() instanceof ItemBlock) {

                // If yes, get the block
                Block block = ((ItemBlock) stack.getItem()).getBlock();

                // Obsidian
                if (block instanceof BlockObsidian) {
                    slot_mat[0] = i;
                }
                else
                    // Anvil
                    if (block instanceof BlockAnvil) {
                        slot_mat[1] = i;
                    }
                    else
                        // Button / Pressure Plate
                        if (feet && (block instanceof BlockPressurePlate || block instanceof BlockButton)) {
                            slot_mat[2] = i;
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
        return count - (feet || pick ? 1 : 0) == 2;

    }

    private boolean is_in_hole() {
        sur_block = new Double[][] {
                {aimTarget.posX + 1, aimTarget.posY, aimTarget.posZ},
                {aimTarget.posX - 1, aimTarget.posY, aimTarget.posZ},
                {aimTarget.posX, aimTarget.posY, aimTarget.posZ + 1},
                {aimTarget.posX, aimTarget.posY, aimTarget.posZ - 1}
        };

        enemyCoords = new double[] {
                aimTarget.posX,
                aimTarget.posY,
                aimTarget.posZ
        };
        // Check if the guy is in a hole
        return !(get_block(sur_block[0][0], sur_block[0][1], sur_block[0][2]) instanceof BlockAir) &&
                !(get_block(sur_block[1][0], sur_block[1][1], sur_block[1][2]) instanceof BlockAir) &&
                !(get_block(sur_block[2][0], sur_block[2][1], sur_block[2][2]) instanceof BlockAir) &&
                !(get_block(sur_block[3][0], sur_block[3][1], sur_block[3][2]) instanceof BlockAir);
    }

    private boolean createStructure() {
        // Add the button
        if (break_mode.in("Feet")){
            to_place.add(new Vec3d(0,0,0));
        }
        /// Add all around the enemy for preventing for him to step outside
        to_place.add(new Vec3d(1, 1, 0));
        to_place.add(new Vec3d(-1, 1,0));
        to_place.add(new Vec3d(0, 1,1));
        to_place.add(new Vec3d(0, 1,-1));
        to_place.add(new Vec3d(1, 2, 0));
        to_place.add(new Vec3d(-1, 2,0));
        to_place.add(new Vec3d(0, 2,1));
        to_place.add(new Vec3d(0, 2,-1));

        /// Decrease hDistance
        // Get the distance from the enemy
        int hDistanceMod = h_distance.get_value(1);
        double distEnemy = mc.player.getDistance(aimTarget);
        while (distEnemy > decrease.get_value(1)) {
            hDistanceMod -= 1;
            distEnemy -= decrease.get_value(1);
        }
        int add = (int) (mc.player.posY - aimTarget.posY);
        if (add > 1)
            add = 2;
        // Different Y
        hDistanceMod += mc.player.posY - aimTarget.posY;

        /// Get in what block the client is going to tower
        // Calculate for each blocks the distance and find the min
        double 	min_found = Double.MAX_VALUE,
                distance_now;
        double[] coords_blocks_min  = new double[] {-1, -1, -1},
                coords_blocks_temp;
        int cor = -1;
        int i = 0;
        // Iterate for every blocks around, find the closest
        for(Double[] cord_b : sur_block) {
            coords_blocks_temp = new double[] {cord_b[0], cord_b[1], cord_b[2]};
            if ((distance_now = mc.player.getDistanceSq(new BlockPos(cord_b[0], cord_b[1], cord_b[2]))) < min_found) {
                min_found = distance_now;
                cor = i;
            }
            i++;
        }
        // We need this for see if we are going to find at list 1 spot for placing
        boolean possible = false;
        int incr = 1;
        // Continue by creating the tower
        do {
            // Search the avaible space
            if ( get_block(enemyCoords[0], enemyCoords[1] + incr, enemyCoords[2]) instanceof BlockAir && incr < hDistanceMod) {
                // Lets check for the block near
                if (!anti_crystal.get_value(true))
                    to_place.add(new Vec3d(model[cor][0], model[cor][1] + incr, model[cor][2]));
                else {
                    for (int ij = 0; ij < 4; ij++) {
                        to_place.add(new Vec3d(model[ij][0], model[ij][1] + incr, model[ij][2]));
                    }
                }
                incr++;
            }else {
                // If there is a block where we want to place the anvil
                if (!(get_block(enemyCoords[0], enemyCoords[1] + incr, enemyCoords[2]) instanceof BlockAir)) {
                    // Go down of 1
                    incr--;
                }
                break;
            }

        }while(true);
        if (incr >=  min_h.get_value(1))
            possible = true;

        // Add the anvil
        to_place.add(new Vec3d(0, model[cor][1] + incr - 1, 0));
        return possible;
    }

    private Block get_block(double x, double y, double z) {
        return mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }
}
