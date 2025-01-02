import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 * Trieda Spawner zabezpecuje vytvaranie nepriatelov a heal-u na mape.
 * Spawner ma moznost nahodne vyberat nepriatelov, ktori sa maju vytvorit.
 * Spawner moze urcit pocet vln nepriatelov, ktore sa maju vytvorit a ich obtiaznost.
 */
public class Spawner extends GameObj {

    // Referencie
    private final ObjManager manager;
    private final Abilities abilities;
    private final SpriteSheet spriteSheetHeal;
    private final SpriteSheet spriteSheetSlime;
    private final SpriteSheet spriteSheetGhoul;
    private final SpriteSheet spriteSheetBee;

    // Atributy
    private final LinkedList<String> enemySpawnList = new LinkedList<>();
    private int wave = 1;
    private int waveSize = 5;
    private int waveDiff = 1;
    private final int waveCountdown = 4;
    private float waveSpawnDelay = 2;
    private float healSpawnDelay = 60;

    /**
     * Enum WaveState sluzi na identifikaciu stavu vlny.
     */
    public enum WaveState {
        /**
         * stav, v ktorom sa nepriatelia vytvaraju
         */
        SPAWNING,
        /**
         * stav, v ktorom sa caka na zaciatok vlny
         */
        COUNTDOWN,
        /**
         * stav, v ktorom hrac vybera schopnosti
         */
        CHOOSING
    }
    private WaveState waveState = WaveState.CHOOSING;

    // Casovanie jednotlivych udalosti
    private long lastTimeCountdown = System.currentTimeMillis();
    private long lastTimeSpawnEnemy = System.currentTimeMillis();
    private long lastTimeSpawnHeal = System.currentTimeMillis();
    private long currentTime = System.currentTimeMillis();

    /**
     * Konstruktor triedy Spawner.
     * 
     * @param x x-ova suradnica objektu, neni pouzita
     * @param y y-ova suradnica objektu, neni pouzita
     * @param gameObjID identifikator objektu
     * @param manager objekt manazera
     * @param spriteSheet spriteSheet, ktory nema v sebe sprite, ale je potrebny pre volanie konstruktor predka
     */
    public Spawner(int x, int y, GameObjID gameObjID, ObjManager manager, SpriteSheet spriteSheet) {
        super(x, y, gameObjID, manager, spriteSheet);

        this.manager = manager;
        this.abilities = new Abilities(this.manager.getPlayer(), new SpriteSheet("/sprites/icons-spritesheet.png"));

        this.spriteSheetHeal = new SpriteSheet("/sprites/heal.png");
        this.spriteSheetSlime = new SpriteSheet("/sprites/slime-spritesheet.png");
        this.spriteSheetGhoul = new SpriteSheet("/sprites/ghoul-spritesheet.png");
        this.spriteSheetBee = new SpriteSheet("/sprites/bee-spritesheet.png");
    }

    /**
     * Metoda tick zabezpecuje aktualizaciu casovacov a vytvaranie nepriatelov a heal-u.
     */
    public void tick() {
        this.startCountdown();
        this.spawningEnemies();
        this.spawningHeal();
    }

    /**
     * Metoda render zabezpecuje vykreslenie schopnosti na obrazovku pocas vyberu.
     * 
     * @param graphics graficky kontext
     */
    public void render(Graphics graphics) {
        if (this.waveState == WaveState.CHOOSING) {
            this.abilities.drawAbilities(graphics);
        }
    }

