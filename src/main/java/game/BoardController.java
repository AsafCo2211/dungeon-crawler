package game;

import tiles.*;
import units.Unit;

public class BoardController {
    public void move(Unit mover, Position to) {
        Tile dst = GameManager.I().tileAt(to);

        if (dst instanceof tiles.Wall) return;

        dst.accept(mover);

        if (!mover.getHealth().isDead() && dst instanceof tiles.Empty) {
            mover.visit((tiles.Empty) dst);
        }
    }
}