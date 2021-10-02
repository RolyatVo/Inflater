package Inflater;

import jig.Entity;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 *			 === Credits ===
 *
 * Sprite Pack
 * 	Author: yd
 * 	https://opengameart.org/content/platformer-sprites
 *
 *
 * 
 */
public class InflaterGame extends StateBasedGame {
	
	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = -1;


	
	public static final String BALL_BALLIMG_RSC = "bounce/resource/ball.png";


	public final int ScreenWidth;
	public final int ScreenHeight;


	/**
	 * Create the BounceGame frame, saving the width and height for later use.
	 * 
	 * @param title
	 *            the window's title
	 * @param width
	 *            the window's width
	 * @param height
	 *            the window's height
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

		// the sound resource takes a particularly long time to load,
		// we preload it here to (1) reduce latency when we first play it
		// and (2) because loading it will load the audio libraries and
		// unless that is done now, we can't *disable* sound as we
		// attempt to do in the startUp() method.
		//ResourceManager.loadSound(BANG_EXPLOSIONSND_RSC);

		// preload all the resources to avoid warnings & minimize latency...
		//ResourceManager.loadImage(BALL_BALLIMG_RSC);


	}
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new InflaterGame("Inflater", 800, 600));
			app.setDisplayMode(768, 576, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

}
