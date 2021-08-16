package me.trambled.ozark.ozarkclient.manager;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.font.FontUtil;
import me.trambled.ozark.ozarkclient.util.render.GuiUtil;
import me.trambled.ozark.ozarkclient.util.render.RainbowUtil;
import me.trambled.ozark.ozarkclient.util.world.TimerUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;

/**
 * Made by @Trambled on 6/28/2021
 */

public class NotificationManager {

    public static ArrayList<Notification> notifications = new ArrayList<>();


    public void update() {
        if (mc.world == null || mc.player == null || !Ozark.get_module_manager().get_module_with_tag("Notifications").is_active()) {
            notifications.clear();
            return;
        }
        if (notifications.size() > Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifMaxNotifications").get_value(1)) {
            Notification notif = null;
            for (Notification notification : notifications) {
                notif = notification;
                break;
            }
            notifications.remove(notif);
        }
        notifications.removeIf(notification -> notification.remove);
        int y_off = 25 * Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifYOff").get_value(1);
        for (Notification notification : notifications) {
            notification.y_offset = y_off;
            notification.update();
            y_off += Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifYOffHeight").get_value(1);
        }

    }

    public ArrayList<Notification> get_notifications() {
        return notifications;
    }

    public void add_notification(Notification notification) {
        notifications.add(notification);
    }

    public void render() {
        try {
            for (Notification notification : notifications) {
                notification.render();
            }
        } catch (Exception ignored) {}
    }

    public static class Notification {
        public String message;
        public TimerUtil timer;
        public NotificationMode mode;
        public int y_offset = 0;
        public boolean remove = false;
        public boolean start_to_remove = false;
        public boolean added = false;
        public boolean error = false;

        private int x;
        private int y;
        private final int height = 25;
        private int width;

        private int scaled_width;
        private int scaled_height;
        private int scale_factor;

        public Notification(String message, TimerUtil timer) {
            this.message = message;
            this.timer = timer;
            this.timer.reset();
            update_settings();
            if (mode == NotificationMode.Upleft || mode == NotificationMode.Downleft) {
                if (Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifMessageWidth").get_value(true)) {
                    x = -FontUtil.getFontWidth(message) - 30;
                } else {
                    x = -160;
                }
                if (mode == NotificationMode.Downleft) {
                    y = scaled_height;
                } else {
                    y = 0;
                }
                width = 0;
            }
        }

