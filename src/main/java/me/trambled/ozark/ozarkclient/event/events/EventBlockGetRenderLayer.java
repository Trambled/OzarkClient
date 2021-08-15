package me.trambled.ozark.ozarkclient.event.events;

import me.trambled.ozark.ozarkclient.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.BlockRenderLayer;

public class EventBlockGetRenderLayer extends Event {
    private BlockRenderLayer _layer;
    private final Block _block;

    public EventBlockGetRenderLayer(Block block) {
        _block = block;
    }

    public Block getBlock() {
        return _block;
    }

    public void setLayer(BlockRenderLayer layer) {
        _layer = layer;
    }

    public BlockRenderLayer getBlockRenderLayer() {
        return _layer;
    }
}
