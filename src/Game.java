import GameLogic.Entity;
import Graphics.Assets;
import Graphics.Display;
import Graphics.DrawInterface;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Game implements DrawInterface {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static final int FPS = 200;
    public static final int DELAY = (int) (1000.0f / FPS);
    public static final int ENEMIES = 20;
    public static final int MOVEMENT_SPEED = 3;
    public static final int SHOOT_DELAY = 1500;
    public static final int SHOOT_DELAY_RND = 5000;
    public static final int ENEMIES_COL = 10;
    public static final int GAP = 2;
    public static final float ENEMY_VEL = 0.25f;

    private Display display;
    private Entity player;
    private ArrayList <Entity> enemies;
    private ArrayList<Entity> bullets;

    private BufferedImage playerSprite;
    private BufferedImage enemySprite;
    private BufferedImage bulletSprite;

    private long shoot;

    public Game() {
        init();
    }

    public void init() {
        playerSprite = Assets.loadImg("player.png");
        enemySprite = Assets.loadImg("enemy.png");
        bulletSprite = Assets.loadImg("bullet.png");
        display = new Display(WIDTH, HEIGHT, this, "SpaceInvaders");
        this.player = new Entity((WIDTH>>2)-(playerSprite.getWidth()>>1), (HEIGHT>>1)-(playerSprite.getHeight()*2), playerSprite);
        this.enemies = new ArrayList<>(ENEMIES);
        this.bullets = new ArrayList<>();

        int rows = (int) ((ENEMIES / (float)ENEMIES_COL) + 1);
        int lastrow = rows-1;
        long time = System.currentTimeMillis();
        for (int j = 0; j < rows-1; j++) {
            for (int i = 0; i < ENEMIES_COL; i++) {
                enemies.add(new Entity(i * enemySprite.getWidth() + i * GAP, j * enemySprite.getHeight() + j * GAP, enemySprite));
                enemies.get(enemies.size()-1).setLife(100);
                enemies.get(enemies.size()-1).setDir(-ENEMY_VEL, 0);
                enemies.get(enemies.size()-1).setTime(time + ((int) (Math.random() * SHOOT_DELAY_RND)));
            }
        }

        for (int i = 0; i < ENEMIES % ENEMIES_COL; i++) {
            enemies.add(new Entity(i * enemySprite.getWidth() + i * GAP, lastrow * enemySprite.getHeight() + lastrow * GAP, enemySprite));
            enemies.get(enemies.size()-1).setLife(100);
            enemies.get(enemies.size()-1).setDir(-ENEMY_VEL, 0);
            enemies.get(enemies.size()-1).setTime(time + ((int) (Math.random() * SHOOT_DELAY_RND)));
        }
        loop();
    }

    public void loop() {
        while (true) {
            long time = System.currentTimeMillis();

            display.display();
            input();
            iterate();

            long delta = System.currentTimeMillis() - time;

            try {
                if (DELAY > delta)
                    Thread.sleep(DELAY - delta);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void iterate() {
        boolean changeDir = false;
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).iterate();
        }

        for (int i = 0; i < bullets.size(); i++) {
            Entity tmp = bullets.get(i);
            if (tmp.getY()+tmp.getHeight() < 0)
                bullets.remove(i--);
            else if (tmp.getY() > HEIGHT>>1)
                bullets.remove(i--);

            for (int j = 0; j < enemies.size(); j++) {
                if (tmp.collides(enemies.get(j)) && tmp.getDirY() < 0) {
                    enemies.get(j).addLife(-50);
                    if (enemies.get(j).isDead())
                        enemies.remove(j);
                    bullets.remove(i--);
                    break;
                }
            }
        }

        long time = System.currentTimeMillis();
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).iterate();
            if (enemies.get(i).getX() <= 0 || enemies.get(i).getX() + enemies.get(i).getWidth() >= WIDTH>>1) {
                changeDir = true;
            }
            if (enemies.get(i).getDelta(time) > SHOOT_DELAY) {
                Entity tmp = new Entity((int) (enemies.get(i).getX()+(enemies.get(i).getWidth()>>1)), (int) enemies.get(i).getY()+enemies.get(i).getHeight()+1, bulletSprite);
                tmp.setDir(0, 1);
                bullets.add(tmp);
                enemies.get(i).setTime(time + (int) (Math.random() * SHOOT_DELAY_RND));
            }
        }
        if (changeDir) {
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).setDir(-enemies.get(i).getDirX(), 0);
                enemies.get(i).iterate();
                enemies.get(i).iterate();
                enemies.get(i).add(0, enemies.get(i).getHeight());
            }
        }
    }

    public void input() {
        long time = System.currentTimeMillis();
        if (display.grabKeys()[KeyEvent.VK_LEFT] || display.grabKeys()[KeyEvent.VK_A]) {
            if ((player.getX()+(player.getWidth()>>1)-MOVEMENT_SPEED) > 0)
                player.add(-MOVEMENT_SPEED, 0);
        } else if (display.grabKeys()[KeyEvent.VK_RIGHT] || display.grabKeys()[KeyEvent.VK_D]) {
            if (((player.getX()+(player.getWidth()>>1))+MOVEMENT_SPEED) < WIDTH>>1)
                player.add(MOVEMENT_SPEED, 0);
        }
        if (display.grabKeys()[KeyEvent.VK_SPACE] && (time - shoot) > 200) {
            Entity tmp = new Entity((int) (player.getX()+(player.getWidth()>>1)), (int) (player.getY()-bulletSprite.getHeight()), bulletSprite);
            tmp.setDir(0, -1);
            bullets.add(tmp);
            shoot = time;
        }
    }

    public void draw(Graphics2D graphics) {
        player.draw(graphics);
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(graphics);
        }
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(graphics);
        }
    }
}
