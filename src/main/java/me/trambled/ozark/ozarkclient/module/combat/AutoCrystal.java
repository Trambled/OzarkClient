package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.events.EventEntityRemoved;
import me.trambled.ozark.ozarkclient.event.events.EventPacket;
import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.event.events.EventRotation;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.chat.AutoEz;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.player.EntityUtil;
import me.trambled.ozark.ozarkclient.util.player.PlayerUtil;
import me.trambled.ozark.ozarkclient.util.player.RotationUtil;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import me.trambled.ozark.ozarkclient.util.render.RenderUtil;
import me.trambled.ozark.ozarkclient.util.world.BlockUtil;
import me.trambled.ozark.ozarkclient.util.world.CrystalUtil;
import me.trambled.ozark.ozarkclient.util.world.TimerUtil;
import me.trambled.turok.draw.RenderHelp;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

// credit to:
// travis for the original w+2 base and for the idea of packet block place
// momentum/linus for momentum calcs, sync options, heuristics, rotations, and the concept of inhibit mode
// perry for settings
// oyvey for predict break and for most of the code for predict place
// pineaple client for glow mode render
// kambing for the render settings
public class AutoCrystal extends Module {
    public AutoCrystal() {
        super(Category.COMBAT);

        this.name = "AutoCrystal";
        this.tag = "AutoCrystal";
        this.description = "Kills people (if ur good).";
    }

    Setting setting = create("Setting", "CaSetting", "Place", combobox("Place", "Break", "Place & Break", "Rotations", "Pause", "Render", "Misc"));
    Setting debug = create("Debug", "CaDebug", false);
    Setting place_crystal = create("Place", "CaPlace", true);
    Setting break_crystal = create("Break", "CaBreak", true);
    Setting anti_weakness = create("Anti-Weakness", "CaAntiWeakness", true);

    Setting module_check = create("Module Check", "CaModuleCheck", true);
    Setting break_predict = create("Break Predict", "CaBreakPredict", true);
    Setting break_predict_2 =  create("Break Predict 2", "CaBreakPredict2", false);
    Setting place_predict = create("Place Predict", "CaPlacePredict", false);
    Setting sound_predict = create("Sound Predict", "CaSoundPredict", true);
    Setting city_predict = create("City Predict", "CaCityPredict", true);
    Setting motion_predict = create("Motion Predict", "CaMotionPredict", true);
    Setting verify_place = create("Verify Place", "CaVerifyPlace", false);
    Setting fast_tick = create("Fast Tick", "CaFastTick", false);

    Setting inhibit = create("Inhibit", "CaInhibit", true);
    Setting inhibit_delay = create("Inhibit Delay", "CaInhibitDelay", 0, 0, 10);
    Setting inhibit_swings = create("Inhibit Swings", "CaInhibitSwings", 50, 1, 100);

    Setting break_trys = create("Break Attempts", "CaBreakAttempts", 1, 1, 6);
    Setting place_trys = create("Place Attempts", "CaPlaceAttempts", 1, 1, 6);

    Setting hit_range = create("Hit Range", "CaHitRange", 5f, 1f, 6f);
    Setting place_range = create("Place Range", "CaPlaceRange", 5f, 1f, 6f);
    Setting hit_range_wall = create("Hit Range Wall", "CaHitRangeWall", 3.5f, 1f, 6f);
    Setting place_range_wall = create("Place Range Wall", "CaPlaceRangeWall", 3.5f, 1f, 6f);
    Setting player_range = create("Player Range", "CaPlayerRange", 10, 1, 20);

    Setting place_delay = create("Place Delay", "CaPlaceDelay", 0, 0, 10);
    Setting break_delay = create("Break Delay", "CaBreakDelay", 1, 0, 10);

    Setting min_player_place = create("Min Enemy Place", "CaMinEnemyPlace", 6, 0, 20);
    Setting min_player_break = create("Min Enemy Break", "CaMinEnemyBreak", 6, 0, 20);
    Setting max_self_damage = create("Max Self Damage", "CaMaxSelfDamage", 8, 0, 36);
    Setting min_health_pause = create("Min Health Pause", "CaMinHealthPause", true);
    Setting required_health = create("Required Health", "CaRequiredHealth", 1, 1, 36);

    Setting ignore_web = create("Ignore Terrain (WIP)", "CaTerrainIgnore", true);

    Setting packet_place = create("Packet Place", "CaPacketPlace", true);
    Setting packet_break = create("Packet Break", "CaPacketBreak", true);

    Setting target_mode = create("Target Mode", "CaTargetMode", "Health", combobox("Health", "Closest"));
    Setting raytrace = create("Raytrace", "CaRaytrace", false);
    Setting auto_switch = create("Auto Switch", "CaAutoSwitch", true);
    Setting silent = create("Silent Switch", "CaSilentSwitch", true);
    Setting silent_hand = create("Silent Switch Hand", "CaSilentSwitchHand", true);
    Setting anti_suicide = create("Anti Suicide", "CaAntiSuicide", true);
    Setting fast_mode = create("Fast Mode", "CaFastMode", true);
    Setting fast_place = create("Fast Place", "CaFastPlace", true);

    Setting break_all = create("Break All", "CaBreakAll", false);
    Setting momentum = create("Momentum Calcs", "CaMomentumMode", false);
    Setting sync = create("Sync", "CaSync", "Sound", combobox("Sound", "Instant", "Inhibit", "Attack", "High Ping", "Full", "Semi", "None"));
    Setting heuristic = create("Heuristic", "CaHeuristic", "Damage", combobox("Damage", "MinMax", "Distance", "Atomic"));
    Setting heuristic_min_health = create("Heuristic Min Health", "CaHeuristicMinHealth", 6, 0, 120);

    Setting anti_stuck = create("Anti Stuck", "CaAntiStuck", true);
    Setting anti_stuck_tries = create("Anti Stuck Tries", "CaAntiStuckTries", 5, 1, 15);
    Setting anti_stuck_time = create("Anti Stuck Time", "CaAntiStuckTime", 1000, 0, 20000);
    Setting endcrystal = create("1.13 Mode", "CaThirteen", false);
    Setting multi_place = create("Multi Place", "CaMultiplace", true);

    Setting faceplace_mode = create("Faceplace Mode", "CaTabbottMode", true);
    Setting faceplace_mode_damage = create("Faceplace Health", "CaTabbottModeHealth", 10, 0, 36);
    Setting faceplace_check = create("No Sword FP", "CaJumpyFaceMode", false);


    Setting fuck_armor_mode = create("Armor Destroy", "CaArmorDestroy", true);
    Setting fuck_armor_mode_precent = create("Enemy Armor %", "CaArmorPercent", 5, 0, 100);
    Setting fuck_armor_mode_precent_self = create("Self Armor %", "CaArmorPercentSelf", 0, 0, 100); // retard idea by me

    Setting stop_while_mining = create("Stop While Mining", "CaStopWhileMining", false);
    Setting stop_while_eating = create("Stop While Eating", "CaStopWhileEatin", false);

    Setting swing = create("Swing", "CaSwing", "Mainhand", combobox("Mainhand", "Offhand", "Both", "None"));

