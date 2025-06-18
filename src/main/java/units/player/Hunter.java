package units.player;

import game.GameManager;
import tiles.Position;
import units.enemy.Enemy;

public final class Hunter extends Player {
    private final int range;
    private int arrows;
    private int tickCounter;

    public Hunter(String name, char sym, Position pos, int hp, int atk, int def, int range) {
        super(name, sym, pos, hp, atk, def);
        this.range = range;
        this.arrows = 10 * xp.getLevel();
        this.tickCounter = 0;
    }

    @Override
    public void castAbility() {
        if (arrows == 0) {
            GameManager.I().msg(name + " has no arrows!");
            return;
        }
        Enemy target = GameManager.I().getEnemiesWithin(movement.pos(), range).stream().min((e1, e2) ->
                Integer.compare(e1.distanceTo(movement.pos()), e2.distanceTo(movement.pos()))).orElse(null);

        if (target == null) {
            GameManager.I().msg(name + " has no target in range.");
            return;
        }

        arrows--;
        GameManager.I().msg(name + " shoots " + target.getName());

        int attRoll = attack;
        int defRoll = target.rollDefense();
        int dmg     = Math.max(0, attRoll - defRoll);

        GameManager.I().msg(name + " rolled " + attRoll + " attack points");
        GameManager.I().msg(target.getName() + " rolled " + defRoll + " defence points");
        GameManager.I().msg(name + " dealt " + dmg + " damage to " + target.getName() + "\n");

        target.takeTrueDamage(dmg);
    }

    @Override
    protected void levelUp() {
        super.levelUp();
        int lvl = xp.getLevel();

        arrows += 10 * lvl;
        attack  += 2 * lvl;
        defense += 1 * lvl;
    }

    @Override
    public void onTick() {
        super.onTick();

        if (tickCounter == 10) {
            arrows += xp.getLevel();
            tickCounter = 0;
        } else
            tickCounter++;
    }

    public int getArrows() { return arrows; }
}
