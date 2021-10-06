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

    private final float PLAYER_SPEED = 0.25f;
    private final float GRAVITY = 0.25f;


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

    /**
     *
     * @param tileWidth Width of a tile in pixels
     * @param tileHeight Height of a tile in pixels
     * @return returns a Vector int for x and y position of player in the 2d map.
     */
    public Vector getTilePosition(float tileWidth, float tileHeight) {
        return new Vector((int)(this.getX() / tileWidth), (int) (this.getY() / tileHeight));
    }

    /**
     * @param Tmap
     * @return Return true if character is above tile ID = 0
     */
    public boolean airborne(int [][] Tmap) {
        return Tmap[(int) ((getY()-32f) / 64)+1][(int) (getX() / 64)] ==0;
    }

    /**
     * Simple move function for the player, uses arrow keys for each direction the player can move.
     * The only vertical movement the player has is on ladders, if a player is standing on, or infront of
     * a ladder they have use of the arrow keys.
     * @param input JIG's Input
     * @param Tmap 2d Array containing ID of collideable stuff
     */
    public void move(Input input, int [][] Tmap) {
        if(input.isKeyDown(Input.KEY_DOWN) && isOnLadder(Tmap) && getCoarseGrainedMaxY() < 14*64) {
            setVelocity(new Vector(0, PLAYER_SPEED));
        }
        else if(input.isKeyDown(Input.KEY_UP) && isOnLadder(Tmap)) {
            setVelocity(new Vector (0, -PLAYER_SPEED ));
        }
        else if (input.isKeyDown(Input.KEY_RIGHT)){
            if(getDirection() != "RIGHT") {
                flipDirection();
            }
            if(isDirectionBlocked(Tmap)) {
                setVelocity(new Vector(0,0));
            }
            else
                setVelocity(new Vector(PLAYER_SPEED, 0));

        }
        else if (input.isKeyDown(Input.KEY_LEFT)){
            if(getDirection() != "LEFT") {
               flipDirection();
            }
            if(isDirectionBlocked(Tmap)) {
                setVelocity(new Vector(0,0));
            }
            else
                setVelocity(new Vector (-PLAYER_SPEED, 0 ));
        }
        else {
            setVelocity(new Vector(0,0));
        }

        //Check if the player is in the air, if so apply gravity
        if(airborne(Tmap))
            setVelocity(new Vector(this.velocity.getX(), this.velocity.getY()+GRAVITY));
    }

    /**
     * Flip and set the direction of the player.
     */
    public void flipDirection() {
        flipImage();
        if(direction == "RIGHT") {
            setDirection("LEFT");
        }
        else {
            setDirection("RIGHT");
        }
    }

    /**
     * Flip image of the player depending on which way they are facing.
     */
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

    /**
     *
     * @param tmap 2d int array containing 0 for open space or !0 for blocked space
     * @return True if the block the player is facing is a blocked tile
     */
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
     * @param tmap
     * @return True if player is in a ladder tile
     */
    public boolean isOnLadder(int [][] tmap) {
        int playerY = (int) getTilePosition(64,64).getY();
        int playerX = (int) getTilePosition(64,64).getX();
        if(playerY+1 < 15) {
            if (tmap[playerY][playerX] == 71 || tmap[playerY][playerX] == 73 ||
                    tmap[playerY + 1][playerX] == 71 || tmap[playerY + 1][playerX] == 73)
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