    // momentum
    Setting rotate_mode = create("Rotate", "CaRotateMode", "Packet", combobox("Off", "Packet", "Strict"));
    Setting rotate_during = create("Rotate During", "CaRotateDuring", "Both", combobox("Break", "Place", "Both"));
    Setting anti_waste = create("Rotate Focus", "CaRotateAntiWaste", true);
    Setting limiter = create("Limiter", "CaRotateLimiter", "None", combobox("Narrow", "Upcoming", "None"));
    Setting max_angle = create("Max Angle", "CaMaxAngle", 180f, 0f, 360f);
    Setting min_angle = create("Min Angle", "CaMinAngle", 180f, 0f, 360f);
    Setting random_rotate = create("Random Rotate", "CaRandomRotate", false);
    Setting queue = create("Queue", "CaQueue", false);
    Setting accurate = create("Accurate", "CaAccurate", true);
    Setting rubberband = create("Detect Rubberband", "CaRotateDetectRubberband", true);
    Setting quick_restore = create("Quick Restore", "CaRestoreRotationInstant", false);

    Setting solid = create("Solid", "CaSolid", true);
    Setting outline = create("Outline", "CaOutline", true);
    Setting glow_solid = create("Glow Solid", "CaGlowSolid", false);
    Setting glow_outline = create("Glow Outline", "CaGlowOutline", false);
    Setting circle = create("Circle", "CaCircle", false);
    Setting flat = create("Circle Flat", "CaCircleFlat", false);
    Setting radius = create("Circle Radius", "CaCircleRadius", 0.7, 0.5, 1.0);
    Setting heightcircle = create("Circle Height", "CaCircleHeight", 1.0, 0.5, 2.0);
    Setting old_render = create("Old Render", "CaOldRender", false);
    Setting future_render = create("Future Render", "CaFutureRender", false);
    Setting top_block = create("Top Block", "CaTopBlock", false);
    Setting r = create("R", "CaR", 255, 0, 255);
    Setting g = create("G", "CaG", 255, 0, 255);
    Setting b = create("B", "CaB", 255, 0, 255);
    Setting a = create("Solid A", "CaA", 100, 0, 255);
    Setting a_out = create("Outline A", "CaOutlineA", 255, 0, 255);
    Setting rainbow_mode = create("Rainbow", "CaRainbow", true);
    Setting sat = create("Satiation", "CaSatiation", 0.8, 0, 1);
    Setting brightness = create("Brightness", "CaBrightness", 0.8, 0, 1);
    Setting height = create("Height", "CaHeight", 1.0, 0.0, 1.0);
    Setting render_damage = create("Render Damage", "CaRenderDamage", "Normal", combobox("Normal", "Heuristic", "None"));

    Setting clean_mode = create("Clean Settings", "CaCleanMode", true);
    Setting switch_bind = create("Switch Bind", "CaSwitchBind", 0);
    Setting faceplace_bind = create("Faceplace Bind", "CaFaceBind", 0);

    private final ConcurrentHashMap<EntityEnderCrystal, Integer> attacked_crystals = new ConcurrentHashMap<>();
    public static ArrayList<EntityEnderCrystal> fake_crystals = new ArrayList<>();

    private final TimerUtil anti_stuck_timer = new TimerUtil();
    public static ICamera camera = new Frustum();


    private RotationUtil.Rotation ca_rotation = null;

    private String detail_name = null;
    private int detail_hp = 0;

    private int old_slot = -1;
    private BlockPos render_block_init;
    private BlockPos render_block_old;

    private double render_damage_value;

    private float yaw;
    private float pitch;
    private static EntityPlayer ca_target = null;

    private boolean already_attacking = false;
    private boolean place_timeout_flag = false;
    private boolean do_switch_bind = false;
    private boolean face_place_bind = false;
    private boolean did_anything;

    private int place_timeout;
    private int break_timeout;
    private int inhibit_delay_counter;
    private int break_delay_counter;
    private int place_delay_counter;
    private int attack_swings;
    private int id = 0;

    @Override
    public void update() {
        if (!rotate_mode.in("Packet") && !fast_tick.get_value(true)) {
            do_ca();
        }
    }

    @Override
    public void fast_update() {
        if (!rotate_mode.in("Packet") && fast_tick.get_value(true)) {
            do_ca();
        }
    }

    public void do_ca() {
        if (mc.player == null || mc.world == null) {
            return;
        }

        prepare_ca();

        if (check_pause()) {
            return;
        }

        if (sync.in("Full")) {
            do_fake_crystal();
        }

        if (check_pause()) {
            return;
        }

        if (fast_mode.get_value(true)) {
            break_delay_counter++;
            place_delay_counter++;
        }

        if (place_crystal.get_value(true) && place_delay_counter > place_timeout) {
            place_crystal();
        }

        if (break_crystal.get_value(true) && break_delay_counter > break_timeout) {
            break_crystal();
        }

        if (ca_target == null) {
            Ozark.TARGET_NAME = "NULL";
        } else {
            Ozark.TARGET_NAME = ca_target.getName();
        }

        if (render_block_init != null) {
            if (!mc.world.getBlockState(render_block_init).getBlock().equals(Blocks.OBSIDIAN) && !mc.world.getBlockState(render_block_init).getBlock().equals(Blocks.BEDROCK)) {
                render_block_init = null;
            }
        }

        if (!did_anything) {
            if (old_render.get_value(true)) {
                render_block_init = null;
            }
            if (quick_restore.get_value(true) && ca_rotation != null) {
                ca_rotation.restoreRotation();
            }
            ca_target = null;
        }

        if (ca_target != null) {
            AutoEz.add_target(ca_target.getName());
            detail_name = ca_target.getName();
            detail_hp = Math.round(ca_target.getHealth() + ca_target.getAbsorptionAmount());
        }

        if (attack_swings > 2000) {
            attack_swings = 0;
        }


        render_block_old = render_block_init;

        if (!fast_mode.get_value(true)) {
            break_delay_counter++;
            place_delay_counter++;
        }
    }

