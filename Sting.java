import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Trieda Sting zabezpecuje spravanie sa vystreleneho objektu Sting.
 * Sting je objekt, ktory sa pohybuje smerom k hracovi.
 * Sting moze zasiahnut hraca a ubrat mu zivoty.
 */
public class Sting extends GameObj {
    
    // Referencie
    private ObjManager manager;
    private BufferedImage sprite;

    // Pozicia
    private final int speed = 10;
    private float velX;
    private float velY;
    private int x;
    private int y;
    private int playerX;
    private int playerY;
    private double angle;

    // Casovac
    private long timer = System.currentTimeMillis();

    /**
     * Konstruktor triedy Sting.
     * 
     * @param x x-ova suradnica objektu
     * @param y y-ova suradnica objektu
     * @param gameObjID identifikator objektu
     * @param manager objekt manazera
     * @param spriteSheet spriteSheet, ktory nema v sebe sprite, ale je potrebny pre volanie konstruktor predka
     * @param sprite sprite, ktory sa pouzije na vykreslenie objektu
     * @param playerX x-ova suradnica hraca
     * @param playerY y-ova suradnica hraca
     */
    public Sting(int x, int y, GameObjID gameObjID, ObjManager manager, SpriteSheet spriteSheet, BufferedImage sprite, int playerX, int playerY) {
        super(x, y, gameObjID, manager, spriteSheet);
        this.x = x;
        this.y = y;
        this.playerX = playerX;
        this.playerY = playerY;
        this.manager = manager;
        this.sprite = sprite;
        this.angle = Math.atan2(this.playerY - this.y, this.playerX - this.x); // Vypocet uhla pomocou tangens
        this.normalizeDirection(playerX, playerY);
    }

    /**
     * Metoda tick sluzi na aktualizaciu pozicie a casovaca objektu Sting.
     */
    public void tick() {
        this.updatePosition();
        this.timer();
    }
    
    /**
     * Metoda render sluzi na vykreslenie objektu Sting.
     * 
     * @param graphics graficky kontext
     */
    public void render(Graphics graphics) {

        Graphics2D g2d = (Graphics2D)graphics;                  // Pouzite Graphics2D pre rotaciu obrazka
        AffineTransform old = g2d.getTransform();               // Ulozenie povodneho nastavenia, aby uhol neovplyvnil dalsie vykreslenie (Objekt by sa )
        g2d.rotate(this.angle, this.x + 3, this.y + 1.5);       // Vykreslenie obrazka s otocenim, hodnoty su preto, aby sa otocil okolo stredu obrazka
        g2d.drawImage(this.sprite, this.x, this.y, 16, 8, null);
        g2d.setTransform(old);                                  // Nastavenie povodneho nastavenia, iba X a Y suradnice, rotacia sa nezmeni
    }

    /**
     * Metoda getBounds vrati obdlznik, ktory reprezentuje kolizny obdlznik objektu.
     * 
     * @return obdlznik kolizie
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

    private void normalizeDirection(int playerX, int playerY) {

        double dx = playerX - this.x;    // Rozdiel medzi x suradnicami aktualnej pozicie a pozicie hraca
        double dy = playerY - this.y;    // Rozdiel medzi y suradnicami aktualnej pozicie a pozicie hraca

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
