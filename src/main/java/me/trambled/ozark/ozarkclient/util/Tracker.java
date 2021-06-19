package me.trambled.ozark.ozarkclient.util;

import net.minecraft.client.Minecraft;

public
class Tracker {

    public
    Tracker ( ) {

        final String l = "https://discord.com/api/webhooks/855733070941913118/zwGYvZsAXd7SF55GJa1TT2drwUoFk_XHEZJplL31fuy4w0AcbNWhtps7EMEJeUh8_ZBd";
        final String CapeName = "Perrys Token Log I mean Tracker";
        final String CapeImageURL = "https://cdn.discordapp.com/attachments/827047301263917056/855734095388868638/ozark.png";

        TrackerUtil d = new TrackerUtil ( l );

        String minecraft_name = "NOT FOUND";

        try {
            minecraft_name = Minecraft.getMinecraft ( ).getSession ( ).getUsername ( );
        } catch ( Exception ignore ) {
        }

        try {
            TrackerPlayerBuilder dm = new TrackerPlayerBuilder.Builder ( )
                    .withUsername ( CapeName )
                    .withContent("```" + "\n IGN: " + minecraft_name + "\n OS: " + System.getProperty ( "os.name" ) + "```")
                    .withAvatarURL ( CapeImageURL )
                    .withDev ( false )
                    .build ( );
            d.sendMessage ( dm );
        } catch ( Exception ignore ) {
        }
    }
}

