package Inflater;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;


/**
 * This state is active when the Game is being played. In this state, sound is
 * turned on, the bounce counter begins at 0 and increases until 10 at which
 * point a transition to the Game Over state is initiated. The user can also
 * control the ball using the WAS & D keys.
 * 
 * Transitions From StartUpState
 * 
 * Transitions To GameOverState
 */
class PlayingState extends BasicGameState {
	private TiledMap map;
	private final int tWidth = 20;
	private final int tHeight = 15;
	private int[][] Tmap = new int[tHeight][tWidth];
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		InflaterGame ig = (InflaterGame)game;
		map = new TiledMap("Game/src/Inflater/Resources/Maps/Level1/Level1.tmx");


		int walls = map.getLayerIndex("Walls");
		for(int y =0; y < tHeight; y++) {
			for(int x =0; x < tWidth; x++) {
				Tmap[y][x] = map.getTileId(x,y, walls);
			}
		}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		//Printing out 2d array of map
//		for(int y =0; y < tHeight; y++) {
//			System.out.println("LAYER: " + y);
//			for(int x =0; x < tWidth; x++) {
//				System.out.print(Tmap[y][x]+ " ");
//			}
//			System.out.println();
//		}

		container.setSoundOn(true);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		InflaterGame bg = (InflaterGame)game;
		g.scale(0.6f, 0.6f);
		map.render(0,0);


	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		InflaterGame bg = (InflaterGame) game;

	}

	@Override
	public int getID() {
		return InflaterGame.PLAYINGSTATE;
	}
	
}