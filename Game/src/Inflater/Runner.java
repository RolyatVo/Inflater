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
    private final SpriteSheet runguy = new SpriteSheet("Inflater/Resources/Sprites/loderunner.png", 16, 16);
    private final Image runLEFT = runguy.getSubImage(0, 0, 16, 16);
    private final Image runRIGHT = runLEFT.getFlippedCopy(true, false);
    private final Image runPumpingL = runguy.getSubImage(0, 32, 16, 16);
    private final Image runPumpingR = runPumpingL.getFlippedCopy(true, false);
    private final Image climbL = runguy.getSubImage(4 * 16, 16, 16, 16);
    private final Image climbR = climbL.getFlippedCopy(true,false);


    private Image currentImage;
    public String tazing;
    public boolean climbing;
    private Vector velocity, initalV;
    private String direction;
    private final float RANGE = 2f;

    private int countdown;
    private int timer;

    public Runner(final float x, final float y, final float vx, final float vy) throws SlickException {
        super(x, y);
        runLEFT.setName("RUN LEFT");
        runRIGHT.setName("RUN RIGHT");
        addImageWithBoundingBox(runLEFT);
        currentImage = runLEFT;
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
    public void reset(int x, int y) {
        this.setX(x*64-32);
        this.setY(y*64-32);
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
        return Tmap[(int) ((getY() - 32f) / 64) + 1][(int) (getX() / 64)] == 0 && !isClimbing(Tmap);
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
        float PLAYER_SPEED = 0.20f;
        if (input.isKeyDown(Input.KEY_DOWN) && !isOnFloorLadder(Tmap) && ( isOnLadder(Tmap) || isOnRope(Tmap) )&& getCoarseGrainedMaxY() < 14 * 64) {
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
        float GRAVITY = 0.25f;
        if (!isClimbing(Tmap) && airborne(Tmap) )
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
            this.currentImage = runLEFT;
        } else {
            removeImage(runLEFT);
            addImage(runRIGHT);
            this.currentImage = runRIGHT;
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
            if ((tmap[(int) ((y) / 64)][(int) ((x - 32) / 64f) + 1] != 0
                    && tmap[(int) ((y+28f) / 64)][(int) ((x + 32) / 64f) - 1] != 0)
                    && tmap[(int) (y / 64)][(int) ((x - 32) / 64f) + 1] != 71
                    &&tmap[(int) (y / 64)][(int) ((x - 32) / 64f) + 1] != 139
                    && tmap[(int) (y / 64)][(int) ((x - 32) / 64f) + 1] != 186)
                return true;
        }
        if (getDirection().compareTo("LEFT") == 0) {
            if (tmap[(int) ((y) / 64)][(int) ((x + 32) / 64f) - 1] != 0
                    && tmap[(int) ((y+28f) / 64)][(int) ((x + 32) / 64f) - 1] != 0
                    && tmap[(int) (y / 64)][(int) ((x + 32) / 64f) - 1] != 71
                    && tmap[(int) (y / 64)][(int) ((x + 32) / 64f) - 1] != 139
                    && tmap[(int) (y / 64)][(int) ((x + 32) / 64f) - 1] != 186)
                return true;
        }
        return false;
    }
    public boolean isOnRope(int[][] tmap) {
        int playerY = (int) getTilePosition(64, 64).getY();
        int playerX = (int) getTilePosition(64, 64).getX();
        if (tmap[playerY][playerX] == 186 || tmap[playerY][playerX] == 139
            || tmap[playerY+1][playerX] == 186 || tmap[playerY+1][playerX] == 139) {
            return true;
        }
        return false;
    }
    public boolean isClimbing(int [][] tmap) {
        int playerY = (int) getTilePosition(64, 64).getY();
        int playerX = (int) getTilePosition(64, 64).getX();
        if (tmap[playerY][playerX] == 186 || tmap[playerY][playerX] == 139) {
            climbing = true;
            return true;
        }
        climbing = false;
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
    public boolean isOnFloorLadder(int [][] tmap) {
        int pX, pY;
        pX = (int) getX() / 64;
        pY = (int) ((getY()-30) / 64);

        return (tmap[pY][pX] != 71 && tmap[pY][pX] != 0 && tmap[pY][pX] != 186 && tmap[pY][pX] != 139)
                || (tmap[pY + 1][pX] != 0 && tmap[pY + 1][pX] != 71 && tmap[pY + 1][pX] != 73 && tmap[pY+1][pX] != 186 && tmap[pY+1][pX] != 139);
    }

    /**
     * Check to see if the guard is directly to the right of the player, with a range.
     *
     * @param guard
     * @return rue if guard is close and to the right of player.
     */
    private boolean guardIsRight(Guard guard) {
        if (this.getCoarseGrainedMaxX() + RANGE > guard.getCoarseGrainedMinX() &&
                this.getX() < guard.getX() && guard.getY() < this.getY() + 10 && guard.getY() > this.getY() - 10)
            return true;
        else
            return false;
    }

    /**
     * Check to see if the guard is directly to the left of the player with a added range.
     *
     * @param guard
     * @return True if guard is close and to the left of player.
     */
    private boolean guardIsLeft(Guard guard) {
        if (this.getCoarseGrainedMinX() - RANGE < guard.getCoarseGrainedMaxX() &&
                this.getX() > guard.getX() && guard.getY() < this.getY() + 10 && guard.getY() > this.getY() - 10)
            return true;
        else
            return false;
    }


    /**
     * Loops through each guard checking if they are in range of the player. if they are then we taze them
     *
     * @param guards
     */
    public void pumpDirection(ArrayList<Guard> guards, int delta) {
        //TODO: Implement taze functionality into pumpDirection change the image when the player pumps
        for (int i = 0; i < guards.size(); i++) {
            if (guardIsRight(guards.get(i))) {
                removeImage(currentImage);
                addImage(runPumpingR);
                this.tazing = "RIGHT";
//                System.out.println("PUMPED RIGHT!!");
                guards.get(i).tazed(delta);
                break;
            } else if (guardIsLeft(guards.get(i))) {
                removeImage(currentImage);
                addImage(runPumpingL);
                this.tazing = "LEFT";
//                System.out.println("PUMPED LEFT!!");
                guards.get(i).tazed(delta);
                break;
                //Taze guard on left
            }
        }
    }
    public void restoreImage() {
        if(direction == "RIGHT") addImage(runRIGHT);
        else addImage(runLEFT);
        tazing = null;
    }
    public void removeTazing() {
        if(tazing == "RIGHT")
            removeImage(runPumpingR);
        else
            removeImage(runPumpingL);
    }
    private void setRopeImage() {
        if(climbing) {

            removeImage(currentImage);
            currentImage = null;

            if(getDirection() == "RIGHT") {
                if(currentImage != climbR) {
                    removeImage(climbL);
                    addImage(climbR);
                    currentImage = climbR;
                }

            }
            else if(getDirection() == "LEFT") {
                if(currentImage != climbL) {
                    removeImage(climbR);
                    addImage(climbL);
                    currentImage = climbL;
                }
            }

        }
    }
    private void restoreFromClimbing() {
        if(currentImage != runLEFT && currentImage != runRIGHT) {
            removeImage(climbL);
            removeImage(climbR);
            if(getDirection() == "RIGHT") {
                addImage(runRIGHT);
                currentImage = runRIGHT;
            }
            else if(getDirection() == "LEFT"){
                addImage(runLEFT);
                currentImage = runLEFT;
            }
        }

    }

    /**
     * @param delta the number of milliseconds since the last update
     */
    public void update(final int delta, int[][] Tmap) {
        if(isClimbing(Tmap)) {
            setRopeImage();
        }
        else if(!isClimbing(Tmap) ) {
            restoreFromClimbing();
        }
//        System.out.println("CURRENT IMAGE: "+ currentImage.getName());
        if (this.timer <= 0) {
            translate(velocity.scale(delta));
        } else if (this.timer > 0) {
            timer--;
            velocity.scale(0);
        }
    }
}
