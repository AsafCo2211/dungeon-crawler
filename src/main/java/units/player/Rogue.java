package units.player;

import abilities.FanOfKnives;
import game.GameManager;
import tiles.Position;
import values.Mana;   // reused as Energy

public final class Rogue extends Player  {

    private final Mana energy;
    private final int  energyCost;

    public Rogue(String n, char sym, Position pos, int hp, int atk, int def, int cost) {
        super(n, sym, pos, hp, atk, def);
        this.energy = new Mana(100);
        this.energyCost = cost;

        setAbility(new FanOfKnives(energyCost, 0, GameManager.I()::msg));
    }

    public Mana getEnergy() { return energy; }

    @Override
    public void onTick() {
        super.onTick();
        energy.regen(10);
    }

    @Override
    protected void levelUp() {
        super.levelUp();

        int lvl = xp.getLevel();

        energy.refillFull();
        attack += 3 * lvl;
    }
}
