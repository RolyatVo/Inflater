package Inflater;

import jig.Entity;
import jig.ResourceManager;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Animation;

/**
 * The Ball class is an Entity that has a velocity (since it's moving). When
 * the Ball bounces off a surface, it temporarily displays a image with
 * cracks for a nice visual effect.
 *
 */
class heart extends Entity {

    public heart(final float x, final float y) throws SlickException {
        super(x, y);
        Image[] heart = new Image[2];
        heart[0] = ResourceManager.getImage(InflaterGame.HEART1_RSC);
        heart[1] = ResourceManager.getImage(InflaterGame.HEART2_RSC);
        Animation beatHeart = new Animation(heart, 400, true);
        addAnimation(beatHeart);
    }


    /**
     * @param delta the number of milliseconds since the last update
     */
    public void update(final int delta) {

    }
}
