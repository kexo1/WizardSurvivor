import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Trieda Slime zabezpecuje spravanie sa nepriatela Slime (Sliz).
 * Slime je objekt, ktory sa pohybuje smerom k hracovi a utoci na neho.
 * Slime je najslabsi nepriatel v hre.
 */
public class Slime extends GameObj {

    // Referencie
    private Player player;
    private ObjManager manager;
    private BufferedImage[] animWalk;
    private BufferedImage[] animAttack;
    private SpriteAnimation spriteAnimationAttack;
    private SpriteAnimation spriteAnimationWalk;
    private Random random = new Random();

    // Statistiky
    private int hp = 100;
    private final int damage = 20;
    private final int speed = 3;
    private final int hitDelay = 1500;
    private final int hitDistance = 20;

    // Pohyb
    private float velX;
    private float velY;
    private int x;
    private int y;
    private double length = 0;

    // Casovac
    private long currentTime = System.currentTimeMillis();
    private long lastShotTime = 0;
    private float lastVelX;

    /**
     * Konstruktor triedy Slime.
     * 
     * @param x
     * @param y
     * @param gameObjID
     * @param manager
     * @param player
     * @param spriteSheet
     */
    public Slime(int x, int y, GameObjID gameObjID, ObjManager manager, Player player, SpriteSheet spriteSheet) {
        super(x, y, gameObjID, manager, spriteSheet);
        this.player = player;
        this.manager = manager;
        this.x = x;
        this.y = y;
   
        int randomIndex = (this.random.nextInt(2) == 0) ? 1 : 3;    // Cerveny alebo zeleny slime
        this.animWalk = spriteSheet.getSpriteSheetRow(16, 16, randomIndex, 6);
        this.spriteAnimationWalk = new SpriteAnimation(this.animWalk, this.x, this.y, 32, 32);

        this.animAttack = spriteSheet.getSpriteSheetRow(16, 16, randomIndex + 1, 5);
        this.spriteAnimationAttack = new SpriteAnimation(this.animAttack, this.x, this.y, 32, 32);
    }

    /**
     * Metoda tick aktualizuje poziciu objektu, detekuje kolizie a pohybuje sa smerom k hracovi.
     */
    public void tick() {
        this.updatePosition();
        this.collisionDetection();
        this.chasePlayer();
    }

    /** 
     * Metoda render zabezpecuje vykreslenie objektu Slime pomocou animovania spritu
     * Jeho animacia zavisi od toho, ci sa pohybuje alebo utoci na hraca.
     * 
     * @param graphics
     */
    public void render(Graphics graphics) {
        if (this.length > this.hitDistance) {
            this.spriteAnimationWalk.setXY(this.x, this.y);
            this.spriteAnimationWalk.animateSprite(graphics, this.shouldFlipImage());
        } else {
            this.spriteAnimationAttack.setXY(this.x, this.y);
            this.spriteAnimationAttack.animateSprite(graphics, this.shouldFlipImage());
        }
    }

    /**
     * Metoda getBounds vrati obdlznik, ktory reprezentuje kolizny obdlznik objektu.
     * 
     * @return Rectangle
     */
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, 32, 32);
    }

    private void collisionDetection() {
        for (int i = 0; i < this.manager.getObjList().size(); i++) {
            GameObj obj = this.manager.getObjList().get(i);
            
            if (obj.getId() == GameObjID.Orb) {

                if (this.getBounds().intersects(obj.getBounds())) {
                    this.takeDamage(this.player.getDamage());
                    this.manager.removeObj(obj); // Odstranenie strely
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

    private void updatePosition() {
        this.x += this.velX;
        this.y += this.velY;
    }

    private void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) {
            this.player.setScore(this.player.getScore() + 10);
            this.manager.removeObj(this);

        }
    }

    private void chasePlayer() {
        int playerX = this.player.getX();
        int playerY = this.player.getY();

        double dx = playerX - this.x;
        double dy = playerY - this.y;
        this.length = Math.sqrt(dx * dx + dy * dy);
        // Ak je slime vnutri hraca, nema sa kam pohybovat
        if (this.length > this.hitDistance) {
            double normalizedX = dx / this.length;
            double normalizedY = dy / this.length;

            double randomFactorX = (this.random.nextDouble() - 0.5) * 0.1;      // Nahodna hodnota medzi -0.1 a 0.1
            double randomFactorY = (this.random.nextDouble() - 0.5) * 0.1;

            this.velX = (float)((normalizedX + randomFactorX) * this.speed);    // Priradenie nahodneho faktora k rychlosti pohybu, aby sa nepriatelia nespajali.
            this.velY = (float)((normalizedY + randomFactorY) * this.speed);
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
}