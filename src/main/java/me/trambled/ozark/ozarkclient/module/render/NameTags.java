package me.trambled.ozark.ozarkclient.module.render;

import me.trambled.ozark.ozarkclient.event.events.EventRender;
import me.trambled.ozark.ozarkclient.event.events.EventRenderName;
import me.trambled.ozark.ozarkclient.manager.TotempopManager;
import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;
import me.trambled.ozark.ozarkclient.module.chat.TotemPopCounter;
import me.trambled.ozark.ozarkclient.util.font.FontUtil;
import me.trambled.ozark.ozarkclient.util.player.social.EnemyUtil;
import me.trambled.ozark.ozarkclient.util.player.social.FriendUtil;
import me.trambled.ozark.ozarkclient.util.render.RenderUtil;
import me.trambled.turok.draw.RenderHelp;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;

// gamesense
public class NameTags extends Module {

    public NameTags() {
        super(Category.RENDER);

        this.name = "Nametags";
        this.tag = "NameTags";
        this.description = "future nametags but in OZARK HOW SNINEHACK .";
    }


    Setting r = create("R", "NametagR", 255, 0, 255);
    Setting g = create("G", "NametagG", 255, 0, 255);
    Setting b = create("B", "NametagB", 255, 0, 255);
    Setting a = create("A", "NametagA", 0.7f, 0f, 1f);
    Setting range = create("Range",  "NametagRange", 100, 10, 260);
    Setting renderSelf = create("Render Self", "NametagRenderSelf", false);
    Setting showTotem = create("Totetms popped", "NametagPROMOMENTOALLAHUAKBAR", false);
    Setting showDurability = create("Durability", "NametagDurability", true);
    Setting showItems = create("Items", "NametagItems", true);
    Setting showEnchantName = create("Enchants", "NametagEnchants", true);
    Setting showItemName = create("Item Name", "NametagItemName", false);
    Setting showGameMode = create("Gamemode", "NametagGamemode", false);
    Setting showHealth = create("Health", "NametagHealth", true);
    Setting showPing = create("Ping", "NametagPing", false);
    Setting pingcolor = create("Show Ping Color", "NametagPingcolor", true);
    Setting showEntityID = create("Entity Id", "NametagEntityID", false);
    Setting levelColor = create("Level Color", "NametagLevelColor", "Green", RenderUtil.colors);
    Setting customColor = create("Border Color", "NametagCustomColor", true);
    Setting border_r = create("Border R", "NametagBorderR", 255, 0, 255);
    Setting border_g = create("Border G", "NametagBorderG", 0, 0, 255);
    Setting border_b = create("Border B", "NametagBorderB", 0, 0, 255);
    Setting border_a = create("Border A", "NametagBorderA", 255, 0, 255);
    Setting enemy_r = create("Enemy R", "NametagEnemyR", 157, 0, 255);
    Setting enemy_g = create("Enemy G", "NametagEnemyG", 99, 0, 255);
    Setting enemy_b = create("Enemy B", "NametagEnemyB", 255, 0, 255);
    Setting friend_r = create("Friend R", "NametagFriendNameR", 255, 0, 255);
    Setting friend_g = create("Friend G", "NametagFriendNameG", 40, 0, 255);
    Setting friend_b = create("Friend B", "NametagFriendNameB", 7, 0, 255);

    @EventHandler
    private final Listener<EventRenderName> on_render_name = new Listener<>(event -> {
            event.cancel();
    });

