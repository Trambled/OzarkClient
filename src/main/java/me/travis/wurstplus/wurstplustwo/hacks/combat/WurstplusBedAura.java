package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.turok.draw.RenderHelp;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBed;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.stream.Collectors;

public class WurstplusBedAura extends WurstplusHack {

    public WurstplusBedAura() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name        = "Bed Aura";
        this.tag         = "BedAura";
        this.description = "automatically places beds";
    }

    WurstplusSetting place_mode = create("Place Mode", "BedAuraPlaceMode", "New", combobox("New", "Old", "None"));
    WurstplusSetting break_mode = create("Break Mode", "BedAuraBreakMode", "Smart", combobox("Smart", "All", "None"));
    WurstplusSetting place_delay = create("Place Delay", "BedAuraPlaceDelay", 6, 0 , 20);
    WurstplusSetting break_delay = create("Break Delay", "BedAuraBreakDelay", 6, 0 , 20);
    WurstplusSetting anti_suicide = create("Anti Suicide", "BedAuraAntiSuicide", true);
    WurstplusSetting max_self_damage = create("Smart Max Self Damage", "BedAuraMaxSelfDamage", 6, 0, 36);
    WurstplusSetting min_player_place = create("Smart Min Enemy Place", "BedAuraMinEnemyPlace", 8, 0, 36);
    WurstplusSetting min_player_break = create("Smart Min Enemy Break", "BedAuraMinEnemyBreak", 6, 0, 36);
    WurstplusSetting break_range = create("Break Range", "BedAuraBreakRange", 5, 0, 6);
    WurstplusSetting player_range = create("Place Range", "BedAuraPlayerRange", 5, 0, 6);
    WurstplusSetting min_health_pause = create("Min Health Pause", "BedAuraMinHealthPause", true);
    WurstplusSetting required_health = create("Required Health", "BedAuraRequiredHealth", 12f, 1f, 36f);
    WurstplusSetting auto_switch = create("Auto Switch", "BedAuraAutoSwitch", true);
    WurstplusSetting refill = create("Refill", "BedAuraRefill", true);
    WurstplusSetting hard = create("Hard Rotate", "BedAuraRotate", false);
    WurstplusSetting dimension_check = create("Dimension Check", "BedAuraDimensionCheck", true);
    WurstplusSetting swing = create("Swing", "BedAuraSwing", "Mainhand", combobox("Mainhand", "Offhand", "Both", "None"));
    WurstplusSetting render = create("Render", "BedAuraRender", true);
    WurstplusSetting render_mode = create("Render Mode", "BedAuraRenderMode", "Outline", combobox("Pretty", "Solid", "Outline"));
    WurstplusSetting r = create("R", "R", 20, 0, 255);
    WurstplusSetting g = create("G", "G", 20, 0, 255);
    WurstplusSetting b = create("B", "B", 180, 0, 255);
    WurstplusSetting solid_a = create("Solid A", "A", 50, 0, 255);
    WurstplusSetting line_a = create("Line A", "A", 255, 0, 255);
    WurstplusSetting rainbow_mode = create("Rainbow", "Rainbow", true);
    WurstplusSetting debug = create("Debug", "Debug", true);

    private BlockPos render_pos;
    private int place_counter;
    private int break_counter;
    private int rot_var;
    private spoof_face spoof_looking;

    private boolean nowTop = false;
    private boolean outline = false;
    private boolean solid   = false;

    @Override
    protected void enable() {
        render_pos = null;
        place_counter = 0;
        break_counter = 0;
    }

    @Override
    protected void disable() {
        render_pos = null;
    }

    @Override
    public void update_always() {
        render_mode.set_shown(render.get_value(true));
        r.set_shown(render.get_value(true));
        g.set_shown(render.get_value(true));
        b.set_shown(render.get_value(true));
        rainbow_mode.set_shown(render.get_value(true));
        if (render.get_value(true)) {
            if (!render_mode.in("Pretty")) {
                line_a.set_shown(!render_mode.in("Solid"));
                solid_a.set_shown(!render_mode.in("Outline"));
            } else {
                line_a.set_shown(true);
                solid_a.set_shown(true);
            }
        } else {
            line_a.set_shown(false);
            solid_a.set_shown(false);
        }
        min_player_break.set_shown(break_mode.in("Smart"));
        required_health.set_shown(min_health_pause.get_value(true));
    }

    @Override
    public void update() {
        if (mc.player == null) return;

        if (find_bed() == -1) {
            WurstplusMessageUtil.send_client_error_message("Cannot find beds in hotbar!");
            this.set_disable();
            return;
        }

        if (dimension_check.get_value(true) && mc.player.dimension == 0) {
            WurstplusMessageUtil.send_client_error_message("You're in the overworld!");
            this.set_disable();
            return;
        }


        if (rainbow_mode.get_value(true)) {
            cycle_rainbow();
        }

        if (render_mode.in("Pretty")) {
            outline = true;
            solid   = true;
        }

        if (render_mode.in("Solid")) {
            outline = false;
            solid   = true;
        }

        if (render_mode.in("Outline")) {
            outline = true;
            solid   = false;
        }

        if (min_health_pause.get_value(true) && (mc.player.getHealth()+mc.player.getAbsorptionAmount()) < required_health.get_value(1)) return;

        if (place_counter > place_delay.get_value(1)) {
            place_counter = 0;
            if (place_mode.in("New")) {
                place_bed_bon();
            } else if (place_mode.in("Old")) {
                place_bed();
            }
        }
        if (break_counter > break_delay.get_value(1)) {
            break_counter = 0;
            if (!break_mode.in("None")) {
                break_bed();
            }
        }
        if (refill.get_value(true)) {
            refill_bed();
        }

        place_counter++;
        break_counter++;
    }

    public void cycle_rainbow() {
        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

        r.set_value((color_rgb_o >> 16) & 0xFF);
        g.set_value((color_rgb_o >> 8) & 0xFF);
        b.set_value(color_rgb_o & 0xFF);
    }

    public void refill_bed() {
        if (!(mc.currentScreen instanceof GuiContainer)) {
            if (mc.player.inventory.getCurrentItem().getItem() == Items.AIR) {
                for (int i = 9; i < 35; ++i) {
                    if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0, ClickType.SWAP, mc.player);
                        break;
                    }
                }
            }
        }
    }

    public void place_bed() {
        int bed_slot = find_bed();

        BlockPos best_pos = null;
        EntityPlayer best_target = null;
        float best_distance = (float) player_range.get_value(1);

        for (EntityPlayer player : mc.world.playerEntities.stream().filter(entityPlayer -> !WurstplusFriendUtil.isFriend(entityPlayer.getName())).collect(Collectors.toList())) {

            if (player == mc.player) continue;

            if (best_distance < mc.player.getDistance(player)) continue;

            boolean face_place = true;

            BlockPos pos = get_pos_floor(player).down();
            BlockPos pos2 = check_side_block(pos);
            BlockPos temp_pos = null;

            if (pos2 != null) {
                temp_pos = pos2.up();
                best_target = player;
                best_distance = mc.player.getDistance(player);
                face_place = false;
            }

            if (face_place) {
                BlockPos upos = get_pos_floor(player);
                BlockPos upos2 = check_side_block(upos);

                if (upos2 != null) {
                    temp_pos = upos2.up();
                    best_target = player;
                    best_distance = mc.player.getDistance(player);
                }
            }

            if (temp_pos == null) continue;

            final double self_damage = WurstplusCrystalUtil.calculateDamageBed(temp_pos, mc.player);

            if ((self_damage > max_self_damage.get_value(1)) && anti_suicide.get_value(true)) continue;

            final double player_damage = WurstplusCrystalUtil.calculateDamageBed(temp_pos, player);

            if (player_damage < min_player_place.get_value(1)) continue;

            // this only is applicable to this mode bc the 1.13 one can place where ever it wants
            // just prevents on placing it on liquids and fire
            if (mc.world.getBlockState(temp_pos.down()).getBlock().equals(Blocks.FIRE) || mc.world.getBlockState(temp_pos.down()).getBlock().equals(Blocks.LAVA) || mc.world.getBlockState(temp_pos.down()).getBlock().equals(Blocks.FLOWING_LAVA) || mc.world.getBlockState(temp_pos.down()).getBlock().equals(Blocks.WATER) || mc.world.getBlockState(temp_pos.down()).getBlock().equals(Blocks.FLOWING_WATER)){
                continue;
            }

            best_pos = temp_pos;
        }

        if (best_target == null) {
            return;
        }

        render_pos = best_pos;

        if (spoof_looking == spoof_face.NORTH) {
            if (hard.get_value(true)) {
                mc.player.rotationYaw = 180;
            }
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(180, 0, mc.player.onGround));
        } else if (spoof_looking == spoof_face.SOUTH) {
            if (hard.get_value(true)) {
                mc.player.rotationYaw = 0;
            }
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0, 0, mc.player.onGround));
        } else if (spoof_looking == spoof_face.WEST) {
            if (hard.get_value(true)) {
                mc.player.rotationYaw = 90;
            }
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(90, 0, mc.player.onGround));
        } else if (spoof_looking == spoof_face.EAST) {
            if (hard.get_value(true)) {
                mc.player.rotationYaw = -90;
            }
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(-90, 0, mc.player.onGround));
        }


        if (auto_switch.get_value(true)) {
            WurstplusBlockUtil.placeBlock(best_pos, bed_slot, false, false, swing);
            WurstplusMessageUtil.send_client_message("Placing");
        }

        // wait for the user to go to the correct slot
        if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem() instanceof ItemBed && !auto_switch.get_value(true)) {
            WurstplusBlockUtil.placeBlock(best_pos, -1, false, false, swing);
            if (debug.get_value(true)) {
                WurstplusMessageUtil.send_client_message("Placing");
            }
         }
    }

    public void place_bed_bon() {
        BlockPos best_pos = null;
        EntityPlayer best_target = null;
        float best_distance = (float) player_range.get_value(1);

        for (EntityPlayer player : mc.world.playerEntities.stream().filter(entityPlayer -> !WurstplusFriendUtil.isFriend(entityPlayer.getName())).collect(Collectors.toList())) {

            BlockPos placeTarget;

            if (player == mc.player) continue;

            if (best_distance < mc.player.getDistance(player)) continue;

            placeTarget = new BlockPos(player.getPositionVector().add(1, 1, 0));
            nowTop = false;
            rot_var = 90;

            BlockPos block1 = placeTarget;
            if(!canPlaceBed(block1)) {
                placeTarget = new BlockPos(player.getPositionVector().add(-1, 1, 0));
                rot_var = -90;
                nowTop = false;
            }
            BlockPos block2 = placeTarget;
            if(!canPlaceBed(block2)) {
                placeTarget = new BlockPos(player.getPositionVector().add(0, 1, 1));
                rot_var = 180;
                nowTop = false;
            }
            BlockPos block3 = placeTarget;
            if(!canPlaceBed(block3)) {
                placeTarget = new BlockPos(player.getPositionVector().add(0, 1, -1));
                rot_var = 0;
                nowTop = false;
            }
            BlockPos block4 = placeTarget;
            if(!canPlaceBed(block4)) {
                placeTarget = new BlockPos(player.getPositionVector().add(0, 2, -1));
                rot_var = 0;
                nowTop = true;
            }
            BlockPos blockt1 = placeTarget;
            if(nowTop && !canPlaceBed(blockt1)) {
                placeTarget = new BlockPos(player.getPositionVector().add(-1, 2, 0));
                rot_var = -90;
            }
            BlockPos blockt2 = placeTarget;
            if(nowTop && !canPlaceBed(blockt2)) {
                placeTarget = new BlockPos(player.getPositionVector().add(0, 2, 1));
                rot_var = 180;
            }
            BlockPos blockt3 = placeTarget;
            if(nowTop && !canPlaceBed(blockt3)) {
                placeTarget = new BlockPos(player.getPositionVector().add(1, 2, 0));
                rot_var = 90;
            }

            final double self_damage = WurstplusCrystalUtil.calculateDamageBed(placeTarget, mc.player);

            if ((self_damage > max_self_damage.get_value(1)) && anti_suicide.get_value(true)) continue;

            final double player_damage = WurstplusCrystalUtil.calculateDamageBed(placeTarget, player);

            if (player_damage < min_player_place.get_value(1)) continue;

            if (debug.get_value(true)) {
                WurstplusMessageUtil.send_client_message("Place target and best pos found");
            }

            best_pos = placeTarget;
            best_target = player;
            best_distance = mc.player.getDistance(player);
        }

        if (best_target == null) {
            return;
        }

        render_pos = best_pos;

        int old_slot = -1;

        if (auto_switch.get_value(true) && find_bed() != mc.player.inventory.currentItem) {
            old_slot = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = find_bed();
        }

        if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem() instanceof ItemBed) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rot_var, 0, mc.player.onGround));
            if (hard.get_value(true)) {
                mc.player.rotationYaw = rot_var;
            }
            placeBlock(new BlockPos(best_pos), EnumFacing.DOWN);
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            WurstplusBlockUtil.swingArm(swing);
            nowTop = false;
            if (debug.get_value(true)) {
                WurstplusMessageUtil.send_client_message("Placing bed");
            }
        }

        if (auto_switch.get_value(true) && old_slot != -1) {
            mc.player.inventory.currentItem = old_slot;
        }
    }

    private boolean canPlaceBed(BlockPos pos) {
        return (mc.world.getBlockState(pos).getBlock() == Blocks.AIR || mc.world.getBlockState(pos).getBlock() == Blocks.BED) &&
                mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos)).isEmpty();

    }

    private void placeBlock(BlockPos pos, EnumFacing side) {
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
    }


    public void break_bed() {
        for (BlockPos pos : WurstplusBlockInteractHelper.getSphere(get_pos_floor(mc.player), break_range.get_value(1), break_range.get_value(1), false, true, 0).stream().filter(WurstplusBedAura::is_bed).collect(Collectors.toList())) {

            if (mc.player.isSneaking()) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }

            if (break_mode.in("Smart")) {
                EntityPlayer target = null;
                float best_distance = (float) player_range.get_value(1);
                for (EntityPlayer player : mc.world.playerEntities.stream().filter(entityPlayer -> !WurstplusFriendUtil.isFriend(entityPlayer.getName())).collect(Collectors.toList())) {
                    if (player == mc.player) continue;

                    if (best_distance < mc.player.getDistance(player)) continue;

                    target = player;
                }

                if (target == null) continue;

                final double self_damage = WurstplusCrystalUtil.calculateDamageBed(pos, mc.player);

                if ((self_damage > max_self_damage.get_value(1)) && anti_suicide.get_value(true)) continue;

                final double player_damage = WurstplusCrystalUtil.calculateDamageBed(pos, target);

                if (player_damage < min_player_break.get_value(1)) continue;
            }

            if (debug.get_value(true)) {
                WurstplusMessageUtil.send_client_message("Breaking bed");
            }

            WurstplusBlockUtil.openBlock(pos);
        }
    }

    public int find_bed() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                return i;
            }
        }
        return -1;
    }

    public BlockPos check_side_block(BlockPos pos) {
        if (mc.world.getBlockState(pos.east()).getBlock() != Blocks.AIR && mc.world.getBlockState(pos.east().up()).getBlock() == Blocks.AIR) {
            spoof_looking = spoof_face.WEST;
            return pos.east();
        }
        if (mc.world.getBlockState(pos.north()).getBlock() != Blocks.AIR && mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR) {
            spoof_looking = spoof_face.SOUTH;
            return pos.north();
        }
        if (mc.world.getBlockState(pos.west()).getBlock() != Blocks.AIR && mc.world.getBlockState(pos.west().up()).getBlock() == Blocks.AIR) {
            spoof_looking = spoof_face.EAST;
            return pos.west();
        }
        if (mc.world.getBlockState(pos.south()).getBlock() != Blocks.AIR && mc.world.getBlockState(pos.south().up()).getBlock() == Blocks.AIR) {
            spoof_looking = spoof_face.NORTH;
            return pos.south();
        }

        return null;
    }

    public static BlockPos get_pos_floor(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static boolean is_bed(final BlockPos pos) {
        final Block block = mc.world.getBlockState(pos).getBlock();
        return block == Blocks.BED;
    }

    @Override
    public void render(WurstplusEventRender event) {
        if (render_pos == null || !render.get_value(true) || place_mode.in("None")) return;

        if (solid) {
            RenderHelp.prepare("quads");
            RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
                    render_pos.getX(), render_pos.getY(), render_pos.getZ(),
                    1, .2f, 1,
                    r.get_value(1), g.get_value(1), b.get_value(1), solid_a.get_value(1),
                    "all"
            );
            if (place_mode.in("New")) {
                if(rot_var == 90) {
                    RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
                            render_pos.getX() - 1, render_pos.getY(), render_pos.getZ(),
                            1, .2f, 1,
                            r.get_value(1), g.get_value(1), b.get_value(1), solid_a.get_value(1),
                            "all"
                    );
                }
                if(rot_var == 0) {
                    RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
                            render_pos.getX(), render_pos.getY(), render_pos.getZ() + 1,
                            1, .2f, 1,
                            r.get_value(1), g.get_value(1), b.get_value(1), solid_a.get_value(1),
                            "all"
                    );
                }
                if(rot_var == -90) {
                    RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
                            render_pos.getX() + 1, render_pos.getY(), render_pos.getZ(),
                            1, .2f, 1,
                            r.get_value(1), g.get_value(1), b.get_value(1), solid_a.get_value(1),
                            "all"
                    );
                }
                if(rot_var == 180) {
                    RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
                            render_pos.getX(), render_pos.getY(), render_pos.getZ() - 1,
                            1, .2f, 1,
                            r.get_value(1), g.get_value(1), b.get_value(1), solid_a.get_value(1),
                            "all"
                    );
                }
            }
            RenderHelp.release();
        }
        if (outline) {
            RenderHelp.prepare("lines");
            RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
                    render_pos.getX(), render_pos.getY(), render_pos.getZ(),
                    1, .2f, 1,
                    r.get_value(1), g.get_value(1), b.get_value(1), line_a.get_value(1),
                    "all"
            );
            if (place_mode.in("New")) {
                if(rot_var == 90) {
                    RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
                            render_pos.getX() - 1, render_pos.getY(), render_pos.getZ(),
                            1, .2f, 1,
                            r.get_value(1), g.get_value(1), b.get_value(1), line_a.get_value(1),
                            "all"
                    );
                }
                if(rot_var == 0) {
                    RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
                            render_pos.getX(), render_pos.getY(), render_pos.getZ() + 1,
                            1, .2f, 1,
                            r.get_value(1), g.get_value(1), b.get_value(1), line_a.get_value(1),
                            "all"
                    );
                }
                if(rot_var == -90) {
                    RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
                            render_pos.getX() + 1, render_pos.getY(), render_pos.getZ(),
                            1, .2f, 1,
                            r.get_value(1), g.get_value(1), b.get_value(1), line_a.get_value(1),
                            "all"
                    );
                }
                if(rot_var == 180) {
                    RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
                            render_pos.getX(), render_pos.getY(), render_pos.getZ() - 1,
                            1, .2f, 1,
                            r.get_value(1), g.get_value(1), b.get_value(1), line_a.get_value(1),
                            "all"
                    );
                }
            }
            RenderHelp.release();
        }
    }

    enum spoof_face {
        EAST,
        WEST,
        NORTH,
        SOUTH
    }
}
