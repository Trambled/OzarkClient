package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.event.events.EventBlockGetRenderLayer;
import me.trambled.ozark.ozarkclient.event.events.EventRenderPutColorMultiplier;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

//combination of xulu & summit
public class Xray extends Module {

    public Xray() {
        super(Category.RENDER);

        this.name        = "Xray";
        this.tag         = "Xray";
        this.description = "Allows you to see ores.";
        initblocks();
    }

    Setting opacity = create("Opacity", "XrayOpacity", 128, 0, 255);
    Setting smooth_reload = create("Smooth Reload", "XraySmoothReload", false);

    private static final ArrayList<Block> BLOCKS;

    public static void initblocks() {
        Xray.BLOCKS.add(Block.getBlockFromName("coal_ore"));
        Xray.BLOCKS.add(Block.getBlockFromName("iron_ore"));
        Xray.BLOCKS.add(Block.getBlockFromName("gold_ore"));
        Xray.BLOCKS.add(Block.getBlockFromName("redstone_ore"));
        Xray.BLOCKS.add(Block.getBlockById(74));
        Xray.BLOCKS.add(Block.getBlockFromName("lapis_ore"));
        Xray.BLOCKS.add(Block.getBlockFromName("diamond_ore"));
        Xray.BLOCKS.add(Block.getBlockFromName("emerald_ore"));
        Xray.BLOCKS.add(Block.getBlockFromName("quartz_ore"));
        Xray.BLOCKS.add(Block.getBlockFromName("clay"));
        Xray.BLOCKS.add(Block.getBlockFromName("glowstone"));
        Xray.BLOCKS.add(Block.getBlockById(8));
        Xray.BLOCKS.add(Block.getBlockById(9));
        Xray.BLOCKS.add(Block.getBlockById(10));
        Xray.BLOCKS.add(Block.getBlockById(11));
        Xray.BLOCKS.add(Block.getBlockFromName("crafting_table"));
        Xray.BLOCKS.add(Block.getBlockById(61));
        Xray.BLOCKS.add(Block.getBlockById(62));
        Xray.BLOCKS.add(Block.getBlockFromName("torch"));
        Xray.BLOCKS.add(Block.getBlockFromName("ladder"));
        Xray.BLOCKS.add(Block.getBlockFromName("tnt"));
        Xray.BLOCKS.add(Block.getBlockFromName("coal_block"));
        Xray.BLOCKS.add(Block.getBlockFromName("iron_block"));
        Xray.BLOCKS.add(Block.getBlockFromName("gold_block"));
        Xray.BLOCKS.add(Block.getBlockFromName("diamond_block"));
        Xray.BLOCKS.add(Block.getBlockFromName("emerald_block"));
        Xray.BLOCKS.add(Block.getBlockFromName("redstone_block"));
        Xray.BLOCKS.add(Block.getBlockFromName("lapis_block"));
        Xray.BLOCKS.add(Block.getBlockFromName("fire"));
        Xray.BLOCKS.add(Block.getBlockFromName("mossy_cobblestone"));
        Xray.BLOCKS.add(Block.getBlockFromName("mob_spawner"));
        Xray.BLOCKS.add(Block.getBlockFromName("end_portal_frame"));
        Xray.BLOCKS.add(Block.getBlockFromName("enchanting_table"));
        Xray.BLOCKS.add(Block.getBlockFromName("bookshelf"));
        Xray.BLOCKS.add(Block.getBlockFromName("command_block"));
    }

    public static ArrayList<Block> getBLOCKS() {
        return Xray.BLOCKS;
    }

    public static boolean shouldXray(final Block block) {
        return Xray.BLOCKS.contains(block);
    }

    public static boolean addBlock(final String string) {
        if (Block.getBlockFromName(string) != null) {
            Xray.BLOCKS.add(Block.getBlockFromName(string));
            mc.renderGlobal.loadRenderers();
            return true;
        }
        return false;
    }

    public static boolean delBlock(final String string) {
        if (Block.getBlockFromName(string) != null) {
            Xray.BLOCKS.remove(Block.getBlockFromName(string));
            mc.renderGlobal.loadRenderers();
            return true;
        }
        return false;
    }

    @EventHandler
    private final Listener<EventRenderPutColorMultiplier> OnPutColorMultiplier = new Listener<>( event ->
    {
        event.cancel();
        event.setOpacity((float) opacity.get_value(1) / 0xFF);
    });
    @EventHandler
    private final Listener<EventBlockGetRenderLayer> OnGetRenderLayer  = new Listener<>( event ->
    {
        if (!shouldXray(event.getBlock()))
        {
            event.cancel();
            event.setLayer(BlockRenderLayer.TRANSLUCENT);
        }
    });

    @Override
    protected void enable() {
        reloadWorld();
    }

    public static void processShouldSideBeRendered(Block block, IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> callback)
    {
        if (shouldXray(block))
            callback.setReturnValue(true);
    }

    public static void processGetLightValue(Block block, CallbackInfoReturnable<Integer> callback)
    {
        if (shouldXray(block))
        {
            callback.setReturnValue(1);
        }
    }

    @Override
    protected void disable() {
        reloadWorld();
    }

    @Override
    public void value_change(String tag) {
        if (tag.equals("XrayOpacity")) {
            reloadWorld();
        }
    }

    static {
        BLOCKS = new ArrayList <> ( );
    }

    private void reloadWorld()
    {
        if (mc.world == null || mc.renderGlobal == null)
            return;

        if (smooth_reload.get_value(true))
        {
            mc.addScheduledTask(() ->
            {
                int x = (int) mc.player.posX;
                int y = (int) mc.player.posY;
                int z = (int) mc.player.posZ;

                int distance = mc.gameSettings.renderDistanceChunks * 16;

                mc.renderGlobal.markBlockRangeForRenderUpdate(x - distance, y - distance, z - distance, x + distance, y + distance, z + distance);
            });
        }
        else
            mc.renderGlobal.loadRenderers();
    }

    @Override
    public String array_detail() {
        if (smooth_reload.get_value(true )) {
            return "Smooth";
        }else{
            return "Hard";

        }
    }}