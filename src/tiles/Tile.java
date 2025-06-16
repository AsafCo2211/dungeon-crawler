package tiles;

/**
 * Represents a generic tile on the game board.
 * All tiles have a position and a character used for display.
 * This is an abstract base class extended by specific tile types
 * such as EmptyTile, WallTile, and Unit.
 */
public abstract class Tile {
    protected Position position;
    protected char tileChar;

    /**
     * Constructs a Tile with the given display character and position.
     *
     * @param tileChar  Character representing the tile on the board
     * @param position  The position of the tile on the board
     */
    public Tile(char tileChar, Position position) {
        this.tileChar = tileChar;
        this.position = position;
    }

    /**
     * Returns the tile's position.
     *
     * @return Position of the tile
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets a new position for the tile.
     *
     * @param position The new position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    public char getTileChar() { return tileChar; }

    public abstract void accept(TileVisitor visitor);

    /**
     * Returns the character that represents the tile when printed.
     *
     * @return The tile character
     */
    @Override
    public String toString() {
        return String.valueOf(tileChar);
    }
}
