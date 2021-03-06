package Inflater;

import jig.Vector;

import java.util.ArrayList;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;
import org.newdawn.slick.state.transition.SelectTransition;
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
    private Sound gameSound;
    private Image pathMarker;
    private TiledMap map;
    private final int tWidth = 20;
    private final int tHeight = 15;
    private final int pHeight = 960;
    private final int pWidth = 1280;
    private final int[][] Tmap = new int[tHeight][tWidth];
    private boolean DEBUG_FLAG = false;
    private int[] spawnPoint = new int[2];
    private int numGuard;
    private int guardTimer = 0;

    private Sound pop;

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
        InflaterGame ig = (InflaterGame) game;
        spawnPoint[0] = 2;
        spawnPoint[1] = 3;
        gameSound = new Sound("Game/src/Inflater/Resources/sounds/265308__volvion__8-bit-bossfight.wav");
        map = new TiledMap("Game/src/Inflater/Resources/Maps/Level1/Level1.tmx");
        pop = new Sound("Game/src/Inflater/Resources/sounds/260614__kwahmah-02__pop.wav");
        pathMarker = new Image("Game/src/Inflater/Resources/Sprites/pathmarker.png");
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
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        InflaterGame ig = (InflaterGame) game;
        //reset level
        ig.hearts.clear();
        ig.coins.clear();
        ig.guards.clear();
        ig.door = null;
        ig.runner.reset(2, 14);
        // gameSound.loop(1,0.5f);
        //Printing out 2d array of map
//        for (int y = 0; y < tHeight; y++) {
//            for (int x = 0; x < tWidth; x++) {
//                System.out.printf("%4d", Tmap[y][x]);
//            }
//            System.out.println();
//        }
        ig.coins.add(new Coin(2 * 64 - 32, 7 * 64 - 32));
        ig.coins.add(new Coin(15 * 64 - 32, 10 * 64 - 32));
        ig.coins.add(new Coin(19*64-32, 4* 64-32));

        ig.door = new Door(10 * 64 - 32, 14 * 64 - 32);
        ig.guards.add(new Guard(19 * 64 - 32, 7 * 64 - 32));
        ig.guards.add(new Guard(3 * 64 - 32, 7 * 64 - 32));
        numGuard = ig.guards.size();

        ig.hearts.add(new heart(1 * 64 - 32, 16 * 64 - 32));
        ig.hearts.add(new heart(2 * 64 - 32, 16 * 64 - 32));
        ig.hearts.add(new heart(3 * 64 - 32, 16 * 64 - 32));


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

            for (int i = 0; i < ig.guards.size(); i++) {
                if (ig.guards.get(i).getPath() != null)
                    ig.guards.get(i).getPath().forEach(node -> g.drawImage(pathMarker, node.getX() * 64, node.getY() * 64));
            }
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


        ig.hearts.forEach(heart -> heart.render(g));

        if(DEBUG_FLAG) {
            g.drawString("TILE POSITION: " + ig.runner.getTilePosition(64f, 64f).toString(), 100, 100);
            g.drawString("DIRECTION BLOCKED: " + ig.runner.isDirectionBlocked(Tmap), 100, 120);
        }
    }

    private void checkGuardsTimer(ArrayList<Guard> guards) {
        for (int i = 0; i < guards.size(); i++) {
            if (guards.get(i).explodetimer > 1000) {
                pop.play();
                guards.remove(i);
            }
        }
    }

    private void reset(ArrayList<Guard> guards) throws SlickException {
        guards.clear();
        guards.add(new Guard(19 * 64 - 32, 7 * 64 - 32));
        guards.add(new Guard(3 * 64 - 32, 7 * 64 - 32));
    }

    private void spawnGuard(ArrayList<Guard> guards) throws SlickException {
        guards.add(new Guard(spawnPoint[0] * 64 - 32, spawnPoint[1] * 64 - 32));
    }

    @Override
    public void update(GameContainer container, StateBasedGame game,
                       int delta) throws SlickException {
        if (!gameSound.playing())
            gameSound.play(1, 0.05f);

        Input input = container.getInput();
        InflaterGame ig = (InflaterGame) game;
//        if (ig.runner.tazing != null) {
//            ig.runner.removeTazing();
//            ig.runner.restoreImage();
//        }
        if (input.isKeyDown(Input.KEY_SPACE)) {
            ig.runner.setVelocity(new Vector(0, 0));
            ig.runner.pumpDirection(ig.guards, delta);
            checkGuardsTimer(ig.guards);
        } else {
            ig.guards.forEach(guard -> guard.explodetimer = 0);
            ig.runner.move(input, Tmap);
        }
        for (int i = 0; i < ig.coins.size(); i++) {
            if (ig.coins.get(i).collides(ig.runner) != null) {
                ig.coins.remove(i);
            }
        }
        for (int i = 0; i < ig.guards.size(); i++) {
            if (ig.guards.get(i).collides(ig.runner) != null && !ig.guards.get(i).tazed) {
                ig.hearts.remove(ig.hearts.size() - 1);
                ig.runner.reset(2, 14);
                reset(ig.guards);
            }
        }


        if (ig.guards.size() < numGuard) {
            if (guardTimer > 3000) {
                spawnGuard(ig.guards);
                guardTimer = 0;
            }
            guardTimer += delta;
        }

        if (input.isKeyPressed(Input.KEY_P))
            DEBUG_FLAG = !DEBUG_FLAG;
        if (input.isKeyPressed(Input.KEY_C))
            ig.coins.clear();
        //Aft
        if (((ig.door != null && ig.coins != null) && ig.door.collides(ig.runner) != null && ig.coins.isEmpty())
                || input.isKeyPressed(Input.KEY_2)) {
            System.out.println("GO TO NEXT LEVEL!");
            gameSound.stop();
            ig.enterState(InflaterGame.LEVEL2, new EmptyTransition(), new SelectTransition());
        }
        if (ig.hearts.isEmpty()) {
            ig.enterState(InflaterGame.GAMEOVERSTATE);
        }
        ig.runner.update(delta, Tmap);
        ig.guards.forEach(guard -> guard.update(delta, Tmap, (int) ig.runner.getTilePosition(64, 64).getX(),
                (int) ig.runner.getTilePosition(64, 64).getY(), ig.guards.size() < numGuard));
    }

    @Override
    public int getID() {
        return InflaterGame.PLAYINGSTATE;
    }

}