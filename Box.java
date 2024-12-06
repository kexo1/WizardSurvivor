import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Box extends GameObj {

    public Box(int x, int y, GameObjID gameObjID, ObjManager manager) {
        super(x, y, gameObjID, manager);
    }

    public void tick() {

    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.green);
        graphics.fillRect(x, y, 32, 32);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }
}
