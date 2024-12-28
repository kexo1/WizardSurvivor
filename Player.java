import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player extends GameObj {

    // References
    private ObjManager manager;
    private BufferedImage sprite;

    // Attributes
    private int hp = 100;
    private int damage = 34;
    private int velX;
    private int velY;
    private int x;
    private int y;

    public Player(int x, int y, GameObjID id, ObjManager manager, SpriteSheet spriteSheet) {
        super(x, y, id, manager, spriteSheet);
        this.manager = manager;
        this.x = x;
        this.y = y;
        this.sprite = spriteSheet.getImage(64, 1, 1, 64, 64);
    }

    public void tick() {
        this.x += this.velX;
        this.y += this.velY;

        this.collisionDetection();
        this.movementVelocity();
        this.isOutOfBounds();
    }

    public void render(Graphics graphics) {
        graphics.drawImage(this.sprite, this.x, this.y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, 32, 64);
    }

    private void movementVelocity() {

        if (this.manager.isUp()) {
            this.velY = -5;
        } else if (this.manager.isDown()) {
            this.velY = 5;
        } else {
            this.velY = 0;
        }

        if (this.manager.isRight()) {
            this.velX = 5;
        } else if (this.manager.isLeft()) {
            this.velX = -5;
        } else {
            this.velX = 0;
        }
    }

    private void collisionDetection() {

        //for (GameObj obj : this.manager.getObjList()) {
            // Nothing for now
        //}
    }

    private void isOutOfBounds() {
        if (this.x <= 0) {
            this.x = 0;
        }
        // 32 is the width of the player + 16
        if (this.x >= 1080 - 48) {
            this.x = 1080 - 48;
        }
        if (this.y <= 0) {
            this.y = 0;
        }
        if (this.y >= 1080 - 96) {
            this.y = 1080 - 96;
        }
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) {
            System.out.println("You died");
            //this.manager.removeObj(this);
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getHp() {
        return this.hp;
    }
}
