package abilities;

import game.GameManager;
import game.callbacks.MessageCallback;
import units.Unit;
import units.player.Rogue;

public final class FanOfKnives extends Ability {

    private final int cost;      // energy cost per cast

    /** @param cd 0 if FoK has no cooldown in your spec */
    public FanOfKnives(int cost, int cd, MessageCallback cb) {
        super("Fan of Knives", cb);
        this.cost = cost;
        this.cooldownMax = cd;
    }

    @Override
    public void cast(Unit owner) {
        if (!startCooldown()) return;             // handles CD & message

        Rogue r = (Rogue) owner;
        if (!r.getEnergy().spend(cost)) {
            out.msg("Not enough energy!");
            return;
        }

        GameManager.I()
                .getEnemiesWithin(r.getMovement().pos(), 2)
                .forEach(e -> e.takeTrueDamage(r.getAttack()));

        out.msg(r.getName() + " unleashes Fan of Knives!");
    }
}
