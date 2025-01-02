import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Trieda MouseInput sluzi na spracovanie vstupu z mysi.
 * Metoda mousePressed spracovava stlacenie tlacidla mysi.
 * Metoda pickAbilities spracovava vyber vylepseni hraca.
 * Metoda deathMenu spracovava stlacenie tlacidla mysi po smrti hraca.
 * Metoda setDelay nastavuje oneskorenie medzi strelbou hraca.
 */
public class MouseInput extends MouseAdapter {

    // Referencie
    private ObjManager manager;
    private BufferedImage sprite;
    private SpriteSheet spriteSheet;
    private Spawner spawner;
    private Abilities abilities;
    private GameLoop gameLoop;

    // Atributy
    private long lastShotTime = 0;
    private int delay;

    /**
     * Konstruktor triedy MouseInput.
     * 
     * @param manager objekt manazera
     * @param spriteSheet spriteSheet, ktory obsahuje sprite pre objekt Orb
     * @param spawner objekt spawner-u
     * @param gameLoop objekt gameLoop
     */
    public MouseInput(ObjManager manager, SpriteSheet spriteSheet, Spawner spawner, GameLoop gameLoop) {
        this.manager = manager;
        this.spriteSheet = spriteSheet;
        this.sprite = spriteSheet.getSprite(16, 16, 1, 1);
        this.gameLoop = gameLoop;
        
        this.spawner = spawner;
        this.abilities = spawner.getAbilities();
    }

    /**
     * Metoda mousePressed spracovava stlacenie tlacidla mysi.
     * Metoda spracovava stlacenie tlacidla mysi, ked hrac zije alebo ked sa nachadza v stave vyberu vylepseni.
     * Metoda vytvara novy Orb, ktory bude vytvoreny na pozicii hraca a bude letiet smerom k pozicii mysi.
     * Metoda nastavuje cas poslednej strely.
     * 
     * @param event vstup z mysi
     */
    public void mousePressed(MouseEvent event) {

        int mouseX = event.getX();
        int mouseY = event.getY();

        if (this.manager.getPlayer().getHp() <= 0) {
            this.deathMenu(mouseX, mouseY);
            return;
        }

        if (this.spawner.getWaveState() == Spawner.WaveState.CHOOSING) {
            this.pickAbilities(mouseX, mouseY);
            return;
        } 

        long currentTime = System.currentTimeMillis();          // Ziskaj aktualny cas v milisekundach
        if (currentTime - this.lastShotTime >= this.delay) {    // Ak ubehol cas od poslednej strely vacsi ako oneskorenie
            this.manager.addObj(new Orb(this.manager.getPlayer().getX() + 16, this.manager.getPlayer().getY() + 6, GameObjID.Orb, this.manager, this.spriteSheet, this.sprite, mouseX, mouseY));
            this.lastShotTime = currentTime;
        }
    }

    private void pickAbilities(int mouseX, int mouseY) {

        if (mouseX >= 250 && mouseX <= 346 && mouseY >= 500 && mouseY <= 596) {
            this.abilities.increaseHp();
            this.spawner.setWaveState(Spawner.WaveState.COUNTDOWN);
            System.out.println("Increased HP");

        } else if (mouseX >= 400 && mouseX <= 496 && mouseY >= 500 && mouseY <= 596) {
            this.abilities.increaseDamage();
            this.spawner.setWaveState(Spawner.WaveState.COUNTDOWN);
            System.out.println("Increased Damage");

        } else if (mouseX >= 550 && mouseX <= 646 && mouseY >= 500 && mouseY <= 596) {
            this.abilities.increaseSpeed();
            this.spawner.setWaveState(Spawner.WaveState.COUNTDOWN);
            System.out.println("Increased Speed");

        } else if (mouseX >= 700 && mouseX <= 796 && mouseY >= 500 && mouseY <= 596) {
            this.abilities.decreaseShootDelay();
            this.spawner.setWaveState(Spawner.WaveState.COUNTDOWN);
            System.out.println("Decreased Shoot Delay");
        }

        this.delay = this.manager.getPlayer().getShootDelay();
    }

    private void deathMenu(int mouseX, int mouseY) {
        
        if (mouseX >= 410 && mouseX <= 620 && mouseY >= 410 && mouseY <= 480) {
            this.gameLoop.retry();
        }
    }

    /**
     * Metoda setDelay nastavuje oneskorenie medzi strelbou hraca.
     * 
     * @param delay oneskorenie medzi strelbou
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }
}
