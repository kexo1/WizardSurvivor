import java.util.LinkedList;
import java.awt.Graphics;

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

    public void tick() {
        for (int i = 0; i < this.objList.size(); i++) {
            GameObj tempObj = this.objList.get(i);
            tempObj.tick();
        }
    }

    public void render(Graphics graphics) {
        // Get all objListects
        for (int i = 0; i < this.objList.size(); i++) {
            GameObj tempObj = this.objList.get(i);
            // Render the objListect
            tempObj.render(graphics);
        }
    }

    public void addObj(GameObj tempObj) {
        this.objList.add(tempObj);
    }

    public void removeObj(GameObj tempObj) {
        this.objList.remove(tempObj);
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

    public void setObj(Object obj) {
        this.obj = obj;
    }

}
