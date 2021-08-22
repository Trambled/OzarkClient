package me.trambled.ozark.ozarkclient.manager;

import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.chat.*;
import me.trambled.ozark.ozarkclient.module.combat.*;
import me.trambled.ozark.ozarkclient.module.exploit.*;
import me.trambled.ozark.ozarkclient.module.gui.*;
import me.trambled.ozark.ozarkclient.module.misc.*;
import me.trambled.ozark.ozarkclient.module.movement.*;
import me.trambled.ozark.ozarkclient.module.render.*;
import me.trambled.turok.draw.RenderHelp;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;

public class ModuleManager {

	public static ArrayList<Module> array_modules = new ArrayList<>();
	private static final LinkedHashMap<Class<? extends Module>, Module> class_map = new LinkedHashMap<>(); // just adding this so i can use module classes easier

	public ModuleManager() {
		// Click GUI and HUD.
		add_module(new OldGUI());
		add_module(new HUD());
		add_module(new PastGUIModule());
		add_module(new CustomMainMenu());
		add_module(new Notifications());
		add_module(new Arraylist());
		add_module(new Safety());

		// Chat.
		add_module(new ChatSuffix());
		add_module(new VisualRange());
		add_module(new TotemPopCounter());
		add_module(new ChatMods());
		add_module(new AutoEz());
		add_module(new Announcer());
		add_module(new AutoExcuse());
		add_module(new WeaknessAlert());
		add_module(new RetardChat());
		add_module(new Spammer());
		add_module(new WhisperSpam());
		add_module(new BurrowAlert());

		// Combat.
		add_module(new Criticals());
		add_module(new Aura());
		add_module(new Surround());
		add_module(new Velocity());
		add_module(new AutoCrystal());
		add_module(new HoleFill());
		add_module(new Trap());
		add_module(new SelfTrap());
		add_module(new AutoArmour());
		add_module(new Auto32K());
		add_module(new Webfill());
		add_module(new AutoWeb());
		add_module(new BedAura());
		add_module(new Offhand());
		add_module(new AutoTotem());
		add_module(new AutoMine());
		add_module(new FastBow());
		add_module(new AntiCrystal());
		add_module(new ManualQuiver());
		add_module(new OffhandBypass());
		add_module(new AntiTrap());
		add_module(new AutoAnvil());
		add_module(new PistonCrystal());
		add_module(new Blocker());
		add_module(new Quiver());
		add_module(new CevBreaker());
		add_module(new OffhandPlus());
		add_module(new ShulkerCrystal());
		add_module(new SilentXP());

		// Exploit.
		add_module(new XCarry());
		add_module(new NoSwing());
		add_module(new PortalGodMode());
		add_module(new PacketMine());
		add_module(new EntityMine());
		add_module(new BuildHeight());
		add_module(new AutoStackDupe());
		add_module(new EntityDesync());
		add_module(new Timer());
		add_module(new Burrow());
		add_module(new Freecam());
		add_module(new EChestBP());
		add_module(new Ghost());
		add_module(new MountBypass());
		add_module(new Scaffold());
		add_module(new CoordExploit());
		add_module(new InstantBurrow());
		add_module(new AntiWeb());
		add_module(new NoRotate());

		// Movement.
		add_module(new Strafe());
		add_module(new Step());
		add_module(new Sprint());
		add_module(new IceSpeed());
		add_module(new FastSwim());
		add_module(new HoleTP());
		add_module(new FastFall());
		add_module(new ElytraFly());
		add_module(new Flight());
		add_module(new NoFall());
		add_module(new NoPush());
		add_module(new NoSlowDown());
		add_module(new LongJump());
		add_module(new Anchor());
		add_module(new AntiLevitation());
		add_module(new EntitySpeed());
		add_module(new Static());
		add_module(new PacketFly());
		add_module(new AutoWalk());

		// Render.
		add_module(new BlockHighlight());
		add_module(new HoleESP());
		add_module(new ShulkerPreview());
		add_module(new ViewmodelChanger());
		add_module(new VoidESP());
		add_module(new Antifog());
		add_module(new NameTags());
		add_module(new FuckedDetector());
		add_module(new Tracers());
		add_module(new SkyColour());
		add_module(new Chams());
		add_module(new Capes());
		add_module(new CityESP());
		add_module(new StorageESP());
		add_module(new NoRender());
		add_module(new FullBright());
		add_module(new Timechanger());
		add_module(new BurrowESP());
		add_module(new Xray());
		add_module(new Weather());
		add_module(new BreakESP());
		add_module(new SmallHands());
		add_module(new LogOutSpots());
		add_module(new Trajectories());
		add_module(new PenisESP());
		add_module(new NewChunks());
		add_module(new Chams2());
		add_module(new ChorusViewer());

		// Misc.
		add_module(new AutoWither());
		add_module(new MCF());
		add_module(new MCP());
		add_module(new StopEXP());
		add_module(new AutoReplenish());
		add_module(new AutoNomadHut());
		add_module(new FastUtil());
		add_module(new Speedmine());
		add_module(new DiscordRPC());
		add_module(new FakePlayer());
		add_module(new EntityControl());
		add_module(new PacketCanceller());
		add_module(new AutoKit());
		add_module(new AutoEat());
		add_module(new DupeIStack());
		add_module(new AutoTool());
		add_module(new AutoBuilder());
		add_module(new AutoRespawn());
		add_module(new InventorySort());
		add_module(new AutoGear());
		add_module(new AntiSound());
		add_module(new EntityAlert());
		add_module(new Portals());
		add_module(new AntiAFK());
		
		array_modules.sort(Comparator.comparing(Module::get_name));
	}

