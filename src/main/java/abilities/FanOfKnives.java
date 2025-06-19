package abilities;

import game.GameManager;
import game.callbacks.MessageCallback;
import units.Unit;
import units.player.Rogue;

public final class FanOfKnives extends Ability {

    private final int cost;

    public FanOfKnives(int cost, int cd, MessageCallback cb) {
        super("Fan of Knives", cb);
        this.cost = cost;
        this.cooldownMax = cd;
    }

    @Override
    public void cast(Unit owner) {
        if (!startCooldown()) return;

        Rogue r = (Rogue) owner;
        if (!r.getEnergy().spend(cost)) {
            GameManager.I().msg("Not enough energy!");
            return;
        }

        GameManager.I().getEnemiesWithin(r.getPosition(), 2).forEach(e -> e.takeTrueDamage(r.getAttack()));

        GameManager.I().msg(r.getName() + " unleashes Fan of Knives!");
    }
}
