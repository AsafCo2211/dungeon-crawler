package tiles;

/**
 * Represents a wall tile (non-walkable) on the game board.
 */
public class WallTile extends Tile {

    /**
     * Constructs a WallTile at the specified position.
     *
     * @param position The position of the tile
     */
    public WallTile(Position position) {
        super('#', position);
    }
}
