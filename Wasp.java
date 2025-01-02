import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Trieda Wasp zabezpecuje spravanie sa nepriatela Wasp (Osa).
 * Wasp je objekt, ktory sa pohybuje smerom k hracovi a utoci na hraca strielanim pichadiel
 * Wasp je stredne silny nepriatel v hre.
 */
public class Wasp extends GameObj {

    // Referencie
    private final Player player;
    private final ObjManager manager;
    private final BufferedImage stingSprite;
    private final SpriteSheet spriteSheet;
    private final SpriteAnimation spriteAnimationWalk;
    private final SpriteAnimation spriteAnimationAttack;
    private final Random random = new Random();

    // Statistiky
    private int hp = 250;
    private final int hitDistance = 250;

    // Pohyb
    private float velX;
    private float velY;
    private int x;
    private int y;
    private double length = 0;

    private long lastShotTime = 0;
    private float lastVelX;

    /**
     * Konstruktor triedy Wasp.
     * 
     * @param x x-ova suradnica objektu
     * @param y y-ova suradnica objektu
     * @param gameObjID identifikator objektu
     * @param manager objekt manazera
     * @param player objekt hraca
     * @param spriteSheet spriteSheet, ktory obsahuje sprity pre animaciu
     */
    public Wasp(int x, int y, GameObjID gameObjID, ObjManager manager, Player player, SpriteSheet spriteSheet) {
        super(x, y, gameObjID, manager, spriteSheet);

        this.player = player;
        this.manager = manager;
        this.spriteSheet = spriteSheet;
        this.x = x;
        this.y = y;

        BufferedImage[] animWalk = spriteSheet.getSpriteSheetRow(32, 32, 1, 4);
        this.spriteAnimationWalk = new SpriteAnimation(animWalk, this.x, this.y, 64, 64);

        BufferedImage[] animAttack = spriteSheet.getSpriteSheetRow(26, 32, 3, 12);
        this.spriteAnimationAttack = new SpriteAnimation(animAttack, this.x, this.y, 52, 64);

        spriteSheet = new SpriteSheet("/sprites/sting.png");
        this.stingSprite = spriteSheet.getSprite(6, 3, 1, 1);
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
     * Metoda render zabezpecuje vykreslenie objektu Wasp pomocou animovania spritu.
     * Jeho animacia zavisi od toho, ci sa pohybuje alebo utoci na hraca.
     * 
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
     * @return Rectangle
     */
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, 64, 64);
    }

    private void collisionDetection() {
        for (int i = 0; i < this.manager.getObjList().size(); i++) {
            GameObj obj = this.manager.getObjList().get(i);
            
            if (obj.getId() == GameObjID.Orb) {

                if (this.getBounds().intersects(obj.getBounds())) {
                    this.takeDamage(this.player.getDamage()); // Odstranenie strely
                    this.manager.removeObj(obj);
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

        if (this.length > this.hitDistance) {
            double normalizedX = dx / this.length;
            double normalizedY = dy / this.length;
            
            double randomFactorX = (this.random.nextDouble() - 0.5) * 0.1;      // Nahodna hodnota medzi -0.1 a 0.1
            double randomFactorY = (this.random.nextDouble() - 0.5) * 0.1;

            int speed = 4;
            this.velX = (float)((normalizedX + randomFactorX) * speed);    // Priradenie nahodneho faktora k rychlosti pohybu, aby sa nepriatelia nespajali.
            this.velY = (float)((normalizedY + randomFactorY) * speed);
        // Wasp moze vystrelit pichadlo aj za pohybu ak je rozdiel medzi jeho a hracovou poziciou je medzi 200 a 250.
        } else if (this.length > this.hitDistance - 50) {
            this.shootSting();
        // Ak je Wasp v uplnom dosahu utoku, zastavi sa a zacne strielat pichadla.
        } else {
            this.velX = 0;
            this.velY = 0;
            this.shootSting();
        }
    }

    private void shootSting() {

        // Casovac
        long currentTime = System.currentTimeMillis();
        int hitDelay = 1500;
        if (currentTime - this.lastShotTime < hitDelay) {
            return;
        }
        this.lastShotTime = currentTime;
        // Vytvorenie pichadla a pridanie do objektov, podobne ako hracov Orb.
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