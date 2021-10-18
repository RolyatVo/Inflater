package Inflater;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import jig.Entity;
import jig.Vector;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

class Guard extends Entity {
    private float GUARD_SPEED = 0.2f;
    private Vector velocity = new Vector(0, 0);
    private int counter;
    public boolean tazed;
    private AStar pathMap;
    private List<Node> path;
    private String direction;

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
    private final Image guardsheet = new SpriteSheet("Inflater/Resources/Sprites/loderunner.png", 16, 16);
    private final Image guard = guardsheet.getSubImage(0, 3 * 16, 16, 16);

    public Guard(final float x, final float y) throws SlickException {
        super(x, y);
        addImageWithBoundingBox(guard);
        counter = getRandomInt(1500, 1000);
        this.tazed = false;
        System.out.println("COUNTER: " + counter);
    }

    public int getTileX() {
        return (int) this.getX() / 64;
    }

    public int getTileY() {
        return (int) this.getY() / 64;
    }

    private boolean guardArrivedAtPath() {
        Node current = path.get(0);
        int cX = current.getX();
        int cY = current.getY();

        if (this.getX() < cX * 64 + 48 && this.getX() > cX * 64 + 16 &&
                this.getY() < cY * 64 + 48 && this.getY() > cY * 64 + 16) {
            System.out.println("Removed X:" + path.get(0).getX() + " Y:" + path.get(0).getY());
            path.remove(0);
            return true;
        }
        return false;
    }

    private Vector getNextInPath() {
        if (!path.isEmpty()) {
            Node next = path.get(0);
            float pX = this.getX();
            float pY = this.getY();
            int nextX = next.getX();
            int nextY = next.getY();
            //Below guard
            if (this.getX() < next.getX() * 64 + 40 && this.getX() > next.getX() * 64 + 24
                    && this.getY() < next.getY() * 64 + 32)
                return new Vector(0, GUARD_SPEED);
                //Above guard
            else if (this.getX() < next.getX() * 64 + 36 && this.getX() > next.getX() * 64 + 28
                    && this.getY() > next.getY() * 64 + 32)
                return new Vector(0, -GUARD_SPEED);
                //RIght of Guard
            else if (this.getX() < next.getX() * 64 + 32 &&
                    this.getY() < next.getY() * 64 + 40 && this.getY() > next.getY() * 64 + 24)
                return new Vector(GUARD_SPEED, 0);
                //Left of Guard
            else if (this.getX() > next.getX() * 64 + 32 &&
                    this.getY() < next.getY() * 64 + 40 && this.getY() > next.getY() * 64 + 24)
                return new Vector(-GUARD_SPEED, 0);
            return this.velocity;
        } else
            return new Vector(0, 0);
    }

    public void tazed() {
        this.tazed = true;
    }


    private int getRandomInt(int max, int min) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public void update(final int delta, int[][] Tmap, int runnerX, int runnnerY) {
        if (this.counter > 0) {
            this.counter -= delta;
            if (this.counter <= 0) {
                this.tazed = false;
                setPathMap(new AStar(Tmap));
                setPath(pathMap.aStarSearch(getTileX(), getTileY(), runnerX, runnnerY));
                this.counter = getRandomInt(2000, 1000);
                System.out.println("NEW PATH: " + this.counter);
                path.forEach(n -> System.out.print("(" + n.getX() + "," + n.getY() + ") "));
                System.out.println("");
            }
        }
        if (path != null) {

            if(!tazed) {
                setVelocity(getNextInPath());
                translate(velocity.scale(delta));
            }
            else {
                setVelocity(new Vector(0,0));
            }

            if (!path.isEmpty() && guardArrivedAtPath()) {
                System.out.println("ARRIVED IN PATH BLOCK");
            }
        }

    }
}
