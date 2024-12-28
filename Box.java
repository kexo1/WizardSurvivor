import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Box extends GameObj {

    public Box(int x, int y, GameObjID gameObjID, ObjManager manager, SpriteSheet spriteSheet) {
        super(x, y, gameObjID, manager, spriteSheet);
    }

    public void tick() { }

    public void render(Graphics graphics) {
        graphics.setColor(Color.green);
        graphics.fillRect(getX(), getY(), 32, 32);
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), 32, 32);
    }
}
