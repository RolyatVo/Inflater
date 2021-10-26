package Inflater;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.*;
import org.newdawn.slick.tiled.TiledMap;

/**
 * This state is active prior to the Game starting. In this state, sound is
 * turned off, and the bounce counter shows '?'. The user can only interact with
 * the game by pressing the SPACE key which transitions to the Playing State.
 * Otherwise, all game objects are rendered and updated normally.
 * 
 * Transitions From (Initialization), GameOverState
 * 
 * Transitions To PlayingState
 */
class StartUpState extends BasicGameState {
	private Image startup;
	private TiledMap map;
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		startup = new Image("Game/src/Inflater/Resources/Sprites/start_screen.png");
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {

		container.setSoundOn(false);
	}


	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		startup.draw(0,0);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		Input input = container.getInput();
		InflaterGame bg = (InflaterGame)game;
		if (input.isKeyDown(Input.KEY_1)) bg.enterState(InflaterGame.PLAYINGSTATE);
		if (input.isKeyDown(Input.KEY_2)) bg.enterState(InflaterGame.LEVEL2);

		if (input.isKeyDown(Input.KEY_SPACE))
			bg.enterState(InflaterGame.PLAYINGSTATE, new EmptyTransition(), new BlobbyTransition());

	}

	@Override
	public int getID() {
		return InflaterGame.STARTUPSTATE;
	}
	
}