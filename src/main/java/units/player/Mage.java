package units.player;

import abilities.Blizzard;
import game.GameManager;
import tiles.Position;
import values.Mana;

public final class Mage extends Player implements ManaUser {

    private final Mana mana;
    private int spellPower;

    public Mage(String name, char sym, Position pos, int hp, int att, int def, int manaPool, int manaCost, int spellPower, int hits, int range) {
        super(name, sym, pos, hp, att, def);
        this.mana = new Mana(manaPool);
        this.spellPower = spellPower;

        setAbility(new Blizzard(manaCost, hits, range, 0, GameManager.I()::msg));
    }

    @Override public Mana getMana() { return mana; }
    public int  getSpellPower()     { return spellPower; }

    @Override
    public void onTick() {
        mana.regen(xp.getLevel());
    }

    @Override
    protected void levelUp() {
        int lvl = xp.getLevel();
        super.levelUp();

        mana.increasePool(25 * lvl);
        mana.regen(mana.get() + mana.getPool() / 4);
        spellPower += 10 * lvl;
    }
}
