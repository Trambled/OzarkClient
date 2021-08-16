package me.trambled.ozark.ozarkclient.module.combat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.misc.MessageUtil;
import me.trambled.ozark.ozarkclient.util.player.EntityUtil;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import me.trambled.ozark.ozarkclient.util.world.BreakUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class AutoMine extends Module {

    public AutoMine() {
        super(Category.COMBAT);

        this.name        = "AutoCity";
        this.tag         = "CityBoss";
        this.description = "Mines out player's surround.";
    }

    Setting end_crystal = create("End Crystal", "MineEndCrystal", false);
    Setting range = create("Range", "MineRange", 4, 0, 6);

    @Override
    protected void enable() {

        BlockPos target_block = null;

        for (EntityPlayer player : mc.world.playerEntities) {
            if (mc.player.getDistance(player) > range.get_value(1)) continue;

            if (FriendUtil.isFriend(player.getName())) continue;

            BlockPos p = EntityUtil.is_cityable(player, end_crystal.get_value(true));

            if (p != null) {
                target_block = p;
            }
        }

        if (target_block == null) {
            MessageUtil.send_client_message("cannot find block");
            this.set_disable();
        }

        BreakUtil.set_current_block(target_block);

    }

    @Override
    protected void disable() {
        BreakUtil.set_current_block(null);
    }
}
