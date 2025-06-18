package units.player;

import abilities.AvengersShield;
import game.GameManager;
import tiles.Position;

public final class Warrior extends Player {

    public Warrior(String name, char sym, Position pos, int hp, int att, int def, int cd) {
        super(name, sym, pos, hp, att, def);
        setAbility(new AvengersShield(cd, GameManager.I()::msg));
    }

    @Override
    public void onTick() {
        if (ability.turnsLeft() > 0) ability.tick();
    }

    @Override
    protected void levelUp() {
        super.levelUp();
        int lvl = xp.getLevel();

        health.increasePool(5 * lvl);

        attack  += 2 * lvl;
        defense += 1 * lvl;

        resetAbilityCooldown();
    }
}
