package abilities;

import game.GameManager;
import units.Unit;
import units.enemy.Enemy;
import units.player.Warrior;
import game.callbacks.MessageCallback;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class AvengersShield extends Ability {

    public AvengersShield(int cooldown, MessageCallback cb) {
        super("Avenger's Shield", cb);
        this.cooldownMax = cooldown;
    }

    @Override
    public void cast(Unit owner) {
        Warrior w = (Warrior) owner;

        if (cooldownLeft > 0) {
            GameManager.I().msg(w.getName() + " – ability on cooldown (" + cooldownLeft + ")");
            return;
        }

        cooldownLeft = cooldownMax;

        List<Enemy> pool = GameManager.I().getEnemiesWithin(w.getPosition(), 3);
        if (pool.isEmpty()) {
            GameManager.I().msg("No target in range!");
            return;
        }

        Enemy target = pool.get(ThreadLocalRandom.current().nextInt(pool.size()));

        int dmg = w.getHealth().getMax() / 10;
        target.takeTrueDamage(dmg);
        w.getHealth().heal(10 * w.getDefense());

        GameManager.I().msg(w.getName() + " hurled Avenger’s Shield at " + target.getName() + " for " + dmg);
    }
}
