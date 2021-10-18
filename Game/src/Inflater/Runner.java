package Inflater;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.ArrayList;

class Runner extends Entity {
    private SpriteSheet runguy = new SpriteSheet("Inflater/Resources/Sprites/loderunner.png", 16, 16);
    private Image runLEFT = runguy.getSubImage(0, 0, 16, 16);
    private Image runRIGHT = runLEFT.getFlippedCopy(true, false);
    private Image runPumpingL = runguy.getSubImage(0, 2, 16, 16);
    private Image runPumpingR = runPumpingL.getFlippedCopy(true, false);
    private Vector velocity, initalV;
    private String direction;
    private float RANGE = 2f;

    private final float PLAYER_SPEED = 0.25f;
    private final float GRAVITY = 0.25f;


    private int countdown;
    private int timer;

    public Runner(final float x, final float y, final float vx, final float vy) throws SlickException {
        super(x, y);
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

    public Vector getInitalV() {
        return initalV;
    }

    public void pause(final int time) {
        this.timer = time;
    }


    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @param tileWidth  Width of a tile in pixels
     * @param tileHeight Height of a tile in pixels
     * @return returns a Vector int for x and y position of player in the 2d map.
     */
    public Vector getTilePosition(float tileWidth, float tileHeight) {
        return new Vector((int) (this.getX() / tileWidth), (int) (this.getY() / tileHeight));
    }

    /**
     * Checks to see if the player is on a air tile. If so return true, or else return false.
     *
     * @param Tmap
     * @return Return true if character is above tile ID = 0
     */
    public boolean airborne(int[][] Tmap) {
        return Tmap[(int) ((getY() - 32f) / 64) + 1][(int) (getX() / 64)] == 0;
    }

    /**
     * Simple move function for the player, uses arrow keys for each direction the player can move.
     * The only vertical movement the player has is on ladders, if a player is standing on, or infront of
     * a ladder they have use of the arrow keys.
     *
     * @param input JIG's Input
     * @param Tmap  2d Array containing ID of collideable stuff
     */
    public void move(Input input, int[][] Tmap) {
        if (input.isKeyDown(Input.KEY_DOWN) && isOnLadder(Tmap) && getCoarseGrainedMaxY() < 14 * 64) {
            setVelocity(new Vector(0, PLAYER_SPEED));
        } else if (input.isKeyDown(Input.KEY_UP) && isOnLadder(Tmap)) {
            setVelocity(new Vector(0, -PLAYER_SPEED));
        } else if (input.isKeyDown(Input.KEY_RIGHT)) {
            if (getDirection() != "RIGHT") {
                flipDirection();
            }
            if (isDirectionBlocked(Tmap)) {
                setVelocity(new Vector(0, 0));
            } else
                setVelocity(new Vector(PLAYER_SPEED, 0));

        } else if (input.isKeyDown(Input.KEY_LEFT)) {
            if (getDirection() != "LEFT") {
                flipDirection();
            }
            if (isDirectionBlocked(Tmap)) {
                setVelocity(new Vector(0, 0));
            } else
                setVelocity(new Vector(-PLAYER_SPEED, 0));
        } else {
            setVelocity(new Vector(0, 0));
        }

        //Check if the player is in the air, if so apply gravity
        if (airborne(Tmap))
            setVelocity(new Vector(this.velocity.getX(), this.velocity.getY() + GRAVITY));
    }

    /**
     * Flip and set the direction of the player.
     */
    public void flipDirection() {
        flipImage();
        if (direction == "RIGHT") {
            setDirection("LEFT");
        } else {
            setDirection("RIGHT");
        }
    }

    /**
     * Flip image of the player depending on which way they are facing.
     */
    public void flipImage() {
        if (direction == "RIGHT") {
            removeImage(runRIGHT);
            addImage(runLEFT);
        } else {
            removeImage(runLEFT);
            addImage(runRIGHT);
        }

    }

    /**
     * Checks if the current facing direction is blocked or not. Currently not blocked for ladders but
     * blocked for all other blocks
     *
     * @param tmap 2d int array containing 0 for open space or !0 for blocked space
     * @return True if the block the player is facing is a blocked tile
     */
    public boolean isDirectionBlocked(int[][] tmap) {
        float x, y;
        x = getX();
        y = getY();

        if (getDirection().compareTo("RIGHT") == 0) {
            if (tmap[(int) (y / 64)][(int) ((x - 32) / 64f) + 1] != 0
                    && tmap[(int) (y / 64)][(int) ((x - 32) / 64f) + 1] != 71)
                return true;
        }
        if (getDirection().compareTo("LEFT") == 0) {
            if (tmap[(int) (y / 64)][(int) ((x + 32) / 64f) - 1] != 0
                    && tmap[(int) (y / 64)][(int) ((x + 32) / 64f) - 1] != 71)
                return true;
        }
        return false;
    }

    /**
     * Check to see if the player is on a ladder tile. (ID of 71)
     *
     * @param tmap
     * @return True if player is in a ladder tile
     */
    public boolean isOnLadder(int[][] tmap) {
        int playerY = (int) getTilePosition(64, 64).getY();
        int playerX = (int) getTilePosition(64, 64).getX();
        if (playerY + 1 < 15) {
            if (tmap[playerY][playerX] == 71 || tmap[playerY][playerX] == 73 ||
                    tmap[playerY + 1][playerX] == 71 || tmap[playerY + 1][playerX] == 73)
                return true;
        }
        return false;
    }

    private boolean guardIsRight(Guard guard) {
        if (this.getCoarseGrainedMaxX() + RANGE > guard.getCoarseGrainedMinX() &&
                this.getX() < guard.getX())
            return true;
        else
            return false;
    }

    private boolean guardIsLeft(Guard guard) {
        if (this.getCoarseGrainedMinX() - RANGE < guard.getCoarseGrainedMaxX() &&
                this.getX() > guard.getX())
            return true;
        else
            return false;
    }

    public void pumpDirection(ArrayList<Guard> guards) {
        //TODO: Implement taze functionality into pumpDirection

        for (int i = 0; i < guards.size(); i++) {
            if (guardIsRight(guards.get(i))) {
              //  System.out.println("PUMPED RIGHT!!");
                guards.get(i).tazed();
            } else if (guardIsLeft(guards.get(i))) {
                //System.out.println("PUMPED LEFT!!");
                guards.get(i).tazed();
                //Taze guard on left
            }
        }
    }

    /**
     * @param delta the number of milliseconds since the last update
     */
    public void update(final int delta) {

        if (this.timer <= 0) {
            translate(velocity.scale(delta));
        } else if (this.timer > 0) {
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
