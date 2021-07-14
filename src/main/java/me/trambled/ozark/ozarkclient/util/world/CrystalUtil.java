package me.trambled.ozark.ozarkclient.util.world;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;

public class CrystalUtil {

    public static List<BlockPos> possiblePlacePositions(final float placeRange, final boolean thirteen, final boolean specialEntityCheck) {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(getSphere(getPlayerPos(mc.player), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> canPlaceCrystal(pos, thirteen, specialEntityCheck)).collect(Collectors.toList()));
        return positions;
    }

    public static BlockPos getPlayerPos(final EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static List<BlockPos> getSphere(final BlockPos pos, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleblocks = new ArrayList<>();
        final int cx = pos.getX();
        final int cy = pos.getY();
        final int cz = pos.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    public static List<BlockPos> crystalBlocksMomentum(EntityPlayer entityPlayer, double placeRange, boolean prediction, double predict_factor, boolean antiSurround, boolean endcrystal) {
        return getNearbyBlocks(entityPlayer, placeRange, prediction, predict_factor).stream().filter(blockPos -> canPlaceCrystal(blockPos, endcrystal, antiSurround)).collect(Collectors.toList());
    }

    public static List<BlockPos> getNearbyBlocks(EntityPlayer player, double blockRange, boolean motion, double motion_factor) {
        List<BlockPos> nearbyBlocks = new ArrayList<>();
        int range = (int) MathUtil.roundDouble(blockRange, 0);

        if (motion)
            player.getPosition().add(new Vec3i(player.motionX * motion_factor, player.motionY * motion_factor, player.motionZ * motion_factor));

        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range - (range / 2); y++)
                for (int z = -range; z <= range; z++)
                    nearbyBlocks.add(player.getPosition().add(x, y, z));

        return nearbyBlocks;
    }


    public static boolean canPlaceCrystal(final BlockPos blockPos, final boolean thirteen, final boolean specialEntityCheck) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        final BlockPos final_boost = blockPos.add(0, 3, 0);
        try {
            if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
            if ((mc.world.getBlockState(boost).getBlock() != Blocks.AIR || (mc.world.getBlockState(boost2).getBlock() != Blocks.AIR && !thirteen))) {
                return false;
            }
            if (!specialEntityCheck) {
                return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
            }
            for (final Object entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                if (!(entity instanceof EntityEnderCrystal)) {
                    return false;
                }
            }
            for (final Object entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2))) {
                if (!(entity instanceof EntityEnderCrystal)) {
                    return false;
                }
            }
            for (final Object entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(final_boost))) {
                if (entity instanceof EntityEnderCrystal) {
                    return false;
                }
            }
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }

    public static boolean canPlaceCrystal(final BlockPos pos)
    {
        final Block block = mc.world.getBlockState(pos).getBlock();

        if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK)
        {
            final Block floor = mc.world.getBlockState(pos.add(0, 1, 0)).getBlock();
            final Block ceil = mc.world.getBlockState(pos.add(0, 2, 0)).getBlock();

            if (floor == Blocks.AIR && ceil == Blocks.AIR)
            {
                return mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.add(0, 1, 0))).isEmpty() && mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.add(0, 2, 0))).isEmpty();
            }
        }

        return false;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        if (mc.player.isCreative() && entity == mc.player) {
            return 0.0f;
        }
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        }
        catch (Exception ignore) {}
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }

    public static float calculateDamageBed(double posX, double posY, double posZ, Entity entity) {
        if (entity == mc.player && mc.player.isCreative()) {
            return 0.0f;
        }
        final float doubleExplosionSize = 10.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        }
        catch (Exception ignore) {}
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }


    public static float getBlastReduction(final EntityLivingBase entity, final float damageI, final Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int k = 0;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            }
            catch (Exception ignored) {}
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    public static float getDamageMultiplied(final float damage) {
        final int diff = mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }


    public static float calculateDamage(EntityEnderCrystal crystal, Entity entity)
    {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }

    public static float calculateDamageBed(BlockPos pos, Entity entity)
    {
        return calculateDamageBed(pos.getX(), pos.getY(), pos.getZ(), entity);
    }

    public static boolean raytraceBlock(BlockPos blockPos, double offset) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(blockPos.getX(), blockPos.getY() + offset, blockPos.getZ()), false, true, false) == null;
    }

    public static boolean raytraceQuill(BlockPos blockPos, double offset) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(blockPos.getX(), blockPos.getY() + offset + 1.5, blockPos.getZ()), false, true, false) == null;
    }



}