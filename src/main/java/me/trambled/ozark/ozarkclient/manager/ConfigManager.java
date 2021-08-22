package me.trambled.ozark.ozarkclient.manager;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.guiscreen.PastGUI;
import me.trambled.ozark.ozarkclient.guiscreen.gui.main.Frame;
import me.trambled.ozark.ozarkclient.guiscreen.gui.past.Panel;
import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.render.Xray;
import me.trambled.ozark.ozarkclient.util.misc.DrawnUtil;
import me.trambled.ozark.ozarkclient.util.misc.EzMessageUtil;
import me.trambled.ozark.ozarkclient.util.player.AutoKitUtil;
import me.trambled.ozark.ozarkclient.util.player.social.EnemyUtil;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import net.minecraft.block.Block;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

import static me.trambled.ozark.Ozark.send_minecraft_log;
import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;

public class ConfigManager {

    public File FILE_DIRECTORY;

    // FOLDERS
    private final String MAIN_FOLDER = "OZARKCLIENT/";
    private final String CONFIGS_FOLDER = MAIN_FOLDER + "configs/";
    private String ACTIVE_CONFIG_FOLDER = CONFIGS_FOLDER + "default/";

    // STATIC FILES
    private final String CLIENT_FILE = "client.json";
    private final String CONFIG_FILE = "config.txt";
    private final String DRAWN_FILE = "drawn.txt";
    private final String EZ_FILE = "ez.txt";
    private final String KIT_FILE = "kit.txt";
    private final String NAME_FILE = "name.txt";
    private final String ENEMIES_FILE = "enemies.json";
    private final String FRIENDS_FILE = "friends.json";
    private final String HUD_FILE = "hud.json";
    private final String BINDS_FILE = "binds.txt";

    // DIRS
    private final String CLIENT_DIR = MAIN_FOLDER + CLIENT_FILE;
    private final String CONFIG_DIR = MAIN_FOLDER + CONFIG_FILE;
    private final String DRAWN_DIR = MAIN_FOLDER + DRAWN_FILE;
    private final String EZ_DIR = MAIN_FOLDER + EZ_FILE;
    private final String KIT_DIR = MAIN_FOLDER + KIT_FILE;
    private final String NAME_DIR = MAIN_FOLDER + NAME_FILE;
    private final String ENEMIES_DIR = MAIN_FOLDER + ENEMIES_FILE;
    private final String FRIENDS_DIR = MAIN_FOLDER + FRIENDS_FILE;
    private final String HUD_DIR = MAIN_FOLDER + HUD_FILE;

    private String CURRENT_CONFIG_DIR = MAIN_FOLDER + CONFIGS_FOLDER + ACTIVE_CONFIG_FOLDER;
    private String BINDS_DIR = CURRENT_CONFIG_DIR + BINDS_FILE;

    // FOLDER PATHS
    private final Path MAIN_FOLDER_PATH = Paths.get(MAIN_FOLDER);
    private final Path CONFIGS_FOLDER_PATH = Paths.get(CONFIGS_FOLDER);
    private Path ACTIVE_CONFIG_FOLDER_PATH = Paths.get(ACTIVE_CONFIG_FOLDER);

    // FILE PATHS
    private final Path CLIENT_PATH = Paths.get(CLIENT_DIR);
    private final Path CONFIG_PATH = Paths.get(CONFIG_DIR);
    private final Path DRAWN_PATH = Paths.get(DRAWN_DIR);
    private final Path KIT_PATH = Paths.get(KIT_DIR);
    private final Path EZ_PATH = Paths.get(EZ_DIR);
    private final Path NAME_PATH = Paths.get(NAME_DIR);
    private final Path ENEMIES_PATH = Paths.get(ENEMIES_DIR);
    private final Path FRIENDS_PATH = Paths.get(FRIENDS_DIR);
    private final Path HUD_PATH = Paths.get(HUD_DIR);

    private Path BINDS_PATH = Paths.get(BINDS_DIR);
    private Path CURRENT_CONFIG_PATH = Paths.get(CURRENT_CONFIG_DIR);

    public ConfigManager() {
        FILE_DIRECTORY = new File(mc.gameDir, "OZARKCLIENT");
    }

