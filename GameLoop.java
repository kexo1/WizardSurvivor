import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.Color;

public class GameLoop extends Canvas implements Runnable {
    
    // References
    private ObjManager manager;
    private Thread thread;

    // Attributes
    private boolean isRunning = false;
    private int fpsLimit = 144;

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

        SpriteSheet spriteSheet1 = new SpriteSheet("/sprites/wizard_spritesheet.png");
        Player player = new Player(100, 100, GameObjID.Player, this.manager, spriteSheet1);
        this.manager.addObj(player);
        this.manager.setPlayer(player);

        SpriteSheet spriteSheet2 = new SpriteSheet("/sprites/heal.png");
        this.manager.addObj(new Box(200, 100, GameObjID.Supply, this.manager, spriteSheet2));

        SpriteSheet spriteSheet3 = new SpriteSheet("/sprites/slime-spritesheet.png");
        this.manager.addObj(new Enemy(300, 100, GameObjID.Enemy, this.manager, this.manager.getPlayer(), spriteSheet3));

        this.addKeyListener(new KeyInput(this.manager));

        SpriteSheet spriteSheet4 = new SpriteSheet("/sprites/slime-spritesheet.png");
        this.addMouseListener(new MouseInput(this.manager, spriteSheet4));
    }

    // Game loop: https://stackoverflow.com/questions/18283199/java-main-game-loop
    public void run() {

        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        /*
        long timer = System.currentTimeMillis();
        int frames = 0;
        */
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

            /* 
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
            */
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
        
        // Draw background
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, 1080, 1080);

        this.manager.render(graphics);

        // Draw health bar
        graphics.setColor(Color.black);
        graphics.fillRect(5, 5, 200, 32);
        graphics.setColor(Color.green);
        graphics.fillRect(5, 5, this.manager.getPlayer().getHp() * 2, 32);
        graphics.setColor(Color.black);
        graphics.drawRect(5, 5, 200, 32);
        graphics.setColor(Color.black);
        graphics.drawString("Health: " + this.manager.getPlayer().getHp(), 5, 56);

        graphics.dispose();
        buffer.show();
    }
}
