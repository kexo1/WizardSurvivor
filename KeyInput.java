import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
    
    // References
    private ObjManager manager;
    private Spawner spawner;

    public KeyInput(ObjManager manager, Spawner spawner) {
        this.manager = manager;
        this.spawner = spawner;
    }

    public void keyPressed(KeyEvent event) {
        
        if (this.spawner.getWaveState() == Spawner.WaveState.CHOOSING) {
            return;
        }

        if (this.manager.getPlayer().getHp() <= 0) {
            return;
        }

        int key = event.getKeyCode();

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

    public void keyReleased(KeyEvent event) {

        int key = event.getKeyCode();

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
