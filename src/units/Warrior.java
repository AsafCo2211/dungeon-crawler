package units;

import java.util.Optional;

public class Warrior extends Player implements HeroicUnit {
    private final int abilityCooldown;
    private int remainingCooldown;

    public Warrior(String name, int hp, int att, int def, int cooldown) {
        super(name, hp, att, def);
        this.abilityCooldown = cooldown;
        this.remainingCooldown = 0;
    }

    @Override public void onGameTick() {
        if (remainingCooldown > 0) remainingCooldown--;
    }

    @Override public void castAbility() {
        if (remainingCooldown > 0) {
            Message.error("Warrior ability is on cooldown: " + remainingCooldown + " ticks left");
            return;
        }
        remainingCooldown = abilityCooldown;
        healthAmount = Math.min(healthPool, healthAmount + 10 * defense);
        GameUtil.randomEnemyInRange(this, 3).ifPresent(e -> GameUtil.attack(this, e, (int)(0.1 * healthPool)));
    }

    @Override public String description() {
        return String.format("Warrior %s [HP: %d/%d, Atk: %d, Def: %d, CD: %d]",
                name, healthAmount, healthPool, attack, defense, remainingCooldown);
    }
}
