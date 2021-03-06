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
 */
class Door extends Entity {
    private final boolean active;

    public Door(final float x, final float y) throws SlickException {
        super(x, y);
        active = false;
        addImageWithBoundingBox(ResourceManager
                .getImage(InflaterGame.OPEN_DOOR_RSC));
    }


    /**
     * @param delta the number of milliseconds since the last update
     */
    public void update(final int delta) {

    }
}
