package units.enemy;

import tiles.Position;
import units.player.Player;

import game.GameManager;


public class Boss extends Monster {

    private final int abilityFreq;
    private int turnCounter = 0;

    public Boss(String name, char symbol, Position pos, int hp, int att, int def, int visionRange, int exp, int abilityFreq) {
        super(name, symbol, pos, hp, att, def, visionRange, exp);
        this.abilityFreq = abilityFreq;
    }

    @Override public void act() {
        turnCounter++;

        if (turnCounter % abilityFreq == 0) {
            castSpecial();
            return;
        }

        super.act();
    }

    private void castSpecial() {
        Player p = GameManager.I().getPlayer();
        if (p == null) return;

        out.msg("⚡ " + name + " unleashes a freezing wave!");
        if (distanceTo(p.getMovement().pos()) <= 3) {
            int dmg = rollAttack() * 2;
            p.takeTrueDamage(dmg);
            out.msg(p.getName() + " is struck for " + dmg + "!");
        }
    }
}