    public boolean set_active_config_folder(String folder) {
        if (folder.equals(this.ACTIVE_CONFIG_FOLDER)) {
            return false;
        }

        this.ACTIVE_CONFIG_FOLDER = CONFIGS_FOLDER + folder;
        this.ACTIVE_CONFIG_FOLDER_PATH = Paths.get(ACTIVE_CONFIG_FOLDER);

        this.CURRENT_CONFIG_DIR = MAIN_FOLDER + CONFIGS_FOLDER + ACTIVE_CONFIG_FOLDER;
        this.CURRENT_CONFIG_PATH = Paths.get(CURRENT_CONFIG_DIR);

        this.BINDS_DIR = CURRENT_CONFIG_DIR + BINDS_FILE;
        this.BINDS_PATH = Paths.get(BINDS_DIR);

        load_settings();
        return true;
    }

    public void save_xray() {
        try {
            final File file = new File(FILE_DIRECTORY, "xray.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Block block : Xray.getBLOCKS()) {
                try {
                    out.write( Block.REGISTRY.getNameForObject(block).getPath());
                    out.write("\r\n");
                }
                catch (Exception ignored ) {}
            }
            out.close();
        }
        catch (Exception ignored ) {}
    }

    private void save_spammer() throws IOException {
        FileWriter writer = new FileWriter("OZARKCLIENT/spammer.txt");
        writer.close();
    }

    public void load_xray() {
        try {
            final File file = new File(FILE_DIRECTORY, "xray.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Xray.getBLOCKS().clear();
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Xray.getBLOCKS().add(Objects.requireNonNull(Block.getBlockFromName(line)));
                }
                catch (NullPointerException ignored ) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LOAD & SAVE EZ MESSAGE

    private void save_ezmessage() throws IOException {

        FileWriter writer = new FileWriter(EZ_DIR);

        try {
            writer.write(EzMessageUtil.get_message());
        } catch (Exception ignored) {
            writer.write("You just got niggered by OzarkClient");
        }

        writer.close();

    }

    private void load_ezmessage() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String s : Files.readAllLines(EZ_PATH)) {
            sb.append(s);
        }
        EzMessageUtil.set_message(sb.toString());
    }

    private void save_kitmessage() throws IOException {

        FileWriter writer = new FileWriter(KIT_DIR);

        try {
            writer.write(AutoKitUtil.get_message());
        } catch (Exception ignored) {
            writer.write("kit");
        }

        writer.close();

    }


    private void load_kitmessage() throws IOException {
        StringBuilder ki = new StringBuilder();
        for (String s : Files.readAllLines(KIT_PATH)) {
            ki.append(s);
        }
        AutoKitUtil.set_message(ki.toString());
    }

    private void save_display_name() throws IOException {

        FileWriter writer = new FileWriter(NAME_DIR);

        try {
            writer.write(Ozark.DISPLAY_NAME);
        } catch (Exception ignored) {
            writer.write("Ozark");
        }

        writer.close();

    }


    private void load_display_name() throws IOException {
        StringBuilder ki = new StringBuilder();
        for (String s : Files.readAllLines(NAME_PATH)) {
            ki.append(s);
        }
        Ozark.DISPLAY_NAME = ki.toString();
    }

    // LOAD & SAVE DRAWN

    private void save_drawn() throws IOException {
        FileWriter writer = new FileWriter(DRAWN_DIR);

        for (String s : DrawnUtil.hidden_tags) {
            writer.write(s + System.lineSeparator());
        }

        writer.close();
    }

    private void load_drawn() throws IOException {
        DrawnUtil.hidden_tags = Files.readAllLines(DRAWN_PATH);
    }

    public void save_past_gui() {
        try {
            File file = new File(FILE_DIRECTORY, "pastgui.txt");
            ArrayList<String> panelsToSave = new ArrayList<>();

            for (Panel panel : PastGUI.panels) {
                panelsToSave.add(panel.getCategory() + ":" + panel.getX() + ":" + panel.getY() + ":" + panel.isOpen());
            }

            try {
                PrintWriter printWriter = new PrintWriter(file);
                for (String string : panelsToSave) {
                    printWriter.println(string);
                }
                printWriter.close();
            } catch (FileNotFoundException ignored ) {}
        } catch (Exception ignored ) {}
    }

