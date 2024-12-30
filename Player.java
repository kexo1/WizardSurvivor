import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player extends GameObj {

    // References
    private ObjManager manager;
    private BufferedImage[] animIdle;
    private BufferedImage[] animWalk;
    private SpriteAnimation spriteAnimationIdle;
    private SpriteAnimation spriteAnimationWalk;

    // Attributes
    private int hp = 100;
    private int maxHp = 100;
    private int damage = 34;
    private int speed = 5;
    private int shootDelay = 200;
    private int score = 0;
    private int velX;
    private int velY;
    private int x;
    private int y;
    private int lastVelX;

    public Player(int x, int y, GameObjID id, ObjManager manager, SpriteSheet spriteSheet) {
        super(x, y, id, manager, spriteSheet);
        this.manager = manager;
        this.x = x;
        this.y = y;

        this.animIdle = spriteSheet.getSpriteSheetRow(32, 64, 1, 2);
        this.spriteAnimationIdle = new SpriteAnimation(this.animIdle, this.x, this.y, 32, 64);

        this.animWalk = spriteSheet.getSpriteSheetRow(32, 64, 2, 4);
        this.spriteAnimationWalk = new SpriteAnimation(this.animWalk, this.x, this.y, 32, 64);
    }

    public void tick() {
        this.x += this.velX;
        this.y += this.velY;

        this.collisionDetection();
        this.movementVelocity();
        this.isOutOfBounds();
        this.shouldFlipImage();
    }

    public void render(Graphics graphics) {

        if (this.velX == 0 && this.velY == 0) {
            this.spriteAnimationIdle.setXY(this.x, this.y);
            this.spriteAnimationIdle.animateSprite(graphics, this.shouldFlipImage());
        } else {
            this.spriteAnimationWalk.setXY(this.x, this.y);
            this.spriteAnimationWalk.animateSprite(graphics, this.shouldFlipImage());
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, 32, 64);
    }

    private void movementVelocity() {

        if (this.manager.isUp()) {
            this.velY = -this.speed;
        } else if (this.manager.isDown()) {
            this.velY = this.speed;
        } else {
            this.velY = 0;
        }

        if (this.manager.isRight()) {
            this.velX = this.speed;
        } else if (this.manager.isLeft()) {
            this.velX = -this.speed;
        } else {
            this.velX = 0;
        }
    }

    private boolean shouldFlipImage() {
        if (this.velX == 0) {
            return this.lastVelX < 0;
        }
        this.lastVelX = this.velX;
        return this.lastVelX < 0;
    }

    private void collisionDetection() {
        for (int i = 0; i < this.manager.getObjList().size(); i++) {
            GameObj obj = this.manager.getObjList().get(i);

            if (obj.getId() == GameObjID.Heal) {
                if (this.getBounds().intersects(obj.getBounds())) {
                        
                    if (this.hp == this.maxHp) {
                        return;
                    }
                    this.healPlayer();
                    this.manager.removeObj(obj);
                }
            }

            if (obj.getId() == GameObjID.Sting) {
                if (this.getBounds().intersects(obj.getBounds())) {
                    this.takeDamage(30);
                    this.manager.removeObj(obj);
                }
            }
        }
    }

    private void isOutOfBounds() {
        if (this.x <= 0) {
            this.x = 0;
        }
        // 32 is the width of the player + 16
        if (this.x >= 1080 - 48) {
            this.x = 1080 - 48;
        }
        if (this.y <= 0) {
            this.y = 0;
        }
        if (this.y >= 1080 - 96) {
            this.y = 1080 - 96;
        }
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) {
            System.out.println("You died");
            //this.manager.removeObj(this);
        }
    }

    public void healPlayer() {
        if (this.hp + 50 > this.maxHp) {
            this.hp = this.maxHp;
        } else {
            this.hp += 50;
        }  
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y + 30; // Offset for the sprite
    }

    public int getDamage() {
        return this.damage;
    }

    public int getHp() {
        return this.hp;
    }

    public int getMaxHp() {
        return this.maxHp;
    }

    public int getShootDelay() {
        return this.shootDelay;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getScore() {
        return this.score;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setShootDelay(int shootDelay) {
        this.shootDelay = shootDelay;
    }

    public void setMaxHp(int hp) {
        this.maxHp = hp;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
