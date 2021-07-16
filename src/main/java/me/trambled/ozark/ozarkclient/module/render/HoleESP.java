package me.trambled.ozark.ozarkclient.module.render;


import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import com.google.common.collect.Sets;
import me.trambled.ozark.ozarkclient.util.player.EntityUtil;
import me.trambled.ozark.ozarkclient.util.player.PlayerUtil;
import me.trambled.ozark.ozarkclient.util.render.OzarkColor;
import me.trambled.ozark.ozarkclient.util.render.RenderUtil2;
import me.trambled.ozark.ozarkclient.util.world.GeometryMasks;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import me.trambled.ozark.ozarkclient.util.world.HoleUtil;
import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class HoleESP extends Module {
	public HoleESP() {
		super(Category.RENDER);

		this.name = "HoleESP";
		this.tag = "HoleESP";
		this.description = "lets you know where holes are";
	}
	Setting range = create("Range", "HoleESPRange", 6, 1, 12);

	Setting customHoles = create("Show", "Holeshow", "Double", combobox("Single", "Double", "Custom"));
	Setting type = create("Type", "holetype", "Both", combobox("Outline", "Fill", "Both"));
	Setting mode = create("Mode", "holemode", "Air", combobox("Air", "Flat", "Slab", "Double"));

	Setting flat_own = create("Hide Own", "HoleESPHideOwn", true);
	Setting hide_own = create("Hide Own", "HoleESPHideOwn", true);

	Setting toggleb = create("Bedrock Colors Setting", "HoleESPBSET", false);
	Setting rb = create("Bedrock Red", "HoleESPRb", 0, 0, 255);
	Setting gb = create("Bedrock Green", "HoleESPGb", 255, 0, 255);
	Setting bb = create("Bedrock Blue", "HoleESPBb", 0, 0, 255);
	Setting ab = create("Bedrock Alpha", "HoleESPAb", 50, 0, 255);

	Setting toggleo = create("Obsidian Colors Setting", "HoleESPoSET", false);
	Setting ro = create("Obby Red", "HoleESPRo", 255, 0, 255);
	Setting go = create("Obby Green", "HoleESPGo", 0, 0, 255);
	Setting bo = create("Obby Blue", "HoleESPBo", 0, 0, 255);
	Setting ao = create("Obby Alpha", "HoleESPAo", 50, 0, 255);

	Setting togglec = create("Custom Colors Setting", "HoleESPcSET", false);
	Setting rc = create("Custom Red", "HoleESPRc", 255, 0, 255);
	Setting gc = create("Custom Green", "HoleESPGc", 0, 0, 255);
	Setting bc = create("Custom Blue", "HoleESPBc", 0, 0, 255);
	Setting ac = create("Custom Alpha", "HoleESPAc", 50, 0, 255);

	Setting width = create("Width", "HoleESPWidth", 1, 1, 10);
	Setting slabHeight = create("Slab Height", "HoleESPSlabHeight", 0.5, 0.1, 1);
	Setting rainbow_ob = create("Rainbow Obsidian", "HoleESPRainbowOb", true);
	Setting rainbow_bed = create("Rainbow Bedrock", "HoleESPRainbowBed", true);
	Setting sat = create("Satiation", "HoleESPSatiation", 0.8, 0, 1);
	Setting brightness = create("Brightness", "HoleESPBrightness", 0.8, 0, 1);
	Setting ufoAlpha = create("FadeAlpha", "HoleespfadeA", 50, 0, 255);

	private ConcurrentHashMap<AxisAlignedBB, OzarkColor> holes;

	public void update() {
		if (mc.player == null || mc.world == null) {
			return;
		}

		if (holes == null) {
			holes = new ConcurrentHashMap<>();
		} else {
			holes.clear();
		}

		int range = (int) Math.ceil(this.range.get_value(1));

		HashSet<BlockPos> possibleHoles = Sets.newHashSet();
		List<BlockPos> blockPosList = EntityUtil.getSphere(PlayerUtil.getPlayerPos(), range, range, false, true, 0);

		for (BlockPos pos : blockPosList) {

			if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
				continue;
			}

			if (mc.world.getBlockState(pos.add(0, -1, 0)).getBlock().equals(Blocks.AIR)) {
				continue;
			}
			if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
				continue;
			}

			if (mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
				possibleHoles.add(pos);
			}
		}

		possibleHoles.forEach(pos -> {
			HoleUtil.HoleInfo holeInfo = HoleUtil.isHole(pos, false, false);
			HoleUtil.HoleType holeType = holeInfo.getType();
			if (holeType != HoleUtil.HoleType.NONE) {

				HoleUtil.BlockSafety holeSafety = holeInfo.getSafety();
				AxisAlignedBB centreBlocks = holeInfo.getCentre();

				if (centreBlocks == null)
					return;
				OzarkColor color = new OzarkColor(255,255,255);

				if (holeSafety == HoleUtil.BlockSafety.UNBREAKABLE) {
					color = new OzarkColor(rb.get_value(1), gb.get_value(1), bb.get_value(1), ab.get_value(1));
				} else if (holeSafety == HoleUtil.BlockSafety.BREAKABLE){
					color = new OzarkColor(ro.get_value(1), go.get_value(1), bo.get_value(1), ao.get_value(1));
				}
				if (holeType == HoleUtil.HoleType.CUSTOM) {
					color = new OzarkColor(rc.get_value(1), gc.get_value(1), bc.get_value(1), ac.get_value(1));
				}

				if (mode.in("Custom") && (holeType == HoleUtil.HoleType.CUSTOM || holeType == HoleUtil.HoleType.DOUBLE)) {
					holes.put(centreBlocks, color);
				} else if (mode.in("Double") && holeType == HoleUtil.HoleType.DOUBLE) {
					holes.put(centreBlocks, color);
				} else if (holeType == HoleUtil.HoleType.SINGLE) {
					holes.put(centreBlocks, color);
				}
			}
		});
	}
	@Override
	public void render(EventRender event) {
		if (mc.player == null || mc.world == null || holes == null || holes.isEmpty()) {
			return;
		}

		holes.forEach(this::renderHoles);
	}

	private void renderHoles(AxisAlignedBB hole, OzarkColor color) {
		switch (type.get_current_value()) {
			case "Outline": {
				renderOutline(hole, color);
				break;
			}
			case "Fill": {
				renderFill(hole, color);
				break;
			}
			case "Both": {
				renderOutline(hole, color);
				renderFill(hole, color);
				break;
			}
		}
	}

	private void renderFill(AxisAlignedBB hole, OzarkColor color) {
		OzarkColor fillColor = new OzarkColor(color, 50);
		int ufoAlpha = (this.ufoAlpha.get_value(1) * 50) / 255;

		if (hide_own.get_value(true) && hole.intersects(mc.player.getEntityBoundingBox())) return;

		switch (mode.get_current_value()) {
			case "Air": {
				if (flat_own.get_value(true) && hole.intersects(mc.player.getEntityBoundingBox())) {
					RenderUtil2.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryMasks.Quad.DOWN);
				} else {
					RenderUtil2.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryMasks.Quad.ALL);
				}
				break;
			}
			case "Ground": {
				RenderUtil2.drawBox(hole.offset(0, -1, 0), true, 1, new OzarkColor(fillColor, ufoAlpha), fillColor.getAlpha(), GeometryMasks.Quad.ALL);
				break;
			}
			case "Flat": {
				RenderUtil2.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryMasks.Quad.DOWN);
				break;
			}
			case "Slab": {
				if (flat_own.get_value(true) && hole.intersects(mc.player.getEntityBoundingBox())) {
					RenderUtil2.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryMasks.Quad.DOWN);
				} else {
					RenderUtil2.drawBox(hole, false, slabHeight.get_value(1), fillColor, ufoAlpha, GeometryMasks.Quad.ALL);
				}
				break;
			}
			case "Double": {
				if (flat_own.get_value(true) && hole.intersects(mc.player.getEntityBoundingBox())) {
					RenderUtil2.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryMasks.Quad.DOWN);
				} else {
					RenderUtil2.drawBox(hole.setMaxY(hole.maxY + 1), true, 2, fillColor, ufoAlpha, GeometryMasks.Quad.ALL);
				}
				break;
			}
		}
	}

	private void renderOutline(AxisAlignedBB hole, OzarkColor color) {
		OzarkColor outlineColor = new OzarkColor(color, 255);

		if (hide_own.get_value(true) && hole.intersects(mc.player.getEntityBoundingBox())) return;

		switch (mode.get_current_value()) {
			case "Air": {
				if (flat_own.get_value(true) && hole.intersects(mc.player.getEntityBoundingBox())) {
					RenderUtil2.drawBoundingBoxWithSides(hole, width.get_value(1), outlineColor, ufoAlpha.get_value(1), GeometryMasks.Quad.DOWN);
				} else {
					RenderUtil2.drawBoundingBox(hole, width.get_value(1), outlineColor, ufoAlpha.get_value(1));
				}
				break;
			}
			case "Flat": {
				RenderUtil2.drawBoundingBoxWithSides(hole, width.get_value(1), outlineColor, ufoAlpha.get_value(1), GeometryMasks.Quad.DOWN);
				break;
			}
			case "Slab": {
				if (this.flat_own.get_value(true) && hole.intersects(mc.player.getEntityBoundingBox())) {
					RenderUtil2.drawBoundingBoxWithSides(hole, width.get_value(1), outlineColor, ufoAlpha.get_value(1), GeometryMasks.Quad.DOWN);
				} else {
					RenderUtil2.drawBoundingBox(hole.setMaxY(hole.minY + slabHeight.get_value(1)), width.get_value(1), outlineColor, ufoAlpha.get_value(1));
				}
				break;
			}
			case "Double": {
				if (this.flat_own.get_value(true) && hole.intersects(mc.player.getEntityBoundingBox())) {
					RenderUtil2.drawBoundingBoxWithSides(hole, width.get_value(1), outlineColor, ufoAlpha.get_value(1), GeometryMasks.Quad.DOWN);
				} else {
					RenderUtil2.drawBoundingBox(hole.setMaxY(hole.maxY + 1), width.get_value(1), outlineColor, ufoAlpha.get_value(1));
				}
				break;
			}
		}
	}
}
