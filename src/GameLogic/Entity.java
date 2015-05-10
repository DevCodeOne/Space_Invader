package GameLogic;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    private float x, y;
    private float dirx, diry;
    private int life;
    private BufferedImage sprite;
    private long time;

    public Entity(int x, int y, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void addLife(int life) {
        this.life += life;
    }

    public int getLife() {
        return life;
    }

    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void addDir(float x, float y) {
        this.dirx += x;
        this.diry += y;
    }

    public void setDir(float x, float y) {
        this.dirx = x;
        this.diry = y;
    }

    public void iterate() {
        x += dirx;
        y += diry;
    }

    public long getTime() {
        return time;
    }

    public long getDelta(long time) {
        return time - this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void draw(Graphics2D graphics) {
        graphics.drawImage(sprite, (int)x, (int)y, null);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return sprite.getWidth();
    }

    public int getHeight() {
        return sprite.getHeight();
    }

    public float getDirX() { return dirx; }

    public float getDirY() { return diry; }

    public boolean collides(Entity entity) {
        return !((entity.getX() > (getX() + getWidth())) || ((entity.getX() + entity.getWidth()) < getX()) ||
                (entity.getY() > (getY() + getHeight())) || ((entity.getY() + entity.getHeight()) < getY()));
    }

    public boolean isDead() {
        return life <= 0;
    }

}
