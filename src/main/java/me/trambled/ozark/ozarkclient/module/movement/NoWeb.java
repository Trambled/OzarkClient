package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.material.Material;

//KAMBING MADE THIS COPYRIGHT CODENAME_KAMBING#0001 ON DISCORD NO SKIDDING OR GAY I CAN SUE UR MOM (listed google this code!! i made this ok?!??!)
public class NoWeb extends Module {

    public NoWeb() {
        super(Category.MOVEMENT);

        this.name = "NoWeb";
        this.tag = "NoWeb";
        this.description = "kambing first original module (i swear)";
    }

    Setting speed = create("Speed", "TimerSpeed", 4f, 0.1f, 10f);
    Setting notify = create("Notify", "Notify",false);

    @Override
    public void update() {
        if (mc.world.isMaterialInBB(mc.player.getEntityBoundingBox().grow(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.WEB)) {
            mc.timer.tickLength = 50.0f / ((speed.get_value(1) == 0f) ? 0.1f : speed.get_value(1));
            if (notify.get_value(true)) {
                MessageUtil.send_client_message("You're in a web!");
            }
        }
    }
    @Override
    protected void disable () {
        mc.timer.tickLength = 50.0f;
    }

}



//ok maybe idk how to make methods but i have creativities :rolling_eyes:
//yes idk java but i have ideas okay listed dont clown me lets be friend