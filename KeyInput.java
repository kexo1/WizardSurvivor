import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
    
    // References
    private ObjManager manager;

    public KeyInput(ObjManager manager) {
        this.manager = manager;
    }

    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();
        
        for (int i = 0; i < this.manager.getObjList().size(); i++) {

            GameObj obj = this.manager.getObjList().get(i);
            if (obj.getId() != GameObjID.Player) {
                continue;
            }

            if (key == KeyEvent.VK_W) {
                this.manager.setUp(true);
            }
            if (key == KeyEvent.VK_S) {
                this.manager.setDown(true);
            }
            if (key == KeyEvent.VK_A) {
                this.manager.setLeft(true);
            }
            if (key == KeyEvent.VK_D) {
                this.manager.setRight(true);
            }
        }
    }

    public void keyReleased(KeyEvent event) {
        int key = event.getKeyCode();

        for (int i = 0; i < this.manager.getObjList().size(); i++) {
            GameObj obj = this.manager.getObjList().get(i);

            if (obj.getId() != GameObjID.Player) {
                continue;
            }

            if (key == KeyEvent.VK_W) {
                this.manager.setUp(false);
            }
            if (key == KeyEvent.VK_S) {
                this.manager.setDown(false);
            }
            if (key == KeyEvent.VK_A) {
                this.manager.setLeft(false);
            }
            if (key == KeyEvent.VK_D) {
                this.manager.setRight(false);
            }
        }
    }
}
