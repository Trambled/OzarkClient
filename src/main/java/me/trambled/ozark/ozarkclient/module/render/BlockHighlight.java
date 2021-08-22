package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.turok.draw.RenderHelp;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

// Travis.


public class BlockHighlight extends Module {

	public
	BlockHighlight () {
		super(Category.RENDER);

		this.name        = "BlockHighlight";
		this.tag         = "BlockHighlight";
		this.description = "See what block ur targeting.";
	}

	Setting mode = create("Mode", "HighlightMode", "Pretty", combobox("Pretty", "Solid", "Outline", "BowTie"));
	
	Setting rgb = create("RGB Effect", "HighlightRGBEffect", false);
	
	Setting r = create("R", "HighlightR", 255, 0, 255);
	Setting g = create("G", "HighlightG", 255, 0, 255);
	Setting b = create("B", "HighlightB", 255, 0, 255);
	Setting a = create("A", "HighlightA", 100, 0, 255);
	
	Setting l_a = create("Outline A", "HighlightLineA", 255, 0, 255);

	int color_r;
	int color_g;
	int color_b;

	boolean outline = false;
	boolean solid   = false;
	boolean bow_tie = false;

	@Override
	public void disable() {
		outline = false;
		solid   = false;
		bow_tie = false;
	}

	@Override
	public void render(EventRender event) {
		if (mc.player != null && mc.world != null) {
			float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
			};
	
			int color_rgb = Color.HSBtoRGB(tick_color[0], 1, 1);
	
			if (rgb.get_value(true)) {
				color_r = ((color_rgb >> 16) & 0xFF);
				color_g = ((color_rgb >> 8) & 0xFF);
				color_b = (color_rgb & 0xFF);
	
				r.set_value(color_r);
				g.set_value(color_g);
				b.set_value(color_b);
			} else {
				color_r = r.get_value(1);
				color_g = g.get_value(2);
				color_b = b.get_value(3);
			}
	
			if (mode.in("Pretty")) {
				outline = true;
				solid   = true;
				bow_tie = false;
			}
	
			if (mode.in("Solid")) {
				outline = false;
				solid   = true;
				bow_tie = false;
			}
	
			if (mode.in("Outline")) {
				outline = true;
				solid   = false;
				bow_tie = false;
			}

			if (mode.in("BowTie")) {
				outline = false;
				solid = false;
				bow_tie = true;
			}
	
			RayTraceResult result = mc.objectMouseOver;
	
			if (result != null) {
				if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
					BlockPos block_pos = result.getBlockPos();
	
					// Solid.
					if (solid) {
						RenderHelp.prepare("quads");
						RenderHelp.draw_cube(block_pos, color_r, color_g, color_b, a.get_value(1), "all");
						RenderHelp.release();
					}
	
					// Outline.
					if (outline) {
						RenderHelp.prepare("lines");
						RenderHelp.draw_cube_line(block_pos, color_r, color_g, color_b, l_a.get_value(1), "all");
						RenderHelp.release();
					}

					if (bow_tie) {
						RenderHelp.prepare("lines");
						RenderHelp.draw_triangle_line(RenderHelp.get_buffer_build(),
								block_pos.getX(), block_pos.getY(), block_pos.getZ(),
								1, 1, 1,
								r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), 1,
								"all"
						);
						RenderHelp.release();
					}
				}
			}
		}
	}
}