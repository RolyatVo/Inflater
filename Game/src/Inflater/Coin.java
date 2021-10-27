package Inflater;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.*;

/**
 * The Ball class is an Entity that has a velocity (since it's moving). When
 * the Ball bounces off a surface, it temporarily displays a image with
 * cracks for a nice visual effect.
 *
 */
class Coin extends Entity {
    private final Image coinpng;
    private final SpriteSheet coinSHEET;
    private final Animation spinningCoin;

    public Coin(final float x, final float y) throws SlickException{
        super(x, y);

        coinpng = new Image("Game/src/Inflater/Resources/Sprites/coin32.png").getScaledCopy(2f);
        coinSHEET = new SpriteSheet(coinpng, 64,64);


        Image coin = ResourceManager
                .getImage(InflaterGame.COIN_RSC);
        addImageWithBoundingBox(coin);
        removeImage(coin);
        spinningCoin = new Animation(coinSHEET, 0,0,60,0, true,30, true);
        addAnimation(spinningCoin);
        spinningCoin.start();
    }


    /**
     * @param delta the number of milliseconds since the last update
     */
    public void update(final int delta) {
    }
}
