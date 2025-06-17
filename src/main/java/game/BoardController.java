package game;

import tiles.*;
import units.Unit;

public class BoardController {
    private final GameManager gm = GameManager.I();

    public void move(Unit mover, Position to) {
        Tile dst = GameManager.I().tileAt(to);

        if (dst instanceof tiles.Wall) return;            // still blocked by walls

        /* NEW – always dispatch the visitor */
        dst.accept(mover);                                // double-dispatch combat

    /* If the mover is still alive AND the dst tile became Empty,
       step into it (this means the enemy died).                   */
        if (!mover.getHealth().isDead() && dst instanceof tiles.Empty) {
            mover.visit((tiles.Empty) dst);               // Unit.visit(Empty) swaps tiles
        }
    }

    public void swapTiles(Position from, Position to, Tile mover) {
        GameManager gm = GameManager.I();

        gm.setTile(from, new Empty(from));
        gm.setTile(to,   mover);
    }
}