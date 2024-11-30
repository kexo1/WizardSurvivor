import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Orb extends GameObj {
    
    private int speed = 10;
    long timer = System.currentTimeMillis();
    private ObjManager manager;

    public Orb(int x, int y, GameObjID gameObjID, ObjManager manager, int mouseX, int mouseY) {
        super(x, y, gameObjID, manager);
        this.manager = manager;

        double dx = mouseX - x;
        double dy = mouseY - y;

        // Length of the vector
        double length = Math.sqrt(dx * dx + dy * dy);
        double normalizedX = dx / length;
        double normalizedY = dy / length;
        // Values between -1 and 1
        velX = (float)(normalizedX * speed);
        velY = (float)(normalizedY * speed);

    }

    public void tick() {
        updatePosition();
        collisionDetection();
        timer();
    }
    
    public void render(Graphics graphics) {
        graphics.setColor(Color.orange);
        graphics.fillOval(x, y, 12, 12);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 12, 12);
    }

    private void updatePosition() {
        x += velX;
        y += velY;
    }

    private void timer() {
        if (System.currentTimeMillis() - timer > 2000)
            manager.removeObj(this);
    }

    private void collisionDetection() {
        for (int i = 0; i < manager.obj.size(); i++) {
            GameObj obj = manager.obj.get(i);
            if (obj.getId() != GameObjID.Block) 
                continue;
                
            if (getBounds().intersects(obj.getBounds())) 
                manager.removeObj(this);

        }
    }

    
}
