package Inflater;

import java.util.ArrayList;
import java.util.PriorityQueue;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

class Guard extends Entity {
    private Vector velocity;
    private int counter;

    //Load these later with resource manager
    private final Image guardsheet = new SpriteSheet("Inflater/Resources/Sprites/loderunner.png", 16, 16);
    private final Image guard = guardsheet.getSubImage(0, 3 * 16, 16, 16);

    public Guard(final float x, final float y) throws SlickException {
        super(x, y);
        addImageWithBoundingBox(guard);
        counter = getRandomInt(5000, 3000);
        System.out.println("COUNTER: " + counter);


    }


    private int getRandomInt(int max, int min) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public void update(final int delta) {
        if (this.counter > 0) {
            this.counter -= delta;
            if (this.counter <= 0) {
                this.counter = getRandomInt(5000, 3000);
                System.out.println("FIND PATH: " + this.counter);
            }
        }

    }
}
