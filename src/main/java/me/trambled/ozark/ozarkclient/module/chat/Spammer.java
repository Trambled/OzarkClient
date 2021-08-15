package me.trambled.ozark.ozarkclient.module.chat;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// cb+
public class Spammer extends Module {

    public Spammer() {
        super(Category.CHAT);

        this.name = "Spammer";
        this.tag = "Spammer";
        this.description = "Spams stuff in chat.";
        FILE_DIRECTORY = new File(mc.gameDir, "OZARKCLIENT");
    }

    Setting mode = create("Mode", "SpammerMode", "Random", combobox("Random", "Ordered"));
    Setting delay = create("Delay", "SpammerDelay", 10, 0, 100);
    public File FILE_DIRECTORY;
    int tick_delay;
    private final List<String> temp_lines = new ArrayList <> ( );
    private String[] lines;
    private static int current_line = 0;

    @Override
    public void enable() {
        try {
            String line;
            final File file = new File(FILE_DIRECTORY, "spammer.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            this.temp_lines.clear();
            while ((line = br.readLine()) != null) {
                this.temp_lines.add(line);
            }
            br.close();
            this.lines = this.temp_lines.toArray(new String[0]);
        }
        catch (IOException ignored ) { }
    }

    @Override
    public void update() {
        String message = "";
        tick_delay++;
        if (tick_delay < delay.get_value(1)*10) return;
        if (mode.in("Ordered")) {
            message = get_ordered(lines);
        } else {
            message = get_random(lines);
        }
        if (!message.equals("")) {
            mc.player.sendChatMessage(message);
        }
        tick_delay = 0;
    }

    private static String get_ordered(String[] array) {
        if (++current_line > array.length - 1) {
            current_line = 0;
        }
        return array[current_line];
    }

    private static String get_random(String[] array) {
        int rand = new Random().nextInt(array.length);
        while (array[rand].isEmpty() || array[rand].equals(" ")) {
            rand = new Random().nextInt(array.length);
        }
        return array[rand];
    }
}
