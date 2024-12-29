import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MouseInput extends MouseAdapter {

    // References
    private ObjManager manager;
    private BufferedImage sprite;
    private SpriteSheet spriteSheet;

    // Attributes
    private long lastShotTime = 0;
    private int delay = 200;

    public MouseInput(ObjManager manager, SpriteSheet spriteSheet) {
        this.manager = manager;
        this.spriteSheet = spriteSheet;
        this.sprite = spriteSheet.getSprite(16, 16, 1, 1);
    }

    public void mousePressed(MouseEvent event) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - this.lastShotTime >= this.delay) {
            int mouseX = event.getX();
            int mouseY = event.getY();

            for (int i = 0; i < this.manager.getObjList().size(); i++) {
                GameObj obj = this.manager.getObjList().get(i);

                if (obj.getId() == GameObjID.Player) {
                    this.manager.addObj(new Orb(obj.getX() + 16, obj.getY() + 24, GameObjID.Orb, this.manager, this.spriteSheet, this.sprite, mouseX, mouseY));
                }
            }
            this.lastShotTime = currentTime;
        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
