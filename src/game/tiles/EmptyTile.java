package game.tiles;

/**
 * Represents an empty tile (walkable space) on the game board.
 */
public class EmptyTile extends Tile {

    /**
     * Constructs an EmptyTile at the specified position.
     *
     * @param position The position of the tile
     */
    public EmptyTile(Position position) {
        super('.', position);
    }
}
