import java.util.LinkedList;
import java.awt.Graphics;

public class Handler {
    
    LinkedList<GameObj> obj = new LinkedList<GameObj>();
    private boolean up = false, down = false, left = false, right = false;

    public void tick() {
        // Get all objects
        for (int i = 0; i < obj.size(); i++) {
            GameObj tempObj = obj.get(i);
            // Tick the object
            tempObj.tick();
        }
    }

    public void render(Graphics g) {
        // Get all objects
        for (int i = 0; i < obj.size(); i++) {
            GameObj tempObj = obj.get(i);
            // Render the object
            tempObj.render(g);
        }

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

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
