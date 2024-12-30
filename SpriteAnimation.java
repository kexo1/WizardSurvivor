import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

public class SpriteAnimation {

    // References
    private BufferedImage[] spriteList;
    private AffineTransform transform;

    // Attributes
    private int index = 0;
    private long lastTime = 0;
    private long timer = 0;
    private int frameSpeed = 100;
    private int x;
    private int y;
    private int width;
    private int height;

    public SpriteAnimation(BufferedImage[] spriteList, int x, int y, int width, int height) {
        this.spriteList = spriteList;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.lastTime = System.currentTimeMillis();
        this.transform = new AffineTransform();
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
        
        BufferedImage image = this.spriteList[this.index];
        Graphics2D g2d = (Graphics2D)graphics;  

        if (flip) {
            this.transform.setToIdentity();                                                                         // Reset transform
            this.transform.translate(this.x + this.width, this.y);                                                  // Move to the center of the image (Because it would be on right)
            this.transform.scale(-1, 1);                                                                         // Flip the image on X axis
            this.transform.scale((double)this.width / image.getWidth(), (double)this.height / image.getHeight());   // Scale the image to width and height
            g2d.drawImage(image, this.transform, null);                                                         // Draw the image
        } else {
            g2d.drawImage(image, this.x, this.y, this.width, this.height, null);
        }
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
