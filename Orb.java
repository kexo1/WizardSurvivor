import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


/**
 * Trieda Orb zabezpecuje spravanie sa vystreleneho objektu Orb.
 * Orb je objekt, ktory sa pohybuje smerom k myske a po 2 sekundach zmizne.
 */
public class Orb extends GameObj {
    
    // Referencie
    private ObjManager manager;
    private BufferedImage sprite;

    // Attributy
    private long timer = System.currentTimeMillis();
    private final int speed = 10;
    private float velX;
    private float velY;
    private int x;
    private int y;

    /**
     * Konstruktor triedy Orb.
     * 
     * @param x
     * @param y
     * @param gameObjID
     * @param manager
     * @param spriteSheet
     * @param sprite
     * @param mouseX
     * @param mouseY
     */
    public Orb(int x, int y, GameObjID gameObjID, ObjManager manager, SpriteSheet spriteSheet, BufferedImage sprite, int mouseX, int mouseY) {
        super(x, y, gameObjID, manager, spriteSheet);
        this.x = x;
        this.y = y;
        this.manager = manager;
        this.sprite = sprite;
        this.normalizeDirection(mouseX, mouseY);
    }

    /** 
     * Metoda tick zabezpecuje aktualizaciu pozicie a casovaca.
     */
    public void tick() {
        this.updatePosition();
        this.timer();
    }
    
    /** 
     * Metoda render zabezpecuje vykreslenie objektu Orb pomocou spritu
     * 
     * @param graphics
     */
    public void render(Graphics graphics) {
        graphics.drawImage(this.sprite, this.x, this.y, 16, 16, null);
    }

    /**
     * Metoda getBounds vrati obdlznik, ktory reprezentuje kolizny obdlznik objektu.
     * 
     * @return Rectangle
     */
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

        double dx = mouseX - this.x;    // Rozdiel medzi x-ovymi suradnicami aktualnej pozicie a pozicie mysky
        double dy = mouseY - this.y;    // Rozdiel medzi y-ovymi suradnicami aktualnej pozicie a pozicie mysky

        double length = Math.sqrt(dx * dx + dy * dy);   // Vypocet na urcenie dlzky vektora (Pytagorova veta: https://tinyurl.com/vpk3ay6h)
        double normalizedX = dx / length;               // Normalizovany smer x-ovej zlozky (hodnota medzi -1 a 1)
        double normalizedY = dy / length;               // Normalizovany smer y-ovej zlozky (hodnota medzi -1 a 1)

        this.velX = (float)(normalizedX * this.speed);  // Nastavenie rychlosti pohybu v x-ovej zlozke
        this.velY = (float)(normalizedY * this.speed);  // Nastavenie rychlosti pohybu v y-ovej zlozke
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }
}
