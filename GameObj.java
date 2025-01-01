import java.awt.Graphics;
import java.awt.Rectangle;

/**     
 * Trieda GameObj sluzi na vytvorenie objektov v hre.
 * Je abstraktna, pretoze sa z nej nevytvoria ziadne objekty, kedze tvori zaklad pre vsetky objekty v hre.
 * Vytvara objekty s ID, ktore su definovane v enum GameObjID.
 * Obsahuje metody render na vykreslenie objektov a tick na aktualizaciu objektov.
 */
public abstract class GameObj {

    // References
    private GameObjID id;

    /**
     * Konstruktor triedy GameObj vytvori novy objekt s ID, ktore je definovane v enum GameObjID.
     * @param x x-ova suradnica objektu
     * @param y y-ova suradnica objektu
     * @param id ID objektu
     * @param manager objekt ObjManager
     * @param spriteSheet objekt SpriteSheet, ktory obsahuje sprite sheet s obrazkami objektov
     */
    public GameObj(int x, int y, GameObjID id, ObjManager manager, SpriteSheet spriteSheet) {
        this.id = id;
    }

    /**
     * Metoda tick sluzi na aktualizaciu objektov v hre.
     */
    public abstract void tick();

    /**
     * Metoda render sluzi na vykreslenie objektov v hre.
     * @param graphics grafika
     */
    public abstract void render(Graphics graphics);

    /**
     * Metoda getBounds sluzi na ziskanie obdlznikovej oblasti objektu.
     * Sluzi na detekciu kolizii objektov.
     * @return obdlznikova oblast objektu
     */
    public abstract Rectangle getBounds();

    /**
     * Metoda getId sluzi na ziskanie ID objektu.
     * @return ID objektu
     */
    public GameObjID getId() {
        return this.id;
    }
}
