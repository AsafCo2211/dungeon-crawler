package abilities;

import game.GameManager;
import game.callbacks.MessageCallback;
import units.Unit;
import units.enemy.Enemy;
import units.player.ManaUser;
import units.player.Mage;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class Blizzard extends Ability {

    private final int manaCost;
    private final int hits;
    private final int range;

    /** @param cd 0 if the spell has no cooldown in your spec */
    public Blizzard(int manaCost, int hits, int range, int cd, MessageCallback cb) {
        super("Blizzard", cb);
        this.manaCost = manaCost;
        this.hits     = hits;
        this.range    = range;
        this.cooldownMax = cd;
    }

    @Override
    public void cast(Unit owner) {
        ManaUser m = (ManaUser) owner;
        if (!m.getMana().spend(manaCost)) {
            out.msg("Not enough mana!");
            return;
        }

        int hitCnt = 0;
        while (hitCnt < hits) {
            List<Enemy> pool = GameManager.I()
                    .getEnemiesWithin(owner.getMovement().pos(), range);
            if (pool.isEmpty()) break;

            Enemy e = pool.get(ThreadLocalRandom.current().nextInt(pool.size()));
            int dmg = ((Mage) owner).getSpellPower();
            int def = e.rollDefense();
            int taken = Math.max(0, dmg - def);
            e.takeTrueDamage(taken);

            out.msg(owner.getName() + " rolled " + dmg
                    + " spell power; " + e.getName()
                    + " rolled " + def + " defence; "
                    + owner.getName() + " dealt " + taken);

            hitCnt++;
        }
    }
}
