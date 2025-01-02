import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Trieda KeyInput sluzi na spracovanie vstupu od klavesnice.
 * Trieda obsahuje referencie na ObjManager a Spawner.
 * Trieda obsahuje metody keyPressed a keyReleased, ktore spracovavaju stlacene a pustene klavesy.
 * Metoda keyPressed spracovava stlacenie klavesu a keyReleased pustenie klavesu.
 * Metoda keyPressed a keyReleased spracovava stlacene klavesy W, S, A, D a nastavuje smer hraca.
 */
public class KeyInput extends KeyAdapter {
    
    // Referencie
    private final ObjManager manager;
    private final Spawner spawner;
    private final GameLoop gameLoop;

    /**
     * Konstruktor triedy KeyInput.
     * 
     * @param manager objekt manazera
     * @param spawner objekt spawner-u
     * @param gameLoop objekt gameLoop
     */
    public KeyInput(ObjManager manager, Spawner spawner, GameLoop gameLoop) {
        this.manager = manager;
        this.spawner = spawner;
        this.gameLoop = gameLoop;
    }

    /** 
     * Metoda keyPressed spracovava stlacene klavesy.
     * Metoda nastavuje smer hraca podla stlacenej klavesy a podla toho, ci hrac zije alebo ci sa nachadza v stave vyberu vylepseni.
     * Tiez moze zmenit texturu pozadia stlacenim klavesy SPACE.
     * 
     * @param event vstup od klavesnice
     */
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

        } else if (key == KeyEvent.VK_S) {
            this.manager.setDown(true);

        } else if (key == KeyEvent.VK_A) {
            this.manager.setLeft(true);

        } else if (key == KeyEvent.VK_D) {
            this.manager.setRight(true);

        } else if (key == KeyEvent.VK_SPACE) {
            this.gameLoop.randomGroundTexture();
        }
    }

    /** 
     * Metoda keyReleased spracovava pustene klavesy.
     * Metoda nastavuje smer hraca podla pustenej klavesy.
     * 
     * @param event vstup od klavesnice
     */
    public void keyReleased(KeyEvent event) {

        int key = event.getKeyCode();

        if (key == KeyEvent.VK_W) {
            this.manager.setUp(false);

        } else if (key == KeyEvent.VK_S) {
            this.manager.setDown(false);

        } else if (key == KeyEvent.VK_A) {
            this.manager.setLeft(false);

        } else if (key == KeyEvent.VK_D) {
            this.manager.setRight(false);
        }
    }
}
