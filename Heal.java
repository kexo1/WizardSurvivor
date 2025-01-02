import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Trieda Heal zabezpecuje spravanie sa objektu Heal.
 * Heal je objekt, ktory sa nachadza na mape a sluzi na doplnenie zivota hraca.
 */
public class Heal extends GameObj {

    // Referencie
    private final BufferedImage sprite;

    // Atributy
    private final int x;
    private final int y;

    /**
     * Konstruktor triedy Heal.
     * 
     * @param x x-ova suradnica objektu
     * @param y y-ova suradnica objektu
     * @param gameObjID identifikator objektu
     * @param manager objekt ObjManager
     * @param spriteSheet spriteSheet, ktory obsahuje sprite pre objekt Heal
     */
    public Heal(int x, int y, GameObjID gameObjID, ObjManager manager, SpriteSheet spriteSheet) {
        super(x, y, gameObjID, manager, spriteSheet);
        this.x = x;
        this.y = y;
        this.sprite = spriteSheet.getSprite(43, 43, 1, 1);
    }

    /**
     * Metoda tick nie je implementovana, pretoze objekt Heal nema ziadne vlastnosti, ktore by sa menili v case.
     */
    public void tick() { }

    /**
     * Metoda render vykresli objekt Heal na obrazovku.
     * 
     * @param graphics graficky kontext
     */
    public void render(Graphics graphics) {
        graphics.drawImage(this.sprite, this.x, this.y, 43, 43, null);
    }

    /**
     * Metoda getBounds vrati obdlznik, ktory reprezentuje kolizny obdlznik objektu.
     * 
     * @return obdlznik kolizie
     */
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, 43, 43);
    }
}
