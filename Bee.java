import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Bee extends GameObj {

    // References
    private Player player;
    private ObjManager manager;
    private BufferedImage[] animWalk;
    private BufferedImage[] animAttack;
    private BufferedImage stingSprite;
    private SpriteSheet spriteSheet;
    private SpriteAnimation spriteAnimationWalk;
    private SpriteAnimation spriteAnimationAttack;
    private Random random = new Random();

    // Stats
    private int hp = 250;
    private int speed = 4;
    private int hitDelay = 1500;
    private int hitDistance = 250;

    // Movement
    private float velX;
    private float velY;
    private int x;
    private int y;

    // Time
    private long currentTime = System.currentTimeMillis();
    private long lastShotTime = 0;

    private double length = 0;
    private float lastVelX;

    public Bee(int x, int y, GameObjID gameObjID, ObjManager manager, Player player, SpriteSheet spriteSheet) {
        super(x, y, gameObjID, manager, spriteSheet);

        this.player = player;
        this.manager = manager;
        this.spriteSheet = spriteSheet;
        this.x = x;
        this.y = y;

        this.animWalk = spriteSheet.getSpriteSheetRow(32, 32, 1, 4);
        this.spriteAnimationWalk = new SpriteAnimation(this.animWalk, this.x, this.y, 64, 64);

        this.animAttack = spriteSheet.getSpriteSheetRow(26, 32, 3, 12);
        this.spriteAnimationAttack = new SpriteAnimation(this.animAttack, this.x, this.y, 52, 64);

        spriteSheet = new SpriteSheet("/sprites/sting.png");
        this.stingSprite = spriteSheet.getSprite(6, 3, 1, 1);
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
        return new Rectangle(this.x, this.y, 64, 64);
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
        }
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) {
            this.player.setScore(this.player.getScore() + 30);
            this.manager.removeObj(this);
        }
    }

    private void chasePlayer() {
        int playerX = this.player.getX();
        int playerY = this.player.getY();

        double dx = playerX - this.x;
        double dy = playerY - this.y;
        this.length = Math.sqrt(dx * dx + dy * dy);
        // If inside player lenght can be 0, which will cause error
        if (this.length > this.hitDistance) {
            double normalizedX = dx / this.length;
            double normalizedY = dy / this.length;
            
            double randomFactorX = (this.random.nextDouble() - 0.5) * 0.1; // Random value between -0.1 and 0.1
            double randomFactorY = (this.random.nextDouble() - 0.5) * 0.1; // Random value between -0.1 and 0.1

            this.velX = (float)((normalizedX + randomFactorX) * this.speed);
            this.velY = (float)((normalizedY + randomFactorY) * this.speed);
        // Bee can shoot sting while moving if player is atleast 200 pixels away
        } else if (this.length > this.hitDistance - 50) {
            this.shootSting();
        // If stationary, bee will shoot sting
        } else {
            this.velX = 0;
            this.velY = 0;
            this.shootSting();
        }
    }

    private void shootSting() {

        this.currentTime = System.currentTimeMillis();
        if (this.currentTime - this.lastShotTime < this.hitDelay) {
            return;
        }
        this.lastShotTime = this.currentTime;

        this.manager.addObj(new Sting(this.x + 10, this.y + 40, GameObjID.Sting, this.manager, this.spriteSheet, this.stingSprite, this.manager.getPlayer().getX(), this.manager.getPlayer().getY()));
    }

    private boolean shouldFlipImage() {
        if (this.velX == 0) {
            return this.lastVelX > 0;
        }
        this.lastVelX = this.velX;
        return this.lastVelX > 0;
    }
}