	public void add_module(Module module) {
		array_modules.add(module);
		class_map.put(module.getClass(), module);
	}

	public ArrayList<Module> get_array_modules() {
		return array_modules;
	}

	public ArrayList<Module> get_array_active_modules() {
		ArrayList<Module> actived_modules = new ArrayList<>();

		for (Module modules : get_array_modules()) {
			if (modules.is_active()) {
				actived_modules.add(modules);
			}
		}

		return actived_modules;
	}

	public Vec3d process(Entity entity, double x, double y, double z) {
		return new Vec3d(
			(entity.posX - entity.lastTickPosX) * x,
			(entity.posY - entity.lastTickPosY) * y,
			(entity.posZ - entity.lastTickPosZ) * z);
	}

	public Vec3d get_interpolated_pos(Entity entity, double ticks) {
		return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(process(entity, ticks, ticks, ticks)); // x, y, z.
	}

	public void render(RenderWorldLastEvent event) {
		mc.profiler.startSection("ozarkclient");
		mc.profiler.startSection("setup");

		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.disableDepth();

		GlStateManager.glLineWidth(1f);

		Vec3d pos = get_interpolated_pos(mc.player, event.getPartialTicks());

		EventRender event_render = new EventRender(RenderHelp.INSTANCE, pos);

		event_render.reset_translation();

		mc.profiler.endSection();

		for (Module modules : get_array_modules()) {
			mc.profiler.startSection(modules.get_tag());

			modules.render_always(event_render);

			mc.profiler.endSection();
			if (modules.is_active()) {
				mc.profiler.startSection(modules.get_tag());

				modules.render(event_render);

				mc.profiler.endSection();
			}

		}

		mc.profiler.startSection("release");

		GlStateManager.glLineWidth(1f);

		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.enableCull();

		RenderHelp.release_gl();

		mc.profiler.endSection();
		mc.profiler.endSection();
	}

	public void update() {
		for (Module modules : get_array_modules()) {
			if (modules.is_active()) {
				modules.update();
			}
			modules.update_always();
		}
	}

	public void on_logout() {
		for (Module modules : get_array_modules()) {
			try {
				if (modules.is_active()) {
					modules.log_out();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public void on_server_join() {
		for (Module modules : get_array_modules()) {
			try {
				if (modules.is_active()) {
					modules.server_join();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}


	public void fast_update() {
		for (Module modules : get_array_modules()) {
			try {
				if (modules.is_active()) {
					modules.fast_update();
				}
				modules.fast_update_always();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public void render() {
		for (Module modules : get_array_modules()) {
			if (modules.is_active()) {
				modules.render();
			}
		}
	}

	public void bind(int event_key) {
		if (event_key == 0) {
			return;
		}

		for (Module modules : get_array_modules()) {
			if (modules.get_bind(0) == event_key) {
				modules.toggle();
			}
		}
	}

	public void on_pressed(int event_key) {
		if (event_key == 0) return;

		for (Module module : get_array_modules()) {
			if (module.get_bind(0) == event_key) {
				module.on_key_pressed();
			}
		}
	}

	public Module get_module_with_tag(String tag) {
		Module module_requested = null;

		for (Module module : get_array_modules()) {
			if (module.get_tag().equalsIgnoreCase(tag)) {
				module_requested = module;
			}
		}

		return module_requested;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Module> T get_module(Class<T> clazz) {
		return (T) class_map.get(clazz);
	}

	public ArrayList<Module> get_modules_with_category(Category category) {
		ArrayList<Module> module_requesteds = new ArrayList<>();

		for (Module modules : get_array_modules()) {
			if (modules.get_category().equals(category)) {
				module_requesteds.add(modules);
			}
		}

		return module_requesteds;
	}

}
