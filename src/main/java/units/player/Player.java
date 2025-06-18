package units.player;

import abilities.Ability;
import game.GameManager;
import tiles.*;
import units.HeroicUnit;
import units.Unit;
import units.enemy.Enemy;

public abstract class Player extends Unit implements HeroicUnit {
    protected Ability ability;

    protected Player(String name, char symbol, Position pos, int hp, int atk, int def) {
        super(name, symbol, pos, hp, atk, def);
    }

    public void setAbility(Ability a) { ability = a; }
    public void castAbility() { if (ability != null) ability.cast(this); }
    public void resetAbilityCooldown() { if (ability != null) ability.reset(); }

    public void gainXP(int amount) {
        int beforeLvl = xp.getLevel();
        boolean leveled = xp.gain(amount);
        if (leveled) {
            levelUp();
            GameManager.I().msg("=== " + name + " reached level " + xp.getLevel() + " ===");
        }
    }

    protected void levelUp() {
        int lvl = xp.getLevel();

        health.increasePool(10 * lvl);
        health.healFull();

        attack  += 4 * lvl;
        defense += 1 * lvl;
    }

    @Override public void visit(Empty e) { super.visit(e); }

    @Override public void visit(Player p) { /* noop */ }

    @Override
    public void visit(Enemy enemy) {
        GameManager.I().msg(name + " engaged into a battle with " + enemy.getName());

        int attRoll = rollAttack();
        int defRoll = enemy.rollDefense();
        int dmg     = Math.max(0, attRoll - defRoll);

        GameManager.I().msg(name + " rolled " + attRoll + " attack points");
        GameManager.I().msg(enemy.getName() + " rolled " + defRoll + " defence points");
        GameManager.I().msg(name + " dealt " + dmg + " damage to " + enemy.getName());
        GameManager.I().msg("");

        enemy.takeTrueDamage(dmg);

        if (enemy.getHealth().isDead()) {
            GameManager.I().msg(enemy.getName() + " was killed by you\n");
            gainXP(enemy.getXP().getXP());

            Position from = movement.pos();
            Position to = enemy.getMovement().pos();
            GameManager.I().setTile(from, new Empty(from));
            GameManager.I().setTile(to,   this);
            movement.move(to);
        }
    }

    @Override public void accept(TileVisitor v) { v.visit(this); }

    public String description() {
        StringBuilder sb = new StringBuilder()
                .append("name: ").append(name)
                .append(",  Health: ").append(health.get()).append('/').append(health.getMax())
                .append(",  Attack: ").append(attack)
                .append(",  Defence: ").append(defense)
                .append(",  Experience: ").append(xp.getXP()).append('/').append(xp.nextThreshold())
                .append(",  level: ").append(xp.getLevel());

        if (ability != null && ability.cooldownMax > 0) {
            sb.append("  Cooldown: ")
                    .append(ability.turnsLeft())
                    .append('/')
                    .append(ability.cooldownMax);
        }

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

        if (this instanceof Hunter h)
            sb.append(",  Arrows: ").append(h.getArrows());

        return sb.toString();
    }

    public void onTick() {
        if (ability != null && ability.turnsLeft() > 0)
            ability.tick();
    }

    @Override
    public String toString() {
        return health.isDead() ? "X" : String.valueOf(tileChar);
    }
}
