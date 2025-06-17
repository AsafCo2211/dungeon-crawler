package abilities;

import units.Unit;
import game.callbacks.MessageCallback;

/**
 * Base class for every active ability.
 * Concrete abilities decide when to call {@link #startCooldown()}.
 */
public abstract class Ability {
    protected final MessageCallback out;
    protected final String          name;

    /* ---------- universal cooldown fields ---------- */
    public int cooldownMax = 0;     // set in subclass ctor
    protected int cooldownLeft = 0;    // counts down each turn

    protected Ability(String name, MessageCallback cb) {
        this.name = name;
        this.out  = cb;
    }

    /* ---------- public API ---------- */
    public String name()       { return name; }
    public boolean ready()     { return cooldownLeft == 0; }
    public int turnsLeft()     { return cooldownLeft; }

    /** Called by GameManager each turn. */
    public void tick() { if (cooldownLeft > 0) cooldownLeft--; }

    /** Clears cooldown (used when a new level starts). */
    public void reset() { cooldownLeft = 0; }

    /** Subclasses must implement their actual effect. */
    public abstract void cast(Unit owner);

    /* ---------- helper for subclasses ---------- */
    protected boolean startCooldown() {
        if (!ready()) {
            out.msg(name + " ability on cooldown (" + cooldownLeft + ")");
            return false;
        }
        cooldownLeft = cooldownMax;
        return true;
    }
}
