package tiles;

/**
 * Represents a position on the game board.
 * Each position is defined by x and y coordinates.
 */
public class Position {
    private final int x;
    private final int y;

    /**
     * Constructs a Position with the specified coordinates.
     *
     * @param x The horizontal coordinate (column)
     * @param y The vertical coordinate (row)
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate.
     *
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate.
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Calculates the Euclidean distance between this position and another.
     *
     * @param other The other position
     * @return The distance between the two positions
     */
    public double range(Position other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Checks if this position is equal to another object.
     * Two positions are equal if their x and y coordinates are the same.
     *
     * @param obj The object to compare
     * @return true if the object is a Position with same coordinates, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.x == other.x && this.y == other.y;
    }

    /**
     * Returns the hash code for this position.
     *
     * @return The hash code
     */
    @Override
    public int hashCode() {
        return x * 31 + y;
    }

    /**
     * Returns a string representation of the position.
     *
     * @return A string in the format "(x,y)"
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
