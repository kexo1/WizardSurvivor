import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
    
    Handler handler;

    public KeyInput(Handler handler) {
        this.handler = handler;
    }

    // Taken from https://curious.com/realtutsgml
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        for (int i = 0; i < handler.obj.size(); i++) {
            GameObj tempObj = handler.obj.get(i);
            if (tempObj.getId() == ID.Player) {
                if (key == KeyEvent.VK_W) handler.setUp(true);
                if (key == KeyEvent.VK_S) handler.setDown(true);
                if (key == KeyEvent.VK_A) handler.setLeft(true);
                if (key == KeyEvent.VK_D) handler.setRight(true);
            }
        }
    }

    // Taken from https://curious.com/realtutsgml
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        for (int i = 0; i < handler.obj.size(); i++) {
            GameObj tempObj = handler.obj.get(i);
            if (tempObj.getId() == ID.Player) {
                if (key == KeyEvent.VK_W) handler.setUp(false);
                if (key == KeyEvent.VK_S) handler.setDown(false);
                if (key == KeyEvent.VK_A) handler.setLeft(false);
                if (key == KeyEvent.VK_D) handler.setRight(false);
            }
        }
    }
}
