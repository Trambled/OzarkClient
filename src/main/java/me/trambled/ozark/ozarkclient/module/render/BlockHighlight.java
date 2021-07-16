package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.render.OzarkColor;
import me.trambled.ozark.ozarkclient.util.render.RenderUtil;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

// GS.


public class BlockHighlight extends Module {

	public
	BlockHighlight () {
		super(Category.RENDER);

		this.name        = "BlockHighlight";
		this.tag         = "BlockHighlight";
		this.description = "See what block ur targeting.";
	}

	Setting renderLook = create("Mode", "HighlightrenderMode", "Side", combobox("Block", "Side"));
	Setting renderType = create("Render Type", "Highlightype", "Outline", combobox("Outline", "Fill", "Both"));
	
	Setting rgb = create("RGB Effect", "HighlightRGBEffect", false);
	
	Setting r = create("R", "HighlightR", 255, 0, 255);
	Setting g = create("G", "HighlightG", 255, 0, 255);
	Setting b = create("B", "HighlightB", 255, 0, 255);
	Setting a = create("A", "HighlightA", 100, 0, 255);

	Setting width = create("Width", "HighlightW", 1, 1, 5);
	Setting sat = create("Satiation", "ChamsSatiation", 0.8, 0, 1);
	Setting brightness = create("Brightness", "ChamsBrightness", 0.8, 0, 1);

	private int lookInt;

	@Override
	public void render(EventRender event) {
		RayTraceResult rayTraceResult = mc.objectMouseOver;

		if (rayTraceResult == null) {
			return;
		}

		EnumFacing enumFacing = mc.objectMouseOver.sideHit;

		if (enumFacing == null) {
			return;
		}

		AxisAlignedBB axisAlignedBB;
		BlockPos blockPos;

		OzarkColor colorWithOpacity = new OzarkColor(r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1));

		switch (renderLook.get_current_value()) {
			case "Block": {
				lookInt = 0;
				break;
			}

			case "Side": {
				lookInt = 1;
				break;
			}
		}

		if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
			blockPos = rayTraceResult.getBlockPos();
			axisAlignedBB = mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos);

			if (axisAlignedBB != null && blockPos != null && mc.world.getBlockState(blockPos).getMaterial() != Material.AIR) {
				switch (renderType.get_current_value()) {
					case "Outline": {
						renderOutline(axisAlignedBB, width.get_value(1), new OzarkColor(r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1)), enumFacing, lookInt);
						break;
					}
					case "Fill": {
						renderFill(axisAlignedBB, colorWithOpacity, enumFacing, lookInt);
						break;
					}

					case "Both": {
						renderOutline(axisAlignedBB, width.get_value(1), new OzarkColor(r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1)), enumFacing, lookInt);
						renderFill(axisAlignedBB, colorWithOpacity, enumFacing, lookInt);
						break;
					}
				}
			}
		}
	}

	public void renderOutline(AxisAlignedBB axisAlignedBB, int width, OzarkColor color, EnumFacing enumFacing, int lookInt) {

		if (lookInt == 0) {
			RenderUtil.drawBoundingBox(axisAlignedBB, width, color);
		} else if (lookInt == 1) {
			RenderUtil.drawBoundingBoxWithSides(axisAlignedBB, width, color, findRenderingSide(enumFacing));
		}
	}

	public void renderFill(AxisAlignedBB axisAlignedBB, OzarkColor color, EnumFacing enumFacing, int lookInt) {
		int facing = 0;

		if (lookInt == 0) {
			facing = RenderUtil.GeometryMasks.Quad.ALL;
		} else if (lookInt == 1) {
			facing = findRenderingSide(enumFacing);
		}

		RenderUtil.drawBox(axisAlignedBB, true, 1, color, facing);
	}

	private int findRenderingSide(EnumFacing enumFacing) {
		int facing = 0;

		if (enumFacing == EnumFacing.EAST) {
			facing = RenderUtil.GeometryMasks.Quad.EAST;
		} else if (enumFacing == EnumFacing.WEST) {
			facing = RenderUtil.GeometryMasks.Quad.WEST;
		} else if (enumFacing == EnumFacing.NORTH) {
			facing = RenderUtil.GeometryMasks.Quad.NORTH;
		} else if (enumFacing == EnumFacing.SOUTH) {
			facing = RenderUtil.GeometryMasks.Quad.SOUTH;
		} else if (enumFacing == EnumFacing.UP) {
			facing = RenderUtil.GeometryMasks.Quad.UP;
		} else if (enumFacing == EnumFacing.DOWN) {
			facing = RenderUtil.GeometryMasks.Quad.DOWN;
		}

		return facing;
	}
	@Override
    public void update() {
		if (rgb.get_value(true)) {
			cycle_rainbow();

		}
	}

	public void cycle_rainbow() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], sat.get_value(1), brightness.get_value(1));

		r.set_value((color_rgb_o >> 16) & 0xFF);
		g.set_value((color_rgb_o >> 8) & 0xFF);
		b.set_value(color_rgb_o & 0xFF);

	}
}

