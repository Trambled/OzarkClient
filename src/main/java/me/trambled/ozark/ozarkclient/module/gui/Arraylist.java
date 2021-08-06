package me.trambled.ozark.ozarkclient.module.gui;

import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.util.font.FontUtil;
import me.trambled.ozark.ozarkclient.util.misc.DrawnUtil;
import me.trambled.ozark.ozarkclient.util.render.RainbowUtil;
import me.trambled.ozark.ozarkclient.util.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.awt.*;
import java.util.Comparator;


public class Arraylist extends Module {
    public Arraylist() {
        super(Category.GUI);
        this.name = "ArrayList";
        this.tag = "ArrayList";
        this.description = "Working customfont araylist with rainbow rolling";

    }
    Setting background = create("RainbowBackground", "Background", false);
    Setting rainbow = create("Rainbow", "Rainbow", false);
    Setting red = create("Red", "Red", 255, 0, 255);
    Setting green = create("Green", "Green",  255, 0, 255);
    Setting blue = create("Blue", "Blue", 255, 0, 255);

    int modCount;
    public boolean flag;




    public void render() {
            modCount = 0;
            int[] counter = {1};
            final ScaledResolution resolution = new ScaledResolution(mc);
            Ozark.get_module_manager().get_array_modules()
                    .stream()
                    .filter(Module::is_active)
                    .sorted(Comparator.comparing(module -> FontUtil.getFontWidth(module.array_detail() == null ? module.get_name() : module.get_name() + " [" + Ozark.w + module.array_detail() + Ozark.r  + "]") * (-1)))
                    .forEach(m -> {
                        flag = true;
                        for (String s : DrawnUtil.hidden_tags) {
                            if (m.get_tag().equalsIgnoreCase(s)) {
                                flag = false;
                                break;
                            }
                            if (!flag) break;
                        }
                        if (flag) {
                            String mod = m.array_detail() == null ? m.get_name() : m.get_name() + " [" + Ozark.w + m.array_detail() + Ozark.r + "]";
                            int x = resolution.getScaledWidth();
                            if (background.get_value(true)) {
                                 x = resolution.getScaledWidth() - 1;
                                drawRecta(x - 1, 1 + (modCount * 10), 2, FontUtil.getFontHeight() + 2, rainbow.get_value(true) ? RainbowUtil.rainbow(counter[0] * 100) : new Color(red.get_value(1), green.get_value(1), blue.get_value(1), 255).getRGB());
                            }
                            FontUtil.drawStringWithShadow(mod, x - 2 - FontUtil.getFontWidth(mod), 1 + (modCount * 10), rainbow.get_value(true) ? RainbowUtil.rainbow(counter[0] * 100) : new Color(red.get_value(1), green.get_value(1), blue.get_value(1), 255).getRGB());
                            modCount++;
                            counter[0]++;
                        }});
        }

    public static void drawRecta(float x, float y, float w, float h, int color) { //ignore this
        float lvt_5_2_;
        float p_drawRect_2_ = x + w;
        float p_drawRect_3_ = y + h;
        if (x < p_drawRect_2_) {
            lvt_5_2_ = x;
            x = p_drawRect_2_;
            p_drawRect_2_ = lvt_5_2_;
        }

        if (y < p_drawRect_3_) {
            lvt_5_2_ = y;
            y = p_drawRect_3_;
            p_drawRect_3_ = lvt_5_2_;
        }

        float lvt_5_3_ = (float) (color >> 24 & 255) / 255.0F;
        float lvt_6_1_ = (float) (color >> 16 & 255) / 255.0F;
        float lvt_7_1_ = (float) (color >> 8 & 255) / 255.0F;
        float lvt_8_1_ = (float) (color & 255) / 255.0F;
        Tessellator lvt_9_1_ = Tessellator.getInstance();
        BufferBuilder lvt_10_1_ = lvt_9_1_.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(lvt_6_1_, lvt_7_1_, lvt_8_1_, lvt_5_3_);
        lvt_10_1_.begin(7, DefaultVertexFormats.POSITION);
        lvt_10_1_.pos(x, p_drawRect_3_, 0.0D).endVertex();
        lvt_10_1_.pos(p_drawRect_2_, p_drawRect_3_, 0.0D).endVertex();
        lvt_10_1_.pos(p_drawRect_2_, y, 0.0D).endVertex();
        lvt_10_1_.pos(x, y, 0.0D).endVertex();
        lvt_9_1_.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

}