package units;

public class Trap extends Enemy {
    private final int visTime, invisTime;
    private int tickCount;
    private boolean visible = true;

    public Trap(String name, int hp, int att, int def, int exp, int visTime, int invisTime) {
        super(name, hp, att, def, exp);
        this.visTime = visTime;
        this.invisTime = invisTime;
    }

    @Override public void onEnemyTurn() {
        tickCount++;
        if (tickCount >= visTime + invisTime) tickCount = 0;
        visible = tickCount < visTime;
        if (visible && GameUtil.rangeToPlayer(this) < 2) {
            GameUtil.attack(this, GameUtil.getPlayer(), att);
        }
    }

    @Override public String toString() {
        return visible ? String.valueOf(tileChar) : ".";
    }

    @Override public String description() {
        return String.format("Trap %s [Visible: %b]", name, visible);
    }
}
