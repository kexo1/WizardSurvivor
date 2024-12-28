import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteSheet {

    private BufferedImage spriteSheet;

    public SpriteSheet(String path) {
        try {
            this.spriteSheet = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage(int imgSize, int col, int row, int width, int height, int offsetX) {
        return this.spriteSheet.getSubimage((col * (imgSize)) - imgSize + offsetX, (row * imgSize) - imgSize, width, height);
    }

}
