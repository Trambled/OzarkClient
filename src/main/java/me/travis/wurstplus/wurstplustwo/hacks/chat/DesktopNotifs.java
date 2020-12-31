package me.travis.wurstplus.wurstplustwo.hacks.chat;

import java.awt.Image;
import java.awt.AWTException;
import java.awt.Toolkit;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class DesktopNotifs extends WurstplusHack
{
    public DesktopNotifs() {
        super(WurstplusCategory.WURSTPLUS_CHAT);
        this.name = "DesktopNotifs";
        this.tag = "DesktopNotifs";
        this.description = "get notifications on your desktop";
    }
    
    public static void sendNotification(final String message, final TrayIcon.MessageType messageType) {
        final SystemTray tray = SystemTray.getSystemTray();
        final Image image = Toolkit.getDefaultToolkit().createImage("emphack.png");
        final TrayIcon icon = new TrayIcon(image, "Emphack+");
        icon.setImageAutoSize(true);
        icon.setToolTip("Emphack+");
        try {
            tray.add(icon);
        }
        catch (AWTException e) {
            e.printStackTrace();
        }
        icon.displayMessage("Emphack+", message, messageType);
    }
}
