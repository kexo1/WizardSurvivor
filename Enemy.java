import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Enemy extends GameObj {

    private Player player;
    private ObjManager manager;
    private int hp = 100;
    private int speed = 3;

    public Enemy(int x, int y, GameObjID gameObjID, ObjManager manager, Player player) {
        super(x, y, gameObjID, manager);
        this.player = player;
        this.manager = manager;
    }

    public void tick() {
        x += velX;
        y += velY;

        collisionDetection();
        chasePlayer();
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.fillRect(x, y, 32, 32);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }

    private void collisionDetection() {
        for (int i = 0; i < manager.obj.size(); i++) {
            GameObj obj = manager.obj.get(i);
            if (obj.getId() == GameObjID.Orb) {

                if (getBounds().intersects(obj.getBounds())) {
                    takeDamage(player.getDamage());
                    manager.removeObj(obj);
                }
            }
        }
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            System.out.println("Enemy died");
            manager.removeObj(this);
        }
    }

    public void chasePlayer() {
        int playerX = player.getX();
        int playerY = player.getY();

        double dx = playerX - x;
        double dy = playerY - y;
        double length = Math.sqrt(dx * dx + dy * dy);

        if (length != 0) {
            double normalizedX = dx / length;
            double normalizedY = dy / length;
            
            velX = (float)(normalizedX * speed);
            velY = (float)(normalizedY * speed);
        } else {
            velX = 0;
            velY = 0;
        }
    }
}