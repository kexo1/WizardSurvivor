import java.util.Iterator;
import java.util.LinkedList;
import java.awt.Graphics;

/**
 * Trieda ObjManager je zodpovedná za správu vsetkych objektov v hre.
 * 
 * Trieda obsahuje:
 * - Referencie na objekty
 * - Atribúty pohybu hráča
 * - Metódy na pridanie a odstránenie objektov, nastavovanie pohybu hráča, získanie hráča
 * 
 * Metody tick a render slúžia na aktualizáciu a vykreslenie všetkých objektov.
 */
public class ObjManager {

    // References
    private LinkedList<GameObj> objList = new LinkedList<GameObj>();
    private Object obj;
    private Player player;

    // Attributes
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    
    /** 
     * Metóda tick slúži na aktualizáciu všetkých objektov.
     * Prejde všetky objekty v objList a zavolá metódu tick.
     */
    public void tick() {
        for (int i = 0; i < this.objList.size(); i++) {
            GameObj tempObj = this.objList.get(i);
            tempObj.tick();
        }
    }

    /** 
     * Metóda render slúži na vykreslenie všetkých objektov.
     * Prejde všetky objekty v objList a zavolá metódu render.
     * 
     * @param graphics
     */
    public void render(Graphics graphics) {
        for (int i = 0; i < this.objList.size(); i++) {
            GameObj tempObj = this.objList.get(i);
            tempObj.render(graphics);
        }
    }

    /** 
     * Metóda addObj pridá objekt do objList.
     * 
     * @param tempObj
     */
    public void addObj(GameObj tempObj) {
        this.objList.add(tempObj);
    }

    /** 
     * Metóda removeObj odstráni objekt z objList.
     * 
     * @param tempObj
     */
    public void removeObj(GameObj tempObj) {
        this.objList.remove(tempObj);
    }

    /** 
     * Metóda clearEnemies odstráni všetky objekty s ID Enemy alebo Heal.
     */
    public void clearEnemies() {
        Iterator<GameObj> iterator = this.objList.iterator();                               // Pouzi iterator pre prechadzanie objektov, kedze normalne prechadzanie by mohlo preskocit nejake objekty
        while (iterator.hasNext()) {                                                        // While loop pre prechadzanie objektov
            GameObj tempObj = iterator.next();                                              // Ziskaj dalsi objekt
            if (tempObj.getId() == GameObjID.Enemy || tempObj.getId() == GameObjID.Heal) {
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

    public Object getObj() {
        return this.obj;
    }

}
