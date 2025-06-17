package units.player;

import abilities.Blizzard;
import game.GameManager;
import tiles.Position;
import values.Mana;

/**
 * Mage = Melisandre / Thoros of Myr.
 * Special ability: Blizzard (mana-based).
 */
public final class Mage extends Player implements ManaUser {

    private final Mana mana;
    private final int  manaCost;
    private int        spellPower;
    private final int  hits, range;

    public Mage(String name, char sym, Position pos,
                int hp, int att, int def,
                int manaPool, int manaCost,
                int spellPower, int hits, int range) {
        super(name, sym, pos, hp, att, def);

        this.mana      = new Mana(manaPool);
        this.manaCost  = manaCost;
        this.spellPower= spellPower;
        this.hits      = hits;
        this.range     = range;

        setAbility(new Blizzard(manaCost, hits, range, 0, GameManager.I()::msg));
    }

    /* ManaUser interface */
    @Override public Mana getMana() { return mana; }
    public int  getSpellPower()     { return spellPower; }

    /* ------------------------------------------------------------
       Game-tick hook – regen mana: +level each round (spec)
       ------------------------------------------------------------ */
    @Override
    public void onTick() {
        super.onTick();
        mana.regen(xp.getLevel());          // +level mana/turn
    }

    /* ------------------------------------------------------------
       Stat growth per spec
       ------------------------------------------------------------ */
    @Override
    protected void levelUp() {
        int lvl = xp.getLevel();            // already incremented
        super.levelUp();

        /* mage-extra growth */
        mana.increasePool(25 * lvl);
        mana.regen(mana.get() + mana.getPool() / 4);
        spellPower += 10 * lvl;
    }
}
