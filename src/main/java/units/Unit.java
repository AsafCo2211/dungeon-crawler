package units;

import values.Health;
import values.Movement;
import values.Experience;

import tiles.*;
import units.enemy.Enemy;
import units.player.Player;

import game.GameManager;
import game.callbacks.MessageCallback;

public abstract class Unit extends Tile implements TileVisitor{

    protected final String name;
    protected final Movement movement;
    protected final Health health;
    protected int attack;
    protected int defense;
    protected final Experience xp = new Experience();

    protected MessageCallback out = s -> {};

    protected Unit(String name, char symbol, Position pos, int hp, int att, int def) {
        super(pos, symbol);
        this.name = name;
        this.movement = new Movement(pos);
        this.health = new Health(hp);
        this.attack = att;
        this.defense = def;
    }

    public String getName() { return name; }
    public Movement getMovement() { return movement; }
    public Health getHealth() { return health; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public Experience getXP() { return xp; }

    public int rollAttack() { return GameManager.I().roll(0, attack);  }
    public int rollDefense() { return GameManager.I().roll(0, defense); }

    public void takeTrueDamage(int dmg) {
        health.damage(dmg);
        out.msg(name + " takes " + dmg + " damage (" + health.get() + "/" + health.getMax() + ")");
        if (health.isDead()) die();
    }
    @Override
    public void visit(Empty empty) {
        GameManager gm = GameManager.I();

        Position from = movement.pos();
        Position to   = empty.getPosition();

        gm.setTile(from, new Empty(from));
        gm.setTile(to,   this);
        movement.move(to);
    }

    @Override
    public void visit(Wall wall) {
    }

    @Override
    public void visit(Player player) { }

    @Override
    public void visit(Enemy enemy)   { }

    protected void die() {
        GameManager.I().removeEnemy(this);
    }
}
