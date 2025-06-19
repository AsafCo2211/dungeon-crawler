package units.enemy;

import game.GameManager;
import values.Position;
import tiles.TileVisitor;
import units.Unit;
import units.player.Player;

public abstract class Enemy extends Unit {

    protected final int visionRange;
    protected final int expValue;

    public Enemy(String n, char sym, Position pos, int hp, int att, int def, int vision, int expValue) {
        super(n, sym, pos, hp, att, def);
        this.visionRange = vision;
        this.expValue = expValue;
    }

    @Override
    public void accept(TileVisitor visitor) { visitor.visit(this); }

    public int getExpValue() { return expValue; }
    public int distanceTo(Position p) { return pos.distance(p); }

    @Override
    public void visit(units.player.Player player) {
        hitPlayer(player);
    }

    protected void hitPlayer(Player p) {
        GameManager.I().msg(name + " engaged into a battle with " + p.getName());

        int attRoll = rollAttack();
        int defRoll = p.rollDefense();
        int dmg = Math.max(0, attRoll - defRoll);

        GameManager.I().msg(name + " rolled " + attRoll + " attack points");
        GameManager.I().msg(p.getName() + " rolled " + defRoll + " defence points");
        GameManager.I().msg(name + " dealt " + dmg + " damage to " + p.getName());
        GameManager.I().msg("");

        p.takeTrueDamage(dmg);
    }

    public void act() { }

    @Override
    protected void die() {
        GameManager gm = GameManager.I();

        gm.removeEnemy(this);
        GameManager.I().msg("the player gained " + this.getExpValue() + " experience from killing " + this.getName() + "\n");
        GameManager.I().getPlayer().gainXP(expValue);
        //super.die();
    }
}
