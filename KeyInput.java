import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
    
    ObjManager manager;

    public KeyInput(ObjManager manager) {
        this.manager = manager;
    }

    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();
        
        for (int i = 0; i < manager.obj.size(); i++) {

            GameObj obj = manager.obj.get(i);
            if (obj.getId() != GameObjID.Player)
                continue;
            
            if (key == KeyEvent.VK_W)
                manager.setUp(true);
            if (key == KeyEvent.VK_S)
                manager.setDown(true);
            if (key == KeyEvent.VK_A)
                manager.setLeft(true);
            if (key == KeyEvent.VK_D)
                manager.setRight(true);
        }
    }

    public void keyReleased(KeyEvent event) {
        int key = event.getKeyCode();

        for (int i = 0; i < manager.obj.size(); i++) {
            GameObj obj = manager.obj.get(i);

            if (obj.getId() != GameObjID.Player)
                continue;

            if (key == KeyEvent.VK_W)
                manager.setUp(false);
            if (key == KeyEvent.VK_S)
                manager.setDown(false);
            if (key == KeyEvent.VK_A)
                manager.setLeft(false);
            if (key == KeyEvent.VK_D)
                manager.setRight(false);
        }
    }
}
