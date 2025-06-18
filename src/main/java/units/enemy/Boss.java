package units.enemy;

import game.GameManager;
import tiles.Position;
import units.HeroicUnit;

public class Boss extends Enemy implements HeroicUnit {

    private final int abilityFreq;
    private int combatTicks = 0;

    public Boss(String n, char sym, Position pos, int hp, int atk, int def, int vision, int abilityFreq) {
        super(n, sym, pos, hp, atk, def, vision);
        this.abilityFreq = abilityFreq;
    }

    @Override
    public void act() {
        GameManager gm = GameManager.I();
        var player = gm.getPlayer();
        if (player == null || player.getHealth().isDead())
            return;

        int dist = distanceTo(player.getMovement().pos());

        if (dist <= visionRange) {
            if (combatTicks == abilityFreq) {
                combatTicks = 0;
                castAbility();
            } else combatTicks++;
        } else {
            combatTicks = 0;
        }

        if (dist < visionRange) {
            int dx = Integer.compare(player.getMovement().pos().getX(), movement.pos().getX());
            int dy = Integer.compare(player.getMovement().pos().getY(), movement.pos().getY());

            Position next = movement.pos().translate(Math.abs(dx) > Math.abs(dy) ? dx : 0, Math.abs(dy) >= Math.abs(dx) ? dy : 0);

            gm.getBoardController().move(this, next);
        } else {
            int dir = gm.roll(0, 4);
            Position next = switch (dir) {
                case 0 -> movement.pos().translate( 1,  0);
                case 1 -> movement.pos().translate(-1,  0);
                case 2 -> movement.pos().translate( 0,  1);
                case 3 -> movement.pos().translate( 0, -1);
                default -> movement.pos();
            };
            gm.getBoardController().move(this, next);
        }
    }

    @Override
    public void castAbility() {
        var player = GameManager.I().getPlayer();
        if (player == null) return;
        if (distanceTo(player.getMovement().pos()) > visionRange) return;

        GameManager.I().msg(name + " casts Shoebodybop!");

        int attRoll = attack;
        int defRoll = player.rollDefense();
        int dmg     = Math.max(0, attRoll - defRoll);

        GameManager.I().msg(name + " rolled " + attRoll + " attack points");
        GameManager.I().msg(player.getName() + " rolled " + defRoll + " defence points");
        GameManager.I().msg(name + " dealt " + dmg + " damage to " + player.getName() + "\n");

        player.takeTrueDamage(dmg);
    }
}
