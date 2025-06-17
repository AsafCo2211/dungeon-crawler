package game;

import units.enemy.Enemy;

import java.util.Iterator;

public class EnemyController {
    public void runAI() {
        GameManager gm = GameManager.I();

        Iterator<Enemy> it = gm.getEnemies().iterator();
        while (it.hasNext()) {
            Enemy e = it.next();

            if (e.getHealth().isDead()) {
                it.remove();           // remove corpse once
                continue;
            }

            e.act();                   // move or attack
        }
    }
}