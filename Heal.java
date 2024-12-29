import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Heal extends GameObj {

    // References
    private BufferedImage sprite;

    // Attributes
    private int x;
    private int y;

    public Heal(int x, int y, GameObjID gameObjID, ObjManager manager, SpriteSheet spriteSheet) {
        super(x, y, gameObjID, manager, spriteSheet);
        this.x = x;
        this.y = y;
        this.sprite = spriteSheet.getSprite(43, 43, 1, 1);
    }

    public void tick() { }

    public void render(Graphics graphics) {
        graphics.drawImage(this.sprite, this.x, this.y, 43, 43, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, 43, 43);
    }
}
