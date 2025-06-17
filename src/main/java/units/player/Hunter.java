package units.player;

import game.GameManager;
import tiles.Position;
import units.HeroicUnit;
import units.enemy.Enemy;

public final class Hunter extends Player {

    /* === bonus fields === */
    private final int range;
    private int arrows;          // current arrows in quiver
    private int tickCounter;     // counts to 10

    public Hunter(String name, char sym, Position pos,
                  int hp, int atk, int def, int range) {
        super(name, sym, pos, hp, atk, def);
        this.range = range;
        this.arrows = 10 * xp.getLevel();
        this.tickCounter = 0;
    }

    /* ---------- HeroicUnit ---------- */
    @Override
    public void castAbility() {
        if (arrows == 0) {
            GameManager.I().msg(name + " has no arrows!");
            return;
        }
        Enemy target = GameManager.I()
                .getEnemiesWithin(movement.pos(), range)
                .stream()
                .min((e1, e2) -> Integer.compare(
                        e1.distanceTo(movement.pos()),
                        e2.distanceTo(movement.pos())))
                .orElse(null);

        if (target == null) {
            GameManager.I().msg(name + " has no target in range.");
            return;
        }

        arrows--;
        GameManager.I().msg(name + " shoots " + target.getName());

        int attRoll = attack;              // full attack points
        int defRoll = target.rollDefense();
        int dmg     = Math.max(0, attRoll - defRoll);

        GameManager.I().msg(name + " rolled " + attRoll + " attack points");
        GameManager.I().msg(target.getName() + " rolled "
                + defRoll + " defence points");
        GameManager.I().msg(name + " dealt " + dmg + " damage to "
                + target.getName() + "\n");

        target.takeTrueDamage(dmg);
    }

    /* ---------- leveling ---------- */
    @Override
    protected void levelUp() {
        super.levelUp();         // regular player bonuses
        int lvl = xp.getLevel();

        arrows += 10 * lvl;      // +arrows
        attack  += 2 * lvl;
        defense += 1 * lvl;
    }

    /* ---------- ticks ---------- */
    @Override
    public void onTick() {
        super.onTick();          // ability cooldowns, etc.

        if (tickCounter == 10) {
            arrows += xp.getLevel();   // regen
            tickCounter = 0;
        } else
            tickCounter++;
    }

    /* ---------- HUD ---------- */
    @Override
    public String description() {
        return super.description()
                + ",  Arrows: " + arrows;
    }
}
