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
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		map = new TiledMap("Game/src/Inflater/Resources/Maps/Level1/Level1.tmx");
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {

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