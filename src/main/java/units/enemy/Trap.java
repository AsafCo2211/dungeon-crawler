package units.enemy;

import tiles.Position;
import units.player.Player;
import game.GameManager;

public class Trap extends Enemy {

    private final int expValue;
    private final int visibleFor;
    private final int invisibleFor;

    private int timer;             // counts down to toggle
    private boolean visible;

    public Trap(String name, char symbol, Position pos, int hp, int attack, int defense, int expValue, int vis, int invis) {
        super(name, symbol, pos, hp, attack, defense, 1);
        this.expValue = expValue;
        this.visibleFor = vis;
        this.invisibleFor = invis;
        this.visible = true;
        this.timer = 0;
    }

    @Override public void act() {
        if (--timer == 0) {
            visible = !visible;
            timer = visible ? visibleFor : invisibleFor;
        }
        if (!visible) return;

        Player p = GameManager.I().getPlayer();
        if (p == null) return;

        if (distanceTo(p.getMovement().pos()) < 2) {
            int dmg = rollAttack();
            p.takeTrueDamage(dmg);
            GameManager.I().msg(name + " ambushes " + p.getName() + " for " + dmg);
        }
    }

    @Override protected void die() {
        super.die();
        GameManager.I().getPlayer().gainXP(expValue);
    }

    public char getTileChar() {
        return visible ? tileChar : '.';
    }

    @Override
    public String toString() { return String.valueOf(getTileChar()); }
}