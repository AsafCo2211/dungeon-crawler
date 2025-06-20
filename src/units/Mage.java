package units;

import java.util.Optional;

public class Mage extends Player implements HeroicUnit {
    private int manaPool, currentMana;
    private final int manaCost, spellPower, hitsCount, abilityRange;

    public Mage(String name, int hp, int att, int def,
                int manaPool, int manaCost, int spellPower, int hitsCount, int abilityRange) {
        super(name, hp, att, def);
        this.manaPool = manaPool;
        this.currentMana = manaPool;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitsCount = hitsCount;
        this.abilityRange = abilityRange;
    }

    @Override public void onGameTick() {
        currentMana = Math.min(manaPool, currentMana + level);
    }

    @Override public void castAbility() {
        if (currentMana < manaCost) {
            Message.error("Not enough mana");
            return;
        }
        currentMana -= manaCost;
        int hits = 0;
        while (hits < hitsCount) {
            GameUtil.randomEnemyInRange(this, abilityRange).ifPresent(e -> { GameUtil.attack(this, e, spellPower); hits++; });
            if (!GameUtil.randomEnemyInRange(this, abilityRange).isPresent()) break;
        }
    }

    @Override protected void levelUp() {
        super.levelUp();
        manaPool += 25 * level;
        currentMana = Math.min(manaPool, currentMana + manaPool / 4);
    }

    @Override public String description() {
        return String.format("Mage %s [HP: %d/%d, Mana: %d/%d, Atk: %d, Def: %d]",
                name, healthAmount, healthPool, currentMana, manaPool, attack, defense);
    }
}
