package units;

public class Rogue extends Player implements HeroicUnit {
    private final int cost;
    private int currentEnergy = 100;

    public Rogue(String name, int hp, int att, int def, int cost) {
        super(name, hp, att, def);
        this.cost = cost;
    }

    @Override public void onGameTick() {
        currentEnergy = Math.min(100, currentEnergy + 10);
    }

    @Override public void castAbility() {
        if (currentEnergy < cost) {
            Message.error("Not enough energy");
            return;
        }
        currentEnergy -= cost;
        GameUtil.enemiesInRange(this, 2).forEach(e -> GameUtil.attack(this, e, attack));
    }

    @Override protected void levelUp() {
        super.levelUp();
        currentEnergy = 100;
        attack += 3 * level;
    }

    @Override public String description() {
        return String.format("Rogue %s [HP: %d/%d, Energy: %d, Atk: %d, Def: %d]",
                name, healthAmount, healthPool, currentEnergy, attack, defense);
    }
}
