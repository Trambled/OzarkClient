package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.player.EntityUtil;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import me.trambled.turok.draw.RenderHelp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

//modified from fucked detector
public class BurrowESP extends Module {
    
    public BurrowESP() {
        super(Category.RENDER);

        this.name = "BurrowESP";
        this.tag = "BurrowESP";
        this.description = "See if people are burrowed.";
    }

    Setting draw_own = create("Draw Own", "BurrowDrawOwn", false);
    Setting draw_friends = create("Draw Friends", "BurrowDrawFriends", true);
    Setting echest = create("Echest", "BurrowESPEchest", true);

    Setting render_mode = create("Render Mode", "BurrowRenderMode", "Pretty", combobox("Pretty", "Solid", "Outline"));
    Setting r = create("R", "BurrowR", 255, 0, 255);
    Setting g = create("G", "BurrowG", 255, 0, 255);
    Setting b = create("B", "BurrowB", 255, 0, 255);
    Setting a = create("A", "BurrowA", 100, 0, 255);

    private boolean solid;
    private boolean outline;

    public Set<BlockPos> burrowed_players = new HashSet <> ( );

    @Override
    protected void enable() {
        burrowed_players.clear();
    }

    @Override
    public void update() {
        if (mc.world == null) return;
        set_burrowed_players();
    }

    public void set_burrowed_players() {

        burrowed_players.clear();

        for (EntityPlayer player : mc.world.playerEntities) {

            if (!EntityUtil.isLiving(player) || player.getHealth() <= 0) continue;

            if (is_burrowed(player)) {

                if (FriendUtil.isFriend(player.getName()) && !draw_friends.get_value(true)) continue;
                if (player == mc.player && !draw_own.get_value(true)) continue;

                burrowed_players.add(new BlockPos(player.posX, player.posY, player.posZ));

            }

        }

    }

    public boolean is_burrowed(EntityPlayer player) {

        //we need to add the 0.2 to prevent it from false flagging shifting
        BlockPos pos = new BlockPos(player.posX, player.posY + 0.2, player.posZ);

        if (mc.world.getBlockState(pos).getBlock().equals(Blocks.OBSIDIAN)) {
              return true;
        }

        return mc.world.getBlockState ( pos ).getBlock ( ).equals ( Blocks.ENDER_CHEST ) && echest.get_value ( true );

    }

    @Override
    public void render(EventRender event) {

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

        for (BlockPos render_block : burrowed_players) {

            if (render_block == null) return;

            if (solid) {
                RenderHelp.prepare("quads");
                RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
                        render_block.getX(), render_block.getY(), render_block.getZ(),
                        1, 1, 1,
                        r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1),
                        "all"
                );
                RenderHelp.release();
            }        
    
            if (outline) {
                RenderHelp.prepare("lines");
                RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
                        render_block.getX(), render_block.getY(), render_block.getZ(),
                        1, 1, 1,
                        r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1),1,
                        "all"
                );
                RenderHelp.release();
            }        

        }
        
    }

}
