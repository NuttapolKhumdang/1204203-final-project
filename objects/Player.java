package objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.Random;

import javax.swing.JPanel;

import config.GameConfig;
import config.XConfig;
import helper.Asset;
import helper.Network;
import helper.Position;
import scence.Game;

public class Player extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {
    private Image shipTexture = Asset.getShip(1);
    private Random random = new Random();
    public Ammo[] ammos = new Ammo[GameConfig.MAX_AMMO];

    int canvasWidth = XConfig.DISPLAY_WIDTH;
    int canvasHeight = XConfig.DISPLAY_HEIGH;

    public int id;
    private boolean player = false;
    private boolean isAPressed = false;
    private boolean isWPressed = false;
    private boolean isSPressed = false;
    private boolean isDPressed = false;

    boolean isEnded = false;
    boolean isCrashed = false;
    double rotation = 0;

    int x, y;
    int vx = 0;
    int vy = 0;
    int size = 50;
    int ammo = GameConfig.MAX_AMMO;
    int life = GameConfig.MAX_LIFE;
    int speed = GameConfig.SHIP_SPEED;

    @Override
    public void run() {
        while (true) {
            if (player && !isCrashed && !isEnded)
                move(isAPressed, isWPressed, isSPressed, isDPressed);

            if (isEnded || isCrashed) {
                move(new Position(0, -100, -100, 0));
                Game.network.emit(Network.ACTION_PLAYER_MOVE, new Position(0, x, y, rotation));
                break;
            }

            ammoObserver();
            lifeObserver();

            try {
                Thread.sleep(15 / speed);
            } catch (Exception e) {
            }
        }
    }

    public Player(int playerId) {
        this.id = playerId;
        this.player = playerId == Game.network.getPort();

        setOpaque(false);
        setSize(canvasWidth, canvasHeight);

        if (player) {
            setFocusable(true);
            requestFocusInWindow();

            addKeyListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);

            x = random.nextInt(canvasWidth - size * 2) + size;
            y = random.nextInt(canvasHeight - size * 2) + size;
        }
    }

    public void draw(Graphics g) {
        g.setColor(new Color(1f, 1f, 1f));
        g.setFont(new Font("CommitMono", Font.BOLD, 16));

        if (player && isEnded && !isCrashed) {
            g.setFont(new Font("CommitMono", Font.BOLD, 32));
            g.drawString("WIN ", XConfig.DISPLAY_WIDTH / 2 - 25, XConfig.DISPLAY_HEIGH / 2);
            return;
        }

        if (player && isCrashed) {
            g.setFont(new Font("CommitMono", Font.BOLD, 32));
            g.drawString("DIE ", XConfig.DISPLAY_WIDTH / 2 - 25, XConfig.DISPLAY_HEIGH / 2);
            return;
        }

        if (player) {
            g.drawString("HP  : " + life + "/" + GameConfig.MAX_LIFE, 10, 25);
            g.drawString("AMMO: " + ammo + "/" + GameConfig.MAX_AMMO, 10, 50);
            g.drawString("NAME: " + String.valueOf(id), 10, 75);
        } else {
            g.setFont(new Font("CommitMono", Font.PLAIN, 12));
            g.drawString("HP  : " + life + "/" + GameConfig.MAX_LIFE, x, y - 10);
        }

        g.setFont(new Font("CommitMono", Font.PLAIN, 12));
        g.drawString(String.valueOf(id), x, y + size + 20);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();

        g2d.rotate(this.rotation, x + size / 2, y + size / 2);
        g2d.drawImage(shipTexture, x, y, size, size, null);
        g2d.setTransform(originalTransform);

        for (Ammo a : ammos) {
            if (a != null)
                a.draw(g);
        }
    }

    protected double calculateRadians(int ax, int ay, int bx, int by) {
        int dx = bx - ax;
        int dy = by - ay;

        double radians = Math.atan2(dy, dx);
        radians += Math.PI / 2;

        return radians;
    }

    protected void ammoObserver() {
        for (int idx = 0; idx < ammos.length; idx++) {
            if (ammos[idx] != null && ammos[idx].isCrashed) {
                ammos[idx] = null;
                ammo++;
            }
        }
    }

    public boolean isCollision(int tx, int ty) {
        return Math.abs(x - tx) <= size - 10
                && Math.abs(y - ty) <= size - 10;
    }

    public void ended() {
        isEnded = true;
    }

    public void crashWithPlanet() {
        life--;

        x = random.nextInt(canvasWidth - size * 2) + size;
        y = random.nextInt(canvasHeight - size * 2) + size;
        Game.network.emit(Network.ACTION_PLAYER_LIFE, life);
    }

    public void crashWithAmmo(Ammo ammo) {
        if (ammo.owner == this)
            return;

        this.life--;
        Game.network.emit(Network.ACTION_PLAYER_LIFE, life);
    }

    protected void lifeObserver() {
        if (life <= 0) {
            life = 0;
            isCrashed = true;
        }
    }

    public void fire(Position position) {
        for (int idx = 0; idx < ammos.length; idx++) {
            if (ammos[idx] != null)
                continue;

            ammos[idx] = new Ammo(this, position.id, position.x, position.y, position.rotation);
            ammos[idx].start();
            ammo--;
            break;
        }
    }

    protected void fire() {
        if (ammo <= 0) {
            return; // out of ammo
        }

        for (int idx = 0; idx < ammos.length; idx++) {
            if (ammos[idx] != null)
                continue;

            int id = random.nextInt(999999);

            Game.network.emit(Network.ACTION_PLAYER_FIRE, new Position(id, x, y, rotation));
            ammos[idx] = new Ammo(this, id, x, y, rotation);
            ammos[idx].start();
            ammo--;
            break;
        }
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void move(Position p) {
        this.x = p.x;
        this.y = p.y;
        this.rotation = p.rotation;
    }

    protected void move(boolean a, boolean w, boolean s, boolean d) {
        if (s) {
            y += speed;

            if ((y + size * 2) >= canvasHeight)
                y = canvasHeight - size * 2;
        }

        if (w) {
            y -= speed;

            if (y <= size)
                y = size;
        }

        if (d) {
            x += speed;

            if ((x + size * 2) >= canvasWidth)
                x = canvasWidth - size * 2;
        }

        if (a) {
            x -= speed;

            if (x <= size)
                x = size;
        }

        if (a || w || s || d)
            Game.network.emit(Network.ACTION_PLAYER_MOVE, new Position(0, x, y, rotation));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 65) {
            // A
            isAPressed = true;
        }
        if (e.getKeyCode() == 87) {
            // W
            isWPressed = true;
        }
        if (e.getKeyCode() == 83) {
            // S
            isSPressed = true;
        }
        if (e.getKeyCode() == 68) {
            // D
            isDPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 65) {
            // A
            isAPressed = false;
        }
        if (e.getKeyCode() == 87) {
            // W
            isWPressed = false;
        }
        if (e.getKeyCode() == 83) {
            // S
            isSPressed = false;
        }
        if (e.getKeyCode() == 68) {
            // D
            isDPressed = false;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        rotation = calculateRadians(x, y, mx, my);
        Game.network.emit(Network.ACTION_PLAYER_MOVE, new Position(0, x, y, rotation));
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        fire();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
