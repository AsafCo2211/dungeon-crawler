package units.enemy;

import tiles.Position;
import tiles.TileVisitor;
import units.Unit;

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
        int dmg = Math.max(0, rollAttack() - player.rollDefense());
        player.takeTrueDamage(dmg);
        out.msg(name + " slashes " + player.getName() + " for " + dmg);
    }

    public void act() { }

    @Override
    protected void die() {
        out.msg("💀 " + name + " died!");
        super.die();
    }
}
