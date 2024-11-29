import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.Color;

public class GameLoop extends Canvas implements Runnable {
    
    private boolean isRunning = false;
    private ObjManager manager;
    private Thread thread;
    private int FPSLimit = 144;

    public static void main(String[] args) {
        new GameLoop();
    }

    
    public GameLoop() {
        // Create a new window
        new Window(1080, 1080, this, "Wizard Survivor");
        start();

        manager = new ObjManager();
        manager.addObj(new Box(100, 100, GameObjID.Block));
        manager.addObj(new Player(100, 100, GameObjID.Player, manager));

        this.addKeyListener(new KeyInput(manager));
    }

    // Game loop: https://stackoverflow.com/questions/18283199/java-main-game-loop
    public void run() {

        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        long frameTime = 1000 / FPSLimit;

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                tick();
                delta--;
            }

            try {
                Thread.sleep(frameTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
            
        }
        stop();
    }

    // Starting: https://stackoverflow.com/questions/18283199/java-main-game-loop
    private void start() {
        // Start the game loop
        if (isRunning) return;
        isRunning = true;
        // Create a new thread and start it
        thread = new Thread(this);
        thread.start();
    }

    // Stopping thread: https://stackoverflow.com/questions/10961714/how-to-properly-stop-the-thread-in-java
    private void stop() {
        // Stop the game loop
        if (!isRunning) return;
        isRunning = false;
        try {
            // Wait for the thread to die
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Game loop: https://stackoverflow.com/questions/18283199/java-main-game-loop
    public void tick() {
        manager.tick();
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
        manager.render(graphics);

        graphics.dispose();
        buffer.show();
    }
    
}
