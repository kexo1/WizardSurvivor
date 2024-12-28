import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Enemy extends GameObj {

    // References
    private Player player;
    private ObjManager manager;
    private long currentTime = System.currentTimeMillis();
    private long lastShotTime = 0;

    // Stats
    private int hp = 100;
    private int damage = 20;
    private int speed = 3;
    private int hitDelay = 1000;
    private int hitDistance = 20;

    // Movement
    private float velX;
    private float velY;
    private int x;
    private int y;

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Enemy(int x, int y, GameObjID gameObjID, ObjManager manager, Player player, SpriteSheet spriteSheet) {
        super(x, y, gameObjID, manager, spriteSheet);
        this.player = player;
        this.manager = manager;
        this.x = x;
        this.y = y;
    }

    public void tick() {
        this.x += this.velX;
        this.y += this.velY;

        this.collisionDetection();
        this.chasePlayer();
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.fillRect(this.x, this.y, 32, 32);
    }

    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, 32, 32);
    }

    private void collisionDetection() {
        for (int i = 0; i < this.manager.getObjList().size(); i++) {
            GameObj obj = this.manager.getObjList().get(i);
            
            if (obj.getId() == GameObjID.Orb) {

                if (this.getBounds().intersects(obj.getBounds())) {
                    this.takeDamage(this.player.getDamage());
                    // Orb removes after hit
                    this.manager.removeObj(obj);
                }
            }

            if (obj.getId() == GameObjID.Player) {
                if (this.getBounds().intersects(obj.getBounds())) {
                    this.currentTime = System.currentTimeMillis();
                    
                    if (this.currentTime - this.lastShotTime < this.hitDelay) {
                        return;
                    }
                    
                    this.lastShotTime = this.currentTime;
                    this.player.takeDamage(this.damage);
                    
                }
            }
        }
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) {
            System.out.println("Enemy died");
            this.manager.removeObj(this);
        }
    }

    public void chasePlayer() {
        int playerX = this.player.getX();
        int playerY = this.player.getY();

        double dx = playerX - this.x;
        double dy = playerY - this.y;
        double length = Math.sqrt(dx * dx + dy * dy);
        // If inside player lenght can be 0, which will cause error
        if (length > this.hitDistance) {
            double normalizedX = dx / length;
            double normalizedY = dy / length;

            this.velX = (float)(normalizedX * this.speed);
            this.velY = (float)(normalizedY * this.speed);
        } else {
            this.velX = 0;
            this.velY = 0;
        }
    }
}