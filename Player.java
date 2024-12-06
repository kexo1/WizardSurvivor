import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends GameObj {

    ObjManager manager;
    private int damage = 34;

    public Player(int x, int y, GameObjID id, ObjManager manager) {
        super(x, y, id, manager);
        this.manager = manager;

    }

    public void tick() {
        x += velX;
        y += velY;

        collisionDetection();
        movementVelocity();
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.black);
        graphics.fillRect(x, y, 32, 64);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 64);
    }

    private void movementVelocity() {

        if (manager.isUp())
            velY = -5;
        else if (manager.isDown())
            velY = 5;
        else
            velY = 0;

        if (manager.isRight())
            velX = 5;
        else if (manager.isLeft())
            velX = -5;
        else 
            velX = 0;
    }

    private void collisionDetection() {
        for (GameObj obj : manager.obj) {
            if (obj.getId() != GameObjID.Block)
                continue;

            if (getBounds().intersects(obj.getBounds())) {
                x += velX * -1;
                y += velY * -1;
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDamage() {
        return damage;
    }
    

    
}
