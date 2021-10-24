package Inflater;

import jig.Vector;

import java.util.ArrayList;
import java.util.List;

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
 * <p>
 * Transitions From StartUpState
 * <p>
 * Transitions To GameOverState
 */
class PlayingState extends BasicGameState {
    private TiledMap map;
    private final int tWidth = 20;
    private final int tHeight = 15;
    private final int pHeight = 960;
    private final int pWidth = 1280;
    private final int[][] Tmap = new int[tHeight][tWidth];
    private AStar pathMap;
    private List<Node> path;
    private boolean DEBUG_FLAG = false;
    private int [] spawnPoint = new int[2];
    private int numGuard = 1;
    private int guardTimer =0;

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
        InflaterGame ig = (InflaterGame) game;
        spawnPoint[0] = 2;
        spawnPoint[1] = 6;

        map = new TiledMap("Game/src/Inflater/Resources/Maps/Level1/Level1.tmx");


        int walls = map.getLayerIndex("Walls");
        for (int y = 0; y < tHeight; y++) {
            for (int x = 0; x < tWidth; x++) {
                if (map.getTileId(x, y, walls) == 73)
                    Tmap[y][x] = 71;
                else
                    Tmap[y][x] = map.getTileId(x, y, walls);
            }
        }
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException{
        InflaterGame ig = (InflaterGame) game;
        //Printing out 2d array of map
        for (int y = 0; y < tHeight; y++) {
            for (int x = 0; x < tWidth; x++) {
                System.out.printf("%4d", Tmap[y][x]);
            }
            System.out.println();
        }
        ig.coins.add(new Coin(2 * 64 - 32, 7 * 64 - 32));
        ig.coins.add(new Coin(15 * 64 - 32, 10 * 64 - 32));

        ig.door = new Door(10 * 64 - 32, 14 * 64 - 32);
        ig.guards.add(new Guard(6 * 64 - 32, 6 * 64 - 32));

        container.setSoundOn(true);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game,
                       Graphics g) throws SlickException {
        InflaterGame ig = (InflaterGame) game;

        g.scale(0.6f, 0.6f);
        map.render(0, 0);
        if (DEBUG_FLAG) {
            for (int i = 1; i < tWidth; i++)
                g.drawLine(map.getTileWidth() * i, 0, map.getTileWidth() * i, pHeight);
            for (int i = 1; i < tHeight; i++)
                g.drawLine(0, map.getTileHeight() * i, pWidth, map.getTileHeight() * i);

        }
        ig.coins.forEach(coin -> coin.render(g));
        for (Guard guard : ig.guards) {
            guard.render(g);
            guard.setScale(4.0f);
        }
        if (ig.coins.isEmpty())
            ig.door.render(g);
        ig.runner.render(g);
        ig.runner.setScale(4.0f);


        g.drawString("TILE POSITION: " + ig.runner.getTilePosition(64f, 64f).toString(), 100, 100);
        g.drawString("DIRECTION BLOCKED: " + ig.runner.isDirectionBlocked(Tmap), 100, 120);
        g.drawString("BOT LADDER " + ig.runner.isOnFloorLadder(Tmap), 100, 140);

    }
    private void checkGuardsTimer(ArrayList<Guard> guards) {
        for(int i =0; i < guards.size(); i++ ) {
            if(guards.get(i).explodetimer > 2500) {
                guards.remove(i);
            }
        }
    }
    private void spawnGuard(ArrayList<Guard> guards) throws SlickException{
        guards.add(new Guard(spawnPoint[0] * 64 - 32, spawnPoint[1] * 64 - 32));
    }

    @Override
    public void update(GameContainer container, StateBasedGame game,
                       int delta) throws SlickException {

        Input input = container.getInput();
        InflaterGame ig = (InflaterGame) game;
        if (ig.runner.tazing != null) {
            ig.runner.removeTazing();
            ig.runner.restoreImage();
        }
        if (input.isKeyDown(Input.KEY_SPACE)) {
            ig.runner.setVelocity(new Vector(0, 0));
            ig.runner.pumpDirection(ig.guards, delta);
            checkGuardsTimer(ig.guards);
        } else {
            ig.guards.forEach(guard -> guard.explodetimer =0);
            ig.runner.move(input, Tmap);
        }
        for (int i = 0; i < ig.coins.size(); i++) {
            if (ig.coins.get(i).collides(ig.runner) != null) {
                ig.coins.remove(i);
            }
        }


        if(ig.guards.size() < numGuard) {
            if(guardTimer > 4000) {
                spawnGuard(ig.guards);
                guardTimer = 0;
            }
            guardTimer += delta;
        }

        if (input.isKeyPressed(Input.KEY_P))
            DEBUG_FLAG = !DEBUG_FLAG;
        //Aft
        if (ig.door.collides(ig.runner) != null && ig.coins.isEmpty() || input.isKeyDown(Input.KEY_2)) {
            System.out.println("GO TO NEXT LEVEL!");
            ig.enterState(InflaterGame.LEVEL2);
        }
        ig.runner.update(delta, Tmap);
        ig.guards.forEach(guard -> guard.update(delta, Tmap, (int) ig.runner.getTilePosition(64, 64).getX(),
                (int) ig.runner.getTilePosition(64, 64).getY()));
    }

    @Override
    public int getID() {
        return InflaterGame.PLAYINGSTATE;
    }

}