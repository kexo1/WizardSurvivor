import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MouseInput extends MouseAdapter {

    // References
    private ObjManager manager;
    private BufferedImage sprite;
    private SpriteSheet spriteSheet;
    private Spawner spawner;
    private Abilities abilities;
    private GameLoop gameLoop;

    // Attributes
    private long lastShotTime = 0;
    private int delay;

    public MouseInput(ObjManager manager, SpriteSheet spriteSheet, Spawner spawner, GameLoop gameLoop) {
        this.manager = manager;
        this.spriteSheet = spriteSheet;
        this.sprite = spriteSheet.getSprite(16, 16, 1, 1);
        this.gameLoop = gameLoop;
        
        this.spawner = spawner;
        this.abilities = spawner.getAbilities();
    }

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

        long currentTime = System.currentTimeMillis();

        if (currentTime - this.lastShotTime >= this.delay) {
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

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
