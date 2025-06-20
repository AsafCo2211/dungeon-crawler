package units;

public class Monster extends Enemy {
    protected final int visionRange;

    public Monster(String name, int hp, int att, int def, int visionRange, int exp) {
        super(name, hp, att, def, exp);
        this.visionRange = visionRange;
    }

    @Override public void onEnemyTurn() {
        GameUtil.monsterMove(this, visionRange);
    }

    @Override public String description() {
        return String.format("Monster %s [HP: %d/%d]", name, healthAmount, healthPool);
    }
}
