package game;

import units.enemy.Enemy;
public class EnemyController {
    private final GameManager gm = GameManager.I();

    public void runAI() {
        for (Enemy e : GameManager.I().getEnemies()) {
            e.act();
        }
    }
}