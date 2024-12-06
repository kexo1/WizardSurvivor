import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {
    private ObjManager manager;
    private long lastShotTime = 0;
    private int delay = 200;

    public MouseInput(ObjManager manager) {
        this.manager = manager;
    }

    public void mousePressed(MouseEvent event) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastShotTime >= delay) {
            int mouseX = event.getX();
            int mouseY = event.getY();

            for (int i = 0; i < manager.obj.size(); i++) {
                GameObj obj = manager.obj.get(i);

                if (obj.getId() == GameObjID.Player) {
                    manager.addObj(new Orb(obj.getX() + 16, obj.getY() + 24, GameObjID.Orb, manager, mouseX, mouseY));
                }
            }
            lastShotTime = currentTime;
        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

}
