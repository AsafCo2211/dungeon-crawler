package units;

public class Hunter extends Player implements HeroicUnit {
    private final int range;
    private int arrowsCount;
    private int tickCount;

    public Hunter(String name, int hp, int att, int def, int range) {
        super(name, hp, att, def);
        this.range = range;
        this.arrowsCount = 10 * level;
    }

    @Override public void onGameTick() {
        tickCount++;
        if (tickCount >= 10) {
            arrowsCount += level;
            tickCount = 0;
        }
    }

    @Override public void castAbility() {
        GameUtil.closestEnemyInRange(this, range)
                .filter(e -> arrowsCount > 0).ifPresentOrElse(e -> { arrowsCount--; GameUtil.attack(this, e, attack); },
                        () -> Message.error("Cannot shoot: no arrows or no targets"));
    }

    @Override protected void levelUp() {
        super.levelUp();
        arrowsCount += 10 * level;
        attack += 2 * level;
        defense += 1 * level;
    }

    @Override public String description() {
        return String.format("Hunter %s [HP: %d/%d, Arrows: %d, Atk: %d, Def: %d]",
                name, healthAmount, healthPool, arrowsCount, attack, defense);
    }
}