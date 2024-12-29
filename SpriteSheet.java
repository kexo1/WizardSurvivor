import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteSheet {

    private BufferedImage spriteSheet;
    private BufferedImage[] spriteSheetRow;

    public SpriteSheet(String path) {
        try {
            this.spriteSheet = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getSprite(int imgSizeX, int imgSizeY, int col, int row) {
        return this.spriteSheet.getSubimage((col * (imgSizeX)) - imgSizeX, (row * imgSizeY) - imgSizeY, imgSizeX, imgSizeY);
    }

    public BufferedImage[] getSpriteSheetRow(int imgSizeX, int imgSizeY, int row, int numberOfSprites) {
        this.spriteSheetRow = new BufferedImage[numberOfSprites];

        for (int i = 0; i < numberOfSprites; i++) {
            this.spriteSheetRow[i] = this.spriteSheet.getSubimage(((i + 1) * (imgSizeX)) - imgSizeX, (row * imgSizeY) - imgSizeY, imgSizeX, imgSizeY);
        }
        return this.spriteSheetRow;
    }

}
