package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;

import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;
import me.trambled.ozark.ozarkclient.util.TextureUtil;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public
class Logo extends Pinnable {

    ResourceLocation r = new ResourceLocation ( "custom/logo.png" );

    public
    Logo ( ) {
        super ( "Logo" , "Logo" , 1 , 0 , 0 );
    }

    @Override
    public
    void render ( ) {

        GL11.glPushMatrix ( );
        GL11.glTranslatef ( this.get_x ( ) , this.get_y ( ) , 0.0F );
        TextureUtil.drawTexture ( r , this.get_x ( ) , this.get_y ( ) , 460 , 425 );
        GL11.glPopMatrix ( );

        this.set_width ( 460 );
        this.set_height ( 425 );
    }


}