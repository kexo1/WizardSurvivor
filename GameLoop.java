import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.image.BufferStrategy;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Trieda GameLoop je hlavnou triedou hry, ktora obsahuje herny loop, inicializaciu objektov a vykreslovanie.
 * Trieda dedi od Canvas a implementuje Runnable.
 */
public class GameLoop extends Canvas implements Runnable {
    
    // Referencie
    private ObjManager manager;
    private Thread thread;
    private Spawner spawner;
    private BufferedImage brickSprite;
    private SpriteSheet spriteSheetOrb;
    private SpriteSheet spriteSheetWizard;
    private SpriteSheet spriteSheetBrick;
    private Random random = new Random();

    // Atributy
    private boolean isRunning = false;
    private final int fpsLimit = 141;

    
    /**
     * Metoda main sluzi na spustenie hry.
     * Vytvori novy objekt GameLoop.
     * 
     * @param args
     */
    public static void main(String[] args ) {
        new GameLoop();
    }

    /**
     * Konstruktor triedy GameLoop vytvori novy objekt Window, inicializuje objekty a input a spusti herny loop.
     */
    public GameLoop() {
        new Window(1080, 1080, this, "Wizard Survivor");    // Vytvori novy objekt Window
        this.init();                                                           // Inicializuje objekty a vstup
        this.start();                                                          // Zacne herny loop
    }

    private void init() {
        // Vytvori novy objekt ObjManager
        this.manager = new ObjManager();
        // Nacitaj sprite sheety
        this.spriteSheetWizard = new SpriteSheet("/sprites/wizard-spritesheet.png");
        this.spriteSheetBrick = new SpriteSheet("/sprites/textures.png");
        this.spriteSheetOrb = new SpriteSheet("/sprites/projectile.png");
        this.randomGroundTexture();
        // Vyrob hraca a pridaj ho do ObjManager
        Player player = new Player(500, 800, GameObjID.Player, this.manager, this.spriteSheetWizard);
        this.manager.addObj(player);
        this.manager.setPlayer(player);
        // Nastav high score zo suboru
        this.setHighScoreFile();
        // Vyrob spawner a pridaj ho do ObjManager
        this.spawner = new Spawner(0, 0, GameObjID.Spawner, this.manager, null);
        this.manager.addObj(this.spawner);
        // Pridaj potrebny input na mys a klavesnicu
        this.addKeyListener(new KeyInput(this.manager, this.spawner, this));
        this.addMouseListener(new MouseInput(this.manager, this.spriteSheetOrb, this.spawner, this));
    }

    /**
     * Metoda retry vymaze vsetkych nepriatelo a nastavi hracovi defaultne hodnoty.
     */
    public void retry() {

        this.manager.clearEnemies();
        // Nastav na originalne hodnoty
        this.manager.getPlayer().setMaxHp(100);
        this.manager.getPlayer().setHp(100);
        this.manager.getPlayer().setScore(0);
        this.manager.getPlayer().setDamage(34);
        this.manager.getPlayer().setSpeed(5);
        this.manager.getPlayer().setShootDelay(200);
        this.manager.getPlayer().setX(500);
        this.manager.getPlayer().setY(800);

        this.spawner.setWave(1);
        this.spawner.setWaveSize(5);
        this.spawner.setWaveDiff(1);
        this.spawner.setWaveState(Spawner.WaveState.CHOOSING);
    }

    /** 
     * Metoda randomGroundTexture vyberie nahodnu texturu pre pozadie.
     */
    public void randomGroundTexture() {
        int randomCol = this.random.nextInt(15);    // Random medzi 0 a 14
        int randomRow = this.random.nextInt(2) + 1; // Random medzi 1 a 2
        this.brickSprite = this.spriteSheetBrick.getSprite(16, 16, randomCol, randomRow);
    }

