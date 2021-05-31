package me.trambled.ozark.ozarkclient.module.render;

import java.awt.Color;
import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.turok.draw.RenderHelp;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.util.PairUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoleESP extends Module {
	public HoleESP() {
		super(Category.RENDER);

		this.name = "Hole ESP";
		this.tag = "HoleESP";
		this.description = "lets you know where holes are";
	}

	Setting mode = create("Mode", "HoleESPMode", "Pretty", combobox("Pretty", "Solid", "Outline", "Glow", "Glow Line", "Glow Pretty", "Glow Pretty2"));
	Setting off_set = create("Height", "HoleESPOffSetSide", 0.2, -1.0, 1.5);
	Setting range = create("Range", "HoleESPRange", 6, 1, 12);
	Setting hide_own = create("Hide Own", "HoleESPHideOwn", true);
	Setting dual_enable = create("Dual holes", "HoleESPDualHoles", true);

	Setting bedrock_enable = create("Bedrock Holes", "HoleESPBedrockHoles", true);
	// Setting rgb_b 				= create("RGB Effect", "HoleColorRGBEffect", true);
	Setting rb = create("R", "HoleESPRb", 0, 0, 255);
	Setting gb = create("G", "HoleESPGb", 255, 0, 255);
	Setting bb = create("B", "HoleESPBb", 0, 0, 255);
	Setting ab = create("A", "HoleESPAb", 50, 0, 255);

	Setting obsidian_enable = create("Obsidian Holes", "HoleESPObsidianHoles", true);
	// Setting rgb_o 				= create("RGB Effect", "HoleColorRGBEffect", true);
	Setting ro = create("R", "HoleESPRo", 255, 0, 255);
	Setting go = create("G", "HoleESPGo", 0, 0, 255);
	Setting bo = create("B", "HoleESPBo", 0, 0, 255);
	Setting ao = create("A", "HoleESPAo", 50, 0, 255);

	Setting line_a = create("Outline A", "HoleESPLineOutlineA", 255, 0, 255);
	Setting rainbow_ob = create("Rainbow Obsidian", "Rainbow1", true);
	Setting rainbow_bed = create("Rainbow Bedrock", "Rainbow2", true);
	Setting sat = create("Satiation", "Satiation", 0.8, 0, 1);
	Setting brightness = create("Brightness", "Brightness", 0.8, 0, 1);


	ArrayList<PairUtil<BlockPos, Boolean>> holes = new ArrayList<>();
	ArrayList<PairUtil<BlockPos, Boolean>> dual_holes = new ArrayList<>();
	Map<BlockPos, Integer> dual_hole_sides = new HashMap<>();

	boolean outline = false;
	boolean solid = false;
	boolean glow = false;
	boolean glowOutline = false;
	boolean testGlow = false;

	int color_r_o;
	int color_g_o;
	int color_b_o;

	int color_r_b;
	int color_g_b;
	int color_b_b;

	int color_r;
	int color_g;
	int color_b;
	int color_a;

	int safe_sides;

	@Override
	public void update() {
		color_r_b = rb.get_value(1);
		color_g_b = gb.get_value(1);
		color_b_b = bb.get_value(1);

		color_r_o = ro.get_value(1);
		color_g_o = go.get_value(1);
		color_b_o = bo.get_value(1);

		holes.clear();
		dual_holes.clear();
		dual_hole_sides.clear();

		if (mc.player != null || mc.world != null) {
			if (mode.in("Pretty")) {
				outline = true;
				solid = true;
				glow = false;
				glowOutline = false;
			}

			if (mode.in("Solid")) {
				outline = false;
				solid = true;
				glow = false;
				glowOutline = false;
			}

			if (mode.in("Outline")) {
				outline = true;
				solid = false;
				glow = false;
				glowOutline = false;
			}

			if (mode.in("Glow")) {
				outline = false;
				solid = false;
				glow = true;
				glowOutline = false;
			}

			if (mode.in("Glow Pretty")) {
				outline = false;
				solid = false;
				glow = true;
				glowOutline = true;
			}

			if (mode.in("Glow Line")) {
				outline = false;
				solid = false;
				glow = false;
				glowOutline = true;
			}

			if (mode.in("Glow Pretty2")) {
				outline = true;
				solid = false;
				glow = true;
				glowOutline = false;
			}
		}
                            

			int colapso_range = (int) Math.ceil(range.get_value(1));

			List<BlockPos> spheres = sphere(player_as_blockpos(), colapso_range);

			for (BlockPos pos : spheres) {
				if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
					continue;
				}

				if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
					continue;
				}

				if (!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
					continue;
				}
				if (rainbow_ob.get_value(true)) {
					cycle_rainbow();
				}
				if (rainbow_bed.get_value(true)) {
					cycle_rainbow2();
				}

				boolean possible = true;

				safe_sides = 0;
				int air_orient = -1;
				int counter = 0;

				for (BlockPos seems_blocks : new BlockPos[]{
						new BlockPos(0, -1, 0),
						new BlockPos(0, 0, -1),
						new BlockPos(1, 0, 0),
						new BlockPos(0, 0, 1),
						new BlockPos(-1, 0, 0)
				}) {
					Block block = mc.world.getBlockState(pos.add(seems_blocks)).getBlock();

					if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
						possible = false;

						if (counter == 0) break;

						if (air_orient != -1) {
							air_orient = -1;
							break;
						}

						if (block.equals(Blocks.AIR)) {
							air_orient = counter;
						} else {
							break;
						}
					}

					if (block == Blocks.BEDROCK) {
						safe_sides++;
					}
					counter++;
				}

				if (possible) {
					if (safe_sides == 5) {
						if (!this.bedrock_enable.get_value(true)) continue;
						holes.add(new PairUtil<>(pos, true));
					} else {
						if (!this.obsidian_enable.get_value(true)) continue;
						holes.add(new PairUtil<>(pos, false));
					}
					continue;
				}

				if (!dual_enable.get_value(true) || air_orient < 0) continue;
				BlockPos second_pos = pos.add(orientConv(air_orient));

				if (checkDual(second_pos, air_orient)) {
					boolean low_ceiling_hole = !mc.world.getBlockState(second_pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR);
					if (safe_sides == 8) {
						if (low_ceiling_hole) {
							holes.add(new PairUtil<BlockPos, Boolean>(pos, true));
						} else {
							if (!dual_hole_sides.containsKey(pos)) {
								dual_holes.add(new PairUtil<BlockPos, Boolean>(pos, true));
								dual_hole_sides.put(pos, air_orient);
							}
							if (!dual_hole_sides.containsKey(second_pos)) {
								dual_holes.add(new PairUtil<BlockPos, Boolean>(second_pos, true));
								dual_hole_sides.put(second_pos, oppositeIntOrient(air_orient));
							}
						}
					} else {
						if (low_ceiling_hole) {
							holes.add(new PairUtil<BlockPos, Boolean>(pos, false));
						} else {
							if (!dual_hole_sides.containsKey(pos)) {
								dual_holes.add(new PairUtil<BlockPos, Boolean>(pos, false));
								dual_hole_sides.put(pos, air_orient);
							}
							if (!dual_hole_sides.containsKey(second_pos)) {
								dual_holes.add(new PairUtil<BlockPos, Boolean>(second_pos, false));
								dual_hole_sides.put(second_pos, oppositeIntOrient(air_orient));
							}
						}
					}
				}
			}
		}


	private int oppositeIntOrient(int orient_count) {

		int opposite = 0;

		switch (orient_count) {
			case 0:
				opposite = 5;
				break;
			case 1:
				opposite = 3;
				break;
			case 2:
				opposite = 4;
				break;
			case 3:
				opposite = 1;
				break;
			case 4:
				opposite = 2;
				break;
		}
		return opposite;
	}

	private boolean checkDual(BlockPos second_block, int counter) {
		int i = -1;

		/*
			lets check down from second block to not have esp of a dual hole of one space
			missing a bottom block
		*/
		for (BlockPos seems_blocks : new BlockPos[]{
				new BlockPos(0, -1, 0), //Down
				new BlockPos(0, 0, -1), //N
				new BlockPos(1, 0, 0), //E
				new BlockPos(0, 0, 1), //S
				new BlockPos(-1, 0, 0)  //W
		}) {
			i++;
			//skips opposite direction check, since its air
			if (counter == oppositeIntOrient(i)) {
				continue;
			}

			Block block = mc.world.getBlockState(second_block.add(seems_blocks)).getBlock();
			if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
				return false;
			}

			if (block == Blocks.BEDROCK) {
				safe_sides++;
			}
		}
		return true;
	}

	private BlockPos orientConv(int orient_count) {
		BlockPos converted = null;

		switch (orient_count) {
			case 0:
				converted = new BlockPos(0, -1, 0);
				break;
			case 1:
				converted = new BlockPos(0, 0, -1);
				break;
			case 2:
				converted = new BlockPos(1, 0, 0);
				break;
			case 3:
				converted = new BlockPos(0, 0, 1);
				break;
			case 4:
				converted = new BlockPos(-1, 0, 0);
				break;
			case 5:
				converted = new BlockPos(0, 1, 0);
				break;
		}
		return converted;
	}

	@Override
	public void render(EventRender event) {
		float off_set_h;
		if (!holes.isEmpty() || !dual_holes.isEmpty()) {
			off_set_h = (float) off_set.get_value(1.0);

			for (PairUtil<BlockPos, Boolean> hole : holes) {
				if (hole.getValue()) {
					color_r = color_r_b;
					color_g = color_g_b;
					color_b = color_b_b;
					color_a = ab.get_value(1);
				} else if (!hole.getValue()) {
					color_r = color_r_o;
					color_g = color_g_o;
					color_b = color_b_o;
					color_a = ao.get_value(1);
				} else continue;

				if (hide_own.get_value(true) && hole.getKey().equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
					continue;
				}

				if (solid) {
					RenderHelp.prepare("quads");
					RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
							hole.getKey().getX(), hole.getKey().getY(), hole.getKey().getZ(),
							1, off_set_h, 1,
							color_r, color_g, color_b, color_a,
							"all"
					);

					RenderHelp.release();
				}

				if (outline) {
					RenderHelp.prepare("lines");
					RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
							hole.getKey().getX(), hole.getKey().getY(), hole.getKey().getZ(),
							1, off_set_h, 1,
							color_r, color_g, color_b, line_a.get_value(1),
							"all"
					);

					RenderHelp.release();
				}

				if (glow) {
					RenderHelp.prepare("quads");
					RenderHelp.draw_gradiant_cube(RenderHelp.get_buffer_build(),
							hole.getKey().getX(), hole.getKey().getY(), hole.getKey().getZ(),
							1, off_set_h, 1,
							new Color(color_r, color_g, color_b, color_a), new Color(0, 0, 0, 0),
							"all"
					);

					RenderHelp.release();
				}

				if (glowOutline) {
					RenderHelp.prepare("lines");
					RenderHelp.draw_gradiant_outline(RenderHelp.get_buffer_build(), hole.getKey().getX(),
							hole.getKey().getY(), hole.getKey().getZ(), off_set_h,
							new Color(color_r, color_g, color_b, line_a.get_value(1)),
							new Color(0, 0, 0, 0), "all");
					RenderHelp.release();

				}
			}

			for (PairUtil<BlockPos, Boolean> hole : dual_holes) {

				BlockPos playerPos = new BlockPos(mc.player);
				if (hide_own.get_value(true) && (hole.getKey().equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)) ||
						hole.getKey().equals(playerPos.add(orientConv(oppositeIntOrient(dual_hole_sides.get(hole.getKey()))))))) {
					continue;
				}

				if (hole.getValue()) {
					color_r = color_r_b;
					color_g = color_g_b;
					color_b = color_b_b;
					color_a = ab.get_value(1);
				} else if (!hole.getValue()) {
					color_r = color_r_o;
					color_g = color_g_o;
					color_b = color_b_o;
					color_a = ao.get_value(1);
				} else continue;

				if (solid) {
					RenderHelp.prepare("quads");
					RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
							hole.getKey().getX(), hole.getKey().getY(), hole.getKey().getZ(),
							1, off_set_h, 1,
							color_r, color_g, color_b, color_a,
							getDirectionsToRenderQuad(hole.getKey())
					);
					RenderHelp.release();
				}

				if (outline) {
					RenderHelp.prepare("lines");
					RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
							hole.getKey().getX(), hole.getKey().getY(), hole.getKey().getZ(),
							1, off_set_h, 1,
							color_r, color_g, color_b, line_a.get_value(1),
							getDirectionsToRenderOutline(hole.getKey())
					);

					RenderHelp.release();
				}

				if (glow) {
					RenderHelp.prepare("lines");
					RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
							hole.getKey().getX(), hole.getKey().getY(), hole.getKey().getZ(),
							1, 0, 1,
							color_r, color_g, color_b, line_a.get_value(1),
							getDirectionsToRenderOutline(hole.getKey())
					);
					RenderHelp.release();

					RenderHelp.prepare("quads");
					RenderHelp.draw_gradiant_cube(RenderHelp.get_buffer_build(),
							hole.getKey().getX(), hole.getKey().getY(), hole.getKey().getZ(),
							1, off_set_h, 1,
							new Color(color_r, color_g, color_b, color_a), new Color(0, 0, 0, 0),
							getDirectionsToRenderQuad(hole.getKey())
					);
					RenderHelp.release();
				}

				if (glowOutline) {
					RenderHelp.prepare("lines");
					RenderHelp.draw_gradiant_outline(RenderHelp.get_buffer_build(), hole.getKey().getX(),
							hole.getKey().getY(), hole.getKey().getZ(), off_set_h,
							new Color(color_r, color_g, color_b, line_a.get_value(1)),
							new Color(0, 0, 0, 0),
							getDirectionsToRenderOutline(hole.getKey())
					);
					RenderHelp.release();
				}
			}
		}
	}

	private String getDirectionsToRenderOutline(BlockPos hole) {
		int sideNoToDraw = dual_hole_sides.get(hole);
		switch (sideNoToDraw) {
			case 1:
				return "downeast-upeast-downsouth-upsouth-downwest-upwest-southwest-southeast";
			case 2:
				return "downnorth-upnorth-downsouth-upsouth-downwest-upwest-northwest-southwest";
			case 3:
				return "upnorth-downnorth-upeast-downeast-upwest-downwest-northeast-northwest";
			case 4:
				return "upnorth-downnorth-upeast-downeast-upsouth-downsouth-northeast-southeast";
			default:
				break;
		}
		return "all";
	}

	private String getDirectionsToRenderQuad(BlockPos hole) {
		int sideNotToDraw = dual_hole_sides.get(hole);

		switch (sideNotToDraw) {
			case 1:
				return "east-south-west-top-bottom";
			case 2:
				return "north-south-west-top-bottom";
			case 3:
				return "north-east-west-top-bottom";
			case 4:
				return "north-east-south-top-bottom";
			default:
				break;
		}

		return "all";
	}

	public List<BlockPos> sphere(BlockPos pos, float r) {
		int plus_y = 0;

		List<BlockPos> sphere_block = new ArrayList<>();

		int cx = pos.getX();
		int cy = pos.getY();
		int cz = pos.getZ();

		for (int x = cx - (int) r; x <= cx + r; ++x) {
			for (int z = cz - (int) r; z <= cz + r; ++z) {
				for (int y = cy - (int) r; y < cy + r; ++y) {
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (cy - y) * (cy - y);
					if (dist < r * r) {
						BlockPos spheres = new BlockPos(x, y + plus_y, z);
						sphere_block.add(spheres);
					}
				}
			}
		}

		return sphere_block;
	}

	public BlockPos player_as_blockpos() {
		return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
	}

	public boolean isBlockHole(BlockPos block) {
		if (!is_active()) {
			update();
		}

		return holes.contains(new PairUtil<BlockPos, Boolean>(block, true)) || holes.contains(new PairUtil<BlockPos, Boolean>(block, false));
	}

	public void cycle_rainbow() {
		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], sat.get_value(1), brightness.get_value(1));


		ro.set_value((color_rgb_o >> 16) & 0xFF);
		go.set_value((color_rgb_o >> 8) & 0xFF);
		bo.set_value(color_rgb_o & 0xFF);
	}
	public void cycle_rainbow2() {
		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], sat.get_value(1), brightness.get_value(1));

		rb.set_value((color_rgb_o >> 16) & 0xFF);
		gb.set_value((color_rgb_o >> 8) & 0xFF);
		bb.set_value(color_rgb_o & 0xFF);

	}

	@Override
	public void update_always() {
		rb.set_shown(bedrock_enable.get_value(false));
		gb.set_shown(bedrock_enable.get_value(false));
		bb.set_shown(bedrock_enable.get_value(false));
		ab.set_shown(bedrock_enable.get_value(false));
		ro.set_shown(obsidian_enable.get_value(false));
		go.set_shown(obsidian_enable.get_value(false));
		bo.set_shown(obsidian_enable.get_value(false));
		ao.set_shown(obsidian_enable.get_value(false));


	}
}
