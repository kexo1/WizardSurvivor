import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

public class SpriteAnimation {

    // References
    private BufferedImage[] spriteList;

    // Attributes
    private int index = 0;
    private long lastTime = 0;
    private long timer = 0;
    private int frameSpeed = 100;
    private int x;
    private int y;
    private int width;
    private int height;

    public SpriteAnimation(BufferedImage[] sprites, int x, int y, int width, int height) {
        this.spriteList = sprites;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.lastTime = System.currentTimeMillis();
    }

    public void animateSprite(Graphics graphics, boolean flip) {
        long now = System.currentTimeMillis();
        this.timer += now - this.lastTime;
        this.lastTime = now;

        if (this.timer > this.frameSpeed) {
            this.index++;
            this.timer = 0;
            if (this.index >= this.spriteList.length) {
                this.index = 0;
            }
        }

        Graphics2D g2d = (Graphics2D)graphics;
        BufferedImage image = this.spriteList[this.index];
        
        if (flip) {
            AffineTransform transform = new AffineTransform();
            transform.translate(this.x + this.width, this.y);
            transform.scale(-1, 1);
            g2d.drawImage(image, transform, null);
        } else {
            g2d.drawImage(image, this.x, this.y, this.width, this.height, null);
        }
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
