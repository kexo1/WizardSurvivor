import java.util.Iterator;
import java.util.LinkedList;
import java.awt.Graphics;

/**
 * Trieda ObjManager je zodpovedná za správu vsetkych objektov v hre.
 * Trieda obsahuje:
 * - Referencie na objekty
 * - Atribúty pohybu hráča
 * - Metódy na pridanie a odstránenie objektov, nastavovanie pohybu hráča, získanie hráča
 * Metody tick a render slúžia na aktualizáciu a vykreslenie všetkých objektov.
 */
public class ObjManager {

    // References
    private final LinkedList<GameObj> objList;
    private Player player;

    // Player movement
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    /**
     * Konstruktor triedy ObjManager.
     */
    public ObjManager() {
        this.objList = new LinkedList<>();
    }

    /** 
     * Metoda tick sluzi na aktualizaciu vsetkych objektov.
     * Prejde vsetky objekty v objList a zavola metodu tick.
     */
    public void tick() {
        for (int i = 0; i < this.objList.size(); i++) {
            GameObj obj = this.objList.get(i);
            obj.tick();
        }
    }

    /** 
     * Metoda render sluzi na vykreslenie vsetkych objektov.
     * Prejde vsetky objekty v objList a zavola metodu render.
     * 
     * @param graphics graficky kontext
     */
    public void render(Graphics graphics) {
        for (int i = 0; i < this.objList.size(); i++) {
            GameObj obj = this.objList.get(i);
            obj.render(graphics);
        }
    }

    /** 
     * Metoda addObj prida objekt do objList.
     * 
     * @param obj objekt, ktory sa ma pridat
     */
    public void addObj(GameObj obj) {
        this.objList.add(obj);
    }

    /** 
     * Metoda removeObj odstrani objekt z objList.
     * 
     * @param obj objekt, ktory sa ma odstranit
     */
    public void removeObj(GameObj obj) {
        this.objList.remove(obj);
    }

    /** 
     * Metoda clearEnemies odstrani vsetky objekty s ID Enemy alebo Heal.
     */
    public void clearEnemies() {
        Iterator<GameObj> iterator = this.objList.iterator();                               // Pouzitie iterator-a pre prechadzanie objektov, kedze normalne prechadzanie by mohlo preskocit nejake objekty
        while (iterator.hasNext()) {                                                        // While loop pre prechadzanie objektov
            GameObj obj = iterator.next();                                                  // Ziskaj dalsi objekt
            if (obj.getId() == GameObjID.Enemy || obj.getId() == GameObjID.Heal) {
                iterator.remove();
            }
        }
    }

    public boolean isUp() {
        return this.up;
    }

    public boolean isDown() {
        return this.down;
    }

    public boolean isLeft() {
        return this.left;
    }

    public boolean isRight() {
        return this.right;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LinkedList<GameObj> getObjList() {
        return this.objList;
    }

}
