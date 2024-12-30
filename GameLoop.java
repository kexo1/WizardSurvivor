import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.image.BufferStrategy;
import java.awt.Color;

public class GameLoop extends Canvas implements Runnable {
    
    // References
    private ObjManager manager;
    private Thread thread;
    private SpriteSheet spriteSheet;
    private Spawner spawner;

    // Attributes
    private boolean isRunning = false;
    private int fpsLimit = 141;

    public static void main(String[] args) {
        new GameLoop();
    }

    public GameLoop() {
        // Create a new window
        new Window(1080, 1080, this, "Wizard Survivor");
        // Initialize objects and input
        this.init();
        // Start the game loop
        this.start();
    }

    private void init() {
        this.manager = new ObjManager();

        this.spriteSheet = new SpriteSheet("/sprites/wizard-spritesheet.png");
        Player player = new Player(500, 800, GameObjID.Player, this.manager, this.spriteSheet);

        this.manager.addObj(player);
        this.manager.setPlayer(player);

        this.spawner = new Spawner(0, 0, GameObjID.Spawner, this.manager, null);
        this.manager.addObj(this.spawner);

        this.addKeyListener(new KeyInput(this.manager, this.spawner));

        this.spriteSheet = new SpriteSheet("/sprites/projectile.png");
        this.addMouseListener(new MouseInput(this.manager, this.spriteSheet, this.spawner));

        
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

        this.manager.render(graphics);

        // Draw health bar
        graphics.setColor(Color.green);
        graphics.fillRect(5, 5, this.manager.getPlayer().getHp() * 2, 32);
        // Draw border
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(5, 5, this.manager.getPlayer().getMaxHp() * 2, 32);
        // Draw stats
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
        // Draw wave countdown
        g2d.setFont(g2d.getFont().deriveFont(20f).deriveFont(java.awt.Font.BOLD));
        if (this.spawner.getWaveState() == Spawner.WaveState.COUNTDOWN) {
            g2d.drawString("Next wave in: " + this.spawner.getCounter(), 470, 110);
        }

        graphics.dispose();
        buffer.show();
    }
}
