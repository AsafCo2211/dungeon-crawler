package units.enemy;

import values.Position;
import units.player.Player;
import game.GameManager;

public class Monster extends Enemy {

    public Monster(String n, char sym, Position pos, int hp, int atk, int def, int vision, int xp) {
        super(n, sym, pos, hp, atk, def, vision, xp);
    }

    @Override
    public void act() {
        GameManager gm = GameManager.I();
        Player player = gm.getPlayer();
        if (player == null) return;

        if (gm.tileAt(getPosition().translate(1, 0)) instanceof Player ||
                gm.tileAt(getPosition().translate(-1, 0)) instanceof Player ||
                gm.tileAt(getPosition().translate(0, 1)) instanceof Player ||
                gm.tileAt(getPosition().translate(0, -1)) instanceof Player) {
            hitPlayer(player);
            return;
        }

        gm.getBoardController().move(this, calcNextStepToward(player.getPosition()));
    }

    public Position calcNextStepToward(Position playerPos) {
        GameManager gm = GameManager.I();

        int dist = distanceTo(playerPos);
        Position next;

        if (dist <= visionRange) {
            int dx = Integer.compare(playerPos.getX(), pos.getX());
            int dy = Integer.compare(playerPos.getY(), pos.getY());

            next = pos.translate(Math.abs(dx) > Math.abs(dy) ? dx : 0, Math.abs(dy) >= Math.abs(dx) ? dy : 0);
        }
        else {
            int dir = gm.roll(0, 4);
            next = switch (dir) {
                case 0 -> pos.translate( 1, 0);
                case 1 -> pos.translate(-1, 0);
                case 2 -> pos.translate( 0, 1);
                case 3 -> pos.translate( 0, -1);
                default -> pos;
            };
        }

        return next;
    }
}
