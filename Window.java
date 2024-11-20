import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.ImageIcon;

public class Window {

    public Window(int width, int height, Game game, String title) {

        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.add(game);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon(Window.class.getResource("/images/wizard.png")).getImage());

    }
}
