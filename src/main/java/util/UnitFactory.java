package util;

import tiles.*;                 // Position, Empty, Wall … (adjust if you use different packages)
import units.enemy.Boss;
import units.enemy.Enemy;
import units.enemy.Monster;
import units.enemy.Trap;
import units.player.*;

import java.util.*;
import java.util.function.Function;

/**
 * Factory for every predefined units.player and units.enemy in HW #3.
 *     • Use {@link #catalogue()}   to show the units.player-selection menu (dummy position (0,0)).
 *     • Use {@link #createPlayer(int, Position)} to place the chosen units.player on the board.
 *     • Use {@link #spawnEnemy(char, Position)}  while parsing a level file.
 */
public final class UnitFactory {

    /* ---------- Pre-defined PLAYERS (order = selection menu) ---------- */
    private static final List<Function<Position, Player>> PLAYERS = List.of(
            p -> new Warrior("Jon Snow", '@', p, 300, 30,  4, 3),
            p -> new Warrior("The Hound", '@', p, 400, 20,  6, 5),

            p -> new Mage("Melisandre", '@', p, 100,  5,  1, 300, 30, 15, 5, 6),
            p -> new Mage("Thoros of Myr", '@', p, 250, 25,  4, 150, 20, 20, 3, 4),

            p -> new Rogue("Arya Stark", '@', p, 150, 40,  2, 20),
            p -> new Rogue("Bronn", '@', p, 250, 35,  3, 50),

            /* Bonus Hunter (+5) */
            p -> new Hunter("Ygritte", '@', p, 220, 30,  2, 6)
    );

    /* ---------- Pre-defined ENEMIES (char → supplier) ---------- */
    private static final Map<Character, Function<Position, Enemy>> ENEMIES =
            Map.ofEntries(
                    /* Monsters */
                    entry('s', p -> new Monster("Lannister Soldier", 's', p,  80,   8,  3, 3,  25)),
                    entry('k', p -> new Monster("Lannister Knight",  'k', p, 200,  14,  8, 4,  50)),
                    entry('q', p -> new Monster("Queen's Guard",     'q', p, 400,  20, 15, 5, 100)),

                    entry('z', p -> new Monster("Wright", 'z', p, 600,  30, 15, 3, 100)),
                    entry('b', p -> new Monster("Bear-Wight", 'b', p,1000,  75, 30, 4, 250)),
                    entry('g', p -> new Monster("Giant-Wight", 'g', p,1500, 100, 40, 5, 500)),
                    entry('w', p -> new Monster("White Walker", 'w', p,2000, 150, 50, 6,1000)),

                    entry('M', p -> new Boss("The Mountain", 'M', p,1000,  60, 25, 6, 500)),
                    entry('C', p -> new Boss("Queen Cersei", 'C', p, 100,  10, 10, 1,1000)),
                    entry('K', p -> new Boss("Night’s King", 'K', p,5000, 300,150, 8,5000)),

                    /* Traps */
                    entry('B', p -> new Trap("Bonus Trap", 'B', p,   1,   1,  1, 250, 1,  5)),
                    entry('Q', p -> new Trap   ("Queen’s Trap", 'Q', p, 250,  50, 10, 100, 3,  7)),
                    entry('D', p -> new Trap   ("Death Trap", 'D', p, 500, 100, 20, 250, 1, 10))

            );

    private UnitFactory() {}

    public static List<Player> catalogue() {
        Position dummy = new Position(0, 0);
        List<Player> copy = new ArrayList<>(PLAYERS.size());
        PLAYERS.forEach(f -> copy.add(f.apply(dummy)));
        return Collections.unmodifiableList(copy);
    }

    public static Player createPlayer(int idx, Position pos) {
        return switch (idx) {
            case 0 -> new Warrior ("Jon Snow",  '@', pos, 300, 30, 4, 3);
            case 1 -> new Warrior ("The Hound", '@', pos, 400, 20, 6, 5);
            case 2 -> new Mage    ("Melisandre",'@', pos, 100, 5,  1, 300, 75, 15, 5, 6);
            case 3 -> new Mage    ("Thoros of Myr",'@',pos, 250, 25, 4, 150, 37, 20, 5, 6);
            case 4 -> new Rogue   ("Arya Stark", '@', pos, 150, 40, 2, 100);
            case 5 -> new Rogue   ("Bronn",      '@', pos, 250, 35, 3, 100);
            case 6 -> new Hunter  ("Ygritte",    '@', pos, 220, 30, 2, 6);
            default -> throw new IllegalArgumentException("bad player index");
        };
    }

    public static Enemy spawnEnemy(char tile, Position pos) {
        Function<Position, Enemy> f = ENEMIES.get(tile);
        return f == null ? null : f.apply(pos);
    }

    public static Tile tileFromChar(char c, Position pos) {
        if (c == '#') return new Wall(pos);
        if (c == '.') return new Empty(pos);
        if (c == '@') return null;             // units.player is created separately

        Enemy e = spawnEnemy(c, pos);
        if (e != null) return e;

        throw new IllegalArgumentException("Unknown tile character '" + c + "'");
    }

    public static int playerCount() { return PLAYERS.size(); }

    private static <K, V> Map.Entry<K, V> entry(K k, V v) { return Map.entry(k, v); }
}
