package units.player;

import abilities.FanOfKnives;            // or your own Arrow-Rain ability
import game.GameManager;
import tiles.Position;
import values.Mana;                      // reused as Energy

/**
 * Ranged rogue - style hero.
 * Energy pool grows by 10 × level and refills every level-up.
 * Regenerates 10 energy each turn.
 */
public final class Hunter extends Player implements ManaUser {

    private final Mana energy;
    private final int  energyCost;

    public Hunter(String name, char sym, Position pos,
                  int hp, int atk, int def,
                  int energyPool) {

        super(name, sym, pos, hp, atk, def);

        this.energy     = new Mana(energyPool);
        this.energyCost = energyPool / 10;                 // assignment spec

        setAbility(new FanOfKnives(energyCost, 0, GameManager.I()::msg));
    }

    /* ------------------------------------------------------------------
       ManaUser interface – return the energy pool so HUD and abilities
       can treat it exactly like Mana.
       ------------------------------------------------------------------ */
    @Override
    public Mana getMana() { return energy; }

    /* convenience alias so other code can call hunter.getEnergy() */
    public Mana getEnergy() { return energy; }

    /* ------------------------------------------------------------------
       Per-turn regeneration: +10 energy (assignment rule)
       ------------------------------------------------------------------ */
    public void onTick() { energy.regen(10); }

    /* ------------------------------------------------------------------
       Stat growth (base player bonuses + energy pool gain)
       ------------------------------------------------------------------ */
    @Override
    protected void levelUp() {
        super.levelUp();                    // HP +10×lvl, Atk +4×lvl, Def +1×lvl

        int lvl = xp.getLevel();
        energy.increasePool(10 * lvl);    // extra rule for Hunters / Rogues
        energy.refillFull();
        attack += 2 * lvl;
        defense += 1 * lvl;
    }
}
