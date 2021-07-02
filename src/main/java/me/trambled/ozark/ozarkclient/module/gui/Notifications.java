package me.trambled.ozark.ozarkclient.module.gui;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

import java.awt.*;

public class Notifications extends Module {
    public Notifications() {
        super(Category.GUI);

        this.name = "Notifications";
        this.tag = "Notifications";
        this.description = "GUI for notifications";
    }

    Setting mode = create("Mode", "NotifMode", "Downright", combobox("Upleft", "Downleft"));
    Setting regular_messages = create("Regular Messages", "NotifRegularMessages", true);
    Setting message_width = create("Message Width", "NotifMessageWidth", false);
    Setting info = create("Info", "NotifInfo", true);
    Setting max_notifs = create("Max Notifs", "NotifMaxNotifications", 5, 1, 10);
    Setting time = create("Time", "NotifTime", 3000, 0, 10000);
    Setting y_off_height = create("Y Off Height", "NotifYOffHeight", 25, 1, 25);
    Setting y_off = create("Y Off", "NotifYOff", 0, 0, 50);
    Setting r = create("R", "NotifR", 255, 0, 255);
    Setting g = create("G", "NotifG", 0, 0, 255);
    Setting b = create("B", "NotifB", 0, 0, 255);
    Setting a = create("A", "NotifA", 255, 0, 255);
    Setting background_r = create("Background R", "NotifBackgroundR", 17, 0, 255);
    Setting background_g = create("Background G", "NotifBackgroundG", 17, 0, 255);
    Setting background_b = create("Background B", "NotifBackgroundB", 17, 0, 255);
    Setting background_a = create("Background A", "NotifBackgroundA", 255, 0, 255);
    Setting rainbow = create("Rainbow", "NotifRainbow", true);
    Setting name_r = create("Name R", "NotifNameR", 0, 0, 255);
    Setting name_g = create("Name G", "NotifNameG", 0, 0, 255);
    Setting name_b = create("Name B", "NotifNameB", 0, 0, 255);
    Setting name_a = create("Name A", "NotifNameA", 255, 0, 255);
    Setting name_rainbow = create("Name Rainbow", "NotifNameRainbow", true);
    Setting name_flow = create("Name Flow", "NotifNameFlow", true);
    Setting notif_r = create("Notif R", "NotifNotifR", 255, 0, 255);
    Setting notif_g = create("Notif G", "NotifNotifG", 255, 0, 255);
    Setting notif_b = create("Notif B", "NotifNotifB", 255, 0, 255);
    Setting notif_a = create("Notif A", "NotifNotifA", 255, 0, 255);
    Setting notif_rainbow = create("Notif Rainbow", "NotifNotifRainbow", false);
    Setting notif_flow = create("Notif Flow", "NotifNotifFlow", false);

    @Override
    public void update() {
        if (rainbow.get_value(true)) {
            cycle_rainbow();
        }
        if (name_rainbow.get_value(true)) {
            cycle_rainbow_name();
        }
        if (notif_rainbow.get_value(true)) {
            cycle_rainbow_notif();
        }
    }

    public void cycle_rainbow() {
        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

        r.set_value((color_rgb_o >> 16) & 0xFF);
        g.set_value((color_rgb_o >> 8) & 0xFF);
        b.set_value(color_rgb_o & 0xFF);
    }

    public void cycle_rainbow_name() {
        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

        name_r.set_value((color_rgb_o >> 16) & 0xFF);
        name_g.set_value((color_rgb_o >> 8) & 0xFF);
        name_b.set_value(color_rgb_o & 0xFF);
    }

    public void cycle_rainbow_notif() {
        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

        notif_r.set_value((color_rgb_o >> 16) & 0xFF);
        notif_g.set_value((color_rgb_o >> 8) & 0xFF);
        notif_b.set_value(color_rgb_o & 0xFF);
    }


}
