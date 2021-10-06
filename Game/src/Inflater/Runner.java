package Inflater;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
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
    private Image runLEFT = runguy.getSubImage(0,0,16,16);
    private Image runRIGHT = runLEFT.getFlippedCopy(true, false);
    private Vector velocity, initalV;
    private String direction;
    private int countdown;
    private int timer;
    public Runner(final float x, final float y, final float vx, final float vy) throws SlickException {
        super(x, y);

//        addImageWithBoundingBox(ResourceManager
//                .getImage(InflaterGame.PLAYER_RSC));

        addImageWithBoundingBox(runLEFT);
        velocity = new Vector(vx, vy);
        initalV = velocity.copy();
        countdown = 0;
        direction = "LEFT";
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

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public Vector getTilePosition(float tileWidth, float tileHeight) {
        return new Vector((int)(this.getX() / tileWidth), (int) (this.getY() / tileHeight));
    }

    public void flipDirection() {
        flipImage();
        if(direction == "RIGHT") {
            setDirection("LEFT");
        }
        else {
            setDirection("RIGHT");
        }
    }

    public void flipImage() {
        if(direction == "RIGHT") {
            removeImage(runRIGHT);
            addImage(runLEFT);
        }
        else {
            removeImage(runLEFT);
            addImage(runRIGHT);
        }

    }
    public boolean isDirectionBlocked(int [][] tmap) {
        Vector currentPosition = getTilePosition(64,64);

        if(getDirection() == "RIGHT") {
            if (tmap[(int)currentPosition.getY()][(int)currentPosition.getX()+1] != 0
                && this.getCoarseGrainedMaxX() > 19*64)
                    return true;
        }
        if(getDirection() == "LEFT") {
            if (tmap[(int)currentPosition.getY()][(int)currentPosition.getX()-1] != 0
                && this.getCoarseGrainedMinX() < 64)
                    return true;
        }


        return false;
    }
    /**
     *
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
