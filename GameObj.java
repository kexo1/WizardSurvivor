import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObj {

    // References
    private GameObjID id;

    public GameObj(int x, int y, GameObjID id, ObjManager manager, SpriteSheet spriteSheet) {
        this.id = id;
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    public abstract Rectangle getBounds();

    public GameObjID getId() {
        return this.id;
    }
}
