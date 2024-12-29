import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Orb extends GameObj {
    
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

    public Orb(int x, int y, GameObjID gameObjID, ObjManager manager, SpriteSheet spriteSheet, BufferedImage sprite, int mouseX, int mouseY) {
        super(x, y, gameObjID, manager, spriteSheet);
        this.x = x;
        this.y = y;
        this.manager = manager;
        this.sprite = sprite;
        this.normalizeDirection(mouseX, mouseY);
        
    }

    public void tick() {
        this.updatePosition();
        this.timer();
    }
    
    public void render(Graphics graphics) {
        graphics.drawImage(this.sprite, this.x, this.y, 16, 16, null);
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