    /**
     * Metoda getBounds vrati nulovy obdlznik, kedze Spawner iba vytvara objekty.
     * 
     * @return Rectangle
     */
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
                    this.waveFinished(); // Ak su vsetci nepriatelia mrtvi, vlna je ukoncena
                    return;
                }
            }
            
            this.lastTimeSpawnEnemy = this.currentTime;
            
            int[] randomPosition = this.generateRandomPosition();
            
            String enemy = this.enemySpawnList.get(0);
            this.enemySpawnList.remove(0);
            
            switch (enemy) {
                case "slime":
                    this.manager.addObj(new Slime(randomPosition[0], randomPosition[1], GameObjID.Enemy, this.manager, this.manager.getPlayer(), this.spriteSheetSlime));
                    break;
                case "bee":
                    this.manager.addObj(new Wasp(randomPosition[0], randomPosition[1], GameObjID.Enemy, this.manager, this.manager.getPlayer(), this.spriteSheetBee));
                    break;
                case "ghoul":
                    this.manager.addObj(new Ghoul(randomPosition[0], randomPosition[1], GameObjID.Enemy, this.manager, this.manager.getPlayer(), this.spriteSheetGhoul));
                    break;
            }
        }
    }

    private void spawningHeal() {
        this.currentTime = System.currentTimeMillis();

        if (this.waveState != WaveState.CHOOSING && this.currentTime - this.lastTimeSpawnHeal >= this.healSpawnDelay * 1000) {
            this.lastTimeSpawnHeal = this.currentTime;

            int x = 50 + (int)(Math.random() * ((900)));    // Nahodne cislo od 50 do 950
            int y = 50 + (int)(Math.random() * ((900)));    // Nahodne cislo od 50 do 950
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

        // Ak hrac prejde 5 alebo 10 vln, tak sa obtiaznost zvysi o 1 a casovac na vytvaranie nepriatelov sa znizi o 0.5 sekundy.
        if (this.wave == 5 || this.wave == 10) {
            this.waveDiff++;
            this.waveSpawnDelay -= 0.5F;
            this.healSpawnDelay -= 0.5F;
        }
        // Ak hrac prejde 10 vln, tak sa obtiaznost bude postupne zvysovat do nastavenych limitov.
        if (this.wave > 10 && this.wave % 5 == 0) {

            this.waveSpawnDelay -= 0.05F;

            if (this.waveSpawnDelay <= 0.5) {
                this.waveSpawnDelay = 0.5f;
            }
            
            this.healSpawnDelay -= 0.5F;

            if (this.waveSpawnDelay <= 15) {
                this.waveSpawnDelay = 15;
            }
        }

        this.waveState = WaveState.CHOOSING;
    }

    private void generateEnemySpawnList() {
        this.enemySpawnList.clear();

        for (int i = 0; i < this.waveSize; i++) {
            int enemy = (int)(Math.random() * this.waveDiff);   // Nahodne cislo od 0 po waveDiff, napr. pri waveDiff = 2 mozu byt Slimovia a Osi.

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
        if (Math.random() < 0.5) {                  // Nahodne cislo od 0 do 1
            x = -100 + (int)(Math.random() * 100);  // Nahodne cislo od -100 do 0
        } else {
            x = 1000 + (int)(Math.random() * 100);  // Nahodne cislo od 1000 do 1100
        }

        if (Math.random() < 0.5) {
            y = -100 + (int)(Math.random() * 100);  // Nahodne cislo od -100 do 0
        } else {
            y = 1000 + (int)(Math.random() * 100);  // Nahodne cislo od 1000 do 1100
        }

        return new int[]{x, y};                     // Pozicie mimo obrazovky
    }

    /**  
     * Metoda setWaveState nastavi stav vlny a nastavi casovac na aktualny cas.
     * 
     * @param waveState stav vlny (spawning, countdown, choosing)
     */
    public void setWaveState(WaveState waveState) {
        this.lastTimeCountdown = System.currentTimeMillis();
        this.waveState = waveState;
    }

    public int getWave() {
        return this.wave;
    }

    /**
     * Metoda getCounter vrati zostavajuci cas do konca vlny.
     * 
     * @return zostavajuci cas do konca vlny
     */
    public int getCounter() {
        return this.waveCountdown - (int)((this.currentTime - this.lastTimeCountdown) / 1000) - 1;
    }

    public WaveState getWaveState() {
        return this.waveState;
    }

    public Abilities getAbilities() {
        return this.abilities;
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

    public void setWaveSize(int waveSize) {
        this.waveSize = waveSize;
    }

    public void setWaveDiff(int waveDiff) {
        this.waveDiff = waveDiff;
    }
}
