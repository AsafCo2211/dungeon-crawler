package abilities;

import units.Unit;
import units.player.Warrior;
import game.callbacks.MessageCallback;

public final class AvengersShield extends Ability {

    public AvengersShield(int cooldown, MessageCallback cb) {
        super("Avenger's Shield", cb);
        this.cooldownMax = cooldown;        // one-time setup
    }

    @Override
    public void cast(Unit owner) {
        /* startCooldown() returns false and prints the
           generic “on cooldown” message if needed.        */
        if (!startCooldown()) return;

        Warrior w = (Warrior) owner;

        // heal 10 × DEF and print a flavour message
        w.getHealth().heal(w.getDefense() * 10);
        out.msg("*** " + w.getName() + " hurls Avenger's Shield! ***");
    }
}
