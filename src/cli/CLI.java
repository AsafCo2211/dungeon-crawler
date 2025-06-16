package cli;
import game.GameManager;
public class CLI {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java cli.CLI <playerName> <levelsDir>");
            return;
        }
        String player = args[0];
        String dir = args[1];
        GameManager gm = new GameManager();
        gm.init(player, java.nio.file.Path.of(dir));
        gm.gameLoop();
    }
}
