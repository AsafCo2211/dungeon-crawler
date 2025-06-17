package units.player;

import abilities.FanOfKnives;
import game.GameManager;
import tiles.Position;
import values.Mana;   // reused as Energy

public final class Rogue extends Player {

    private final Mana energy;        // energy pool (0-…)
    private final int  energyCost;    // cost per Fan-of-Knives

    public Rogue(String n, char sym, Position pos,
                 int hp, int atk, int def,
                 int energyPool) {

        super(n, sym, pos, hp, atk, def);
        this.energy      = new Mana(energyPool);
        this.energyCost  = energyPool / 10;            // spec
        setAbility(new FanOfKnives(energyCost, 0, GameManager.I()::msg));
    }

    /* Energy access for HUD */
    public Mana getEnergy() { return energy; }

    /* auto-regen 10 energy each round (assignment) */
    public void onTick() { energy.regen(10); }

    /* level-up extras */
    @Override
    protected void levelUp() {
        super.levelUp();                 // base +HP/+Atk/+Def

        int lvl = xp.getLevel();

        energy.refillFull();
        attack += 3 * lvl;
    }
}
