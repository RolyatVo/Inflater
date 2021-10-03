package Inflater;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * The Ball class is an Entity that has a velocity (since it's moving). When
 * the Ball bounces off a surface, it temporarily displays a image with
 * cracks for a nice visual effect.
 *
 */
class Runner extends Entity {
    private Image runguy = new SpriteSheet("Inflater/Resources/Sprites/loderunner.png", 16, 16);
    private Vector velocity, initalV;
    private int countdown;
    private int timer;
    public Runner(final float x, final float y, final float vx, final float vy) throws SlickException {
        super(x, y);

//        addImageWithBoundingBox(ResourceManager
//                .getImage(InflaterGame.PLAYER_RSC));

        addImageWithBoundingBox(runguy.getSubImage(0,0,16,16));
        velocity = new Vector(vx, vy);
        initalV = velocity.copy();
        countdown = 0;
        timer = 0;
    }

    public void setVelocity(final Vector v) {
        velocity = v;
    }

    public Vector getVelocity() {
        return velocity;
    }
    public Vector getInitalV() { return initalV; }

    public void pause(final int time) { this.timer = time; }

    /**
     * Update the Ball based on how much time has passed...
     *
     * @param delta
     *            the number of milliseconds since the last update
     */
    public void update(final int delta) {

        if (this.timer <=0 ) {
            translate(velocity.scale(delta));
        }
        else if(this.timer > 0) {
            timer--;
            velocity.scale(0);
        }
//        if (countdown > 0) {
//            countdown -= delta;
//            if (countdown <= 0) {
//                addImageWithBoundingBox(ResourceManager
//                        .getImage(BounceGame.BALL_BALLIMG_RSC));
//                removeImage(ResourceManager
//                        .getImage(BounceGame.BALL_BROKENIMG_RSC));
//            }
//        }
    }
}
