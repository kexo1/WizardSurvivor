import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObj {

    protected int x, y;
    protected float velX = 0, velY = 0;
    protected GameObjID id;

    public GameObj(int x, int y, GameObjID id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Rectangle getBounds();

    // Getters
    public GameObjID getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    // Setters
    public void setId(GameObjID id) {
        this.id = id;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

}
