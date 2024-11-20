import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.ImageIcon;

public class Window {

    public Window(int width, int height, Game game, String title) {
        // Create a new window and set its size
        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));
        // Add the game to the window
        frame.add(game);
        // Set window to be not resizable
        frame.setResizable(false);
        // Set the window to close when the user clicks the close button
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // Set the icon of the window
        frame.setIconImage(new ImageIcon(Window.class.getResource("/images/wizard.png")).getImage());

    }
}
