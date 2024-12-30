import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Sting extends GameObj {
    
    // References
    private ObjManager manager;
    private BufferedImage sprite;

    // Attributes
    private long timer = System.currentTimeMillis();
    private int speed = 10;
    private float velX;
    private float velY;
    private int x;
    private int y;
    private int playerX;
    private int playerY;
    private double angle;

    public Sting(int x, int y, GameObjID gameObjID, ObjManager manager, SpriteSheet spriteSheet, BufferedImage sprite, int playerX, int playerY) {
        super(x, y, gameObjID, manager, spriteSheet);
        this.x = x;
        this.y = y;
        this.playerX = playerX;
        this.playerY = playerY;
        this.manager = manager;
        this.sprite = sprite;
        this.angle = Math.atan2(this.playerY - this.y, this.playerX - this.x);
        this.normalizeDirection(playerX, playerY);
        
    }

    public void tick() {
        this.updatePosition();
        this.timer();
    }
    
    public void render(Graphics graphics) {

        // Not mine!!
        Graphics2D g2d = (Graphics2D)graphics;
        AffineTransform old = g2d.getTransform();
        g2d.rotate(this.angle, this.x + this.sprite.getWidth(null) / 2, this.y + this.sprite.getHeight(null) / 2);
        g2d.drawImage(this.sprite, this.x, this.y, 16, 8, null);
        g2d.setTransform(old);
    }

    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, 12, 12);
    }

    private void updatePosition() {
        this.x += this.velX;
        this.y += this.velY;
    }

    private void timer() {
        if (System.currentTimeMillis() - this.timer > 2000) {
            this.manager.removeObj(this);
        }
    }

    private void normalizeDirection(int mouseX, int mouseY) {
        double dx = mouseX - this.x;
        double dy = mouseY - this.y;

        // Length of the vector
        double length = Math.sqrt(dx * dx + dy * dy);
        double normalizedX = dx / length;
        double normalizedY = dy / length;
        // Values between -1 and 1
        this.velX = (float)(normalizedX * this.speed);
        this.velY = (float)(normalizedY * this.speed);
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }
}
