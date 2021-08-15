package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.player.EntityUtil;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import me.trambled.ozark.ozarkclient.util.world.CrystalUtil;
import me.trambled.turok.draw.RenderHelp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class FuckedDetector extends Module {
    
    public FuckedDetector() {
        super(Category.RENDER);

        this.name = "FuckedDetector";
        this.tag = "FuckedDetector";
        this.description = "See if people are hecked.";
    }

    Setting draw_own = create("Draw Own", "FuckedDrawOwn", false);
    Setting draw_friends = create("Draw Friends", "FuckedDrawFriends", false);

    Setting render_mode = create("Render Mode", "FuckedRenderMode", "Pretty", combobox("Pretty", "Solid", "Outline"));
    Setting r = create("R", "FuckedR", 255, 0, 255);
    Setting g = create("G", "FuckedG", 255, 0, 255);
    Setting b = create("B", "FuckedB", 255, 0, 255);
    Setting a = create("A", "FuckedA", 100, 0, 255);

    private boolean solid;
    private boolean outline;

    public Set<BlockPos> fucked_players = new HashSet<>();

    @Override
    protected void enable() {
        fucked_players.clear();
    }

    @Override
    public void update() {
        if (mc.world == null) return;
        set_fucked_players();
    }

    public void set_fucked_players() {

        fucked_players.clear();

        for (EntityPlayer player : mc.world.playerEntities) {

            if (!EntityUtil.isLiving(player) || player.getHealth() <= 0) continue;
            // if (!player.onGround) continue;

            if (is_fucked(player)) {

                if (FriendUtil.isFriend(player.getName()) && !draw_friends.get_value(true)) continue;
                if (player == mc.player && !draw_own.get_value(true)) continue;

                fucked_players.add(new BlockPos(player.posX, player.posY, player.posZ));

            }

        }

    }

    public boolean is_fucked(EntityPlayer player) {

        BlockPos pos = new BlockPos(player.posX, player.posY - 1, player.posZ);
        BlockPos player_pos = new BlockPos(player.posX, player.posY, player.posZ);
	    
	if (mc.world.getBlockState(player_pos).getBlock().equals(Blocks.OBSIDIAN)) {
            return false;
        }

        if (CrystalUtil.canPlaceCrystal(pos.south()) || (CrystalUtil.canPlaceCrystal(pos.south().south()) && mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR)) {
            return true;
        }
            
        if (CrystalUtil.canPlaceCrystal(pos.east()) || (CrystalUtil.canPlaceCrystal(pos.east().east()) && mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR)) {
            return true;
        } 
            
        if (CrystalUtil.canPlaceCrystal(pos.west()) || (CrystalUtil.canPlaceCrystal(pos.west().west()) && mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR)) {
            return true;
        }

        return CrystalUtil.canPlaceCrystal ( pos.north ( ) ) || ( CrystalUtil.canPlaceCrystal ( pos.north ( ).north ( ) ) && mc.world.getBlockState ( pos.add ( 0 , 1 , - 1 ) ).getBlock ( ) == Blocks.AIR );

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

        for (BlockPos render_block : fucked_players) {

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
