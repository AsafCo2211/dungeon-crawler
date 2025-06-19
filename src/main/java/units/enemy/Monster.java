package units.enemy;

import tiles.Position;
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

        int dist = distanceTo(player.getPosition());

        if (gm.tileAt(getPosition().translate(1, 0)) instanceof Player ||
                gm.tileAt(getPosition().translate(-1, 0)) instanceof Player ||
                gm.tileAt(getPosition().translate(0, 1)) instanceof Player ||
                gm.tileAt(getPosition().translate(0, -1)) instanceof Player) {
            hitPlayer(player);
            return;
        }

        if (dist <= visionRange) {

            int dx = Integer.compare(player.getPosition().getX(), pos.getX());
            int dy = Integer.compare(player.getPosition().getY(), pos.getY());

            Position next = pos.translate(Math.abs(dx) > Math.abs(dy) ? dx : 0, Math.abs(dy) >= Math.abs(dx) ? dy : 0);

            gm.getBoardController().move(this, next);
            return;
        }

        int dir = gm.roll(0, 4);
        Position next = switch (dir) {
            case 0 -> pos.translate( 1, 0);
            case 1 -> pos.translate(-1, 0);
            case 2 -> pos.translate( 0, 1);
            case 3 -> pos.translate( 0, -1);
            default -> pos;
        };
        gm.getBoardController().move(this, next);
    }
}
