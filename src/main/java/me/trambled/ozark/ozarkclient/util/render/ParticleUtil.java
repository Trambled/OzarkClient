package me.trambled.ozark.ozarkclient.util.render;

import me.trambled.ozark.Ozark;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// cursa
public class ParticleUtil {

    private static final float SPEED = 0.1f;
    private final List<Particle> particleList = new ArrayList<>();

    public ParticleUtil(int initAmount) {
        this.addParticles(initAmount);
    }

    public void addParticles(int n) {
        for (int i = 0; i < n; ++i) {
            this.particleList.add(Particle.generateParticle());
        }
    }

    public static double distance(float x, float y, float x1, float y1) {
        return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
    }

    public void tick(int delta) {
     if (Mouse.isButtonDown(0)) addParticles(1);
        for (Particle particle : particleList) {
            particle.tick(delta, SPEED);
        }
    }

    private void drawLine(float f, float f2, float f3, float f4, float r, float g, float b, float a) {
        GL11.glColor4f(r, g, b, a);
        GL11.glLineWidth(0.5f);
        GL11.glBegin(1);
        GL11.glVertex2f(f, f2);
        GL11.glVertex2f(f3, f4);
        GL11.glEnd();
    }

    public void render() {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);

        if (Minecraft.getMinecraft().currentScreen == null) {
            return;
        }

        for (Particle particle : particleList) {
            GL11.glColor4f((float) Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIParticleR").get_value(1) / 0xFF, (float) Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIParticleG").get_value(1) / 0xFF, (float) Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIParticleB").get_value(1) / 0xFF, (float) Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIParticleA").get_value(1) / 0xFF);
            GL11.glPointSize(particle.getSize());
            GL11.glBegin(0);
            GL11.glVertex2f(particle.getX(), particle.getY());
            GL11.glEnd();

            int Width = Mouse.getEventX() * Minecraft.getMinecraft().currentScreen.width / Minecraft.getMinecraft().displayWidth;
            int Height = Minecraft.getMinecraft().currentScreen.height - Mouse.getEventY() * Minecraft.getMinecraft().currentScreen.height / Minecraft.getMinecraft().displayHeight - 1;

            float nearestDistance = 0.0f;
            Particle nearestParticle = null;
            int dist = 100;

            for (Particle particle1 : this.particleList) {
                float distance = particle.getDistanceTo(particle1);
                if (distance > dist || distance(Width, Height, particle.getX(), particle.getY()) > dist && distance(Width, Height, particle1.getX(), particle1.getY()) > dist || nearestDistance > 0.0f && distance > nearestDistance) {
                    continue;
                }
                nearestDistance = distance;
                nearestParticle  = particle1;
            }

            if (nearestParticle == null) {
                continue;
            }
            float alpha = Math.min(1.0f, Math.min(1.0f, 1.0f - nearestDistance / dist));
            drawLine(particle.getX(), particle.getY(), nearestParticle.getX(), nearestParticle.getY(), (float) Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIParticleR").get_value(1) / 0xFF, (float) Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIParticleG").get_value(1) / 0xFF, (float) Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIParticleB").get_value(1) / 0xFF, Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIChangeLineAlpha").get_value(true) ? alpha : (float) Ozark.get_setting_manager().get_setting_with_tag("PastGUI", "PastGUIChangeLineAlpha").get_value(1) / 0xFF);
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    // cursa
    public static class Particle {

        private float alpha;
        private final Vector2f pos;
        private static final Random random = new Random();
        private float size;
        private Vector2f velocity;

        public Particle(Vector2f velocity, float x, float y, float size) {
            this.velocity = velocity;
            this.pos = new Vector2f(x, y);
            this.size = size;
        }

        public static Particle generateParticle() {
            Vector2f velocity = new Vector2f((float) (Math.random() * 2.0f - 1.0f), (float) (Math.random() * 2.0f - 1.0f));
            float x = random.nextInt(Display.getWidth());
            float y = random.nextInt(Display.getHeight());
            float size = (float) (Math.random() * 4.0f) + 1.0f;
            return new Particle(velocity, x, y, size);
        }

        public Vector2f getVelocity() {
            return velocity;
        }

        public void setVelocity(Vector2f velocity) {
            this.velocity = velocity;
        }
        public float getAlpha() {
            return this.alpha;
        }

        public float getDistanceTo(Particle particle1) {
            return getDistanceTo(particle1.getX(), particle1.getY());
        }

        public float getDistanceTo(float f, float f2) {
            return (float) distance(this.getX(), this.getY(), f, f2);
        }

        public float getSize() {
            return this.size;
        }

        public float getX() {
            return this.pos.getX();
        }

        public float getY() {
            return this.pos.getY();
        }

        public void setSize(float f) {
            this.size = f;
        }

        public void setX(float f) {
            this.pos.setX(f);
        }

        public void setY(float f) {
            this.pos.setY(f);
        }

        public void tick(int delta, float speed) {
            pos.x += velocity.getX() * delta * speed;
            pos.y += velocity.getY() * delta * speed;
            if(alpha < 255.0f)this.alpha += 0.05f * delta;

            if (pos.getX() > Display.getWidth()) pos.setX(0);
            if (pos.getX() < 0) pos.setX(Display.getWidth());

            if (pos.getY() > Display.getHeight()) pos.setY(0);
            if (pos.getY() < 0) pos.setY(Display.getHeight());
        }

    }
}
