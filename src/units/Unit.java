package units;

import tiles.Position;
import tiles.Tile;

import java.util.Random;

/**
 * Abstract class representing a living unit on the game board,
 * including players and enemies.
 */
public abstract class Unit extends Tile {
    protected String name;
    protected int healthPool;
    protected int currentHealth;
    protected int attack;
    protected int defense;
    protected Random random;

    /**
     * Constructs a unit with the specified character, position, and stats.
     *
     * @param tileChar Character representing the unit on the board
     * @param position Unit's position on the board
     * @param name     Unit name
     * @param healthPool Maximum health points
     * @param attack   Attack points
     * @param defense  Defense points
     */
    public Unit(char tileChar, Position position, String name, int healthPool, int attack, int defense) {
        super(tileChar, position);
        this.name = name;
        this.healthPool = healthPool;
        this.currentHealth = healthPool; // מתחיל מלא
        this.attack = attack;
        this.defense = defense;
        this.random = new Random();
    }

    public String getName() { return name; }

    public int getHealthPool() { return healthPool; }

    public int getCurrentHealth() { return currentHealth; }

    public void setHealthPool(int newHealthPool){ this.healthPool = newHealthPool; }

    public void setCurrentHealth(int health){ this.currentHealth = health; }

    public int getAttack() { return attack; }

    public int getDefense() { return defense; }

    /**
     * Simulates an attack roll.
     *
     * @return random value between 0 and attack inclusive
     */
    public int attack() {
        return random.nextInt(attack + 1);
    }

    /**
     * Simulates a defense roll.
     *
     * @return random value between 0 and defense inclusive
     */
    public int defend() {
        return random.nextInt(defense + 1);
    }

    /**
     * Abstract method for describing the unit.
     *
     * @return A string describing the unit
     */
    public String description() {
        return String.format("%s\tHP: %d/%d\tAttack: %d\tDefense: %d",
                name, currentHealth, healthPool, attack, defense);
    }

}