        public void update() {
            update_settings();
            if (!added) {
                timer.reset();
            }
            if (timer.passed(Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifTime").get_value(1))) {
                start_to_remove = true;
            }


        }

        private void update_settings() {
            updateResolution();
            Setting setting = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifMode");
            if (setting.in("Upleft")) {
                this.mode = NotificationMode.Upleft;
            } else if (setting.in("Downright")) {
                this.mode = NotificationMode.Downright;
            } else if (setting.in("Downleft")) {
                this.mode = NotificationMode.Downleft;
                this.y = scaled_height;
            } else if (setting.in("Upright")) {
                this.mode = NotificationMode.Upright;
            }
        }


        public void render() {
            if (x >= 0) {
                x = 0;
                added = true;
            }
            if (!added) {
                x++;
                width++;
            }
            if (start_to_remove) {
                x--;
                width--;
            }
            if (width <= 0) {
                remove = true;
            }
            int r = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifR").get_value(1);
            int g = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifG").get_value(1);
            int b = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifB").get_value(1);
            int a = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifA").get_value(1);

            int background_r = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifBackgroundR").get_value(1);
            int background_g = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifBackgroundG").get_value(1);
            int background_b = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifBackgroundB").get_value(1);
            int background_a = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifBackgroundA").get_value(1);

            int name_r = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifNameR").get_value(1);
            int name_g = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifNameG").get_value(1);
            int name_b = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifNameB").get_value(1);
            int name_a = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifNameA").get_value(1);

            int notif_r = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifNotifR").get_value(1);
            int notif_g = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifNotifG").get_value(1);
            int notif_b = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifNotifB").get_value(1);
            int notif_a = Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifNotifA").get_value(1);

            if (this.mode == NotificationMode.Upleft) {
                GuiUtil.draw_outline(x, y + y_offset, width + 10, y + height + y_offset, r, g, b, a);
                GuiUtil.draw_rect(x, y + y_offset, width + 10, y + height + y_offset, background_r, background_g, background_b, background_a);
                if (Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifNameFlow").get_value(true)) {
                    RainbowUtil.drawRainbowStringCustomFont(Ozark.DISPLAY_NAME, x + 2, y + 3 + y_offset, new Color(name_r, name_g, name_b, name_a).getRGB(), 100f);
                } else {
                    FontUtil.drawString(Ozark.DISPLAY_NAME, x + 2, y + 3 + y_offset, name_r, name_g, name_b, name_a);
                }
                if (Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifNotifFlow").get_value(true)) {
                    RainbowUtil.drawRainbowStringChatCustomFont(message, x + 2, y + 17 + y_offset, new Color(notif_r, notif_g, notif_b, notif_a).getRGB(), 100f);
                } else {
                    FontUtil.drawString(message, x + 2, y + 15 + y_offset, notif_r, notif_g, notif_b, notif_a);
                }

                if (Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifInfo").get_value(true)) {
                    ResourceLocation info;
                    if (error) {
                        info = new ResourceLocation("custom/error.png");
                    } else {
                        info = new ResourceLocation("custom/info.png");
                    }
                    GlStateManager.enableAlpha();
                    mc.getTextureManager().bindTexture(info);
                    GlStateManager.color((float) r / 0xFF, (float) g / 0xFF, (float) b / 0xFF, (float) a / 0xFF);
                    GL11.glPushMatrix();
                    GuiScreen.drawScaledCustomSizeModalRect(x + width - 12, this.y + 4 + y_offset, 0, 0, 256, 256, 18, 18, 256, 256);
                    GL11.glPopMatrix();
                    GlStateManager.disableAlpha();
                    GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
                }
            } else {
                GuiUtil.draw_outline(x, y + y_offset, width + 10, y + height + y_offset, r, g, b, a);
                GuiUtil.draw_rect(x, y + y_offset, width + 10, y + height + y_offset, background_r, background_g, background_b, background_a);
                if (Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifNameFlow").get_value(true)) {
                    RainbowUtil.drawRainbowStringChatCustomFont(Ozark.DISPLAY_NAME, x + 2, y - 22 - y_offset, new Color(name_r, name_g, name_b, name_a).getRGB(), 100f);
                } else {
                    FontUtil.drawString(Ozark.DISPLAY_NAME, x + 2, y - 22 - y_offset, name_r, name_g, name_b, name_a);
                }
                if (Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifNotifFlow").get_value(true)) {
                    RainbowUtil.drawRainbowStringChatCustomFont(message, x + 2, y - 8 - y_offset, new Color(notif_r, notif_g, notif_b, notif_a).getRGB(), 100f);
                } else {
                    FontUtil.drawString(message, x + 2, y - 8 - y_offset, notif_r, notif_g, notif_b, notif_a);
                }

                if (Ozark.get_setting_manager().get_setting_with_tag("Notifications", "NotifInfo").get_value(true)) {
                    ResourceLocation info = new ResourceLocation("custom/info.png");
                    GlStateManager.enableAlpha();
                    mc.getTextureManager().bindTexture(info);
                    GlStateManager.color((float) r / 0xFF, (float) g / 0xFF, (float) b / 0xFF, (float) a / 0xFF);
                    GL11.glPushMatrix();
                    GuiScreen.drawScaledCustomSizeModalRect(x + width - 12, this.y - 21 - y_offset, 0, 0, 256, 256, 18, 18, 256, 256);
                    GL11.glPopMatrix();
                    GlStateManager.disableAlpha();
                    GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
                }
            }
        }

        public void updateResolution() {
            this.scaled_width = mc.displayWidth;
            this.scaled_height = mc.displayHeight;
            this.scale_factor = 1;
            final boolean flag = mc.isUnicode();
            int i = mc.gameSettings.guiScale;
            if (i == 0) {
                i = 1000;
            }
            while (this.scale_factor < i && this.scaled_width / (this.scale_factor + 1) >= 320 && this.scaled_height / (this.scale_factor + 1) >= 240) {
                ++this.scale_factor;
            }
            if (flag && this.scale_factor % 2 != 0 && this.scale_factor != 1) {
                --this.scale_factor;
            }
            final double scaledWidthD = this.scaled_width / (double)this.scale_factor;
            final double scaledHeightD = this.scaled_height / (double)this.scale_factor;
            this.scaled_width = MathHelper.ceil(scaledWidthD);
            this.scaled_height = MathHelper.ceil(scaledHeightD);
        }

    }



    public enum NotificationMode {
        Upleft,
        Upright,
        Downleft,
        Downright
    }
}
