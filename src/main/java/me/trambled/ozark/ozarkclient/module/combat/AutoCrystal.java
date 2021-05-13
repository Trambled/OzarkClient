package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.turok.draw.RenderHelp;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.event.events.*;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.chat.AutoEz;
import me.trambled.ozark.ozarkclient.util.*;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

// credit to:
// travis for the original w+2 base and for the idea of packet block place
// momentum/linus for momentum calcs and sync options
// perry for settings
// oyvey for packet break and for most of the code for packet place
public class AutoCrystal extends Module {
    public AutoCrystal() {
        super(Category.COMBAT);

        this.name        = "AutoCrystal";
        this.tag         = "AutoCrystal";
        this.description = "kills people (if ur good)";
    }

    Setting debug = create("Debug", "CaDebug", false);
    Setting place_crystal = create("Place", "CaPlace", true);
    Setting break_crystal = create("Break", "CaBreak", true);
    Setting anti_weakness = create("Anti-Weakness", "CaAntiWeakness", true);
    Setting alternative = create("Alternative", "CaAlternative", false);
    Setting module_check = create("Module Check", "CaModuleCheck", false);
    Setting break_predict = create("Break Predict", "CaBreakPredict", true);
    Setting break_predict_factor = create("Break Predict Delay", "CaBreakPredictFactor", 0, 0, 200);
    Setting place_predict = create("Place Predict", "CaPlacePredict", false);
    Setting place_predict_factor = create("Place Predict Delay", "CaPlacePredictFactor", 0, 0, 200); // too lazy to make it mc ticks
    Setting motion_predict = create("Motion Predict", "CaMotionPredict", true);
    Setting motion_predict_factor = create("Motion Factor", "CaMotionPredictFactor", 1f, 0f, 2f);
    Setting verify_place = create("Verify Place", "CaVerifyPlace", false);
    Setting inhibit = create("Inhibit", "CaInhibit", true);
    Setting inhibit_delay = create("Inhibit Delay", "CaInhibitDelay", 0, 0, 10);
    Setting inhibit_swings = create("Inhibit Swings", "CaInhibitSwings", 50, 1, 100);
    Setting break_trys = create("Break Attempts", "CaBreakAttempts", 1, 1, 6);
    Setting place_trys = create("Place Attempts", "CaPlaceAttempts", 1, 1, 6);

    Setting hit_range = create("Hit Range", "CaHitRange", 5f, 1f, 6f);
    Setting place_range = create("Place Range", "CaPlaceRange", 5f, 1f, 6f);
    Setting hit_range_wall = create("Range Wall", "CaRangeWall", 3.5f, 1f, 6f);
    Setting player_range = create("Player Range", "CaPlayerRange", 10, 1, 20);

    Setting place_delay = create("Place Delay", "CaPlaceDelay", 0, 0, 10);
    Setting break_delay = create("Break Delay", "CaBreakDelay", 1, 0, 10);

    Setting min_player_place = create("Min Enemy Place", "CaMinEnemyPlace", 6, 0, 20);
    Setting min_player_break = create("Min Enemy Break", "CaMinEnemyBreak", 6, 0, 20);
    Setting max_self_damage = create("Max Self Damage", "CaMaxSelfDamage", 8, 0, 36);

    Setting min_health_pause = create("Min Health Pause", "CaMinHealthPause", true);
    Setting required_health = create("Required Health", "CaRequiredHealth", 1, 1, 36);

    Setting packet_place = create("Packet Place", "CaPacketPlace", true);
    Setting packet_break = create("Packet Break", "CaPackeBreak", true);

    Setting rotate_mode = create("Rotate", "CaRotateMode", "Packet", combobox("Off", "Packet", "Const", "Seizure"));
    Setting target_mode = create("Target Mode", "CaTargetMode", "Health", combobox("Health", "Closest"));
    Setting raytrace = create("Raytrace", "CaRaytrace", false);
    Setting switch_mode = create("Switch Mode", "CaSwitchMode", "Normal", combobox("Normal", "Ghost", "None"));
    Setting anti_suicide = create("Anti Suicide", "CaAntiSuicide", true);
    Setting fast_mode = create("Fast Mode", "CaSpeed", true);

