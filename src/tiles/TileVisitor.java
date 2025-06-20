package tiles;

import units.Player;
import units.Enemy;

public interface TileVisitor {
    void visit(Empty e);
    void visit(Wall w);
    void visit(Player p);
    void visit(Enemy e);
}