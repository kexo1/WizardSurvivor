import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObj {

    // References
    private GameObjID id;
    
    // Attributes
    private int x;
    private int y;
    private float velX;
    private float velY;

    public GameObj(int x, int y, GameObjID id, ObjManager manager, SpriteSheet spriteSheet) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    public abstract Rectangle getBounds();

    // Getters
    public GameObjID getId() {
        return this.id;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public float getVelX() {
        return this.velX;
    }

    public float getVelY() {
        return this.velY;
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
