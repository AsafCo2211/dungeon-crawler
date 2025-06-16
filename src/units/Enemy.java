package units;

public abstract class Enemy extends Unit {
    protected final int expValue;

    public Enemy(String name, int hp, int att, int def, int exp) {
        super(name, hp, att, def);
        this.expValue = exp;
    }

    public int getExpValue() { return expValue; }
    public abstract void onEnemyTurn();
}
