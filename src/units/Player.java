package units;

import tiles.Position;

public abstract class Player extends Unit {
    protected int experience;
    protected int level;

    public Player(char tile, Position position, String name, int healthPool, int attack, int defense) {
        super(tile, position, name, healthPool, attack, defense);
        this.experience = 0;
        this.level = 1;
    }

    public void gainExperience(int amount) {
        experience += amount;
        while (experience >= 50 * level) {
            levelUp();
        }
    }

    protected void levelUp() {
        experience -= 50 * level;
        level++;
        setHealthPool(getHealthPool() + (10 * level));
        setCurrentHealth(getHealthPool());
        attack += 4 * level;
        defense += level;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    @Override
    public String description() {
        return String.format("%s\tLevel: %d\tExperience: %d/%d",
                super.description(), level, experience, 50 * level);
    }

}
