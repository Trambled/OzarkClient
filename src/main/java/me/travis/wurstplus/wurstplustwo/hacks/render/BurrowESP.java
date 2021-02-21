package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.turok.draw.RenderHelp;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusCrystalUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusEntityUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusFriendUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

//modified from fucked detector
public class BurrowESP extends WurstplusHack {
    
    public BurrowESP() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        this.name = "BurrowESP";
        this.tag = "BurrowESP";
        this.description = "see if people are burrowed";
    }

    WurstplusSetting draw_own = create("Draw Own", "BurrowDrawOwn", false);
    WurstplusSetting draw_friends = create("Draw Friends", "BurrowDrawFriends", false);

    WurstplusSetting render_mode = create("Render Mode", "BurrowRenderMode", "Pretty", combobox("Pretty", "Solid", "Outline"));
    WurstplusSetting r = create("R", "BurrowR", 255, 0, 255);
	WurstplusSetting g = create("G", "BurrowG", 255, 0, 255);
	WurstplusSetting b = create("B", "BurrowB", 255, 0, 255);
    WurstplusSetting a = create("A", "BurrowA", 100, 0, 255);

    private boolean solid;
    private boolean outline;

    public Set<BlockPos> burrowed_players = new HashSet<BlockPos>();

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

            if (!WurstplusEntityUtil.isLiving(player) || player.getHealth() <= 0) continue;

            if (is_burrowed(player)) {

                if (WurstplusFriendUtil.isFriend(player.getName()) && !draw_friends.get_value(true)) continue;
                if (player == mc.player && !draw_own.get_value(true)) continue;

                burrowed_players.add(new BlockPos(player.posX, player.posY, player.posZ));

            }

        }

    }

    public boolean is_burrowed(EntityPlayer player) {

        BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);

        if (mc.world.getBlockState(pos).getBlock().equals(Blocks.OBSIDIAN)) {
            return true;
        } 

        return false;

    }

    @Override
	public void render(WurstplusEventRender event) {

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
                        r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1),
                        "all"
                );
                RenderHelp.release();
            }        

        }
        
    }

}