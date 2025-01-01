import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

/**
 * Trieda Abilities zabezpecuje zobrazenie a spravanie sa schopnosti hraca.
 * Schopnosti hraca su zobrazeni na obrazovke a hrac ich moze vybranim zvysit.
 */
public class Abilities {
    
    // Referencie
    private Player player;

    // Atributy
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

    /**
     * Konstruktor triedy Abilities.
     * @param player Referencia na hraca.
     * @param spriteSheet SpriteSheet s obrazkami.
     */
    public Abilities(Player player, SpriteSheet spriteSheet) {
        this.player = player;

        this.abilitiesSprites = new BufferedImage[] {
            spriteSheet.getSprite(48, 48, 4, 3),    // Heal
            spriteSheet.getSprite(48, 48, 10, 21),  // Damage
            spriteSheet.getSprite(48, 48, 2, 5),    // Speed
            spriteSheet.getSprite(48, 48, 14, 2)    // Shoot Delay
        };
    }

    /**
     * Metoda drawAbilities vykresli schopnosti hraca na obrazovku.
     * Schoptnosti su: Zvysenie zivotov, Zvysenie sily, Zvysenie rychlosti, Zvysenie rychlosti strelby.
     * 
     * @param graphics Graficky kontext.
     */
    public void drawAbilities(Graphics graphics) {

        Graphics2D g2d = (Graphics2D)graphics;

        for (int i = 1; i < this.abilities.length + 1; i++) {

            g2d.setColor(Color.white);
            g2d.setFont(g2d.getFont().deriveFont(12f).deriveFont(java.awt.Font.BOLD));
            g2d.drawString(this.abilities[i - 1], 100 + 150 * i, 630);
            g2d.drawString(this.abilitiesValues[i - 1], 140 + 150 * i, 650);

            g2d.setColor(Color.black);
            g2d.drawImage(this.abilitiesSprites[i - 1], 100 + 150 * i, 500, 96, 96, null);
            g2d.setStroke(new BasicStroke(5));
            g2d.drawRect(100 + 150 * i - 8, 490, 116, 116);
        }
    }

    /**
     * Metoda increaseHp zvysi zivoty hraca o 10.
     * 
     * @param ability Vybrana schopnost.
     */
    public void increaseHp() {
        this.player.setMaxHp(this.player.getMaxHp() + 10);
    }

    /**
     * Metoda increaseDamage zvysi silu hraca o 5.
     * 
     * @param ability Vybrana schopnost.
     */
    public void increaseDamage() {
        this.player.setDamage(this.player.getDamage() + 5);
    }

    /**
     * Metoda increaseSpeed zvysi rychlost hraca o 1.
     * 
     * @param ability Vybrana schopnost.
     */
    public void increaseSpeed() {
        this.player.setSpeed(this.player.getSpeed() + 1);
    }

    /**
     * Metoda decreaseShootDelay zvysi rychlost strelby hraca o 10ms.
     * 
     * @param ability Vybrana schopnost.
     */
    public void decreaseShootDelay() {
        this.player.setShootDelay(this.player.getShootDelay() - 10);
    }

}
