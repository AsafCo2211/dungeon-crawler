package units.player;

import abilities.AvengersShield;
import game.GameManager;
import tiles.Position;
import values.Health;

/**
 * Warrior = Jon Snow / The Hound.
 * Special ability: Avenger’s Shield (cool-down based).
 */
public final class Warrior extends Player {

    /* class-specific field: ability cooldown */
    private final int abilityCooldown;

    public Warrior(String name, char sym, Position pos,
                   int hp, int att, int def, int cd) {
        super(name, sym, pos, hp, att, def);
        this.abilityCooldown = cd;
        setAbility(new AvengersShield(cd, GameManager.I()::msg));
    }

    /* ------------------------------------------------------------
       Game-tick hook (called from GameManager)
       ------------------------------------------------------------ */
    public void onTick() {                   // called once each round
        if (ability.turnsLeft() > 0) ability.tick();   // decrement cooldown
    }

    /* ------------------------------------------------------------
       Stat growth – follows assignment formula
       ------------------------------------------------------------ */
    @Override
    protected void levelUp() {
        super.levelUp();
        int lvl = xp.getLevel();                            // already incremented

        health.increasePool(5 * lvl);

        /* attack ↑ 4 × level ; defense ↑ 1 × level */
        attack  += 2 * lvl;
        defense += 1 * lvl;

        /* reset ability cooldown */
        ability.reset();
    }
}