    private int setHighScoreFile() {
        File file = new File("highscore.txt");

        if (file.exists()) {
            this.manager.getPlayer().setHighScore(this.readHighScoreFile());
            
        } else {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println(0);
                this.manager.getPlayer().setHighScore(0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return 0;
        
    }

    private int readHighScoreFile() {
        try (Scanner scanner = new Scanner(new File("highscore.txt"))) {
            return scanner.nextInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Metoda run je hlavnym hernym loopom, ktory sa stara o spravne fungovanie hry.
     * Metoda funguje na principe tick a render, ktore sa striedaju v nekonecnom cykle.
     * Vypocet herneho loopu je zabezpeceny pomocou System.nanoTime() a Thread.sleep() ktore zabezpecuju spravne casovanie.
     * Tento loop sa opakuje 60x za sekundu, pricom render sa vykresluje 141x za sekundu co zabezpecuje plynuly pohyb.
     * Hlavnou inspiraciou pre tuto metodu bola diskusia na StackOverflow, a jeho tvorcom je Notch, ktory je byvali programator hry Minecraft.
     * https://stackoverflow.com/questions/18283199/java-main-game-loop
     */
    public void run() {

        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long frameTime = 1000 / this.fpsLimit;

        while (this.isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                this.tick();
                delta--;
            }

            try {
                Thread.sleep(frameTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.render();
        }
        this.stop();
    }

    /**
     * Metoda start sluzi na spustenie herny loopu.
     * Metoda kontroluje, ci uz herny loop bezi, ak ano, tak sa metoda ukonci.
     * https://stackoverflow.com/questions/18283199/java-main-game-loop
     */
    private void start() {

        if (this.isRunning) {
            return;
        }
        this.isRunning = true;
        this.thread = new Thread(this);     // Vytvor novy thread (vlakno)
        this.thread.start();                
    }

    /**
     * Metoda stop sluzi na zastavenie herneho loopu.
     * Metoda kontroluje, ci uz herny loop bezi, ak nie, tak sa metoda ukonci.
     * https://stackoverflow.com/questions/10961714/how-to-properly-stop-the-thread-in-java
     */ 
    private void stop() {

        if (!this.isRunning) {
            return;
        }

        this.isRunning = false;

        try {
            this.thread.join();             // Pockaj na ukoncenie threadu
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda tick sluzi na aktualizaciu objektov v hre.
     * Metoda vola tick() na objekte manager, ktory aktualizuje vsetky objekty v hre.
     * https://stackoverflow.com/questions/18283199/java-main-game-loop
     */
    public void tick() {
        this.manager.tick();
    }

    public void render() {

        BufferStrategy buffer = this.getBufferStrategy();  // Ziskaj buffer strategy
        if (buffer == null) {
            this.createBufferStrategy(3);       // Prednacitaj 3 snimky
            return;
        }

        Graphics graphics = buffer.getDrawGraphics();      // Ziskaj graficky objekt z bufferu 
        Graphics2D g2d = (Graphics2D)graphics;             // Ziskaj 2D graficky objekt ktory ma viacej moznosti na vykreslovanie (preferovane na vykreslovanie textu)

        // Vykresli pozadie z brickSprite
        for (int i = 0; i < 1080; i += 32) {
            for (int j = 0; j < 1080; j += 32) {
                graphics.drawImage(this.brickSprite, i, j, 32, 32, null);
            }
        }
        
        this.manager.render(graphics);
        // Ak je hrac mrtvy, vykresli death menu, inak vykresli HUD
        if (this.manager.getPlayer().getHp() <= 0) {
            this.drawDeathMenu(g2d);    
        } else {
            this.drawHUD(g2d);
        }

        graphics.dispose();       // Uvolni graficky objekt z pamate
        buffer.show();            // Vyrenderuj graficky objekt na obrazovku
    }

    private void drawHUD(Graphics2D g2d) {

        // Vypln health bar zelene
        g2d.setColor(Color.green);
        g2d.fillRect(5, 5, this.manager.getPlayer().getHp() * 2, 32);
        // Vykresli border okolo health baru
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(5, 5, this.manager.getPlayer().getMaxHp() * 2, 32);
        // Vykrsli statistiky hraca
        g2d.setColor(Color.white);
        g2d.setFont(g2d.getFont().deriveFont(12f).deriveFont(java.awt.Font.BOLD));
        g2d.drawString("Health: " + this.manager.getPlayer().getHp(), 5, 56);
        g2d.drawString("Damage: " + this.manager.getPlayer().getDamage(), 5, 70);
        g2d.drawString("Speed: " + this.manager.getPlayer().getSpeed(), 5, 84);
        g2d.drawString("Shoot Delay: " + this.manager.getPlayer().getShootDelay(), 5, 98);
        // Vykresli kolo vlny (wave)
        g2d.setFont(g2d.getFont().deriveFont(18f).deriveFont(java.awt.Font.BOLD));
        g2d.drawString("Wave: " + this.spawner.getWave(), 500, 56);
        // Vykresli score a high score
        g2d.setFont(g2d.getFont().deriveFont(13f).deriveFont(java.awt.Font.BOLD));
        g2d.drawString("Score: " + this.manager.getPlayer().getScore(), 510, 70);
        g2d.drawString("High Score: " + this.manager.getPlayer().getHighScore(), 495, 85);
        // Vykresli odpocet k vlnam
        g2d.setFont(g2d.getFont().deriveFont(20f).deriveFont(java.awt.Font.BOLD));
        if (this.spawner.getWaveState() == Spawner.WaveState.COUNTDOWN) {
            g2d.drawString("Next wave in: " + this.spawner.getCounter(), 470, 110);
        }
    }

    private void drawDeathMenu(Graphics2D g2d) {
        // Vykresli "You died!" text
        g2d.setColor(Color.red);
        g2d.setFont(g2d.getFont().deriveFont(50f).deriveFont(java.awt.Font.BOLD));
        g2d.drawString("You died!", 420, 350);
        // Vykresli "Retry" text
        g2d.setColor(Color.white);
        g2d.setFont(g2d.getFont().deriveFont(40f).deriveFont(java.awt.Font.BOLD));
        g2d.drawString("Retry", 480, 460);
        // Vykresli obldznik okolo "Retry" textu
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(440, 410, 180, 70);
    }
}
