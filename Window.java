import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.ImageIcon;

/**
 * Trieda Window sluzi na vytvorenie okna pre hru.
 */
public class Window {
    /**
     * Konstruktor triedy Window vytvori novy objekt pomocou modulu JFrame, nastavi jeho velkost a vytvori okno s hrou.
     * @param width sirka okna
     * @param height vyska okna
     * @param game objekt GameLoop
     * @param title nazov okna
     */
    public Window(int width, int height, GameLoop game, String title) {

        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));
        frame.add(game);                                        // Pridaj objekt GameLoop do okna
        frame.setResizable(false);                    // Nastav okno na nerozsirovane
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Nastav operaciu pri zatvoreni okna
        frame.setLocationRelativeTo(null);                    // Nastav polohu okna na stred obrazovky
        frame.setVisible(true);                             
        frame.setIconImage(new ImageIcon(Window.class.getResource("/sprites/wizard.png")).getImage());  // Nastav ikonu okna
        frame.setAlwaysOnTop(true);                                                              // Nastav okno na vrch
    }
}
