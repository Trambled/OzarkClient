package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import java.util.ArrayList;
import java.util.List;

// @author Perry

public class Trajectories extends Module {

    public Trajectories() {
        super(Category.RENDER);

        this.name        = "Trajectories";
        this.tag         = "Trajectories";
        this.description = "Shows where you are aiming.";
    }
    Setting innerSize = create("Inner Size", "TrajectoriesInnersize", 1, - 5, 5);
    Setting slices = create("Slices", "TrajectoriesSlices", 3, 2, 50);
    Setting size = create("Size", "TrajectoriesSize", 1, - 5, 5);

    @Override
    public void render(EventRender event) {
        if ( Trajectories.mc.world != null && Trajectories.mc.player != null ) {
            mc.getRenderManager ( );
            mc.getRenderManager ( );
            double var2 = mc.player.lastTickPosX + ( mc.player.posX - mc.player.lastTickPosX ) * (double) event.get_partial_ticks ( );
            double var4 = mc.player.lastTickPosY + ( mc.player.posY - mc.player.lastTickPosY ) * (double) event.get_partial_ticks ( );
            double var6 = mc.player.lastTickPosZ + ( mc.player.posZ - mc.player.lastTickPosZ ) * (double) event.get_partial_ticks ( );
            mc.player.getHeldItem ( EnumHand.MAIN_HAND );
            if ( mc.gameSettings.thirdPersonView == 0 && ( mc.player.getHeldItem ( EnumHand.MAIN_HAND ).getItem ( ) instanceof ItemBow || mc.player.getHeldItem ( EnumHand.MAIN_HAND ).getItem ( ) instanceof ItemFishingRod || mc.player.getHeldItem ( EnumHand.MAIN_HAND ).getItem ( ) instanceof ItemEnderPearl || mc.player.getHeldItem ( EnumHand.MAIN_HAND ).getItem ( ) instanceof ItemEgg || mc.player.getHeldItem ( EnumHand.MAIN_HAND ).getItem ( ) instanceof ItemSnowball || mc.player.getHeldItem ( EnumHand.MAIN_HAND ).getItem ( ) instanceof ItemExpBottle ) ) {
                GL11.glPushMatrix ( );
                Item var8 = mc.player.getHeldItem ( EnumHand.MAIN_HAND ).getItem ( );
                double var9 = var2 - (double) ( MathHelper.cos ( mc.player.rotationYaw / 180.0F * 3.1415927F ) * 0.16F );
                double var11 = var4 + (double) mc.player.getEyeHeight ( ) - 0.1000000014901161D;
                double var13 = var6 - (double) ( MathHelper.sin ( mc.player.rotationYaw / 180.0F * 3.1415927F ) * 0.16F );
                double var15 = (double) ( - MathHelper.sin ( mc.player.rotationYaw / 180.0F * 3.1415927F ) * MathHelper.cos ( mc.player.rotationPitch / 180.0F * 3.1415927F ) ) * ( var8 instanceof ItemBow ? 1.0D : 0.4D );
                double var17 = (double) ( - MathHelper.sin ( mc.player.rotationPitch / 180.0F * 3.1415927F ) ) * ( var8 instanceof ItemBow ? 1.0D : 0.4D );
                double var19 = (double) ( MathHelper.cos ( mc.player.rotationYaw / 180.0F * 3.1415927F ) * MathHelper.cos ( mc.player.rotationPitch / 180.0F * 3.1415927F ) ) * ( var8 instanceof ItemBow ? 1.0D : 0.4D );
                int var21 = 72000 - mc.player.getItemInUseCount ( );
                float var22 = (float) var21 / 20.0F;
                var22 = ( var22 * var22 + var22 * 2.0F ) / 3.0F;
                if ( var22 > 1.0F ) {
                    var22 = 1.0F;
                }

                float var23 = MathHelper.sqrt ( var15 * var15 + var17 * var17 + var19 * var19 );
                var15 /= var23;
                var17 /= var23;
                var19 /= var23;
                float var24 = var8 instanceof ItemBow ? var22 * 2.0F : ( var8 instanceof ItemFishingRod ? 1.25F : ( mc.player.getHeldItem ( EnumHand.MAIN_HAND ).getItem ( ) == Items.EXPERIENCE_BOTTLE ? 0.9F : 1.0F ) );
                var15 *= var24 * ( var8 instanceof ItemFishingRod ? 0.75F : ( mc.player.getHeldItem ( EnumHand.MAIN_HAND ).getItem ( ) == Items.EXPERIENCE_BOTTLE ? 0.75F : 1.5F ) );
                var17 *= var24 * ( var8 instanceof ItemFishingRod ? 0.75F : ( mc.player.getHeldItem ( EnumHand.MAIN_HAND ).getItem ( ) == Items.EXPERIENCE_BOTTLE ? 0.75F : 1.5F ) );
                var19 *= var24 * ( var8 instanceof ItemFishingRod ? 0.75F : ( mc.player.getHeldItem ( EnumHand.MAIN_HAND ).getItem ( ) == Items.EXPERIENCE_BOTTLE ? 0.75F : 1.5F ) );
                this.enableGL3D ( 2.0F );
                GlStateManager.color ( 0.0F , 1.0F , 0.0F , 1.0F );
                GL11.glEnable ( 2848 );
                float var25 = (float) ( var8 instanceof ItemBow ? 0.3D : 0.25D );
                boolean var26 = false;
                Entity var27 = null;
                RayTraceResult var28 = null;

                while ( ! var26 && var11 > 0.0D ) {
                    Vec3d var29 = new Vec3d ( var9 , var11 , var13 );
                    Vec3d var30 = new Vec3d ( var9 + var15 , var11 + var17 , var13 + var19 );
                    RayTraceResult var31 = mc.world.rayTraceBlocks ( var29 , var30 , false , true , false );
                    if ( var31 != null && var31.typeOfHit != Type.MISS ) {
                        var28 = var31;
                        var26 = true;
                    }

                    AxisAlignedBB var32 = new AxisAlignedBB ( var9 - (double) var25 , var11 - (double) var25 , var13 - (double) var25 , var9 + (double) var25 , var11 + (double) var25 , var13 + (double) var25 );
                    List < Entity > var33 = this.getEntitiesWithinAABB ( var32.offset ( var15 , var17 , var19 ).expand ( 1.0D , 1.0D , 1.0D ) );

                    for (Entity var35 : var33) {
                        if ( var35.canBeCollidedWith ( ) && var35 != mc.player ) {
                            AxisAlignedBB var38 = var35.getEntityBoundingBox ( ).expand ( 0.30000001192092896D , 0.30000001192092896D , 0.30000001192092896D );
                            RayTraceResult var39 = var38.calculateIntercept ( var29 , var30 );
                            if ( var39 != null ) {
                                var26 = true;
                                var27 = var35;
                                var28 = var39;
                            }
                        }
                    }

                    if ( var27 != null ) {
                        GlStateManager.color ( 1.0F , 0.0F , 0.0F , 255.0F );
                    }

                    var9 += var15;
                    var11 += var17;
                    var13 += var19;
                    var15 *= 0.9900000095367432D;
                    var17 *= 0.9900000095367432D;
                    var19 *= 0.9900000095367432D;
                    var17 -= var8 instanceof ItemBow ? 0.05D : 0.03D;
                    this.drawLine3D ( var9 - var2 , var11 - var4 , var13 - var6 );
                }

                if ( var28 != null && var28.typeOfHit == Type.BLOCK ) {
                    GlStateManager.translate ( var9 - var2 , var11 - var4 , var13 - var6 );
                    int var40 = var28.sideHit.getIndex ( );
                    if ( var40 == 2 ) {
                        GlStateManager.rotate ( 90.0F , 1.0F , 0.0F , 0.0F );
                    } else if ( var40 == 3 ) {
                        GlStateManager.rotate ( 90.0F , 1.0F , 0.0F , 0.0F );
                    } else if ( var40 == 4 ) {
                        GlStateManager.rotate ( 90.0F , 0.0F , 0.0F , 1.0F );
                    } else if ( var40 == 5 ) {
                        GlStateManager.rotate ( 90.0F , 0.0F , 0.0F , 1.0F );
                    }

                    Cylinder var41 = new Cylinder ( );
                    GlStateManager.rotate ( - 90.0F , 1.0F , 0.0F , 0.0F );
                    var41.setDrawStyle ( 100011 );
                    if ( var27 != null ) {
                        GlStateManager.color ( 1.0F , 0.0F , 0.0F , 1.0F );
                        GL11.glLineWidth ( 2.5F );
                        var41.draw ( 0.5F , 0.5F , 0.0F , this.slices.get_value ( 1 ) , 1 );
                        GL11.glLineWidth ( 0.1F );
                        GlStateManager.color ( 1.0F , 0.0F , 0.0F , 1.0F );
                    }

                    var41.draw ( this.size.get_value ( 1 ) , this.innerSize.get_value ( 1 ) , 0.0F , this.slices.get_value ( 1 ) , 1 );
                }

                this.disableGL3D ( );
                GL11.glPopMatrix ( );
            }
        }

    }

