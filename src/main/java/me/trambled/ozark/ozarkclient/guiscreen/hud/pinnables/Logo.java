package me.trambled.ozark.ozarkclient.guiscreen.hud.pinnables;

import me.trambled.ozark.ozarkclient.guiscreen.hud.items.Pinnable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Logo extends Pinnable {
    
    public Logo() {
        super("Logo", "Logo", 1, 0, 0);
    }

    ResourceLocation r = new ResourceLocation("custom/logo2.png");

    @Override
	public void render() {

        GlStateManager.enableAlpha();
        mc.getTextureManager().bindTexture(r);
        GlStateManager.color(1, 1, 1, 1);

        GL11.glPushMatrix();
        GuiScreen.drawScaledCustomSizeModalRect(get_x(), get_y(), 0, 0, 786, 786, 100, 100, 786, 786);
        GL11.glPopMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);

		this.set_width(100);
		this.set_height(100);
	}


}