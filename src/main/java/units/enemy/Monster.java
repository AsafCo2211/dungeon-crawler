package units.enemy;

import tiles.Position;
import units.player.Player;
import game.GameManager;

public class Monster extends Enemy {

    private final int expValue;

    public Monster(String name, char symbol, Position pos, int hp, int attack, int defense, int visionRange, int expValue) {
        super(name, symbol, pos, hp, attack, defense, visionRange);
        this.expValue = expValue;
    }

    public int getExpValue() { return expValue; }

    @Override
    public void act() {
        super.act();

        GameManager gm = GameManager.I();
        Player player = gm.getPlayer();
        if (player == null) return;

        int dist = distanceTo(player.getMovement().pos());

        if (dist <= 1) {
            hitPlayer(player);
            return;
        }

        if (dist <= visionRange) {
            int dx = Integer.compare(player.getMovement().pos().getX(), movement.pos().getX());
            int dy = Integer.compare(player.getMovement().pos().getY(), movement.pos().getY());

            Position next = movement.pos().translate(dx, dy);
            gm.getBoardController().move(this, next);
            return;
        }

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

    @Override
    protected void die() {
        super.die();
        GameManager.I().getPlayer().gainXP(expValue);
    }
}
