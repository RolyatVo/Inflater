package Inflater;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

class Guard extends Entity {
    private float GUARD_SPEED = 0.18f;
    private Vector velocity = new Vector(0, 0);
    private int counter;
    public int explodetimer;
    public boolean scared;
    public boolean tazed;
    private AStar pathMap;
    private List<Node> path;
    private String direction;
    private boolean climbing;

    private Image currentImage;
    private Animation  currentAnimation;


    public List<Node> getPath() {
        return path;
    }

    public void setPath(List<Node> path) {
        if (path != null)
            this.path = path;
    }

    public void setPathMap(AStar pathMap) {
        this.pathMap = pathMap;
    }

    public Vector getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector v) {
        this.velocity = v;
    }

    //Load these later with resource manager
    private final Image guard;

    private final SpriteSheet guardSheet = new SpriteSheet(ResourceManager.getImage(InflaterGame.LODERUNNER_RSC), 16, 16);

    private final Animation guardWalkRight, guardWalkLeft, guardClimbing, guardRopeRIght, guardRopeLeft;


    public Guard(final float x, final float y) throws SlickException {
        super(x, y);

        guard = guardSheet.getSubImage(3 * 16, 3 * 16, 16, 16);
        addImageWithBoundingBox(guard);
        currentImage = guard;
        direction = "LEFT";
        climbing = false;

        counter = getRandomInt(1500, 1000);
        this.tazed = false;
        //System.out.println("COUNTER: " + counter);
        guardWalkLeft = new Animation(guardSheet, 0, 3, 3, 3, true, 300, true);
        guardWalkRight = flippedAnimation(guardWalkLeft);

        guardClimbing = new Animation(guardSheet, 0,4,3,4,true,300,true);

        guardRopeLeft = new Animation(guardSheet, 4,4, 7, 4, true, 300, true);
        guardRopeRIght = flippedAnimation(guardRopeLeft);
    }

    public int getTileX() {
        return (int) this.getX() / 64;
    }

    public int getTileY() {
        return (int) this.getY() / 64;
    }

    private Animation flippedAnimation(Animation animation) {
        Image[] reversed = new Image[animation.getFrameCount()];

        for(int i=0; i < animation.getFrameCount(); i++) {
            reversed[i] = animation.getImage(i).getFlippedCopy(true,false);
        }
        return new Animation(reversed,300,true);
    }

    private boolean guardArrivedAtPath() {
        Node current = path.get(0);
        int cX = current.getX();
        int cY = current.getY();

        if (this.getX() < cX * 64 + 48 && this.getX() > cX * 64 + 16 &&
                this.getY() < cY * 64 + 48 && this.getY() > cY * 64 + 16) {
            // System.out.println("Removed X:" + path.get(0).getX() + " Y:" + path.get(0).getY());
            path.remove(0);
            return true;
        }
        return false;
    }

    private Vector getNextInPath(int [][] Tmap) {
        if (!path.isEmpty()) {
            Node next = path.get(0);
            float pX = this.getX();
            float pY = this.getY();
            int nextX = next.getX();
            int nextY = next.getY();
            //Below guard
            if (this.getX() < next.getX() * 64 + 36 && this.getX() > next.getX() * 64 + 28
                    && this.getY() < next.getY() * 64 + 32) {
                setClimbing();
                return new Vector(0, GUARD_SPEED);
            }
                //Above guard
            else if (this.getX() < next.getX() * 64 + 36 && this.getX() > next.getX() * 64 + 28
                    && this.getY() > next.getY() * 64 + 32) {
                setClimbing();
                return new Vector(0, -GUARD_SPEED);
            }
                //RIght of Guard
            else if (this.getX() < next.getX() * 64 + 32 &&
                    this.getY() < next.getY() * 64 + 36 && this.getY() > next.getY() * 64 + 28) {
                setDirection("LEFT", Tmap);
                return new Vector(GUARD_SPEED, 0);
            }
            //Left of Guard
            else if (this.getX() > next.getX() * 64 + 32 &&
                    this.getY() < next.getY() * 64 + 36 && this.getY() > next.getY() * 64 + 28) {
                setDirection("RIGHT", Tmap);
                return new Vector(-GUARD_SPEED, 0);
            }
            return this.velocity;

        } else {
            return new Vector(0, 0);
        }
    }
    public boolean isClimbing(int [][] tmap) {
        int playerY = (int)  getY() / 64;
        int playerX = (int) getX() / 64;
        if (tmap[playerY][playerX] == 186 || tmap[playerY][playerX] == 139) {
            climbing = true;
            return true;
        }
        climbing = false;
        return false;
    }
    private void setDirection(String Direction, int [][] Tmap) {
        this.direction = Direction;
        removeImage(guard);
        removeAnimation(currentAnimation);
        if(Direction == "LEFT") {
            if(!isClimbing(Tmap)) {
                addAnimation(guardWalkRight);
                currentAnimation = guardWalkRight;
                guardWalkRight.start();
            }
            else {
                addAnimation(guardRopeRIght);
                currentAnimation = guardRopeRIght;
                guardRopeRIght.start();
            }

        }
        else {
            if(!isClimbing(Tmap)) {
                addAnimation(guardWalkLeft);
                currentAnimation = guardWalkLeft;
                guardWalkLeft.start();
            }
            else {
                addAnimation(guardRopeLeft);
                currentAnimation = guardRopeLeft;
                guardRopeLeft.start();
            }
        }

    }
    private void setClimbing() {
        removeImage(guard);
        removeAnimation(currentAnimation);
        addAnimation(guardClimbing);
        currentAnimation = guardClimbing;
        guardClimbing.start();
    }

    public void tazed(int delta) {
        this.tazed = true;
        explodetimer += delta;
        currentAnimation.stop();
    }


    private int getRandomInt(int max, int min) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public void update(final int delta, int[][] Tmap, int runnerX, int runnerY, boolean scared) {
        if(currentAnimation != null)
            currentAnimation.update(delta);
        if (this.counter > 0) {
            this.counter -= delta;
            if (this.counter <= 0) {
                this.tazed = false;
                setPathMap(new AStar(Tmap));
//                pathMap.printPathMap();
//                System.out.println(" ");
                if (!this.scared) {
                    List<Node> currentPath = scared ? pathMap.randomPath((int) (this.getX() / 64), (int) (this.getX() / 64), runnerX, runnerY) : pathMap.aStarSearch(getTileX(), getTileY(), runnerX, runnerY);
                    setPath(currentPath);
                }


                this.scared = scared;
                this.counter = getRandomInt(2000, 1000);
                //System.out.println("NEW PATH: " + this.counter);
                //path.forEach(n -> System.out.print("(" + n.getX() + "," + n.getY() + ") "));
                //System.out.println("");
            }
        }
        if (path != null) {

            if (!tazed) {
                setVelocity(getNextInPath(Tmap));
                translate(velocity.scale(!this.scared ? delta : delta * 0.7f));
            } else {
                setVelocity(new Vector(0, 0));
            }

            if (!path.isEmpty() && guardArrivedAtPath()) {
                //   System.out.println("ARRIVED IN PATH BLOCK");
            }
        }

    }
}
