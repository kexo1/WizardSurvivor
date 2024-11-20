import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.Color;

public class Game extends Canvas implements Runnable {
    
    private boolean isRunning = false;
    private Thread thread;
    private Handler handler;

    public static void main(String[] args) {
        // Create a new game
        new Game();
    }

    // Partially inspired by https://curious.com/realtutsgml
    public Game() {
        // Create a new window
        new Window(1200, 660, this, "Wizard Survivor");
        start();
        // Create a new handler
        handler = new Handler();
        handler.addObj(new Box(100, 100, ID.Block));
        handler.addObj(new Player(100, 100, ID.Player, handler));

        this.addKeyListener(new KeyInput(handler));
    }

    // Game loop made by Notch (Used by many game developers)
    public void run() {

        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
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

    // Taken from https://curious.com/realtutsgml
    private void start() {
        // Start the game loop
        if (isRunning) return;
        isRunning = true;
        // Create a new thread and start it
        thread = new Thread(this);
        thread.start();
    }

    // Taken from https://curious.com/realtutsgml
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

    // Taken from https://curious.com/realtutsgml
    public void tick() {
        handler.tick();
    }

    // Taken from https://curious.com/realtutsgml
    public void render() {
        // Render game graphics
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            // Preloads 3 frames
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        // Draw background
        g.setColor(Color.black);
        g.fillRect(0, 0, 1200, 660);
        handler.render(g);

        g.dispose();
        bs.show();
    }
    
}
