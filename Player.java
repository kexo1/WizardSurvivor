import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends GameObj {

    ObjManager manager;

    public Player(int x, int y, GameObjID id, ObjManager manager) {
        super(x, y, id);
        this.manager = manager;

    }

    public void tick() {
        movementVelocity();
        collisionDetection();
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.black);
        graphics.fillRect(x, y, 32, 56);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 56);
    }

    private void movementVelocity() {

        x += velX;
        y += velY;
        // Velocity needs to be set to 0 othervise the player will slide
        velY = manager.isUp() ? -5 : (!manager.isDown() ? 0 : velY);
        velY = manager.isDown() ? 5 : (!manager.isUp() ? 0 : velY);
        velX = manager.isRight() ? 5 : (!manager.isLeft() ? 0 : velX);
        velX = manager.isLeft() ? -5 : (!manager.isRight() ? 0 : velX);
    }

    private void collisionDetection() {
        for (GameObj obj : manager.obj) {
            if (obj.getId() != GameObjID.Block)
                continue;
    
            if (getBounds().intersects(obj.getBounds())) {
                x -= velX;
                y -= velY;
            }
        }
    }
    

    
}
