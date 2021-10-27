package Inflater;

import jig.Entity;

import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * === Credits ===
 * <p>
 * Sprite Pack
 * Author: yd
 * https://opengameart.org/content/platformer-sprites
 * <p>
 * coins
 * https://opengameart.org/content/coin-animation
 * <p>
 * level2
 * https://opengameart.org/content/2d-castle-platformer-starter-assets
 * <p>
 * https://freesound.org/people/kwahmah_02/sounds/260614/
 * https://freesound.org/people/MATRIXXX_/sounds/459145/
 * https://freesound.org/people/jeckkech/sounds/391666/
 * https://freesound.org/people/InspectorJ/sounds/394430/
 * https://freesound.org/people/Volvion/sounds/265308/
 * https://freesound.org/people/Samulis/sounds/197780/
 * https://opengameart.org/content/coins-asset
 *
 * https://temok.itch.io/heart-container-animated-in-pixel-art
 */
public class InflaterGame extends StateBasedGame {

    public static final int STARTUPSTATE = 0;
    public static final int PLAYINGSTATE = 1;
    public static final int LEVEL2 = 2;
    public static final int WINSTATE = 64;
    public static final int GAMEOVERSTATE = -1;


    //public static final String PLAYER_RSC = "Inflater/Resources/Sprites/testplayer.png";
    public static final String COIN_RSC = "Inflater/Resources/Sprites/star_coin_normal_64x64.png";
    public static final String OPEN_DOOR_RSC = "Inflater/Resources/Sprites/OpenDoor.png";
    public static final String HEART_RSC = "Inflater/Resources/Sprites/heart.png";
    public static final String LODERUNNER_RSC = "Inflater/Resources/Sprites/loderunner.png";
    public static final String SPINNING_COIN_RSC = "Inflater/Resources/Sprites/coin32.png";
    public static final String HEART1_RSC = "Inflater/Resources/Sprites/Gif-1.png";
    public static final String HEART2_RSC = "Inflater/Resources/Sprites/Gif-2.png";

//    public static final String a = "";
//    public static final String a = "";
//    public static final String a = "";
//    public static final String a = "";
//    public static final String a = "";
//    public static final String a = "";
//    public static final String a = "";
//    public static final String a = "";
//    public static final String a = "";


    public final int ScreenWidth;
    public final int ScreenHeight;
    public int current_level = 1;

    public static final int TilePixelHeight = 64, TilePixelWidth = 64;


    Runner runner;
    Door door;
    ArrayList<Coin> coins;
    ArrayList<Guard> guards;
    ArrayList<heart> hearts;

    /**
     * Create the BounceGame frame, saving the width and height for later use.
     *
     * @param title  the window's title
     * @param width  the window's width
     * @param height the window's height
     */
    public InflaterGame(String title, int width, int height) {
        super(title);
        ScreenHeight = height;
        ScreenWidth = width;
        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
    }

    public int getScreenWidth() {
        return this.ScreenWidth;
    }

    public int getScreenHeight() {
        return this.ScreenHeight;
    }


    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new StartUpState());
        addState(new GameOverState());
        addState(new PlayingState());
        addState(new Level2());
        addState(new WinState());

        // the sound resource takes a particularly long time to load,
        // we preload it here to (1) reduce latency when we first play it
        // and (2) because loading it will load the audio libraries and
        // unless that is done now, we can't *disable* sound as we
        // attempt to do in the startUp() method.
        //ResourceManager.loadSound(BANG_EXPLOSIONSND_RSC);


        // preload all the resources to avoid warnings & minimize latency...
        //ResourceManager.loadImage(BALL_BALLIMG_RSC);
        //ResourceManager.loadImage(PLAYER_RSC);

        ResourceManager.loadImage(COIN_RSC);
        ResourceManager.loadImage(OPEN_DOOR_RSC);
        ResourceManager.loadImage(HEART_RSC);
        ResourceManager.loadImage(LODERUNNER_RSC);
        ResourceManager.loadImage(SPINNING_COIN_RSC);
        ResourceManager.loadImage(HEART1_RSC);
        ResourceManager.loadImage(HEART2_RSC);

        runner = new Runner(2 * 64 - 32, 14 * 64 - 32, 0f, 0f);
        coins = new ArrayList<Coin>();
        guards = new ArrayList<Guard>();
        hearts = new ArrayList<heart>();

    }

    public static void main(String[] args) {
        AppGameContainer app;
        try {
            app = new AppGameContainer(new InflaterGame("Inflater", 900, 700));
            app.setDisplayMode(768, 626, false);
            app.setVSync(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }

}
