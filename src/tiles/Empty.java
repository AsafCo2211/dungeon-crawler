package tiles;

/**
 * Represents an empty tile (walkable space) on the game board.
 */
public class Empty extends Tile {

    /**
     * Constructs an EmptyTile at the specified position.
     *
     * @param position The position of the tile
     */
    public Empty(Position position) {
        super('.', position);
    }
}