    @Override
    public void render(EventRender event) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        mc.world.playerEntities.stream().filter(this::shouldRender).forEach(entityPlayer -> {
            Vec3d vec3d = findEntityVec3d(entityPlayer);
            renderNameTags(entityPlayer, vec3d.x, vec3d.y, vec3d.z);
        });
    }

    private boolean shouldRender(EntityPlayer entityPlayer) {

        if (entityPlayer.getName() == mc.player.getName() && !renderSelf.get_value(true)) return false;

        if (entityPlayer == mc.player && !renderSelf.get_value(true)) return false;

        if (entityPlayer.isDead || entityPlayer.getHealth() <= 0) return false;

        return !(entityPlayer.getDistance(mc.player) > range.get_value(1));
    }

    private Vec3d findEntityVec3d(EntityPlayer entityPlayer) {
        double posX = balancePosition(entityPlayer.posX, entityPlayer.lastTickPosX);
        double posY = balancePosition(entityPlayer.posY, entityPlayer.lastTickPosY);
        double posZ = balancePosition(entityPlayer.posZ, entityPlayer.lastTickPosZ);

        return new Vec3d(posX, posY, posZ);
    }

    private double balancePosition(double newPosition, double oldPosition) {
        return oldPosition + (newPosition - oldPosition) * mc.timer.renderPartialTicks;
    }

    private void renderNameTags(EntityPlayer entityPlayer, double posX, double posY, double posZ) {
        double adjustedY = posY + (entityPlayer.isSneaking() ? 1.9 : 2.1);

        String[] name = new String[1];
        name[0] = buildEntityNameString(entityPlayer);
        RenderUtil.drawNametag(posX, adjustedY, posZ, name, findTextColor(entityPlayer), r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1d),  2, customColor.get_value(true), new Color(border_r.get_value(1), border_g.get_value(1), border_b.get_value(1), border_a.get_value(1)));
        renderItemsAndArmor(entityPlayer, 0, 0);
        GlStateManager.popMatrix();
        }

    private String buildEntityNameString(EntityPlayer entityPlayer) {
        String name = entityPlayer.getName();

        if (showEntityID.get_value(true)) {
            name = name + " ID: " + entityPlayer.getEntityId();
        }

        if (showGameMode.get_value(true)) {
            if (entityPlayer.isCreative()) {
                name = name + " [C]";
            }
            else if (entityPlayer.isSpectator()) {
                name = name + " [I]";
            }
            else {
                name = name + " [S]";
            }
        }


        if (showPing.get_value(true)) {
            int value = 0;

            if (mc.getConnection() != null && mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID()) != null) {
                value = mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime();

            }
            TextFormatting textFormatting = findPingColor(value);
            if (pingcolor.get_value(false)) {
                name = name + " " + textFormatting + value + "ms";
            }else{
                name = name + " " + value + "ms";
            }
        }

        if (showHealth.get_value(true)) {
            int health = (int) (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount());
            TextFormatting textFormatting = findHealthColor(health);

            name = name + " " + textFormatting + health;
        }
        if (showTotem.get_value(true)) {
            int pop = 0;
            TextFormatting textFormatting = findPopColor(pop);
            if (TotempopManager.getPops(entityPlayer.getName()) != 0) {
                pop = TotempopManager.getPops(entityPlayer.getName());
            }
            if (pop != 0) {
                name = name +  textFormatting + " -" + pop;
            }}

        return name;
    }

    private TextFormatting findHealthColor(int health) {
        if (health <= 0) {
            return TextFormatting.DARK_RED;
        } else if (health <= 5) {
            return TextFormatting.RED;
        } else if (health <= 10) {
            return TextFormatting.GOLD;
        } else if (health <= 15) {
            return TextFormatting.YELLOW;
        } else if (health <= 20) {
            return TextFormatting.DARK_GREEN;
        }

        return TextFormatting.GREEN;
    }

    private Color findTextColor(EntityPlayer entityPlayer) {
        if (FriendUtil.isFriend(entityPlayer.getName())) {
            return new Color(friend_r.get_value(1), friend_g.get_value(1), friend_b.get_value(1));
        } else if (EnemyUtil.isEnemy(entityPlayer.getName())) {
            return new Color(enemy_r.get_value(1), enemy_g.get_value(1), enemy_b.get_value(1));
        } else if (entityPlayer.isInvisible()) {
            return new Color(128, 128, 128);
        } else {
            if ( mc.getConnection ( ) != null ) {
                mc.getConnection ( ).getPlayerInfo ( entityPlayer.getUniqueID ( ) );
            }
            if (entityPlayer.isSneaking()) {
                return new Color(255, 153, 0);
            } else if (entityPlayer.getName() == mc.player.getName()) {
                return new Color(0, 200, 0);
            }
        }

        return new Color(255, 255, 255);
    }
    private TextFormatting findPopColor(int pop) {
          if (pop == 1) {
            return TextFormatting.GREEN;
        } else if (pop == 2) {
            return TextFormatting.DARK_GREEN;
        } else if (pop == 3) {
            return TextFormatting.YELLOW;
        } else if (pop == 4) {
            return TextFormatting.GOLD;
          } else if (pop <= 5) {
        return TextFormatting.RED;
    }else{

        return TextFormatting.DARK_RED;
    }}
    private TextFormatting findPingColor(int ping) {
        if (ping <= 100) {
            return TextFormatting.GREEN;
        } else if (ping <= 170) {
            return TextFormatting.YELLOW;
        } else if (ping <= 200) {
            return TextFormatting.RED;
        }

        return TextFormatting.DARK_RED;
    }

    private void renderItemsAndArmor(EntityPlayer entityPlayer, int posX, int posY) {
        ItemStack mainHandItem = entityPlayer.getHeldItemMainhand();
        ItemStack offHandItem = entityPlayer.getHeldItemOffhand();

        int armorCount = 3;
        for (int i = 0; i <= 3; i++) {
            ItemStack itemStack = entityPlayer.inventory.armorInventory.get(armorCount);

            if (!itemStack.isEmpty()) {
                posX -= 8;

                int size = EnchantmentHelper.getEnchantments(itemStack).size();

                if (showItems.get_value(true) && size > posY) {
                    posY = size;
                }
            }
            armorCount --;
        }

        if (!mainHandItem.isEmpty() && (showItems.get_value(true) || showDurability.get_value(true) && offHandItem.isItemStackDamageable())) {
            posX -= 8;

            int enchantSize = EnchantmentHelper.getEnchantments(offHandItem).size();
            if (showItems.get_value(true) && enchantSize > posY) {
                posY = enchantSize;
            }
        }

        if (!mainHandItem.isEmpty()) {

            int enchantSize = EnchantmentHelper.getEnchantments(mainHandItem).size();

            if (showItems.get_value(true) && enchantSize > posY) {
                posY = enchantSize;
            }

            int armorY = findArmorY(posY);

            if (showItems.get_value(true) || (showDurability.get_value(true) && mainHandItem.isItemStackDamageable())) {
                posX -= 8;
            }

            if (showItems.get_value(true)) {
                renderItem(mainHandItem, posX, armorY, posY);
                armorY -= 32;
            }

            if (showDurability.get_value(true) && mainHandItem.isItemStackDamageable()) {
                renderItemDurability(mainHandItem, posX, armorY);
            }

            armorY -= (FontUtil.getFontHeight());

            if (showItemName.get_value(true)) {
                renderItemName(mainHandItem, armorY);
            }

            if (showItems.get_value(true) || (showDurability.get_value(true) && mainHandItem.isItemStackDamageable())) {
                posX += 16;
            }
        }

        int armorCount2 = 3;
        for (int i = 0; i <= 3; i++) {
            ItemStack itemStack = entityPlayer.inventory.armorInventory.get(armorCount2);

            if (!itemStack.isEmpty()) {
                int armorY = findArmorY(posY);

                if (showItems.get_value(true)) {
                    renderItem(itemStack, posX, armorY, posY);
                    armorY -= 32;
                }

                if (showDurability.get_value(true) && itemStack.isItemStackDamageable()) {
                    renderItemDurability(itemStack, posX, armorY);
                }
                posX += 16;
            }
            armorCount2--;
        }

        if (!offHandItem.isEmpty()) {
            int armorY = findArmorY(posY);

            if (showItems.get_value(true)) {
                renderItem(offHandItem, posX, armorY, posY);
                armorY -= 32;
            }

            if (showDurability.get_value(true) && offHandItem.isItemStackDamageable()) {
                renderItemDurability(offHandItem, posX, armorY);
            }
        }
    }

    private int findArmorY(int posY) {
        int posY2 = showItems.get_value(true) ? -26 : -27;
        if (posY > 4) {
            posY2 -= (posY - 4) * 8;
        }

        return posY2;
    }

    private void renderItemName(ItemStack itemStack, int posY) {
        GlStateManager.enableTexture2D();
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        FontUtil.drawStringWithShadow(itemStack.getDisplayName(), -FontUtil.getFontWidth(itemStack.getDisplayName()) / 2, posY, new Color(255, 255, 255).getRGB());
        GlStateManager.popMatrix();
        GlStateManager.disableTexture2D();
    }

    private void renderItemDurability(ItemStack itemStack, int posX, int posY) {
        float damagePercent = (itemStack.getMaxDamage() - itemStack.getItemDamage()) / (float) itemStack.getMaxDamage();

        float green = damagePercent;
        if (green > 1) green = 1;
        else if (green < 0) green = 0;

        float red = 1 - green;

        GlStateManager.enableTexture2D();
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        FontUtil.drawStringWithShadow((int) (damagePercent * 100) + "%", posX * 2, posY, new Color((int) (red * 255), (int) (green * 255), 0).getRGB());
        GlStateManager.popMatrix();
        GlStateManager.disableTexture2D();
    }

    private void renderItem(ItemStack itemStack, int posX, int posY, int posY2) {
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();

        final int posY3 = (posY2 > 4) ? ((posY2 - 4) * 8 / 2) : 0;

        mc.getRenderItem().zLevel = -150.0f;
        RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, posX, posY + posY3);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, itemStack, posX, posY + posY3);
        RenderHelper.disableStandardItemLighting();
        mc.getRenderItem().zLevel = 0.0f;
        RenderHelp.prepare_gl_2d();
        GlStateManager.pushMatrix();
        GlStateManager.scale(.5, .5, .5);
        renderEnchants(itemStack, posX, posY - 24);
        GlStateManager.popMatrix();
    }

    private void renderEnchants(ItemStack itemStack, int posX, int posY) {
        GlStateManager.enableTexture2D();

        for (Enchantment enchantment : EnchantmentHelper.getEnchantments(itemStack).keySet()) {
            if (enchantment == null) {
                continue;
            }

            if (showEnchantName.get_value(true)) {
                int level = EnchantmentHelper.getEnchantmentLevel(enchantment, itemStack);
                FontUtil.drawStringWithShadow(findStringForEnchants(enchantment, level), posX * 2, posY, new Color(255, 255, 255).getRGB());
            }
            posY += 8;
        }

        if (itemStack.getItem().equals(Items.GOLDEN_APPLE) && itemStack.hasEffect()) {
            FontUtil.drawStringWithShadow("God", posX * 2, posY, new Color(195, 77, 65).getRGB());
        }
        if (itemStack.getItem().equals(Items.END_CRYSTAL) && itemStack.hasEffect()) {
            FontUtil.drawStringWithShadow("Crystal", posX * 2, posY, new Color(141, 65, 195).getRGB());
        }

        GlStateManager.disableTexture2D();
    }

    private String findStringForEnchants(Enchantment enchantment, int level) {
        ResourceLocation resourceLocation = Enchantment.REGISTRY.getNameForObject(enchantment);

        String string = resourceLocation == null ? enchantment.getName() : resourceLocation.toString();

        int charCount = (level > 1) ? 12 : 13;

        if (string.length() > charCount) {
            string = string.substring(10, charCount);
        }

        return string.substring(0, 1).toUpperCase() + string.substring(1) + RenderUtil.settingToTextFormatting(levelColor) + ((level > 1) ? level : "");
    }

    @Override
    public void update_always() {
        border_r.set_shown(customColor.get_value(true));
        border_g.set_shown(customColor.get_value(true));
        border_b.set_shown(customColor.get_value(true));
        border_a.set_shown(customColor.get_value(true));

    }
    }
