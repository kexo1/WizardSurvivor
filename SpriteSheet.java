import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * Trieda SpriteSheet sluzi na nacitanie obrazku a jeho rozdelenie na jednotlive casti.
 */
public class SpriteSheet {

    // Atributy
    private BufferedImage spriteSheet;

    /**
     * Konstruktor triedy SpriteSheet.
     * 
     * @param path cesta k obrazku.
     */
    public SpriteSheet(String path) {
        try {
            this.spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResource(path))); // Nacitanie obrazku, tak aby to nenavrajalo NullPointerException
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda getSprite vrati jednu cast obrazku na zaklade zadanych parametrov.
     * 
     * @param imgSizeX sirka obrazku.
     * @param imgSizeY vyska obrazku.
     * @param col stlpec obrazku.
     * @param row riadok obrazku.
     * @return finalna cast obrazku v podobe BufferedImage.
     */
    public BufferedImage getSprite(int imgSizeX, int imgSizeY, int col, int row) {
        return this.spriteSheet.getSubimage((col * (imgSizeX)) - imgSizeX, (row * imgSizeY) - imgSizeY, imgSizeX, imgSizeY);
    }

    /**
     * Metoda getSpriteSheetRow vrati riadok obrazku na zaklade zadanych parametrov.
     * 
     * @param imgSizeX sirka obrazku.
     * @param imgSizeY vyska obrazku.
     * @param row riadok obrazku.
     * @param numberOfSprites pocet obrazkov v riadku.
     * @return array BufferedImage[] obsahujuci jednotlive casti obrazkov pouzitelne v animacii.
     */
    public BufferedImage[] getSpriteSheetRow(int imgSizeX, int imgSizeY, int row, int numberOfSprites) {
        BufferedImage[] spriteSheetRow = new BufferedImage[numberOfSprites];

        for (int i = 0; i < numberOfSprites; i++) {
            spriteSheetRow[i] = this.spriteSheet.getSubimage(((i + 1) * (imgSizeX)) - imgSizeX, (row * imgSizeY) - imgSizeY, imgSizeX, imgSizeY);
        }
        return spriteSheetRow;
    }

}
