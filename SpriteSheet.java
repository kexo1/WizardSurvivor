import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Trieda SpriteSheet sluzi na nacitanie obrazku a jeho rozdelenie na jednotlive casti.
 */
public class SpriteSheet {

    // Atributy
    private BufferedImage spriteSheet;
    private BufferedImage[] spriteSheetRow;

    /**
     * Konstruktor triedy SpriteSheet.
     * @param path Cesta k obrazku.
     */
    public SpriteSheet(String path) {
        try {
            this.spriteSheet = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda getSprite vrati jednu cast obrazku na zaklade zadanych parametrov.
     * @param imgSizeX Sirka obrazku.
     * @param imgSizeY Vyska obrazku.
     * @param col Stlpec obrazku.
     * @param row Riadok obrazku.
     * @return BufferedImage
     */
    public BufferedImage getSprite(int imgSizeX, int imgSizeY, int col, int row) {
        return this.spriteSheet.getSubimage((col * (imgSizeX)) - imgSizeX, (row * imgSizeY) - imgSizeY, imgSizeX, imgSizeY);
    }

    /**
     * Metoda getSpriteSheetRow vrati riadok obrazku na zaklade zadanych parametrov.
     * @param imgSizeX Sirka obrazku.
     * @param imgSizeY Vyska obrazku.
     * @param row Riadok obrazku.
     * @param numberOfSprites Pocet obrazkov v riadku.
     * @return BufferedImage[]
     */
    public BufferedImage[] getSpriteSheetRow(int imgSizeX, int imgSizeY, int row, int numberOfSprites) {
        this.spriteSheetRow = new BufferedImage[numberOfSprites];

        for (int i = 0; i < numberOfSprites; i++) {
            this.spriteSheetRow[i] = this.spriteSheet.getSubimage(((i + 1) * (imgSizeX)) - imgSizeX, (row * imgSizeY) - imgSizeY, imgSizeX, imgSizeY);
        }
        return this.spriteSheetRow;
    }

}