    public void load_past_gui() {
        try {
            File file = new File(FILE_DIRECTORY, "pastgui.txt");
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;

            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String name = curLine.split(":")[0];
                String x = curLine.split(":")[1];
                String y = curLine.split(":")[2];
                String open = curLine.split(":")[3];
                int x1 = Integer.parseInt(x);
                int y1 = Integer.parseInt(y);
                boolean opened = Boolean.parseBoolean(open);
                Panel p = PastGUI.getPanelByName (name);
                if (p != null) {
                    p.x = x1;
                    p.y = y1;
                    p.setOpen(opened);
                }
            }

            br.close();
        } catch (Exception ignored ) {}
    }

    // LOAD & SAVE PALS

    private void save_friends() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(FriendUtil.friends);
        OutputStreamWriter file;

        file = new OutputStreamWriter(new FileOutputStream(FRIENDS_DIR), StandardCharsets.UTF_8);
        file.write(json);
        file.close();
    }

    private void load_friends() throws IOException {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(FRIENDS_DIR));

        FriendUtil.friends = gson.fromJson(reader, new TypeToken<ArrayList<FriendUtil.Friend>>(){}.getType());

        reader.close();
    }

    // LOAD & SAVE ENEMIES

    private void save_enemies() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(EnemyUtil.enemies);

        OutputStreamWriter file;

        file = new OutputStreamWriter(new FileOutputStream(ENEMIES_DIR), StandardCharsets.UTF_8);
        file.write(json);
        file.close();
    }

    private void load_enemies() throws IOException {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(ENEMIES_DIR));

        EnemyUtil.enemies = gson.fromJson(reader, new TypeToken<ArrayList<EnemyUtil.Enemy>>(){}.getType());

        reader.close();
    }

    // LOAD & SAVE HACKS

    private void save_modules() throws IOException {

        for (Module module : Ozark.get_module_manager().get_array_modules()) {

            final String file_name = ACTIVE_CONFIG_FOLDER + module.get_tag() + ".txt";
            final Path file_path = Paths.get(file_name);
            delete_file(file_name);
            verify_file(file_path);

            final File file = new File(file_name);
            final BufferedWriter br = new BufferedWriter(new FileWriter(file));

            for (Setting setting : Ozark.get_setting_manager().get_settings_with_module(module)) {
                switch (setting.get_type()) {
                    case "button":
                        br.write(setting.get_tag() + ":" + setting.get_value(true) + "\r\n");
                        break;
                    case "combobox":
                        br.write(setting.get_tag() + ":" + setting.get_current_value() + "\r\n");
                        break;
                    case "label":
                        br.write(setting.get_tag() + ":" + setting.get_value("") + "\r\n");
                        break;
                    case "doubleslider":
                        br.write(setting.get_tag() + ":" + setting.get_value(1.0) + "\r\n");
                        break;
                    case "bind":
                        br.write(setting.get_tag() + ":" + setting.get_bind(1) + "\r\n");
                        break;
                    case "integerslider":
                        br.write(setting.get_tag() + ":" + setting.get_value(1) + "\r\n");
                        break;
					case "string":
						br.write(setting.get_tag() + ":" + setting.get_message("") + "\r\n");

                }
            }


            br.close();

        }
    }


    private void load_modules() throws IOException {
        for (Module module : Ozark.get_module_manager().get_array_modules()) {
            final String file_name = ACTIVE_CONFIG_FOLDER + module.get_tag() + ".txt";
            final Path path = Paths.get(file_name);
            if (!verify_file_without_creating(path)) continue;
            final File file = new File(file_name);
            final FileInputStream fi_stream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream di_stream = new DataInputStream(fi_stream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(di_stream));

            String line;
            while((line = br.readLine()) != null) {

                final String colune = line.trim();
                final String tag = colune.split(":")[0];
                final String value = colune.split(":")[1];

                Setting setting = Ozark.get_setting_manager().get_setting_with_tag(module, tag);
				
				if (setting == null) {
					// send_minecraft_log("setting error, the setting assigned (" + tag + ") doesn't exist (assigned value is " + value);
					continue;
				}

                // send_minecraft_log("Attempting to assign value '" + value + "' to setting '" + tag + "'");		

				switch (setting.get_type()) {
                    case "button":
                        setting.set_value(Boolean.parseBoolean(value));
                        break;
                    case "label":
                        setting.set_value(value);
                        break;
                    case "doubleslider":
					    // reason we have this here is that sometimes you change a double setting to an int, and since i actually dont want it to fuck up and have a NumberFormatException, we put it here, the rest shouldnt matter
						if (!value.contains(".")) continue;
                        setting.set_value(Double.parseDouble(value));
                        break;
					case "integerslider":
						if (value.contains(".")) continue;
                        setting.set_value(Integer.parseInt(value));
                        break;
                    case "combobox":
                        setting.set_current_value(value);
                        break;
                    case "bind":
                        setting.set_bind(Integer.parseInt(value));
						break;
					case "string":
						setting.set_message(value);
                }
            }
            br.close();
        }
    }

    // LOAD & SAVE client/gui

    private void save_client() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();

        JsonObject main_json = new JsonObject();

        JsonObject config = new JsonObject();
        JsonObject gui = new JsonObject();

        config.add("name", new JsonPrimitive(Ozark.get_name()));
        config.add("version", new JsonPrimitive(Ozark.get_version()));
        config.add("user", new JsonPrimitive(Ozark.get_actual_user()));
        config.add("prefix", new JsonPrimitive(CommandManager.get_prefix()));

        for (Frame frames_gui : Ozark.main_gui.get_array_frames()) {
            JsonObject frame_info = new JsonObject();

            frame_info.add("name", new JsonPrimitive(frames_gui.get_name()));
            frame_info.add("tag", new JsonPrimitive(frames_gui.get_tag()));
            frame_info.add("x", new JsonPrimitive(frames_gui.get_x()));
            frame_info.add("y", new JsonPrimitive(frames_gui.get_y()));

            gui.add(frames_gui.get_tag(), frame_info);
        }

        main_json.add("configuration", config);
        main_json.add("gui",           gui);

        JsonElement json_pretty = parser.parse(main_json.toString());

        String json = gson.toJson(json_pretty);

        OutputStreamWriter file;

        file = new OutputStreamWriter(new FileOutputStream(CLIENT_DIR), StandardCharsets.UTF_8);
        file.write(json);

        file.close();
    }

    private void load_client() throws IOException {
        InputStream stream = Files.newInputStream(CLIENT_PATH);
        JsonObject  json_client = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
        JsonObject  json_config = json_client.get("configuration").getAsJsonObject();
        JsonObject  json_gui = json_client.get("gui").getAsJsonObject();

        CommandManager.set_prefix(json_config.get("prefix").getAsString());

        for (Frame frames : Ozark.main_gui.get_array_frames()) {
            JsonObject frame_info = json_gui.get(frames.get_tag()).getAsJsonObject();

            Frame frame_requested = Ozark.main_gui.get_frame_with_tag(frame_info.get("tag").getAsString());

            if (frame_requested == null) continue;

            frame_requested.set_x(frame_info.get("x").getAsInt());
            frame_requested.set_y(frame_info.get("y").getAsInt());
        }

        stream.close();
    }

    // LOAD & SAVE HUD

    private void save_hud() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();

        JsonObject main_json = new JsonObject();

        JsonObject main_frame = new JsonObject();
        JsonObject main_hud   = new JsonObject();

        main_frame.add("name", new JsonPrimitive(Ozark.main_hud.get_frame_hud().get_name()));
        main_frame.add("tag",  new JsonPrimitive(Ozark.main_hud.get_frame_hud().get_tag()));
        main_frame.add("x",    new JsonPrimitive(Ozark.main_hud.get_frame_hud().get_x()));
        main_frame.add("y",    new JsonPrimitive(Ozark.main_hud.get_frame_hud().get_y()));

        for (Pinnable pinnables_hud : Ozark.get_hud_manager().get_array_huds()) {
            JsonObject frame_info = new JsonObject();

            frame_info.add("title", new JsonPrimitive(pinnables_hud.get_title()));
            frame_info.add("tag",   new JsonPrimitive(pinnables_hud.get_tag()));
            frame_info.add("state", new JsonPrimitive(pinnables_hud.is_active()));
            frame_info.add("dock",  new JsonPrimitive(pinnables_hud.get_dock()));
            frame_info.add("x",     new JsonPrimitive(pinnables_hud.get_x()));
            frame_info.add("y",     new JsonPrimitive(pinnables_hud.get_y()));

            main_hud.add(pinnables_hud.get_tag(), frame_info);
        }

        main_json.add("frame", main_frame);
        main_json.add("hud",   main_hud);

        JsonElement pretty_json = parser.parse(main_json.toString());

        String json = gson.toJson(pretty_json);

        delete_file(HUD_DIR);
        verify_file(HUD_PATH);

        OutputStreamWriter file;

        file = new OutputStreamWriter(new FileOutputStream(HUD_DIR), StandardCharsets.UTF_8);
        file.write(json);

        file.close();
    }

    private void load_hud() throws IOException {
        InputStream input_stream  = Files.newInputStream(HUD_PATH);
        JsonObject  main_hud   = new JsonParser().parse(new InputStreamReader(input_stream)).getAsJsonObject();
        JsonObject  main_frame = main_hud.get("frame").getAsJsonObject();
        JsonObject  main_huds  = main_hud.get("hud").getAsJsonObject();

        Ozark.main_hud.get_frame_hud().set_x(main_frame.get("x").getAsInt());
        Ozark.main_hud.get_frame_hud().set_y(main_frame.get("y").getAsInt());

        for (Pinnable pinnables : Ozark.get_hud_manager().get_array_huds()) {
            JsonObject hud_info = main_huds.get(pinnables.get_tag()).getAsJsonObject();

            Pinnable pinnable_requested = Ozark.get_hud_manager().get_pinnable_with_tag(hud_info.get("tag").getAsString());

            if (pinnable_requested == null) continue;

            pinnable_requested.set_active(hud_info.get("state").getAsBoolean());
            pinnable_requested.set_dock(hud_info.get("dock").getAsBoolean());

            pinnable_requested.set_x(hud_info.get("x").getAsInt());
            pinnable_requested.set_y(hud_info.get("y").getAsInt());
        }

        input_stream.close();
    }

    // LOAD & SAVE BINDS

    private void save_binds() throws IOException {
        final String file_name = ACTIVE_CONFIG_FOLDER + "BINDS.txt";
        final Path file_path = Paths.get(file_name);

        this.delete_file(file_name);
        this.verify_file(file_path);
        final File file = new File(file_name);
        final BufferedWriter br = new BufferedWriter(new FileWriter(file));
        for (final Module modules : Ozark.get_module_manager().get_array_modules()) {
            br.write(modules.get_tag() + ":" + modules.get_bind(1) + ":" + modules.is_active() + "\r\n");
        }
        br.close();
    }

    private void load_binds() throws IOException {

        final String file_name = ACTIVE_CONFIG_FOLDER + "BINDS.txt";
        final File file = new File(file_name);
        final FileInputStream fi_stream = new FileInputStream(file.getAbsolutePath());
        final DataInputStream di_stream = new DataInputStream(fi_stream);
        final BufferedReader br = new BufferedReader(new InputStreamReader(di_stream));
        String line;
        while ((line = br.readLine()) != null) {
            try {
                final String colune = line.trim();
                final String tag = colune.split(":")[0];
                final String bind = colune.split(":")[1];
                final String active = colune.split(":")[2];
                final Module module = Ozark.get_module_manager().get_module_with_tag(tag);
                if (module == null) continue;
                module.set_bind(Integer.parseInt(bind));
                module.set_active(Boolean.parseBoolean(active));
            } catch (Exception ignored) {}
        }
        br.close();

    }

    // LOAD & SAVE SETTINGS

    public void save_settings() {
        try {
            verify_dir(MAIN_FOLDER_PATH);
            verify_dir(CONFIGS_FOLDER_PATH);
            verify_dir(ACTIVE_CONFIG_FOLDER_PATH);
            save_modules();
            save_binds();
            save_friends();
            save_enemies();
            save_client();
            save_drawn();
            save_ezmessage();
            save_hud();
            save_kitmessage();
            save_past_gui();
            save_xray();
            save_spammer();
            save_display_name();
            save_modules();
            send_minecraft_log("Saved settings");
        } catch (IOException e) {
            send_minecraft_log("Something has gone wrong while saving settings please report it to trambled");
            send_minecraft_log(e.toString());
        }
    }

    public void load_settings() {
        try {
            load_client();
            load_drawn();
            load_enemies();
            load_ezmessage();
            load_friends();
            load_kitmessage();
            load_past_gui();
            load_xray();
            load_display_name();
            load_modules();
            load_binds();
            try {
                load_hud();
            } catch (Exception e) {
                send_minecraft_log("something has gone wrong while loading hud");
            }
        } catch (IOException e) {
            send_minecraft_log("Something has gone wrong while loading settings please report it to trambled");
            send_minecraft_log(e.toString());
        }
    }

    // GENERAL UTIL

    public boolean delete_file(final String path) throws IOException {
        final File f = new File(path);
        return f.delete();
    }

    public void verify_file(final Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    public boolean verify_file_without_creating(final Path path) throws IOException {
        return Files.exists(path);
    }

    public void verify_dir(final Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }
}
