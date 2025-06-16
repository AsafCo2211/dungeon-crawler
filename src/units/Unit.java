package units;

import tiles.TileVisitor;

public abstract class Unit implements TileVisitor {
    protected final String name;
    protected int healthPool, healthAmount;
    protected int attack, defense;

    public Unit(String name, int hp, int attack, int defense) {
        this.name = name;
        this.healthPool = hp;
        this.healthAmount = hp;
        this.attack = attack;
        this.defense = defense;
    }

    public String getName() { return name; }
    public int getHealthAmount() { return healthAmount; }

    public void takeDamage(int dmg) {
        healthAmount = Math.max(0, healthAmount - dmg);
    }

    public int rollAttack() { return GameUtil.random(0, attack); }
    public int rollDefense() { return GameUtil.random(0, defense); }

    @Override public void visit(tiles.Empty e) {}
    @Override public void visit(tiles.Wall w) {}
    @Override public void visit(Player p) {}
    @Override public void visit(Enemy e) {}

    public abstract String description();
}