package game;
import units.*;import java.util.*;import java.util.stream.*;
public class GameUtil{
    private static final Random RNG=new Random();
    private static Player player;
    private static List<Enemy> enemies=new ArrayList<>();
    public static int random(int min,int max){return RNG.nextInt(max-min+1)+min;}
    public static void attack(Unit a,Unit d,int dmg){int ra=a.rollAttack(),rd=d.rollDefense(),net=ra-rd;Message.info(a.getName()+" rolls "+ra+" vs "+rd);if(net>0){d.takeDamage(net);Message.info(d.getName()+" takes "+net);if(d.getHealthAmount()==0&&a instanceof Player){player.gainExperience(((Enemy)d).getExp());enemies.remove(d);}}}
    public static List<Enemy> enemiesInRange(Player p,int r){return enemies.stream().filter(e->distance(e,p)<=r).collect(Collectors.toList());}
    public static Optional<Enemy> randomEnemyInRange(Player p,int r){List<Enemy>l=enemiesInRange(p,r);return l.isEmpty()?Optional.empty():Optional.of(l.get(RNG.nextInt(l.size())));}
    public static Optional<Enemy> closestEnemyInRange(Player p,int r){return enemies.stream().filter(e->distance(e,p)<=r).min(Comparator.comparingDouble(e->distance(e,p)));}
    public static double distance(Unit a,Unit b){return Math.hypot(0,0);}
    public static int rangeToPlayer(Enemy e){return (int)distance(e,player);}
    public static void monsterMove(Monster m,int v){}
    public static Player getPlayer(){return player;}public static void setPlayer(Player p){player=p;}public static void setEnemies(List<Enemy>l){enemies=l;}
}