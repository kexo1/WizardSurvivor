import java.util.LinkedList;
import java.awt.Graphics;

public class ObjManager {
    
    public LinkedList<GameObj> obj = new LinkedList<GameObj>();
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    public void tick() {
        for (int i = 0; i < obj.size(); i++) {
            GameObj tempObj = obj.get(i);
            tempObj.tick();
        }
    }

    public void render(Graphics graphics) {
        // Get all objects
        for (int i = 0; i < obj.size(); i++) {
            GameObj tempObj = obj.get(i);
            // Render the object
            tempObj.render(graphics);
        }

    }

    public Player getPlayer() {
        for (int i = 0; i < obj.size(); i++) {
            GameObj tempObj = obj.get(i);
            if (tempObj.getId() == GameObjID.Player)
                return (Player) tempObj;
        }
        return null;
    }

    public void addObj(GameObj tempObj) {
        // Add an object
        obj.add(tempObj);
    }

    public void removeObj(GameObj tempObj) {
        // Remove an object
        obj.remove(tempObj);
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
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
}
