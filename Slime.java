import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Slime extends GameObj {

    // References
    private Player player;
    private ObjManager manager;
    private BufferedImage[] animWalk;
    private BufferedImage[] animAttack;
    private SpriteAnimation spriteAnimationAttack;
    private SpriteAnimation spriteAnimationWalk;
    private Random random = new Random();

    // Stats
    private int hp = 100;
    private int damage = 20;
    private int speed = 2;
    private int hitDelay = 1500;
    private int hitDistance = 20;

    // Movement
    private double length = 0;
    private float velX;
    private float velY;
    private int x;
    private int y;

    // Time
    private long currentTime = System.currentTimeMillis();
    private long lastShotTime = 0;
    private float lastVelX;

    public Slime(int x, int y, GameObjID gameObjID, ObjManager manager, Player player, SpriteSheet spriteSheet) {
        super(x, y, gameObjID, manager, spriteSheet);
        this.player = player;
        this.manager = manager;
        this.x = x;
        this.y = y;

        int randomIndex = (this.random.nextInt(2) == 0) ? 1 : 3;
        this.animWalk = spriteSheet.getSpriteSheetRow(16, 16, randomIndex, 6);
        this.spriteAnimationWalk = new SpriteAnimation(this.animWalk, this.x, this.y, 32, 32);

        this.animAttack = spriteSheet.getSpriteSheetRow(16, 16, randomIndex + 1, 5);
        this.spriteAnimationAttack = new SpriteAnimation(this.animAttack, this.x, this.y, 32, 32);
    }

    public void tick() {
        this.x += this.velX;
        this.y += this.velY;

        this.collisionDetection();
        this.chasePlayer();
    }

    public void render(Graphics graphics) {
        if (this.length > this.hitDistance) {
            this.spriteAnimationWalk.setXY(this.x, this.y);
            this.spriteAnimationWalk.animateSprite(graphics, this.shouldFlipImage());
        } else {
            this.spriteAnimationAttack.setXY(this.x, this.y);
            this.spriteAnimationAttack.animateSprite(graphics, this.shouldFlipImage());
        }
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
            System.out.println("Slime died");
            this.manager.removeObj(this);
        }
    }

    public void chasePlayer() {
        int playerX = this.player.getX();
        int playerY = this.player.getY();

        double dx = playerX - this.x;
        double dy = playerY - this.y;
        this.length = Math.sqrt(dx * dx + dy * dy);
        // If inside player lenght can be 0, which will cause error
        if (this.length > this.hitDistance) {
            double normalizedX = dx / this.length;
            double normalizedY = dy / this.length;

            this.velX = (float)(normalizedX * this.speed);
            this.velY = (float)(normalizedY * this.speed);
        } else {
            this.velX = 0;
            this.velY = 0;
        }
    }

    private boolean shouldFlipImage() {
        if (this.velX == 0) {
            return this.lastVelX < 0;
        }
        this.lastVelX = this.velX;
        return this.lastVelX < 0;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
}