package units;

public abstract class Player extends Unit {
    protected int experience, level;

    public Player(String name, int hp, int att, int def) {
        super(name, hp, att, def);
        this.experience = 0;
        this.level = 1;
    }

    public void gainExperience(int xp) {
        experience += xp;
        while (experience >= 50 * level) levelUp();
    }

    protected void levelUp() {
        experience -= 50 * level;
        level++;
        healthPool += 10 * level;
        healthAmount = healthPool;
        attack += 4 * level;
        defense += 1 * level;
    }

    public abstract void onGameTick();
    public abstract void castAbility();
}
