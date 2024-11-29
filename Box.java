import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Box extends GameObj {

    public Box(int x, int y, GameObjID gameObjID) {
        super(x, y, gameObjID);
        velX = 1;
    }

    public void tick() {
        x += velX;
        y += velY;
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.fillRect(x, y, 32, 32);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }
}
