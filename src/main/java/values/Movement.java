package values;

import tiles.Position;

public final class Movement {
    private Position pos;
    public Movement(Position start) { this.pos = start; }
    public Position pos() { return pos; }
    public void move(Position p) { this.pos = p; }
    public int distance(Movement other) {
        return pos.distance(other.pos());
    }
}