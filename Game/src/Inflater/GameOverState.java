package Inflater;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;


/**
 * This state is active when the Game is over. In this state, the ball is
 * neither drawn nor updated; and a gameover banner is displayed. A timer
 * automatically transitions back to the StartUp State.
 * 
 * Transitions From PlayingState
 * 
 * Transitions To StartUpState
 */
class GameOverState extends BasicGameState {
	

	private int timer;
	private int lastKnownBounces; // the user's score, to be displayed, but not updated.
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		timer = 4000;
	}

	public void setUserScore(int bounces) {
		lastKnownBounces = bounces;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {

		InflaterGame bg = (InflaterGame)game;


	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		timer -= delta;
		if (timer <= 0)
			game.enterState(InflaterGame.STARTUPSTATE, new EmptyTransition(), new HorizontalSplitTransition() );


	}

	@Override
	public int getID() {
		return InflaterGame.GAMEOVERSTATE;
	}
	
}