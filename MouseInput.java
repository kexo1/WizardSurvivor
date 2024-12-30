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

    // Attributes
    private long lastShotTime = 0;
    private int delay;

    public MouseInput(ObjManager manager, SpriteSheet spriteSheet, Spawner spawner) {
        this.manager = manager;
        this.spriteSheet = spriteSheet;
        this.sprite = spriteSheet.getSprite(16, 16, 1, 1);
        
        this.spawner = spawner;
        this.abilities = spawner.getAbilities();
    }

    public void mousePressed(MouseEvent event) {

        int mouseX = event.getX();
        int mouseY = event.getY();

        if (this.spawner.getWaveState() == Spawner.WaveState.CHOOSING) {

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
            return;
        } 

        long currentTime = System.currentTimeMillis();

        if (currentTime - this.lastShotTime >= this.delay) {
            this.manager.addObj(new Orb(this.manager.getPlayer().getX() + 16, this.manager.getPlayer().getY() + 6, GameObjID.Orb, this.manager, this.spriteSheet, this.sprite, mouseX, mouseY));
            this.lastShotTime = currentTime;
        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
