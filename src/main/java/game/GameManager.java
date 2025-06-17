package game;

import tiles.Tile;
import tiles.Position;
import units.enemy.Enemy;
import units.Unit;
import units.player.Player;

import game.callbacks.MessageCallback;
import game.callbacks.DeathCallback;
import game.generator.ValueGenerator;
import game.generator.RandomGenerator;

import java.util.*;

public final class GameManager {
    private static GameManager INSTANCE;
    public static GameManager create(ValueGenerator rng) {
        if (INSTANCE != null) throw new IllegalStateException("GameManager already initialised");
        return INSTANCE = new GameManager(rng);
    }
    public static GameManager I() { return INSTANCE; }

    private final ValueGenerator rng;
    private MessageCallback out = s -> {};
    private DeathCallback   death = u -> {};

    private final List<Enemy> enemies = new ArrayList<>();
    private Player player;
    private Tile[][] board;

    private final BoardController boardCtl = new BoardController();
    private final EnemyController enemyCtl = new EnemyController();

    private final StringBuilder roundLog = new StringBuilder();

    private GameManager(ValueGenerator rng) { this.rng = rng; }

    public GameManager setCallbacks(MessageCallback m, DeathCallback d) {
        out   = m != null ? m : s -> {};
        death = d != null ? d : u -> {};
        return this;
    }
    public void loadBoard(Tile[][] b) { board = b; }
    public void setPlayer(Player p) { player = p; }
    public Player getPlayer() { return player; }

    public List<Enemy> getEnemies() { return enemies; }
    public void addEnemy(Enemy e) { enemies.add(e); }
    public void removeEnemy(Unit u) {
        if (u instanceof Enemy e) { enemies.remove(e); death.onDeath(e); }
    }

    public BoardController getBoardController() { return boardCtl; }
    public EnemyController getEnemyController() { return enemyCtl; }

    public int roll(int min, int max) { return rng.roll(min, max); }
    public Tile tileAt(Position p) { return board[p.getY()][p.getX()]; }

    public List<Enemy> getEnemiesWithin(Position p, int range) {
        List<Enemy> list = new ArrayList<>();
        for (Enemy e : enemies)
            if (e.distanceTo(p) <= range) list.add(e);
        return list;
    }

    public void setTile(Position p, tiles.Tile t) {
        board[p.getY()][p.getX()] = t;
    }

    public void msg(String text) {
        out.msg(text);
        roundLog.append(text).append('\n');
    }
    public String flushRoundMessages() {
        String s = roundLog.toString();
        roundLog.setLength(0);
        return s;
    }

    public void gameTick() { enemyCtl.runAI(); }

    public static GameManager bootstrap() {
        return create(new RandomGenerator());
    }
}
