package units.enemy;

import game.GameManager;
import tiles.Position;
import tiles.TileVisitor;
import units.Unit;
import units.player.Player;

public abstract class Enemy extends Unit {

    protected final int visionRange;

    public Enemy(String n, char sym, Position pos, int hp, int att, int def, int vision) {
        super(n, sym, pos, hp, att, def);
        this.visionRange = vision;
    }

    @Override
    public void accept(TileVisitor visitor) { visitor.visit(this); }

    public int distanceTo(Position p) { return movement.pos().distance(p); }

    @Override
    public void visit(units.player.Player player) {
        hitPlayer(player);
    }

    /* ------------------------------------------------------------------
   Logs a single melee swing from *this enemy* to the player.
   ------------------------------------------------------------------ */
    protected void hitPlayer(Player p) {
        GameManager.I().msg(name + " engaged into a battle with " + p.getName());

        int attRoll = rollAttack();
        int defRoll = p.rollDefense();
        int dmg = Math.max(0, attRoll - defRoll);

        GameManager.I().msg(name        + " rolled " + attRoll + " attack points");
        GameManager.I().msg(p.getName() + " rolled " + defRoll + " defence points");
        GameManager.I().msg(name + " dealt " + dmg + " damage to " + p.getName());
        GameManager.I().msg("");

        p.takeTrueDamage(dmg);
    }


    public void act() {
        if (health.isDead())            // ← NEW: do nothing if already dead
            return;
    }

    @Override
    protected void die() {
        GameManager gm = GameManager.I();

        gm.msg(name + " died!");
        gm.removeEnemy(this);          // <--  NEW  – drop the corpse right now

        super.die();
    }
}
