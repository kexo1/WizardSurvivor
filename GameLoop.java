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
public class GameLoop extends Canvas implements Runnable {
    
    // References
    private ObjManager manager;
    private Thread thread;
    private SpriteSheet spriteSheetOrb;
    private SpriteSheet spriteSheetWizard;
    private SpriteSheet spriteSheetBrick;
    private Spawner spawner;
    private BufferedImage brickSprite;
    private Random random = new Random();

    // Attributes
    private boolean isRunning = false;
    private int fpsLimit = 141;

    public static void main(String[] args) {
        new GameLoop();
    }

    public GameLoop() {
        new Window(1080, 1080, this, "Wizard Survivor");    // Create a new window
        this.init();                                                           // Initialize objects and input
        this.start();                                                          // Start the game loop
    }

    private void init() {
        this.manager = new ObjManager();

        this.spriteSheetWizard = new SpriteSheet("/sprites/wizard-spritesheet.png");
        this.spriteSheetBrick = new SpriteSheet("/sprites/textures.png");
        this.spriteSheetOrb = new SpriteSheet("/sprites/projectile.png");

        // Random between 0 and 14
        int randomCol = this.random.nextInt(15);
        int randomRow = this.random.nextInt(2) + 1;

        this.brickSprite = this.spriteSheetBrick.getSprite(16, 16, randomCol, randomRow);

        Player player = new Player(500, 800, GameObjID.Player, this.manager, this.spriteSheetWizard);

        this.manager.addObj(player);
        this.manager.setPlayer(player);
        this.setHighScoreFile();

        this.spawner = new Spawner(0, 0, GameObjID.Spawner, this.manager, null);
        this.manager.addObj(this.spawner);

        this.addKeyListener(new KeyInput(this.manager, this.spawner));
        this.addMouseListener(new MouseInput(this.manager, this.spriteSheetOrb, this.spawner, this));

    }

    public void retry() {
        // Random between 0 and 14
        int randomCol = this.random.nextInt(15);
        int randomRow = this.random.nextInt(2) + 1;
        this.brickSprite = this.spriteSheetBrick.getSprite(16, 16, randomCol, randomRow);

        this.manager.clearEnemies();

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

    // Game loop: https://stackoverflow.com/questions/18283199/java-main-game-loop
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

    // Starting: https://stackoverflow.com/questions/18283199/java-main-game-loop
    private void start() {
        // Start the game loop
        if (this.isRunning) {
            return;
        }
        this.isRunning = true;
        // Create a new thread and start it
        this.thread = new Thread(this);
        this.thread.start();
    }

    // Stopping thread: https://stackoverflow.com/questions/10961714/how-to-properly-stop-the-thread-in-java
    private void stop() {
        // Stop the game loop
        if (!this.isRunning) {
            return;
        }

        this.isRunning = false;

        try {
            // Wait for the thread to die
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Game loop: https://stackoverflow.com/questions/18283199/java-main-game-loop
    public void tick() {
        this.manager.tick();
    }

    // Rendering: https://stackoverflow.com/questions/47377513/getting-graphics-object-to-draw-with-buffer-strategy
    public void render() {

        BufferStrategy buffer = this.getBufferStrategy();
        if (buffer == null) {
            // Preloads 3 frames
            this.createBufferStrategy(3);
            return;
        }

        Graphics graphics = buffer.getDrawGraphics();
        Graphics2D g2d = (Graphics2D)graphics;
        
        // Draw background

        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, 1080, 1080);

        // Draw brick ground (brickSprite) that has size 16x16 using drawImage
        for (int i = 0; i < 1080; i += 32) {
            for (int j = 0; j < 1080; j += 32) {
                graphics.drawImage(this.brickSprite, i, j, 32, 32, null);
            }
        }
        

        this.manager.render(graphics);

        if (this.manager.getPlayer().getHp() <= 0) {
            this.drawDeathMenu(g2d);    
        } else {
            this.drawHUD(g2d);
        }

        graphics.dispose();
        buffer.show();
    }

    public void drawHUD(Graphics2D g2d) {

        // Draw health bar
        g2d.setColor(Color.green);
        g2d.fillRect(5, 5, this.manager.getPlayer().getHp() * 2, 32);
        // Draw border
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(5, 5, this.manager.getPlayer().getMaxHp() * 2, 32);
        // Draw stats
        g2d.setColor(Color.white);
        g2d.setFont(g2d.getFont().deriveFont(12f).deriveFont(java.awt.Font.BOLD));
        g2d.drawString("Health: " + this.manager.getPlayer().getHp(), 5, 56);
        g2d.drawString("Damage: " + this.manager.getPlayer().getDamage(), 5, 70);
        g2d.drawString("Speed: " + this.manager.getPlayer().getSpeed(), 5, 84);
        g2d.drawString("Shoot Delay: " + this.manager.getPlayer().getShootDelay(), 5, 98);
        // Draw wave
        g2d.setFont(g2d.getFont().deriveFont(18f).deriveFont(java.awt.Font.BOLD));
        g2d.drawString("Wave: " + this.spawner.getWave(), 500, 56);
        // Draw score
        g2d.setFont(g2d.getFont().deriveFont(13f).deriveFont(java.awt.Font.BOLD));
        g2d.drawString("Score: " + this.manager.getPlayer().getScore(), 510, 70);
        g2d.drawString("High Score: " + this.manager.getPlayer().getHighScore(), 495, 85);
        // Draw wave countdown
        g2d.setFont(g2d.getFont().deriveFont(20f).deriveFont(java.awt.Font.BOLD));
        if (this.spawner.getWaveState() == Spawner.WaveState.COUNTDOWN) {
            g2d.drawString("Next wave in: " + this.spawner.getCounter(), 470, 110);
        }
    }

    private void drawDeathMenu(Graphics2D g2d) {

        g2d.setColor(Color.red);
        g2d.setFont(g2d.getFont().deriveFont(50f).deriveFont(java.awt.Font.BOLD));
        g2d.drawString("You died!", 420, 350);

        g2d.setColor(Color.white);
        g2d.setFont(g2d.getFont().deriveFont(40f).deriveFont(java.awt.Font.BOLD));
        g2d.drawString("Retry", 480, 460);

        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(440, 410, 180, 70);
    }
}
