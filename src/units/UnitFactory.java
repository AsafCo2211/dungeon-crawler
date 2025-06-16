package units;

import tiles.Position;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class UnitFactory {
    private static final Map<String, Supplier<Player>> PLAYER_REGISTRY = Map.of(
            "Jon Snow", () -> new Warrior("Jon Snow", 300, 30, 4, 3),
            "The Hound", () -> new Warrior("The Hound", 400, 20, 6, 5),
            "Melisandre", () -> new Mage("Melisandre", 100, 5, 1, 300, 30, 15, 5, 6),
            "Thoros of Myr", () -> new Mage("Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4),
            "Arya Stark", () -> new Rogue("Arya Stark", 150, 40, 2, 20),
            "Bronn", () -> new Rogue("Bronn", 250, 35, 3, 50),
            "Ygritte", () -> new Hunter("Ygritte", 220, 30, 2, 6)
    );

    private static final Map<Character, Function<Position, Enemy>> ENEMY_REGISTRY = Map.of(
            's', pos -> new Monster("Lannister Soldier", 80, 8, 3, 3, 25),
            'k', pos -> new Monster("Lannister Knight", 200, 14, 8, 4, 50),
            'q', pos -> new Monster("Queen's Guard", 400, 20, 15, 5, 100),
            'b', pos -> new Monster("Bear-Wright", 1000, 75, 30, 4, 250),
            'g', pos -> new Monster("Giant-Wright", 1500, 100, 40, 5, 500),
            'w', pos -> new Monster("White Walker", 2000, 150, 50, 6, 1000),
            'M', pos -> new Boss("The Mountain", 1000, 60, 25, 6, 500, 5),
            'C', pos -> new Boss("Queen Cersei", 100, 10, 10, 1, 1000, 8),
            'K', pos -> new Boss("Night's King", 5000, 300, 150, 8, 5000, 3),
            'B', pos -> new Trap("Bonus Trap", 1, 1, 1, 250, 1, 5),
            'Q', pos -> new Trap("Queen's Trap", 250, 50, 10, 100, 3, 7),
            'D', pos -> new Trap("Death Trap", 500, 100, 20, 250, 1, 10)
    );

    public static Player createPlayer(String name) {
        Supplier<Player> sup = PLAYER_REGISTRY.get(name);
        return sup != null ? sup.get() : null;
    }

    public static Enemy createEnemy(char tile, Position pos) {
        Function<Position, Enemy> fn = ENEMY_REGISTRY.get(tile);
        return fn != null ? fn.apply(pos) : null;
    }
}
