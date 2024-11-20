import java.awt.Canvas;

public class Game extends Canvas implements Runnable {
    
    public static void main(String[] args) {
        // Create a new game
        new Game();
    }

    public Game() {
        new Window(1000, 550, this, "Wizard Survivor");
    }

    public void run() {
        // Game loop
    }
}
