package abilities;

import game.GameManager;
import game.callbacks.MessageCallback;
import units.Unit;
import units.player.ManaUser;
import units.player.Mage;

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
        if (!startCooldown()) return;                 // generic CD check / message

        /* mana check */
        if (!(owner instanceof ManaUser mUser)) return;
        if (!mUser.getMana().spend(manaCost)) {
            out.msg("Not enough mana!");
            return;
        }

        /* apply damage */
        int spellPower = ((Mage) owner).getSpellPower();
        GameManager.I()
                .getEnemiesWithin(owner.getMovement().pos(), range)
                .stream()
                .limit(hits)
                .forEach(e -> {
                    e.takeTrueDamage(spellPower);
                    out.msg(owner.getName() + " hits " + e.getName());
                });
    }
}
