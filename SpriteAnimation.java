import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

/**
 * Trieda SpriteAnimation zabezpecuje animovanie obrazkov.
 * Obrazky su plynule zobrazene na obrazovke a postupne vytvaraju animaciu.
 */
public class SpriteAnimation {

    // Referencie
    private final BufferedImage[] spriteList;
    private final AffineTransform transform;

    // Animacia
    private int index = 0;
    private long lastTime;
    private long timer = 0;

    // Pozicia a velkost obrazka
    private int x;
    private int y;
    private final int width;
    private final int height;

    /**
     * Konstruktor triedy SpriteAnimation.
     * 
     * @param spriteList pole obrazkov pre animaciu.
     * @param x x-ova pozicia objektu.
     * @param y y-ova pozicia objektu.
     * @param width sirka obrazka.
     * @param height vyska obrazka.
     */
    public SpriteAnimation(BufferedImage[] spriteList, int x, int y, int width, int height) {
        this.spriteList = spriteList;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.lastTime = System.currentTimeMillis();
        this.transform = new AffineTransform();
    }

    /**
     * Metoda animateSprite vykresli obrazok a animuje ho na obrazovke.
     * Obrazok je vykresleny na pozicii x, y a je animovany pomocou indexovania obrazkov v poli.
     * Animacia je obmedzena rychlostou frameSpeed.
     * Funkcia animateSprite je volana v render metodach.
     * 
     * @param graphics graficky kontext.
     * @param flip true, ak ma byt obrazok otoceny.
     */
    public void animateSprite(Graphics graphics, boolean flip) {

        long now = System.currentTimeMillis();
        this.timer += now - this.lastTime;
        this.lastTime = now;

        int frameSpeed = 100;
        if (this.timer > frameSpeed) {                 // Ak casovac neprekrocil rychlost
            this.index++;                                   // Posun na dalsi obrazok
            this.timer = 0;                                 // Reset casovaca    
            if (this.index >= this.spriteList.length) {     // Ak index prekrocil velkost pola
                this.index = 0; 
            }
        }
        
        BufferedImage image = this.spriteList[this.index];  // Vyber obrazok z pola
        Graphics2D g2d = (Graphics2D)graphics;  

        if (flip) {                                                                                                 // Ak ma byt obrazok otoceny
            this.transform.setToIdentity();                                                                         // Reset transformacie, kedze sa pouziva pre kazdy obrazok
            this.transform.translate(this.x + this.width, this.y);                                                  // Posun do stredu obrazka, aby sa otocil spravne
            this.transform.scale(-1, 1);                                                                         // Otocenie obrazka podla osi x
            this.transform.scale((double)this.width / image.getWidth(), (double)this.height / image.getHeight());   // Zmena velkosti obrazka
            g2d.drawImage(image, this.transform, null);                                                         
        } else {
            g2d.drawImage(image, this.x, this.y, this.width, this.height, null);
        }
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
