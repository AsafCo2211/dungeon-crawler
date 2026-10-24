package tests;

import game.GameManager;
import game.callbacks.DeathCallback;
import game.callbacks.MessageCallback;
import game.generator.DeterministicGenerator;
import org.junit.jupiter.api.*;
import tiles.Tile;
import units.enemy.Monster;
import units.player.Warrior;
import values.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Melee combat on a one-row board: hero at (0,0), monster at (1,0).
 * Dice are fixed so the hero's single attack is lethal.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CombatTest {

    static final int MONSTER_XP = 25;

    Warrior hero;
    Monster monster;

    @BeforeAll
    void bootstrap() {
        // Rolls alternate 50, 0: the hero's attack roll is 50 and the
        // monster's defence roll is 0, so the first hit kills a 1-HP monster.
        GameManager.create(new DeterministicGenerator(50, 0))
                   .setCallbacks(MessageCallback.ignore(), DeathCallback.ignore());
    }

    @BeforeEach
    void setUp() {
        hero = new Warrior("Hero", '@', new Position(0, 0), 100, 30, 0, 3);
        monster = new Monster("Soldier", 's', new Position(1, 0), 1, 8, 0, 3, MONSTER_XP);

        GameManager gm = GameManager.I();
        gm.loadBoard(new Tile[][] { { hero, monster } });
        gm.setPlayer(hero);
        gm.getEnemies().clear();
        gm.addEnemy(monster);
    }

    @Test
    void killing_an_enemy_awards_its_experience_once() {
        // Regression: the kill used to be rewarded both by the game manager's
        // death handler and again by the player's attack code.
        hero.visit(monster);

        assertTrue(monster.getHealth().isDead());
        assertEquals(1, hero.getXP().getLevel(), "one 25 XP kill must not level up (threshold 50)");
        assertEquals(MONSTER_XP, hero.getXP().getXP());
    }

    @Test
    void killed_enemy_is_removed_from_play() {
        hero.visit(monster);
        assertFalse(GameManager.I().getEnemies().contains(monster));
    }

    @AfterAll
    void reset() throws Exception {
        var field = GameManager.class.getDeclaredField("INSTANCE");
        field.setAccessible(true);
        field.set(null, null);
    }
}
