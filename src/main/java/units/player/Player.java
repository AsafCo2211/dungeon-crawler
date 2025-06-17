package units.player;

import abilities.Ability;
import game.GameManager;
import tiles.*;
import units.Unit;
import units.enemy.Enemy;

/**
 * Base class for every playable hero.
 * Warrior / Mage / Rogue extend this and override {@link #levelUp()} to
 * increase their stats and resource pools.
 */
public abstract class Player extends Unit {

    /* ------------ state ------------ */
    protected Ability ability;                        // active ability (may be null)

    protected Player(String name, char symbol, Position pos,
                     int hp, int atk, int def) {
        super(name, symbol, pos, hp, atk, def);
    }

    /* ------------ ability plumbing ------------ */
    public void setAbility(Ability a)          { ability = a; }
    public void useAbility()                   { if (ability != null) ability.cast(this); }
    public void resetAbilityCooldown()         { if (ability != null) ability.reset(); }

    /* ------------ XP handling ----------------- */
    public void gainXP(int amount) {
        int beforeLvl = xp.getLevel();
        boolean leveled = xp.gain(amount);          // returns true if level-up
        if (leveled) {
            levelUp();                              // subclass bonuses
            GameManager.I().msg("=== " + name +
                    " reached level " + xp.getLevel() + " ===");
        }
    }

    protected void levelUp() {
        int lvl = xp.getLevel();          // already incremented

        /* assignment’s “regular player” formula */
        health.increasePool(10 * lvl);
        health.healFull();

        attack  += 4 * lvl;
        defense += 1 * lvl;
    }

    /* ------------ double-dispatch combat ------------ */

    /** Step onto empty tile (actual move logic lives in super.visit). */
    @Override public void visit(Empty e) { super.visit(e); }

    /** Friendly players ignore each other (no PvP). */
    @Override public void visit(Player p) { /* noop */ }

    /** Melee combat with an enemy. */
    @Override
    public void visit(Enemy enemy) {

        /* 1 — banner */
        GameManager.I().msg(name + " engaged into a battle with " + enemy.getName());

        /* 2 — rolls + damage */
        int attRoll = rollAttack();
        int defRoll = enemy.rollDefense();
        int dmg     = Math.max(0, attRoll - defRoll);

        GameManager.I().msg(name           + " rolled " + attRoll + " attack points");
        GameManager.I().msg(enemy.getName() + " rolled " + defRoll + " defence points");
        GameManager.I().msg(name + " dealt " + dmg + " damage to " + enemy.getName());
        GameManager.I().msg("");                              // blank line

        enemy.takeTrueDamage(dmg);

        /* 3 — if the enemy died */
        if (enemy.getHealth().isDead()) {
            GameManager.I().msg(enemy.getName() + " was killed by you\n");
            gainXP(enemy.getXP().getXP());

            Position from = movement.pos();
            Position to   = enemy.getMovement().pos();
            GameManager.I().setTile(from, new Empty(from));
            GameManager.I().setTile(to,   this);
            movement.move(to);
        }
    }


    /* Accept hook so tiles dispatch to us */
    @Override public void accept(TileVisitor v) { v.visit(this); }

    /* ------------ verbose HUD line -------------- */
    public String description() {
        StringBuilder sb = new StringBuilder()
                .append("name: ").append(name)
                .append(",  Health: ").append(health.get()).append('/').append(health.getMax())
                .append(",  Attack: ").append(attack)
                .append(",  Defence: ").append(defense)
                .append(",  Experience: ").append(xp.getXP()).append('/').append(xp.nextThreshold())
                .append(",  level: ").append(xp.getLevel());

        if (ability != null)
            sb.append("  Cooldown: ")
                    .append(ability.turnsLeft())
                    .append('/')
                    .append(ability.cooldownMax);

        /* resource-specific tails */
        if (this instanceof ManaUser mu)
            sb.append(",  Mana: ")
                    .append(mu.getMana().get()).append('/')
                    .append(mu.getMana().getPool());

        if (this instanceof Rogue r)
            sb.append(",  Energy: ")
                    .append(r.getEnergy().get()).append('/')
                    .append(r.getEnergy().getPool());

        if (this instanceof Mage m)
            sb.append(",  SpellPower: ").append(m.getSpellPower());

        return sb.toString();
    }

    public void onTick() {
        /* generic part: tick cooldown if the ability uses one */
        if (ability != null && ability.turnsLeft() > 0)
            ability.tick();
    }

    @Override
    public String toString() {
        return health.isDead() ? "X" : String.valueOf(tileChar);
    }
}