    public void place_crystal() {
        BlockPos target_block;

        target_block = get_best_block();

        if (target_block == null) {
            return;
        }

        place_delay_counter = 0;

        already_attacking = false;

        boolean offhand_check = false;
        int slot = find_crystals_hotbar();
        int old = mc.player.inventory.currentItem;
        EnumHand hand = null;
        if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && auto_switch.get_value(true) && !silent.get_value(true)) {
                if (switch_bind.get_bind("").equalsIgnoreCase("None") || do_switch_bind) {
                    if (find_crystals_hotbar() == -1) return;
                    mc.player.inventory.currentItem = find_crystals_hotbar();
                    return;
                }
            }
        } else {
            offhand_check = true;
        }
        if (silent.get_value(true) && auto_switch.get_value(true)) {
            if (slot != -1) {
                if (mc.player.isHandActive() && silent_hand.get_value(true)) {
                    hand = mc.player.getActiveHand();
                }
                mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            }
        }

        if (debug.get_value(true)) {
            MessageUtil.send_client_message("placing");
        }

        if (rotate_during.in("Both") || rotate_during.in("Place")) {
            handle_rotations(false, target_block, null);
        }

        did_anything = true;
        for (int i = 0; i < place_trys.get_value(1); i++) {
            BlockUtil.placeCrystalOnBlock(target_block, offhand_check ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, packet_place.get_value(true));
        }
        if (sync.in("Semi")) {
            EntityEnderCrystal crystal = new EntityEnderCrystal(mc.world, (double) target_block.getX() + 0.5, (double) target_block.getY() + 1, (double) target_block.getZ() + 0.5);
            mc.world.addEntityToWorld(-101, crystal);
            crystal.setInvisible(true);
            fake_crystals.add(crystal);
        }

        if (silent.get_value(true) && auto_switch.get_value(true)) {
            if (slot != -1) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(old));
                if (silent_hand.get_value(true) && hand != null) {
                    mc.player.setActiveHand(hand);
                }
            }
        }
    }

    public void break_crystal() {
        EntityEnderCrystal crystal = get_best_crystal();
        if (crystal == null) {
            return;
        }

        if (anti_weakness.get_value(true) && mc.player.isPotionActive(MobEffects.WEAKNESS)) {

            boolean should_weakness = true;

            if (mc.player.isPotionActive(MobEffects.STRENGTH)) {

                if (Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() == 2) {
                    should_weakness = false;
                }

            }

            if (should_weakness) {

                if (!already_attacking) {
                    already_attacking = true;
                }

                int new_slot = -1;

                for (int i = 0; i < 9; i++) {

                    ItemStack stack = mc.player.inventory.getStackInSlot(i);

                    if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) {
                        new_slot = i;
                        mc.playerController.updateController();
                        break;
                    }

                }


                if (new_slot != -1) {
                    mc.player.inventory.currentItem = new_slot;

                }

            }

        }

        if (debug.get_value(true)) {
            MessageUtil.send_client_message("attacking");
        }

        did_anything = true;

        if (rotate_during.in("Break") || rotate_during.in("Both")) {
            handle_rotations(true, null, crystal);
        }

        for (int i = 0; i < break_trys.get_value(1); i++) {
            EntityUtil.attackEntity(crystal, packet_break.get_value(true), swing);
            attack_swings++;
        }
        add_attacked_crystal(crystal);

        if ((sync.in("Instant") || sync.in("High Ping")) && crystal.isEntityAlive()) {
            crystal.setDead();
            if (sync.in("High Ping")) {
                mc.world.removeAllEntities();
                mc.world.getLoadedEntityList();
            }
        }

        if (fake_crystals.contains(crystal) && sync.in("Semi")) {
            crystal.setDead();
        }

        break_delay_counter = 0;
    }

    public void do_fake_crystal() {
        int slot = find_crystals_hotbar();
        int old = mc.player.inventory.currentItem;
        EnumHand hand = null;
        if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && auto_switch.get_value(true)) {
                if (find_crystals_hotbar() == -1) return;
                mc.player.inventory.currentItem = find_crystals_hotbar();
                return;
            }
        }

        if (silent.get_value(true) && auto_switch.get_value(true)) {
            if (slot != -1) {
                if (mc.player.isHandActive() && silent_hand.get_value(true)) {
                    hand = mc.player.getActiveHand();
                }
                mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            }
        }

        BlockPos block = get_best_block();
        if (block == null) return;
        if (mc.world == null) return;
        if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL)
            return;
        if (debug.get_value(true)) {
            MessageUtil.send_client_message("Doing fake crystal");
        }
        EntityEnderCrystal crystal = new EntityEnderCrystal(mc.world, (double) block.getX() + 0.5, (double) block.getY() + 1, (double) block.getZ() + 0.5);
        mc.world.addEntityToWorld(-101, crystal);
        crystal.setDead();
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            mc.player.getHeldItemOffhand().setCount(mc.player.getHeldItemMainhand().getCount() - 1);
            BlockUtil.swingArm(swing);
        } else if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
            mc.player.getHeldItemMainhand().setCount(mc.player.getHeldItemMainhand().getCount() - 1);
            BlockUtil.swingArm(swing);
        }
        if (silent.get_value(true) && auto_switch.get_value(true)) {
            if (slot != -1) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(old));
                if (silent_hand.get_value(true) && hand != null) {
                    mc.player.setActiveHand(hand);
                }
            }
        }
    }

    public void break_predict_id() {
        if (id != 0 && break_predict_2.get_value(true)) {
            CPacketUseEntity predict = new CPacketUseEntity();
            if (debug.get_value(true)) {
                MessageUtil.send_client_message("attacking predicted entity id");
            }
            if (mc.isIntegratedServerRunning()) {
                predict.entityId = id + 2;
            } else {
                predict.entityId = id + 1;
            }
            predict.action = CPacketUseEntity.Action.ATTACK;
            mc.player.connection.sendPacket(predict);
            BlockUtil.swingArm(swing, EnumHand.MAIN_HAND);
            attack_swings++;
        }
    }

    public BlockPos get_best_block() {
        if (get_best_crystal() != null && !fast_place.get_value(true)) {
            place_timeout_flag = true;
            return null;
        }

        if (place_timeout_flag) {
            place_timeout_flag = false;
            return null;
        }

        double best_damage = 0;
        double rendered_damage = 0;
        double minimum_damage;
        double maximum_damage_self = this.max_self_damage.get_value(1);

        BlockPos best_block = null;

        List<BlockPos> blocks;

        if (momentum.get_value(true)) {
            blocks = CrystalUtil.crystalBlocksMomentum(mc.player, place_range.get_value(1), motion_predict.get_value(true), 1, !multi_place.get_value(true), endcrystal.get_value(true));
        } else {
            blocks = CrystalUtil.possiblePlacePositions(place_range.get_value(1), endcrystal.get_value(true), !multi_place.get_value(true));
        }

        for (Entity player : mc.world.playerEntities) {

            if (target_mode.in("Health")) {
                if (FriendUtil.isFriend(player.getName())) continue;
            }

            for (BlockPos block : blocks) {

                if (player.getDistance(mc.player) >= player_range.get_value(1)) continue;

                if (player == mc.player || !(player instanceof EntityPlayer)) continue;

                if (!BlockUtil.rayTracePlaceCheck(block, this.raytrace.get_value(true))) {
                    continue;
                }

                if (verify_place.get_value(true) && mc.player.getDistanceSq(block) > Math.pow(hit_range.get_value(1), 2))
                    continue;

                if (!BlockUtil.canSeeBlock(block) && mc.player.getDistance(block.getX(), block.getY(), block.getZ()) > place_range_wall.get_value(1)) {
                    continue;
                }

                EntityPlayer target;

                if (target_mode.in("Health")) {
                    target = (EntityPlayer) player;
                } else {
                    target = get_closest_target();
                }

                if (target == null) continue;

                if (target.isDead || target.getHealth() <= 0) continue;

                boolean no_place = faceplace_check.get_value(true) && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                if ((target.getHealth() < faceplace_mode_damage.get_value(1) && faceplace_mode.get_value(true) && !no_place) || (get_armor_fucker(target) && !no_place && !get_armor_fucker(mc.player))) {
                    minimum_damage = 2;
                } else {
                    minimum_damage = this.min_player_place.get_value(1);
                }

                if (ignore_web.get_value(true) && mc.world.getBlockState(EntityUtil.getRoundedBlockPos(target)).getBlock() != BlockUtil.getUnbreakableBlocks())  {
                    mc.world.setBlockToAir(EntityUtil.getRoundedBlockPos(target));
                }

                double target_damage = CrystalUtil.calculateDamage((double) block.getX() + 0.5, (double) block.getY() + 1, (double) block.getZ() + 0.5, target);

                if (target_damage < minimum_damage) continue;

                final double self_damage = CrystalUtil.calculateDamage((double) block.getX() + 0.5, (double) block.getY() + 1, (double) block.getZ() + 0.5, mc.player);

                if (self_damage > maximum_damage_self || (anti_suicide.get_value(true) && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) - self_damage <= 0.5))
                    continue;

                final double original_damage = target_damage;
                if (target_damage > heuristic_min_health.get_value(1)) {
                    if (heuristic.in("MinMax")) {
                        target_damage -= self_damage;
                    } else if (heuristic.in("Distance")) {
                        target_damage -= mc.player.getDistance(block.getX(), block.getY(), block.getZ());
                    } else if (heuristic.in("Atomic")) {
                        target_damage -= self_damage + mc.player.getDistance(block.getX(), block.getY(), block.getZ());
                    }
                }
                if (target_damage > best_damage) {
                    best_damage = target_damage;
                    rendered_damage = original_damage;
                    best_block = block;
                    ca_target = target;
                }
            }
        }


        if (!momentum.get_value(true)) {
            blocks.clear();
        }


        if (render_damage.in("Heuristic")) {
            render_damage_value = best_damage;
        } else if (render_damage.in("Normal")) {
            render_damage_value = rendered_damage;
        }
        render_block_init = best_block;

        return best_block;
    }

    public EntityEnderCrystal get_best_crystal() {
        double best_damage = 0;

        double minimum_damage;
        double maximum_damage_self = this.max_self_damage.get_value(1);

        double best_distance = 0;

        EntityEnderCrystal best_crystal = null;

        for (Entity c : mc.world.loadedEntityList) {

            if (!(c instanceof EntityEnderCrystal)) continue;

            EntityEnderCrystal crystal = (EntityEnderCrystal) c;
            if (mc.player.getDistance(crystal) > (!mc.player.canEntityBeSeen(crystal) ? hit_range_wall.get_value(1) : hit_range.get_value(1))) {
                continue;
            }

            if (!mc.player.canEntityBeSeen(crystal) && raytrace.get_value(true)) {
                continue;
            }

            if (crystal.isDead) continue;

            if (attacked_crystals.containsKey(crystal) && attacked_crystals.get(crystal) > anti_stuck_tries.get_value(1) && anti_stuck.get_value(true))
                continue;

            for (Entity player : mc.world.playerEntities) {

                if (player.getDistance(mc.player) >= player_range.get_value(1)) continue;

                if (player == mc.player || !(player instanceof EntityPlayer)) continue;

                if (target_mode.in("Health")) {
                    if (FriendUtil.isFriend(player.getName())) continue;

                    final EntityPlayer target = (EntityPlayer) player;

                    if (target.isDead || target.getHealth() <= 0) continue;
                }

                EntityPlayer target;

                if (target_mode.in("Health")) {
                    target = (EntityPlayer) player;
                } else {
                    target = get_closest_target();
                }

                if (target == null) continue;

                boolean no_place = faceplace_check.get_value(true) && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                if ((target.getHealth() < faceplace_mode_damage.get_value(1) && faceplace_mode.get_value(true) && !no_place) || (get_armor_fucker(target) && !no_place && !get_armor_fucker(mc.player)) || (face_place_bind)) {
                    minimum_damage = 2;
                } else {
                    minimum_damage = this.min_player_break.get_value(1);
                }

                final double target_damage = CrystalUtil.calculateDamage(crystal, target);

                if (target_damage < minimum_damage) continue;

                final double self_damage = CrystalUtil.calculateDamage(crystal, mc.player);

                if (self_damage > maximum_damage_self || (anti_suicide.get_value(true) && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) - self_damage <= 0.5))
                    continue;

                if (target_damage > best_damage && !break_all.get_value(true)) {
                    ca_target = target;
                    best_damage = target_damage;
                    best_crystal = crystal;
                }


            }

            if (break_all.get_value(true) && mc.player.getDistanceSq(crystal) > best_distance) {
                best_distance = mc.player.getDistanceSq(crystal);
                best_crystal = crystal;
            }

        }

        return best_crystal;

    }

    public boolean get_armor_fucker(EntityPlayer p) {
        for (ItemStack stack : p.getArmorInventoryList()) {

            if (stack == null || stack.getItem() == Items.AIR) return true;

            final float armor_percent = ((float) (stack.getMaxDamage() - stack.getItemDamage()) / (float) stack.getMaxDamage()) * 100.0f;

            if (p == mc.player) {
                if (fuck_armor_mode.get_value(true) && fuck_armor_mode_precent_self.get_value(1) >= armor_percent)
                    return true;
            } else {
                if (fuck_armor_mode.get_value(true) && fuck_armor_mode_precent.get_value(1) >= armor_percent)
                    return true;
            }
        }
        return false;
    }

    public void prepare_ca() {
        did_anything = false;

        if (switch_bind.get_bind("").equalsIgnoreCase("None")) {
            do_switch_bind = false;
        }
        if (faceplace_bind.get_bind("").equalsIgnoreCase("None")) {
            face_place_bind = false;
        }

        if (rainbow_mode.get_value(true)) {
            cycle_rainbow();
        }

        if (ca_rotation != null && mc.gameSettings.keyBindUseItem.pressed && (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow || mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle)) {
            ca_rotation.restoreRotation();
        }

        for (Entity entity : mc.world.loadedEntityList) {
            if (entity.getEntityId() <= this.id) continue;
            this.id = entity.getEntityId();
        }

        place_timeout = this.place_delay.get_value(1);
        break_timeout = this.break_delay.get_value(1);

        if (anti_stuck_timer.passed(anti_stuck_time.get_value(1))) {
            anti_stuck_timer.reset();
            attacked_crystals.clear();
        }

        if (rotate_mode.in("Strict")) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;  //trambled, rotation yaw and pitch is for strict servers
        }
    }

    public boolean check_pause() {
        if (find_crystals_hotbar() == -1 && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            return true;
        }

        if (inhibit.get_value(true)) {
            if (attack_swings > inhibit_swings.get_value(1)) {
                if (inhibit_delay_counter > inhibit_delay.get_value(1)) {
                    attack_swings = 0;
                    inhibit_delay_counter = 0;
                } else {
                    if (old_render.get_value(true)) {
                        render_block_init = null;
                    }
                    inhibit_delay_counter++;
                    EntityEnderCrystal crystal = get_best_crystal();
                    if (sync.in("Inhibit") && crystal != null) {
                        crystal.setDead();
                    }
                    return true;
                }
            }
        }

        if (stop_while_mining.get_value(true) && mc.gameSettings.keyBindAttack.isKeyDown() && mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
            if (old_render.get_value(true)) {
                render_block_init = null;
            }
            return true;
        }
        if (stop_while_eating.get_value(true) && PlayerUtil.IsEating()) {
            if (old_render.get_value(true)) {
                render_block_init = null;
            }
            return true;
        }

        if (min_health_pause.get_value(true) && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) < required_health.get_value(1)) {
            return true;
        }

        if (AntiTrap.is_trapped && Ozark.get_module_manager().get_module_with_tag("AntiTrap").is_active() && module_check.get_value(true)) {
            if (old_render.get_value(true)) {
                render_block_init = null;
            }
            return true;
        }

        if (Ozark.get_module_manager().get_module_with_tag("Surround").is_active() && module_check.get_value(true)) {
            if (old_render.get_value(true)) {
                render_block_init = null;
            }
            return true;
        }

        if (Ozark.get_module_manager().get_module_with_tag("HoleFill").is_active() && !Ozark.get_setting_manager().get_setting_with_tag("HoleFill", "HoleFillSmart").get_value(true) && module_check.get_value(true)) {
            if (old_render.get_value(true)) {
                render_block_init = null;
            }
            return true;
        }

        if (Ozark.get_module_manager().get_module_with_tag("Trap").is_active() && module_check.get_value(true)) {
            if (old_render.get_value(true)) {
                render_block_init = null;
            }
            return true;
        }

        if (Ozark.get_module_manager().get_module_with_tag("AutoAnvil").is_active() && module_check.get_value(true)) {
            if (old_render.get_value(true)) {
                render_block_init = null;
            }
            return true;
        }

        if (Ozark.get_module_manager().get_module_with_tag("PistonCrystal").is_active() && module_check.get_value(true)) {
            if (old_render.get_value(true)) {
                render_block_init = null;
            }
            return true;
        }

        if (AntiTrap.is_trapped && Ozark.get_module_manager().get_module_with_tag("AntiTrap").is_active() && module_check.get_value(true)) {
            if (old_render.get_value(true)) {
                render_block_init = null;
            }
            return true;
        }

        return false;
    }

    public EntityPlayer get_closest_target() {
        if (mc.world.playerEntities.isEmpty())
            return null;

        EntityPlayer closestTarget = null;

        for (final EntityPlayer target : mc.world.playerEntities) {
            if (target == mc.player)
                continue;

            if (FriendUtil.isFriend(target.getName()))
                continue;

            if (!EntityUtil.isLiving(target))
                continue;

            if (target.getHealth() <= 0.0f)
                continue;

            if (target.getDistance(mc.player) >= player_range.get_value(1)) continue;

            if (closestTarget != null)
                if (mc.player.getDistance(target) > mc.player.getDistance(closestTarget))
                    continue;

            closestTarget = target;
        }

        return closestTarget;
    }

    public void handle_rotations(boolean breaking_crystal, BlockPos pos, EntityEnderCrystal crystal) {
        if (breaking_crystal && crystal == null) return;
        if (!breaking_crystal && pos == null) return;
        if (rotate_mode.in("Off")) return;
        if (debug.get_value(true)) {
            if (breaking_crystal) {
                MessageUtil.send_client_message("Rotating to crystal");
            } else {
                MessageUtil.send_client_message("Rotating to block");
            }
        }

        if (ca_target != null) {
            float yaw;
            float pitch;
            if (breaking_crystal) {
                yaw = RotationUtil.getAngles(crystal)[0];
                pitch = RotationUtil.getAngles(crystal)[1];
            } else {
                yaw = RotationUtil.getPositionAngles(pos)[0];
                pitch = RotationUtil.getPositionAngles(pos)[1];
            }

            if (pitch == this.pitch && yaw == this.yaw && !anti_waste.get_value(true)) return;

            if (rotate_mode.in("Off")) { //                       [ why.... ]
                ca_rotation = new RotationUtil.Rotation(0, 0, RotationUtil.RotationMode.None, RotationUtil.RotationPriority.Lowest);
            } else if (rotate_mode.in("Packet")) {
                ca_rotation = new RotationUtil.Rotation(yaw, pitch, RotationUtil.RotationMode.Packet, RotationUtil.RotationPriority.Highest);
            } else if (rotate_mode.in("Strict")) {
                ca_rotation = new RotationUtil.Rotation(yaw, pitch, RotationUtil.RotationMode.Legit, RotationUtil.RotationPriority.Highest);
            }
        }

        if (!limiter.in("None") && Ozark.get_rotation_manager().serverRotation != null)
            ca_rotation = RotationUtil.rotationStep(Ozark.get_rotation_manager().serverRotation, ca_rotation, (float) (((random_rotate.get_value(true) ? Math.random() : 1) * (max_angle.get_value(1d) - min_angle.get_value(1d))) + min_angle.get_value(1)), limiter);

        if (queue.get_value(true)) {
            Ozark.get_rotation_manager().rotationQueue.add(ca_rotation);
        } else {
            Ozark.get_rotation_manager().setCurrentRotation(ca_rotation);
        }

    }

    @Override
    public void render(EventRender event) {
        if (render_block_init == null) return;

        boolean render_render = outline.get_value(true) || solid.get_value(true) || glow_solid.get_value(true) || glow_outline.get_value(true);

        if (!render_render) return;

        render_block(render_block_init);

        if (future_render.get_value(true) && render_block_old != null) {
            render_block(render_block_old);
        }

        if (!render_damage.in("None")) {
            try {
                RenderUtil.drawText(render_block_init, ((Math.floor(this.render_damage_value) == this.render_damage_value) ? Integer.valueOf((int) this.render_damage_value) : String.format("%.1f", this.render_damage_value)) + "", true);
            } catch (Exception ignored) {
            }
        }

    }

    public void render_block(BlockPos pos) {
        BlockPos render_block = (top_block.get_value(true) ? pos.up() : pos);

        float h = (float) height.get_value(1.0);

        if (solid.get_value(true)) {
            RenderHelp.prepare("quads");
            RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
                    render_block.getX(), render_block.getY(), render_block.getZ(),
                    1, h, 1,
                    r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1),
                    "all"
            );
            RenderHelp.release();
        }
        if (circle.get_value(true)) {
            drawCircle(render_block.getX(), (flat.get_value(true)) ? render_block.getY() + heightcircle.get_value(1) : render_block.getY(), render_block.getZ(), radius.get_value(1), new Color(r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1)));
        }

        if (outline.get_value(true)) {
            RenderHelp.prepare("lines");
            RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
                    render_block.getX(), render_block.getY(), render_block.getZ(),
                    1, h, 1,
                    r.get_value(1), g.get_value(1), b.get_value(1), a_out.get_value(1), 1,
                    "all"
            );
            RenderHelp.release();
        }
        if (glow_solid.get_value(true)) {

            RenderHelp.prepare("quads");
            RenderHelp.draw_gradiant_cube(RenderHelp.get_buffer_build(),
                    render_block.getX(), render_block.getY(), render_block.getZ(),
                    1, h, 1, new Color(r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1)),
                    new Color(0, 0, 0, 0),
                    "all"
            );
            RenderHelp.release();
        }

        if (glow_outline.get_value(true)) {
            RenderHelp.prepare("lines");
            RenderHelp.draw_gradiant_outline(RenderHelp.get_buffer_build(),
                    render_block.getX(), render_block.getY(), render_block.getZ(),
                    h, new Color(r.get_value(1), g.get_value(1), b.get_value(1), a_out.get_value(1)),
                    new Color(0, 0, 0, 0),
                    "all"
            );
            RenderHelp.release();
        }
    }

    @EventHandler
    private final Listener<EventEntityRemoved> on_entity_removed = new Listener<>(event -> {
        if (event.get_entity() instanceof EntityEnderCrystal) {
            attacked_crystals.remove(event.get_entity());
            fake_crystals.remove(event.get_entity());
        }
    });

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> receive_listener = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.get_packet();

            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity e : mc.world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal) {
                        if (e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0f) {
                            if (sync.in("Sound")) {
                                e.setDead();
                            }
                            if (sound_predict.get_value(true)) {
                                if (debug.get_value(true)) {
                                    MessageUtil.send_client_message("Sound predicting");
                                }
                                place_crystal();
                            }
                        }
                    }
                }
            }
        }

        if (event.get_packet() instanceof SPacketSpawnObject) {
            final SPacketSpawnObject packet = (SPacketSpawnObject) event.get_packet();
            if (packet.getType() == 51 && ca_target != null && break_predict.get_value(true)) {
                if (!this.is_predicting_crystal(packet)) {
                    return;
                }
                if (debug.get_value(true)) {
                    MessageUtil.send_client_message("break predicting");
                }
                BlockPos pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
                CPacketUseEntity predict = new CPacketUseEntity();
                predict.entityId = packet.getEntityID();
                predict.action = CPacketUseEntity.Action.ATTACK;
                if (rotate_during.in("Break") || rotate_during.in("Both")) {
                    handle_rotations(false, pos, null);
                }
                boolean offhand_check = mc.player.getHeldItemOffhand ( ).getItem ( ) == Items.END_CRYSTAL;
                AutoCrystal.mc.player.connection.sendPacket(predict);
                attack_swings++;
                BlockUtil.swingArm(swing, offhand_check ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            }
        }
        if (event.get_packet() instanceof SPacketPlayerPosLook && rubberband.get_value(true)) {
            ca_rotation.restoreRotation();
        }
    });

    @EventHandler
    private final Listener<EventPacket.SendPacket> send_listener = new Listener<>(event -> {
        if (event.get_packet() instanceof CPacketUseEntity && ((CPacketUseEntity) event.get_packet()).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity) event.get_packet()).getEntityFromWorld(mc.world) instanceof EntityEnderCrystal) {
            if (sync.in("Attack"))
                Objects.requireNonNull(((CPacketUseEntity) event.get_packet()).getEntityFromWorld(mc.world)).setDead();
        }
        if (event.get_packet() instanceof CPacketUseEntity && ((CPacketUseEntity) event.get_packet()).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity) event.get_packet()).getEntityFromWorld(mc.world) instanceof EntityEnderCrystal) {
            EntityEnderCrystal predicted_crystal = (EntityEnderCrystal) ((CPacketUseEntity) event.get_packet()).getEntityFromWorld(mc.world);
            if (predicted_crystal != null && place_predict.get_value(true) && ca_target != null) {
                if (is_predicting_block(predicted_crystal)) {
                    boolean offhand_check = false;
                    if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                        if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && auto_switch.get_value(true) && !place_crystal.get_value(true) && !silent.get_value(true)) {
                            if (find_crystals_hotbar() == -1) return;
                            mc.player.inventory.currentItem = find_crystals_hotbar();
                            return;
                        }
                    } else {
                        offhand_check = true;
                    }
                    int slot = find_crystals_hotbar();
                    int old = mc.player.inventory.currentItem;
                    EnumHand hand = null;
                    if (silent.get_value(true) && auto_switch.get_value(true)) {
                        if (slot != -1) {
                            if (mc.player.isHandActive() && silent_hand.get_value(true)) {
                                hand = mc.player.getActiveHand();
                            }
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
                        }
                    }
                    if (debug.get_value(true)) {
                        MessageUtil.send_client_message("Place predicting");
                    }
                    if (rotate_during.in("Both") || rotate_during.in("Place")) {
                        handle_rotations(false, predicted_crystal.getPosition().down(), null);
                    }
                    BlockUtil.placeCrystalOnBlock(predicted_crystal.getPosition().down(), offhand_check ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, packet_place.get_value(true));
                    if (silent.get_value(true) && auto_switch.get_value(true)) {
                        if (slot != -1) {
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(old));
                            if (silent_hand.get_value(true) && hand != null) {
                                mc.player.setActiveHand(hand);
                            }
                        }
                    }
                }
            }
        }

        if (event.get_packet() instanceof CPacketPlayerTryUseItemOnBlock) {
            if (id != 0 && break_predict_2.get_value(true)) {
                CPacketUseEntity predict = new CPacketUseEntity();
                if (debug.get_value(true)) {
                    MessageUtil.send_client_message("attacking predicted entity id");
                }
                if (mc.isIntegratedServerRunning()) {
                    predict.entityId = id + 2;
                } else {
                    predict.entityId = id + 1;
                }
                predict.action = CPacketUseEntity.Action.ATTACK;
                mc.player.connection.sendPacket(predict);
                BlockUtil.swingArm(swing, EnumHand.MAIN_HAND);
                attack_swings++;
            }
        }
        if (event.get_packet() instanceof CPacketPlayer && rotate_mode.in("Packet")) {
            final CPacketPlayer p = (CPacketPlayer) event.get_packet();
            yaw = p.yaw;
            pitch = p.pitch;
        }
    });

    @EventHandler
    private final Listener<EventRotation> motion_update = new Listener<>(event -> {
        if (rotate_mode.in("Packet")) {
            do_ca();
        }
    });

    @SubscribeEvent
    public void block_break(BlockEvent.BreakEvent event) {
        if (city_predict.get_value(true)) {
            if (debug.get_value(true)) {
                MessageUtil.send_client_message("city predicting");
            }
            place_crystal();
        }
    }

    private boolean is_predicting_crystal(SPacketSpawnObject packet) {
        BlockPos pack_pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
        if (AutoCrystal.mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) > (double) this.hit_range.get_value(1)) {
            return false;
        }
        if (!BlockUtil.canSeeBlock(pack_pos) && AutoCrystal.mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) > (double) this.hit_range_wall.get_value(1)) {
            return false;
        }
        double targetDmg = CrystalUtil.calculateDamage(packet.getX() + 0.5, packet.getY() + 1.0, packet.getZ() + 0.5, ca_target);
        if (EntityUtil.isInHole(AutoCrystal.mc.player) && targetDmg >= 1.0) {
            return true;
        }
        double selfDmg = CrystalUtil.calculateDamage(packet.getX() + 0.5, packet.getY() + 1.0, packet.getZ() + 0.5, AutoCrystal.mc.player);
        double d = anti_suicide.get_value(true) ? 2.0 : 0.5;
        if (get_armor_fucker(ca_target) && !get_armor_fucker(mc.player)) {
            return true;
        }
        if (selfDmg + d < (double) (AutoCrystal.mc.player.getHealth() + AutoCrystal.mc.player.getAbsorptionAmount()) && targetDmg >= (double) (ca_target.getAbsorptionAmount() + ca_target.getHealth())) {
            return true;
        }
        if (targetDmg >= (double) this.min_player_break.get_value(1) && selfDmg <= (double) this.max_self_damage.get_value(1)) {
            return true;
        }
        return faceplace_mode.get_value(true) && EntityUtil.isInHole(ca_target) && ca_target.getHealth() + ca_target.getAbsorptionAmount() <= this.faceplace_mode.get_value(1);
    }

    private boolean is_predicting_block(EntityEnderCrystal crystal) {
        BlockPos packPos = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
        if (AutoCrystal.mc.player.getDistance(crystal.posX, crystal.posY, crystal.posZ) > (double) this.hit_range.get_value(1)) {
            return false;
        }
        if (!BlockUtil.canSeeBlock(packPos) && AutoCrystal.mc.player.getDistance(crystal.posX, crystal.posY, crystal.posZ) > (double) this.hit_range_wall.get_value(1)) {
            return false;
        }
        double targetDmg = CrystalUtil.calculateDamage(crystal.posX + 0.5, crystal.posY + 1.0, crystal.posZ + 0.5, ca_target);
        if (EntityUtil.isInHole(AutoCrystal.mc.player) && targetDmg >= 1.0) {
            return true;
        }
        double selfDmg = CrystalUtil.calculateDamage(crystal.posX + 0.5, crystal.posY + 1.0, crystal.posZ + 0.5, AutoCrystal.mc.player);
        double d = anti_suicide.get_value(true) ? 2.0 : 0.5;
        if (get_armor_fucker(ca_target) && !get_armor_fucker(mc.player)) {
            return true;
        }
        if (selfDmg + d < (double) (AutoCrystal.mc.player.getHealth() + AutoCrystal.mc.player.getAbsorptionAmount()) && targetDmg >= (double) (ca_target.getAbsorptionAmount() + ca_target.getHealth())) {
            return true;
        }
        if (targetDmg >= (double) this.min_player_break.get_value(1) && selfDmg <= (double) this.max_self_damage.get_value(1)) {
            return true;
        }
        return faceplace_mode.get_value(true) && EntityUtil.isInHole(ca_target) && ca_target.getHealth() + ca_target.getAbsorptionAmount() <= this.faceplace_mode.get_value(1);
    }

    public void cycle_rainbow() {
        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_o = Color.HSBtoRGB(tick_color[0], sat.get_value(1), brightness.get_value(1));

        r.set_value((color_rgb_o >> 16) & 0xFF);
        g.set_value((color_rgb_o >> 8) & 0xFF);
        b.set_value(color_rgb_o & 0xFF);
    }

    private int find_crystals_hotbar() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                return i;
            }
        }
        return -1;
    }

    private void add_attacked_crystal(EntityEnderCrystal crystal) {
        if (crystal == null) return;
        if (attacked_crystals.containsKey(crystal)) {
            int value = attacked_crystals.get(crystal);
            attacked_crystals.put(crystal, value + 1);
        } else {
            attacked_crystals.put(crystal, 1);
        }
    }

    @Override
    public void enable() {

        inhibit_delay_counter = 0;
        attack_swings = 0;
        place_timeout_flag = false;
        ca_target = null;
        anti_stuck_timer.reset();
        detail_name = null;
        detail_hp = 20;


    }

    @Override
    public void disable() {
        render_block_init = null;
        ca_target = null;
        Ozark.TARGET_NAME = "NULL";
        if (ca_rotation != null) {
            ca_rotation.restoreRotation();
        }
        old_slot = -1;
    }

    @Override
    public void on_bind(String tag) {
        if (tag.equals("CaSwitchBind")) {
            do_switch_bind = !do_switch_bind;
        }
        if (tag.equals("CaFaceBind")) {
            face_place_bind = !face_place_bind;
        }
    }

    @Override
    public void update_always() {
        // PLACE
        place_crystal.set_shown((!clean_mode.get_value(true) || setting.in("Place")));
        place_trys.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        place_range.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        place_range_wall.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        place_delay.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        min_player_place.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        packet_place.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        endcrystal.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        momentum.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        multi_place.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        faceplace_mode.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        faceplace_mode_damage.set_shown(faceplace_mode.get_value(true) && (!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        faceplace_check.set_shown(faceplace_mode.get_value(true) && (!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        heuristic.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        heuristic_min_health.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true) && !heuristic.in("Damage"));
        fast_place.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        sound_predict.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        motion_predict.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        verify_place.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        city_predict.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        auto_switch.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true));
        silent.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true) && auto_switch.get_value(true));
        silent_hand.set_shown((!clean_mode.get_value(true) || setting.in("Place")) && place_crystal.get_value(true) && auto_switch.get_value(true) && silent.get_value(true));
        place_predict.set_shown((!clean_mode.get_value(true) || setting.in("Place")));
        ignore_web.set_shown((!clean_mode.get_value(true) || setting.in("Place")));

        // BREAK
        break_crystal.set_shown((!clean_mode.get_value(true) || setting.in("Break")));
        break_trys.set_shown((!clean_mode.get_value(true) || setting.in("Break")) && break_crystal.get_value(true));
        break_predict.set_shown((!clean_mode.get_value(true) || setting.in("Break")) && break_crystal.get_value(true));
        break_predict_2.set_shown((!clean_mode.get_value(true) || setting.in("Break")) && break_crystal.get_value(true));
        break_all.set_shown((!clean_mode.get_value(true) || setting.in("Break")) && break_crystal.get_value(true));
        swing.set_shown((!clean_mode.get_value(true) || setting.in("Break")) && break_crystal.get_value(true));
        packet_break.set_shown((!clean_mode.get_value(true) || setting.in("Break")) && break_crystal.get_value(true));
        min_player_break.set_shown((!clean_mode.get_value(true) || setting.in("Break")) && break_crystal.get_value(true));
        break_delay.set_shown((!clean_mode.get_value(true) || setting.in("Break")) && break_crystal.get_value(true));
        hit_range.set_shown((!clean_mode.get_value(true) || setting.in("Break")) && break_crystal.get_value(true));
        hit_range_wall.set_shown((!clean_mode.get_value(true) || setting.in("Break")) && break_crystal.get_value(true));
        anti_weakness.set_shown((!clean_mode.get_value(true) || setting.in("Break")) && break_crystal.get_value(true));

        // PLACE & BREAK
        target_mode.set_shown(!clean_mode.get_value(true) || setting.in("Place & Break"));
        anti_suicide.set_shown(!clean_mode.get_value(true) || setting.in("Place & Break"));
        player_range.set_shown(!clean_mode.get_value(true) || setting.in("Place & Break"));
        raytrace.set_shown(!clean_mode.get_value(true) || setting.in("Place & Break"));
        fast_mode.set_shown(!clean_mode.get_value(true) || setting.in("Place & Break"));
        sync.set_shown(!clean_mode.get_value(true) || setting.in("Place & Break"));
        anti_stuck.set_shown(!clean_mode.get_value(true) || setting.in("Place & Break"));
        anti_stuck_time.set_shown(anti_stuck.get_value(true) && (!clean_mode.get_value(true) || setting.in("Place & Break")));
        anti_stuck_tries.set_shown(anti_stuck.get_value(true) && (!clean_mode.get_value(true) || setting.in("Place & Break")));
        fuck_armor_mode.set_shown(!clean_mode.get_value(true) || setting.in("Place & Break"));
        fuck_armor_mode_precent.set_shown(fuck_armor_mode.get_value(true) && (!clean_mode.get_value(true) || setting.in("Place & Break")));
        fuck_armor_mode_precent_self.set_shown(fuck_armor_mode.get_value(true) && (!clean_mode.get_value(true) || setting.in("Place & Break")));
        max_self_damage.set_shown(!clean_mode.get_value(true) || setting.in("Place & Break"));
        fast_tick.set_shown(!clean_mode.get_value(true) || setting.in("Place & Break"));

        // ROTATIONS
        rotate_mode.set_shown(!clean_mode.get_value(true) || setting.in("Rotations"));
        limiter.set_shown(!rotate_mode.in("Off") && (!clean_mode.get_value(true) || setting.in("Rotations")));
        rotate_during.set_shown(!rotate_mode.in("Off") && (!clean_mode.get_value(true) || setting.in("Rotations")));
        max_angle.set_shown(!rotate_mode.in("Off") && !limiter.in("None") && (!clean_mode.get_value(true) || setting.in("Rotations")));
        min_angle.set_shown(!rotate_mode.in("Off") && !limiter.in("None") && (!clean_mode.get_value(true) || setting.in("Rotations")));
        random_rotate.set_shown(!rotate_mode.in("Off") && !limiter.in("None") && (!clean_mode.get_value(true) || setting.in("Rotations")));
        rubberband.set_shown(!rotate_mode.in("Off") && (!clean_mode.get_value(true) || setting.in("Rotations")));
        anti_waste.set_shown(!rotate_mode.in("Off") && (!clean_mode.get_value(true) || setting.in("Rotations")));
        quick_restore.set_shown(!rotate_mode.in("Off") && (!clean_mode.get_value(true) || setting.in("Rotations")));
        queue.set_shown(!rotate_mode.in("Off") && (!clean_mode.get_value(true) || setting.in("Rotations")));
        accurate.set_shown(!rotate_mode.in("Off") && queue.get_value(true) && (!clean_mode.get_value(true) || setting.in("Rotations")));

        // PAUSE
        inhibit.set_shown(!clean_mode.get_value(true) || setting.in("Pause"));
        inhibit_delay.set_shown(inhibit.get_value(true) && (!clean_mode.get_value(true) || setting.in("Pause")));
        inhibit_swings.set_shown(inhibit.get_value(true) && (!clean_mode.get_value(true) || setting.in("Pause")));
        module_check.set_shown(!clean_mode.get_value(true) || setting.in("Pause"));
        min_health_pause.set_shown(!clean_mode.get_value(true) || setting.in("Pause"));
        required_health.set_shown(min_health_pause.get_value(true) && (!clean_mode.get_value(true) || setting.in("Pause")));
        stop_while_mining.set_shown(!clean_mode.get_value(true) || setting.in("Pause"));
        stop_while_eating.set_shown(!clean_mode.get_value(true) || setting.in("Pause"));

        // RENDER
        boolean render_render = outline.get_value(true) || solid.get_value(true) || glow_solid.get_value(true) || glow_outline.get_value(true) || circle.get_value(true);
        circle.set_shown(!clean_mode.get_value(true) || setting.in("Render"));
        outline.set_shown(!clean_mode.get_value(true) || setting.in("Render"));
        solid.set_shown(!clean_mode.get_value(true) || setting.in("Render"));
        glow_solid.set_shown(!clean_mode.get_value(true) || setting.in("Render"));
        glow_outline.set_shown(!clean_mode.get_value(true) || setting.in("Render"));
        flat.set_shown(circle.get_value(true) && render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        heightcircle.set_shown(circle.get_value(true) && render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        radius.set_shown(circle.get_value(true) && render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        old_render.set_shown(render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        future_render.set_shown(render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        top_block.set_shown(render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        r.set_shown(render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        g.set_shown(render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        b.set_shown(render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        a.set_shown(render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        a_out.set_shown(render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        rainbow_mode.set_shown(render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        sat.set_shown(render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        brightness.set_shown(render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        height.set_shown(render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        render_damage.set_shown(render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        sat.set_shown(rainbow_mode.get_value(true) && render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        brightness.set_shown(rainbow_mode.get_value(true) && render_render && (!clean_mode.get_value(true) || setting.in("Render")));
        a.set_shown((solid.get_value(true) || glow_solid.get_value(true)) && (!clean_mode.get_value(true) || setting.in("Render")));
        a_out.set_shown((outline.get_value(true) || glow_outline.get_value(true)) && (!clean_mode.get_value(true) || setting.in("Render")));


        // MISC
        debug.set_shown(!clean_mode.get_value(true) || setting.in("Misc"));
        switch_bind.set_shown(!clean_mode.get_value(true) || setting.in("Misc"));
        faceplace_bind.set_shown(!clean_mode.get_value(true) || setting.in("Misc"));
        clean_mode.set_shown(!clean_mode.get_value(true) || setting.in("Misc"));
        setting.set_shown(clean_mode.get_value(true));
    }

    @Override
    public void log_out() {
        this.set_disable();
        id = 0;
    }

    @Override
    public String array_detail() {
        return (detail_name != null) ? detail_name + " | " + detail_hp : "None";
    }

    public static EntityPlayer get_target() {
        return ca_target;
    }

    public static void drawCircleVertices(AxisAlignedBB bb, float radius, Color colour){
        float r = (float) colour.getRed() / 255.0f;
        float g = (float) colour.getGreen() / 255.0f;
        float b = (float) colour.getBlue() / 255.0f;
        float a = (float) colour.getAlpha() / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(1f);
        for (int i = 0; i < 360; i++) {
            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(bb.getCenter().x + (Math.sin((i * 3.1415926D / 180)) * radius), bb.minY, bb.getCenter().z + (Math.cos((i * 3.1415926D / 180)) * radius)).color(r, g, b, a).endVertex();
            buffer.pos(bb.getCenter().x + (Math.sin(((i + 1) * 3.1415926D / 180)) * radius), bb.minY, bb.getCenter().z + (Math.cos(((i + 1) * 3.1415926D / 180)) * radius)).color(r, g, b, a).endVertex();
            tessellator.draw();
        }
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawCircle(float x, float y, float z, float radius, Color colour){
        //IBlockState iblockstate = RenderUtil.mc.world.getBlockState(new BlockPos(x, y, z));
        //Vec3d interpPos = EntityUtil.getInterpolatedPos(RenderUtil.mc.player, RenderUtil.mc.getRenderPartialTicks());
        BlockPos pos = new BlockPos(x, y, z);
        //AxisAlignedBB bb = iblockstate.getSelectedBoundingBox(RenderUtil.mc.world, new BlockPos(x, y, z)).offset(-interpPos.x, -interpPos.y, -interpPos.z);
        AxisAlignedBB bb = new AxisAlignedBB((double) pos.getX() - mc.getRenderManager().viewerPosX, (double) pos.getY() - mc.getRenderManager().viewerPosY,
                (double) pos.getZ() - mc.getRenderManager().viewerPosZ,
                (double) (pos.getX() + 1) - mc.getRenderManager().viewerPosX,
                (double) (pos.getY() + 1) - mc.getRenderManager().viewerPosY, (double) (pos.getZ() + 1) - mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + mc.getRenderManager().viewerPosX, bb.minY + mc.getRenderManager().viewerPosY, bb.minZ + mc.getRenderManager().viewerPosZ, bb.maxX + mc.getRenderManager().viewerPosX, bb.maxY +mc.getRenderManager().viewerPosY, bb.maxZ + mc.getRenderManager().viewerPosZ))) {
            drawCircleVertices(bb, radius, colour);
        }
    }

}
