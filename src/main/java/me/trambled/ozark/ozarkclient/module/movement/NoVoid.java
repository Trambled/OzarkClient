package me.trambled.ozark.ozarkclient.module.movement;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import me.trambled.ozark.ozarkclient.util.WrapperUtil;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.guiscreen.settings.Setting;
import me.tranbled.ozark.ozarkclient.module.Module;

// listedhack again, thx listed 

public class NoVoid extends Module {
    Setting height;
    
    public NoVoid() {
        super(Category.MOVEMENT);
        this.height = this.create("Height", "Height", 0, 0, 256);
        this.name = "NoVoid";
        this.tag = "NoVoid";
        this.description = "avoids getting voided";
    }
    
    @Override
    public void update() {
        if (Wrapper.mc.world != null) {
            if (Wrapper.mc.player.noClip || Wrapper.mc.player.posY > this.height.get_value(1)) {
                return;
            }
            final RayTraceResult trace = Wrapper.mc.world.rayTraceBlocks(Wrapper.mc.player.getPositionVector(), new Vec3d(Wrapper.mc.player.posX, 0.0, Wrapper.mc.player.posZ), false, false, false);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                return;
            }
            Wrapper.mc.player.setVelocity(0.0, 0.0, 0.0);
        }
    }
}
