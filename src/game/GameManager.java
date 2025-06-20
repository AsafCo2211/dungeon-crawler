package game;
import tiles.Position;import units.*;import java.nio.file.*;import java.util.*;
public class GameManager{
    private Player player;private List<Enemy>enemies;
    public GameManager(){enemies=new ArrayList<>();}
    public void init(String pname,Path dir){player=UnitFactory.createPlayer(pname);GameUtil.setPlayer(player);try(DirectoryStream<Path>ds=Files.newDirectoryStream(dir,"level*.txt")){for(Path f:ds){List<String>lines=Files.readAllLines(f);for(int y=0;y<lines.size();y++){String row=lines.get(y);for(int x=0;x<row.length();x++){char c=row.charAt(x);Position p=new Position(x,y);Enemy e=UnitFactory.createEnemy(c,p);if(e!=null)enemies.add(e);}}}}catch(Exception ex){Message.error("Load levels: "+ex.getMessage());}GameUtil.setEnemies(enemies);}
    public void gameLoop(){Scanner sc=new Scanner(System.in);
        while(true){
            render();
            if(player.getHealthAmount()==0){Message.info("Game Over");break;}
            if(enemies.isEmpty()){Message.info("You Win!");break;}
            Message.info("Enter action (w/a/s/d/e/q):");char c=sc.next().charAt(0);
            switch(c){
                case 'w':move(0,-1);break;
                case 's':move(0,1);break;
                case 'a':move(-1,0);break;
                case 'd':move(1,0);break;
                case 'e':player.castAbility();break;
                case 'q':break;
                default:Message.error("Invalid");
            }
            enemies.forEach(Enemy::onEnemyTurn);
            player.onGameTick();
        }
    }
    private void move(int dx,int dy){/* update player pos and handle combat */}
    private void render(){
        // print board
        Message.board(player.description());
        enemies.forEach(e->Message.board(e.description()));
    }
    public static void main(String[]args){
        if(args.length<2){System.err.println("Usage: java -jar game.jar <player> <levelsDir>");return;}
        GameManager gm=new GameManager();
        gm.init(args[0],Path.of(args[1]));
        gm.gameLoop();
    }
}