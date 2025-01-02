import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Trieda Ghoul zabezpecuje spravanie sa nepriatela Ghoul.
 * Ghoul je objekt, ktory sa pohybuje smerom k hracovi a utoci na neho.
 * Ghoul je najsilnejsi nepriatel v hre.
 */
public class Ghoul extends GameObj {

    // Referencie
    private final Player player;
    private final ObjManager manager;
    private final SpriteAnimation spriteAnimationWalk;
    private final SpriteAnimation spriteAnimationAttack;
    private final Random random = new Random();

    // Statistiky
    private int hp = 400;
    private final int hitDistance = 50;

    // Pohyb
    private float velX;
    private float velY;
    private int x;
    private int y;
    private double length = 0;

    private long lastShotTime = 0;
    private float lastVelX;

    /**
     * Konstruktor triedy Ghoul.
     * 
     * @param x x-ova suradnica objektu
     * @param y y-ova suradnica objektu
     * @param gameObjID identifikator objektu
     * @param manager objekt manazera
     * @param player objekt hraca
     * @param spriteSheet spriteSheet, ktory obsahuje sprit-y pre animaciu   
     */
    public Ghoul(int x, int y, GameObjID gameObjID, ObjManager manager, Player player, SpriteSheet spriteSheet) {
        super(x, y, gameObjID, manager, spriteSheet);
        this.player = player;
        this.manager = manager;
        this.x = x;
        this.y = y;

        BufferedImage[] animWalk = spriteSheet.getSpriteSheetRow(32, 32, 2, 8);
        this.spriteAnimationWalk = new SpriteAnimation(animWalk, this.x, this.y, 86, 86);

        BufferedImage[] animAttack = spriteSheet.getSpriteSheetRow(32, 32, 3, 6);
        this.spriteAnimationAttack = new SpriteAnimation(animAttack, this.x, this.y, 86, 86);
    }
    
    /**
     * Metoda tick aktualizuje poziciu objektu, deteguje kolizie a pohybuje sa smerom k hracovi.
     */
    public void tick() {
        this.updatePosition();
        this.collisionDetection();
        this.chasePlayer();
    }

    /** 
     * Metoda render zabezpecuje vykreslenie objektu Ghoul pomocou animovania sprit-u
     * Jeho animacia zavisi od toho, ci sa pohybuje alebo utoci na hraca.
     * @param graphics graficky kontext
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
     * @return obdlznik kolizie
     */
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, 86, 86);
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
                    // Casovac
                    long currentTime = System.currentTimeMillis();

                    int hitDelay = 800;
                    if (currentTime - this.lastShotTime < hitDelay) {
                        return;
                    }
                    
                    this.lastShotTime = currentTime;
                    int damage = 40;
                    this.player.takeDamage(damage);
                    
                }
            }
        }
    }

    private void updatePosition() {
        this.x += (int)this.velX;
        this.y += (int)this.velY;
    }

    private void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) {
            this.player.setScore(this.player.getScore() + 50);
            this.manager.removeObj(this);
        }
    }

    private void chasePlayer() {
        int playerX = this.player.getX();
        int playerY = this.player.getY();

        double dx = playerX - this.x;
        double dy = playerY - this.y;
        this.length = Math.sqrt(dx * dx + dy * dy);

        if (this.length > this.hitDistance) {
            double normalizedX = dx / this.length;
            double normalizedY = dy / this.length;

            double randomFactorX = (this.random.nextDouble() - 0.5) * 0.1;      // Nahodna hodnota medzi -0.1 a 0.1
            double randomFactorY = (this.random.nextDouble() - 0.5) * 0.1;

            int speed = 4;
            this.velX = (float)((normalizedX + randomFactorX) * speed);    // Priradenie nahodneho faktora k rychlosti pohybu, aby sa nepriatelia nespajali.
            this.velY = (float)((normalizedY + randomFactorY) * speed);
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