    public
    void drawLine3D ( double var1 , double var3 , double var5 ) {
        GL11.glVertex3d ( var1 , var3 , var5 );
    }

    public
    void enableGL3D ( float var1 ) {
        GL11.glDisable ( 3008 );
        GL11.glEnable ( 3042 );
        GL11.glBlendFunc ( 770 , 771 );
        GL11.glDisable ( 3553 );
        GL11.glDisable ( 2929 );
        GL11.glDepthMask ( false );
        GL11.glEnable ( 2884 );
        mc.entityRenderer.disableLightmap ( );
        GL11.glEnable ( 2848 );
        GL11.glHint ( 3154 , 4354 );
        GL11.glHint ( 3155 , 4354 );
        GL11.glLineWidth ( var1 );
    }

    public
    void disableGL3D ( ) {
        GL11.glEnable ( 3553 );
        GL11.glEnable ( 2929 );
        GL11.glDisable ( 3042 );
        GL11.glEnable ( 3008 );
        GL11.glDepthMask ( true );
        GL11.glCullFace ( 1029 );
        GL11.glDisable ( 2848 );
        GL11.glHint ( 3154 , 4352 );
        GL11.glHint ( 3155 , 4352 );
    }

    private
    List < Entity > getEntitiesWithinAABB ( AxisAlignedBB var1 ) {
        ArrayList < Entity > var2 = new ArrayList <> ( );
        int var3 = MathHelper.floor ( ( var1.minX - 2.0D ) / 16.0D );
        int var4 = MathHelper.floor ( ( var1.maxX + 2.0D ) / 16.0D );
        int var5 = MathHelper.floor ( ( var1.minZ - 2.0D ) / 16.0D );
        int var6 = MathHelper.floor ( ( var1.maxZ + 2.0D ) / 16.0D );

        for (int var7 = var3; var7 <= var4; ++ var7) {
            for (int var8 = var5; var8 <= var6; ++ var8) {
                if ( mc.world.getChunkProvider ( ).getLoadedChunk ( var7 , var8 ) != null ) {
                    mc.world.getChunk ( var7 , var8 ).getEntitiesWithinAABBForEntity ( mc.player , var1 , var2 , null );
                }
            }
        }

        return var2;
    }
    @Override
    public String array_detail() {
            return Integer.toString(slices.get_value(1));

        }
    }
