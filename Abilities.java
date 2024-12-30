import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;


public class Abilities {
    
    // References
    private Player player;

    // Attributes
    private BufferedImage[] abilitiesSprites;
    private String[] abilities = {
        "     Increase HP",
        " Increase Damage",
        "  Increase Speed",
        "Decrease Shoot Delay"
    };
    private String[] abilitiesValues = {
        "+ 10",
        "+ 5",
        "+ 1",
        "- 10ms"
    };

    public Abilities(Player player, SpriteSheet spriteSheet) {
        this.player = player;

        this.abilitiesSprites = new BufferedImage[] {
            spriteSheet.getSprite(48, 48, 4, 3),    // Heal
            spriteSheet.getSprite(48, 48, 10, 21),  // Damage
            spriteSheet.getSprite(48, 48, 2, 5),    // Speed
            spriteSheet.getSprite(48, 48, 14, 2)    // Shoot Delay
        };
    }

    public void drawAbilities(Graphics graphics) {
        for (int i = 1; i < this.abilities.length + 1; i++) {
            Graphics2D g2d = (Graphics2D)graphics;
            
            graphics.setColor(Color.black);
            g2d.setFont(g2d.getFont().deriveFont(12f).deriveFont(java.awt.Font.BOLD));
            g2d.drawString(this.abilities[i - 1], 100 + 150 * i, 630);
            g2d.drawString(this.abilitiesValues[i - 1], 140 + 150 * i, 650);

            graphics.drawImage(this.abilitiesSprites[i - 1], 100 + 150 * i, 500, 96, 96, null);
            g2d.setStroke(new BasicStroke(5));
            graphics.drawRect(100 + 150 * i - 8, 490, 116, 116);
        }
    }

    public void increaseHp() {
        this.player.setMaxHp(this.player.getMaxHp() + 10);
    }

    public void increaseDamage() {
        this.player.setDamage(this.player.getDamage() + 5);
    }

    public void increaseSpeed() {
        this.player.setSpeed(this.player.getSpeed() + 1);
    }

    public void decreaseShootDelay() {
        this.player.setShootDelay(this.player.getShootDelay() - 10);
    }

}
