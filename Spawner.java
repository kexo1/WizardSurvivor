import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Spawner extends GameObj {

    // References
    private ObjManager manager;
    private Abilities abilities;
    private SpriteSheet spriteSheetHeal;
    private SpriteSheet spriteSheetSlime;
    private SpriteSheet spriteSheetGhoul;
    private SpriteSheet spriteSheetBee;

    // Spawner attributes
    private ArrayList<String> enemySpawnList = new ArrayList<String>();
    private int wave = 1;
    private int waveSize = 5;
    private int waveDiff = 1;
    private int waveCountdown = 4;
    private int waveSpawnDelay = 2;
    private float healSpawnDelay = 60;

    public enum WaveState {
        SPAWNING,
        COUNTDOWN,
        CHOOSING
    }
    private WaveState waveState = WaveState.CHOOSING;

    //Countdown attributes
    private long lastTimeCountdown = System.currentTimeMillis();
    private long lastTimeSpawnEnemy = System.currentTimeMillis();
    private long lastTimeSpawnHeal = System.currentTimeMillis();
    private long currentTime = System.currentTimeMillis();

    public Spawner(int x, int y, GameObjID gameObjID, ObjManager manager, SpriteSheet spriteSheet) {
        super(x, y, gameObjID, manager, spriteSheet);

        this.manager = manager;
        this.abilities = new Abilities(this.manager.getPlayer(), new SpriteSheet("/sprites/icons-spritesheet.png"));

        this.spriteSheetHeal = new SpriteSheet("/sprites/heal.png");
        this.spriteSheetSlime = new SpriteSheet("/sprites/slime-spritesheet.png");
        this.spriteSheetGhoul = new SpriteSheet("/sprites/ghoul-spritesheet.png");
        this.spriteSheetBee = new SpriteSheet("/sprites/bee-spritesheet.png");
    }

    public void tick() {
        this.startCountdown();
        this.spawningEnemies();
        this.spawningHeal();
    }

    public void render(Graphics graphics) {
        if (this.waveState == WaveState.CHOOSING) {
            this.abilities.drawAbilities(graphics);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(0, 0, 0, 0);
    }


    private void startCountdown() {
        this.currentTime = System.currentTimeMillis();

        if (this.waveState == WaveState.COUNTDOWN && this.currentTime - this.lastTimeCountdown >= this.waveCountdown * 1000) {
            this.lastTimeCountdown = this.currentTime;
            this.waveState = WaveState.SPAWNING;
            this.generateEnemySpawnList();
        }
    }

    private void spawningEnemies() {
        this.currentTime = System.currentTimeMillis();

        if (this.waveState == WaveState.SPAWNING && this.currentTime - this.lastTimeSpawnEnemy >= this.waveSpawnDelay * 1000) {

            if (this.enemySpawnList.isEmpty()) {
                if (this.isEnemyAlive()) {
                    return;
                } else {
                    this.waveFinished();
                    return;
                }
            }
            
            System.out.println("Spawning enemy");
            this.lastTimeSpawnEnemy = this.currentTime;
            
            int[] randomPosition = this.generateRandomPosition();
            
            String enemy = this.enemySpawnList.get(0);
            this.enemySpawnList.remove(0);
            
            if ("slime".equals(enemy)) {
                this.manager.addObj(new Slime(randomPosition[0], randomPosition[1], GameObjID.Enemy, this.manager, this.manager.getPlayer(), this.spriteSheetSlime));
            } else if ("bee".equals(enemy)) {
                this.manager.addObj(new Bee(randomPosition[0], randomPosition[1], GameObjID.Enemy, this.manager, this.manager.getPlayer(), this.spriteSheetBee));
            } else if ("ghoul".equals(enemy)) {
                this.manager.addObj(new Ghoul(randomPosition[0], randomPosition[1], GameObjID.Enemy, this.manager, this.manager.getPlayer(), this.spriteSheetGhoul));
            }

            

        }
    }

    private void spawningHeal() {
        this.currentTime = System.currentTimeMillis();

        if (this.waveState != WaveState.CHOOSING && this.currentTime - this.lastTimeSpawnHeal >= this.healSpawnDelay * 1000) {
            this.lastTimeSpawnHeal = this.currentTime;
            int x = 50 + (int)(Math.random() * ((900)));
            int y = 50 + (int)(Math.random() * ((900)));
            System.out.println("Spawning heal at " + x + ", " + y);
            this.manager.addObj(new Heal(x, y, GameObjID.Heal, this.manager, this.spriteSheetHeal));
        }
    }

    private boolean isEnemyAlive() {
        for (GameObj obj : this.manager.getObjList()) {
            if (obj.getId() == GameObjID.Enemy) {
                return true;
            }
        }
        return false;
    }

    private void waveFinished() {
        this.wave++;
        this.waveSize += 1;

        if (this.wave == 5 || this.wave == 10) {
            this.waveDiff++;
        }

        this.waveState = WaveState.CHOOSING;
    }

    private void generateEnemySpawnList() {
        this.enemySpawnList.clear();

        for (int i = 0; i < this.waveSize; i++) {
            int enemy = (int)(Math.random() * this.waveDiff);

            switch (enemy) {
                case 0:
                    this.enemySpawnList.add("slime");
                    break;
                case 1:
                    this.enemySpawnList.add("bee");
                    break;
                case 2:
                    this.enemySpawnList.add("ghoul");
                    break;
            }
        }
    }

    private int[] generateRandomPosition() {
        int x;
        int y;
        if (Math.random() < 0.5) {
            x = -100 + (int)(Math.random() * 100);
        } else {
            x = 1000 + (int)(Math.random() * 100);
        }

        if (Math.random() < 0.5) {
            y = -100 + (int)(Math.random() * 100);
        } else {
            y = 1000 + (int)(Math.random() * 100);
        }

        return new int[]{x, y};
    }

    public void setWaveState(WaveState waveState) {
        this.lastTimeCountdown = System.currentTimeMillis();
        this.waveState = waveState;
    }

    public int getWave() {
        return this.wave;
    }

    public int getCounter() {
        return ((int)(this.waveCountdown - (int)((this.currentTime - this.lastTimeCountdown) / 1000))) - 1;
    }

    public WaveState getWaveState() {
        return this.waveState;
    }

    public Abilities getAbilities() {
        return this.abilities;
    }
}