    Setting break_all = create("Break All", "CaBreakAll", false);
    Setting momentum = create("Momentum Calcs", "CaMomentumMode", false);
    Setting sync = create("Sync", "CaSync", "Sound", combobox("Sound", "Instant", "Inhibit", "Attack", "Full", "Semi", "None"));

    Setting anti_stuck = create("Anti Stuck", "CaAntiStuck", true);
    Setting anti_stuck_tries = create("Anti Stuck Tries", "CaAntiStuckTries", 5, 1, 15);
    Setting anti_stuck_time = create("Anti Stuck Time", "CaAntiStuckTime", 1000, 0, 20000);
    Setting endcrystal = create("1.13 Mode", "CaThirteen", false);
    Setting multi_place = create("Multi Place", "CaMultiplace", false);

    Setting faceplace_mode = create("Faceplace Mode", "CaTabbottMode", true);
    Setting faceplace_mode_damage = create("Faceplace Health", "CaTabbottModeHealth", 10, 0, 36);

    Setting fuck_armor_mode = create("Armor Destroy", "CaArmorDestroy", true);
    Setting fuck_armor_mode_precent = create("Enemy Armor %", "CaArmorPercent", 5, 0, 100);
    Setting fuck_armor_mode_precent_self = create("Self Armor %", "CaArmorPercentSelf", 0, 0, 100); // retard idea by me

    Setting stop_while_mining = create("Stop While Mining", "CaStopWhileMining", false);
    Setting stop_while_eating = create("Stop While Eating", "CaStopWhileEatin", false);
    Setting faceplace_check = create("No Sword FP", "CaJumpyFaceMode", false);

    Setting swing = create("Swing", "CaSwing", "Mainhand", combobox("Mainhand", "Offhand", "Both", "None"));

    Setting render_mode = create("Render", "CaRenderMode", "Pretty", combobox("Pretty", "Solid", "Outline", "None"));
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

    Setting render_damage = create("Render Damage", "CaRenderDamage", true);

    Setting switch_bind = create("Switch Bind", "CaSwitchBind", 0);
    Setting faceplace_bind = create("Faceplace Bind", "CaFaceBind", 0);

    private final ConcurrentHashMap<EntityEnderCrystal, Integer> attacked_crystals = new ConcurrentHashMap<>();
    public static ArrayList<EntityEnderCrystal> fake_crystals = new ArrayList<>();

    private final TimerUtil remove_visual_timer = new TimerUtil();
    private final TimerUtil break_predict_timer = new TimerUtil();
    private final TimerUtil place_predict_timer = new TimerUtil();

    private EntityPlayer ca_target = null;

    private String detail_name = null;
    private int detail_hp = 0;

    private BlockPos render_block_init;
    private BlockPos render_block_old;

    private double render_damage_value;

    private float yaw;
    private float pitch;

    private boolean already_attacking = false;
    private boolean place_timeout_flag = false;
    private boolean do_switch_bind = false;
    private boolean face_place_bind = false;
    private boolean is_rotating;
    private boolean did_anything;
    private boolean outline;
    private boolean solid;

    private int place_timeout;
    private int break_timeout;
    private int inhibit_delay_counter;
    private int break_delay_counter;
    private int place_delay_counter;
    private int attack_swings;

    @Override
    public void update() {
        if (rotate_mode.in("Off") || rotate_mode.in("Packet")) {
            do_ca();
        }
    }

