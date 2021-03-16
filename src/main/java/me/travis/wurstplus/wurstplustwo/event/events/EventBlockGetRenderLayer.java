package me.travis.wurstplus.wurstplustwo.event.events;

import me.travis.wurstplus.wurstplustwo.event.WurstplusEventCancellable;

import net.minecraft.block.Block;
import net.minecraft.util.BlockRenderLayer;

public class EventBlockGetRenderLayer extends WurstplusEventCancellable {
    private BlockRenderLayer _layer;
    private Block _block;

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
