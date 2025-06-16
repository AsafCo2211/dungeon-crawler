package units;

public class Boss extends Monster implements HeroicUnit {
    private final int abilityFreq;
    private int combatTicks;

    public Boss(String name, int hp, int att, int def,
                int visionRange, int exp, int abilityFreq) {
        super(name, hp, att, def, visionRange, exp);
        this.abilityFreq = abilityFreq;
    }

    @Override public void onEnemyTurn() {
        combatTicks++;
        if (GameUtil.rangeToPlayer(this) < visionRange && combatTicks >= abilityFreq) {
            combatTicks = 0;
            castAbility();
            return;
        }
        super.onEnemyTurn();
    }

    @Override public void castAbility() {
        Message.info(name + " casts Shoebodybop!");
        GameUtil.attack(this, GameUtil.getPlayer(), attack);
    }

    @Override public String description() {
        return String.format("Boss %s [HP: %d/%d]", name, healthAmount, healthPool);
    }
}