    public void do_ca() {
        did_anything = false;

        if (mc.player == null || mc.world == null) {
            return;
        }

        if (switch_bind.get_bind("").equalsIgnoreCase("None")) {
            do_switch_bind = false;
        }
        if (faceplace_bind.get_bind("").equalsIgnoreCase("None")) {
            face_place_bind = false;
        }

        if (rainbow_mode.get_value(true)) {
            cycle_rainbow();
        }

        if (remove_visual_timer.passed(anti_stuck_time.get_value(1))) {
            remove_visual_timer.reset();
            attacked_crystals.clear();
        }

        if (check_pause()) {
            return;
        }

        if (sync.in("Full")) {
            do_fake_crystal();
        }

        if (place_crystal.get_value(true) && place_delay_counter > place_timeout) {
            place_crystal();
        }

        if (break_crystal.get_value(true) && break_delay_counter > break_timeout) {
            break_crystal();
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
            ca_target = null;
            is_rotating = false;
        }

        if (ca_target != null) {
            AutoEz.add_target(ca_target.getName());
            detail_name = ca_target.getName();
            detail_hp = Math.round(ca_target.getHealth() + ca_target.getAbsorptionAmount());
        }

        if (attack_swings > 2000) {
            attack_swings = 0;
        }

        if (inhibit_delay_counter > 2000) {
            inhibit_delay_counter = 0;
        }

        render_block_old = render_block_init;

        break_delay_counter++;
        place_delay_counter++;
        inhibit_delay_counter++;
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
        if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && !switch_mode.in("None")) {
                if (switch_bind.get_bind("").equalsIgnoreCase("None") || do_switch_bind) {
                    if (switch_mode.in("Normal")) {
                        mc.player.inventory.currentItem = find_crystals_hotbar();
                    } else {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(find_crystals_hotbar()));
                    }
                    return;
                }
            }
        } else {
            offhand_check = true;
        }

        if (debug.get_value(true)) {
            MessageUtil.send_client_message("placing");
        }

        did_anything = true;
        rotate_to_pos(target_block);
        for (int i = 0; i < place_trys.get_value(1); i++) {
            BlockUtil.placeCrystalOnBlock(target_block, offhand_check ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, packet_place.get_value(true));
        }
        if (sync.in("Semi")) {
            EntityEnderCrystal crystal = new EntityEnderCrystal(mc.world,(double) get_best_block().getX() + 0.5, (double) get_best_block().getY() + 1, (double) get_best_block().getZ() + 0.5);
            mc.world.addEntityToWorld(-101, crystal);
            crystal.setInvisible(true);
            fake_crystals.add(crystal);
        }
    }

    public void break_crystal() {
        EntityEnderCrystal crystal = get_best_crystal();
        if (crystal == null) {
            if (alternative.get_value(true)) {
                place_crystal();
            }
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

        rotate_to(crystal);
        for (int i = 0; i < break_trys.get_value(1); i++) {
            EntityUtil.attackEntity(crystal, packet_break.get_value(true), swing);
            attack_swings++;
        }
        add_attacked_crystal(crystal);

        if (sync.in("Instant") && crystal.isEntityAlive()) {
            crystal.setDead();
        }

        if (fake_crystals.contains(crystal) && sync.in("Semi")) {
            crystal.setDead();
        }

        break_delay_counter = 0;
    }

    // trololol
    public void do_fake_crystal() {
        if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && !switch_mode.in("None")) {
                if (switch_mode.in("Normal")) {
                    mc.player.inventory.currentItem = find_crystals_hotbar();
                } else {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(find_crystals_hotbar()));
                }
                return;
            }
        }
        if (get_best_block() == null) return;
        if (mc.world == null) return;
        if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) return;
        if (debug.get_value(true)) {
            MessageUtil.send_client_message("Doing fake crystal");
        }
        EntityEnderCrystal crystal = new EntityEnderCrystal(mc.world,(double) get_best_block().getX() + 0.5, (double) get_best_block().getY() + 1, (double) get_best_block().getZ() + 0.5);
        mc.world.addEntityToWorld(-101, crystal);
        crystal.setDead();
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            mc.player.getHeldItemOffhand().setCount(mc.player.getHeldItemMainhand().getCount() - 1);
            BlockUtil.swingArm(swing);
        } else if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
            mc.player.getHeldItemMainhand().setCount(mc.player.getHeldItemMainhand().getCount() - 1);
            BlockUtil.swingArm(swing);
        }
    }

    public BlockPos get_best_block() {
        if (get_best_crystal() != null && !fast_mode.get_value(true)) {
            place_timeout_flag = true;
            return null;
        }

        if (place_timeout_flag) {
            place_timeout_flag = false;
            return null;
        }

        double best_damage = 0;
        double minimum_damage;
        double maximum_damage_self = this.max_self_damage.get_value(1);

        BlockPos best_block = null;

        List<BlockPos> blocks_momentum = CrystalUtil.crystalBlocksMomentum(mc.player, place_range.get_value(1), motion_predict.get_value(true), motion_predict_factor.get_value(1), !multi_place.get_value(true), endcrystal.get_value(true));
        List<BlockPos> blocks = CrystalUtil.possiblePlacePositions(place_range.get_value(1), endcrystal.get_value(true), !multi_place.get_value(true));

        for (Entity player : mc.world.playerEntities) {

            if (target_mode.in("Health")) {
                if (FriendUtil.isFriend(player.getName())) continue;
            }

            for (BlockPos block : momentum.get_value(true) ? blocks_momentum : blocks) {

                if (player.getDistance(mc.player) >= player_range.get_value(1)) continue;

                if (player == mc.player || !(player instanceof EntityPlayer)) continue;

                if (!BlockUtil.rayTracePlaceCheck(block, this.raytrace.get_value(true))) {
                    continue;
                }

                if (verify_place.get_value(true) && mc.player.getDistanceSq(block) > Math.pow(hit_range.get_value(1), 2))
                    continue;

                if (!BlockUtil.canSeeBlock(block) && mc.player.getDistance(block.getX(), block.getY(), block.getZ()) > hit_range_wall.get_value(1)) {
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
                if ((target.getHealth() < faceplace_mode_damage.get_value(1) && faceplace_mode.get_value(true)&& !no_place) || (get_armor_fucker(target) && !no_place && !get_armor_fucker(mc.player))) {
                    minimum_damage = 2;
                } else {
                    minimum_damage = this.min_player_place.get_value(1);
                }

                final double target_damage = CrystalUtil.calculateDamage((double) block.getX() + 0.5, (double) block.getY() + 1, (double) block.getZ() + 0.5, target);

                if (target_damage < minimum_damage) continue;

                final double self_damage = CrystalUtil.calculateDamage((double) block.getX() + 0.5, (double) block.getY() + 1, (double) block.getZ() + 0.5, mc.player);

                if (self_damage > maximum_damage_self || (anti_suicide.get_value(true) && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) - self_damage <= 0.5)) continue;

                if (target_damage > best_damage) {
                    best_damage = target_damage;
                    best_block = block;
                    ca_target = target;
                }
            }
        }

        if (!momentum.get_value(true)) {
            blocks.clear();
        }


        render_damage_value = best_damage;
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

            if (attacked_crystals.containsKey(crystal) && attacked_crystals.get(crystal) > anti_stuck_tries.get_value(1) && anti_stuck.get_value(true)) continue;

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
                if ((target.getHealth() < faceplace_mode_damage.get_value(1) && faceplace_mode.get_value(true)&& !no_place) || (get_armor_fucker(target) && !no_place && !get_armor_fucker(mc.player)) || (face_place_bind)) {
                    minimum_damage = 2;
                } else {
                    minimum_damage = this.min_player_break.get_value(1);
                }

                final double target_damage = CrystalUtil.calculateDamage(crystal, target);

                if (target_damage < minimum_damage) continue;

                final double self_damage = CrystalUtil.calculateDamage(crystal, mc.player);

                if (self_damage > maximum_damage_self || (anti_suicide.get_value(true) && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) - self_damage <= 0.5)) continue;

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
                if (fuck_armor_mode.get_value(true) && fuck_armor_mode_precent_self.get_value(1) >= armor_percent) return true;
            } else {
                if (fuck_armor_mode.get_value(true) && fuck_armor_mode_precent.get_value(1) >= armor_percent) return true;
            }
        }
        return false;
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
                    if (old_render.get_value(true)) {
                        render_block_init = null;
                    }
                    if (sync.in("Inhibit") && get_best_crystal() != null) {
                        get_best_crystal().setDead();
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

        if (min_health_pause.get_value(true) && (mc.player.getHealth()+mc.player.getAbsorptionAmount()) < required_health.get_value(1)) {
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

    public EntityPlayer get_closest_target()  {
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

            if (target.getDistance(mc.player) >= player_range.get_value(1)) continue;

            if (closestTarget != null)
                if (mc.player.getDistance(target) > mc.player.getDistance(closestTarget))
                    continue;

            closestTarget = target;
        }

        return closestTarget;
    }

    @Override
    public void render(EventRender event) {
        if (render_block_init == null) return;

        if (render_mode.in("None")) return;

        if (render_mode.in("Pretty")) {
            outline = true;
            solid = true;
        }

        if (render_mode.in("Solid")) {
            outline = false;
            solid = true;
        }

        if (render_mode.in("Outline")) {
            outline = true;
            solid = false;
        }

        render_block(render_block_init);

        if (future_render.get_value(true) && render_block_old != null) {
            render_block(render_block_old);
        }

        if (render_damage.get_value(true)) {
            RenderUtil.drawText(render_block_init, ((Math.floor(this.render_damage_value) == this.render_damage_value) ? Integer.valueOf((int)this.render_damage_value) : String.format("%.1f", this.render_damage_value)) + "");
        }

    }

    public void render_block(BlockPos pos) {
        BlockPos render_block = (top_block.get_value(true) ? pos.up() : pos);

        float h = (float) height.get_value(1.0);

        if (solid) {
            RenderHelp.prepare("quads");
            RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
                    render_block.getX(), render_block.getY(), render_block.getZ(),
                    1, h, 1,
                    r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1),
                    "all"
            );
            RenderHelp.release();
        }

        if (outline) {
            RenderHelp.prepare("lines");
            RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
                    render_block.getX(), render_block.getY(), render_block.getZ(),
                    1, h, 1,
                    r.get_value(1), g.get_value(1), b.get_value(1), a_out.get_value(1),
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
    private final Listener<EventMotionUpdate> on_movement = new Listener<>(event -> {
        if (event.stage == 0 && (rotate_mode.in("Seizure") || rotate_mode.in("Const"))) {
            if (debug.get_value(true)) {
                MessageUtil.send_client_message("updating rotation");
            }
            PosUtil.updatePosition();
            RotationUtil.updateRotations();
            do_ca();
        }
        if (event.stage == 1 && (rotate_mode.in("Seizure") || rotate_mode.in("Const"))) {
            if (debug.get_value(true)) {
                MessageUtil.send_client_message("resetting rotation");
            }
            PosUtil.restorePosition();
            RotationUtil.restoreRotations();
        }
    });

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> receive_listener = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.get_packet();

            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity e : mc.world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal) {
                        if (e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0f && sync.in("Sound")) {
                            e.setDead();
                        }
                    }
                }
            }
        }

        if (event.get_packet() instanceof SPacketSpawnObject) {
            final SPacketSpawnObject packet = (SPacketSpawnObject) event.get_packet();
            if (debug.get_value(true)) {
                if (packet.getType() == 51) {
                    MessageUtil.send_client_message("Entity ID: " + packet.getEntityID()); // I was just testng something here
                }
            }
            if (packet.getType() == 51 && this.ca_target != null && break_predict.get_value(true) && break_predict_timer.passed(break_predict_factor.get_value(1))) {
                break_predict_timer.reset();
                if (!this.is_predicting_crystal(packet)) {
                    return;
                }
                if (debug.get_value(true)) {
                    MessageUtil.send_client_message("break predicting");
                }
                CPacketUseEntity predict = new CPacketUseEntity();
                predict.entityId = packet.getEntityID();
                predict.action = CPacketUseEntity.Action.ATTACK;
                AutoCrystal.mc.player.connection.sendPacket(predict);
            }
        }
    });

    @EventHandler
    private final Listener<EventPacket.SendPacket> send_listener = new Listener<>(event -> {
        if (event.get_packet() instanceof CPacketPlayer && is_rotating && rotate_mode.in("Packet")) {
            if (debug.get_value(true)) {
                MessageUtil.send_client_message("Rotating");
            }
            final CPacketPlayer p = (CPacketPlayer) event.get_packet();
            p.yaw = yaw;
            p.pitch = pitch;
            is_rotating = false;
        }
        if (event.get_packet() instanceof CPacketUseEntity && ((CPacketUseEntity) event.get_packet()).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity) event.get_packet()).getEntityFromWorld(mc.world) instanceof EntityEnderCrystal) {
            if (sync.in("Attack"))
                Objects.requireNonNull(((CPacketUseEntity) event.get_packet()).getEntityFromWorld(mc.world)).setDead();
        }
        if (event.get_packet() instanceof CPacketUseEntity && ((CPacketUseEntity) event.get_packet()).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity) event.get_packet()).getEntityFromWorld(mc.world) instanceof EntityEnderCrystal) {
            EntityEnderCrystal predicted_crystal = (EntityEnderCrystal) ((CPacketUseEntity) event.get_packet()).getEntityFromWorld(mc.world);
            if (predicted_crystal != null && place_predict_timer.passed(place_predict_factor.get_value(1)) && place_predict.get_value(true) && ca_target != null) {
                place_predict_timer.reset();
                if (is_predicting_block(predicted_crystal)) {
                    boolean offhand_check = false;
                    if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                        if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && !switch_mode.in("None") && !place_crystal.get_value(true)) {
                            if (switch_bind.get_bind("").equalsIgnoreCase("None") || do_switch_bind) {
                                if (switch_mode.in("Normal")) {
                                    mc.player.inventory.currentItem = find_crystals_hotbar();
                                } else {
                                    mc.player.connection.sendPacket(new CPacketHeldItemChange(find_crystals_hotbar()));
                                }
                                return;
                            }
                        }
                    } else {
                        offhand_check = true;
                    }
                    if (debug.get_value(true)) {
                        MessageUtil.send_client_message("Place predicting");
                    }
                    BlockUtil.placeCrystalOnBlock(predicted_crystal.getPosition().down(), offhand_check ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, packet_place.get_value(true));
                }
            }
        }
    });

    private boolean is_predicting_crystal(SPacketSpawnObject packet) {
        BlockPos packPos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
        if (AutoCrystal.mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) > (double) this.hit_range.get_value(1)) {
            return false;
        }
        if (!BlockUtil.canSeeBlock(packPos) && AutoCrystal.mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) > (double) this.hit_range_wall.get_value(1)) {
            return false;
        }
        double targetDmg = CrystalUtil.calculateDamage(packet.getX() + 0.5, packet.getY() + 1.0, packet.getZ() + 0.5, this.ca_target);
        if (EntityUtil.isInHole(AutoCrystal.mc.player) && targetDmg >= 1.0) {
            return true;
        }
        double selfDmg = CrystalUtil.calculateDamage(packet.getX() + 0.5, packet.getY() + 1.0, packet.getZ() + 0.5, AutoCrystal.mc.player);
        double d = anti_suicide.get_value(true) ? 2.0 : 0.5;
        if (get_armor_fucker(ca_target) && !get_armor_fucker(mc.player)) {
            return true;
        }
        if (selfDmg + d < (double) (AutoCrystal.mc.player.getHealth() + AutoCrystal.mc.player.getAbsorptionAmount()) && targetDmg >= (double) (this.ca_target.getAbsorptionAmount() + this.ca_target.getHealth())) {
            return true;
        }
        if (targetDmg >= (double) this.min_player_break.get_value(1) && selfDmg <= (double) this.max_self_damage.get_value(1)) {
            return true;
        }
        return faceplace_mode.get_value(true) && EntityUtil.isInHole(this.ca_target) && this.ca_target.getHealth() + this.ca_target.getAbsorptionAmount() <= this.faceplace_mode.get_value(1);
    }

    private boolean is_predicting_block(EntityEnderCrystal crystal) {
        BlockPos packPos = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
        if (AutoCrystal.mc.player.getDistance(crystal.posX, crystal.posY, crystal.posZ) > (double) this.hit_range.get_value(1)) {
            return false;
        }
        if (!BlockUtil.canSeeBlock(packPos) && AutoCrystal.mc.player.getDistance(crystal.posX, crystal.posY, crystal.posZ) > (double) this.hit_range_wall.get_value(1)) {
            return false;
        }
        double targetDmg = CrystalUtil.calculateDamage(crystal.posX + 0.5, crystal.posY + 1.0, crystal.posZ + 0.5, this.ca_target);
        if (EntityUtil.isInHole(AutoCrystal.mc.player) && targetDmg >= 1.0) {
            return true;
        }
        double selfDmg = CrystalUtil.calculateDamage(crystal.posX + 0.5, crystal.posY + 1.0, crystal.posZ + 0.5, AutoCrystal.mc.player);
        double d = anti_suicide.get_value(true) ? 2.0 : 0.5;
        if (get_armor_fucker(ca_target) && !get_armor_fucker(mc.player)) {
            return true;
        }
        if (selfDmg + d < (double) (AutoCrystal.mc.player.getHealth() + AutoCrystal.mc.player.getAbsorptionAmount()) && targetDmg >= (double) (this.ca_target.getAbsorptionAmount() + this.ca_target.getHealth())) {
            return true;
        }
        if (targetDmg >= (double) this.min_player_break.get_value(1) && selfDmg <= (double) this.max_self_damage.get_value(1)) {
            return true;
        }
        return faceplace_mode.get_value(true) && EntityUtil.isInHole(this.ca_target) && this.ca_target.getHealth() + this.ca_target.getAbsorptionAmount() <= this.faceplace_mode.get_value(1);
    }

    public void rotate_to(final Entity entity) {
        final float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
        if (rotate_mode.in("Off")) {
            is_rotating = false;
        }
        if (rotate_mode.in("Seizure")) {
            RotationUtil.setPlayerRotations(angle[0], angle[1]);
        }
        if (rotate_mode.in("Packet") || rotate_mode.in("Const")) { //im p sure travis made a typo here, it said cons, might be him wanting it to not do that
            yaw = angle[0];
            pitch = angle[1];
            is_rotating = true;
        }
    }

    public void rotate_to_pos(final BlockPos pos) {
        final float[] angle;
        if (rotate_mode.in("Const")) {
            angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f));
        } else {
            angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d(pos.getX() + 0.5f, pos.getY() - 0.5f, pos.getZ() + 0.5f));
        }
        if (rotate_mode.in("Off")) {
            is_rotating = false;
        }
        if (rotate_mode.in("Seizure") || rotate_mode.in("Const")) {
            RotationUtil.setPlayerRotations(angle[0], angle[1]);
        }
        if (rotate_mode.in("Packet")) {
            yaw = angle[0];
            pitch = angle[1];
            is_rotating = true;
        }
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
        if (attacked_crystals.containsKey(crystal)) {
            int value = attacked_crystals.get(crystal);
            attacked_crystals.put(crystal, value + 1);
        } else {
            attacked_crystals.put(crystal, 1);
        }
    }

    @Override
    public void enable() {
        place_timeout = this.place_delay.get_value(1);
        break_timeout = this.break_delay.get_value(1);
        inhibit_delay_counter = 0;
        attack_swings = 0;
        place_timeout_flag = false;
        is_rotating = false;
        ca_target = null;
        remove_visual_timer.reset();
        break_predict_timer.reset();
        place_predict_timer.reset();
        detail_name = null;
        detail_hp = 20;
    }

    @Override
    public void disable() {
        render_block_init = null;
        ca_target = null;
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
    public String array_detail() {
        return (detail_name != null) ? detail_name + " | " + detail_hp : "None";
    }

}
