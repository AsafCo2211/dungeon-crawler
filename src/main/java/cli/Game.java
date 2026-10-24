package cli;

import cli.InputProvider.Action;
import game.GameManager;
import game.callbacks.DeathCallback;
import game.generator.RandomGenerator;
import values.Position;
import units.player.Player;
import util.UnitFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class Game {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: java cli.Game <levels_dir>");
            return;
        }

        Map<Character, Action> keys = InputProvider.promptForMapping();
        if (keys == null) return;   // input ended during setup
        InputProvider input = new InputProvider(keys);

        // Deaths are already reported in the round log ("... was defeated."), in
        // order with the battle that caused them; printing here as well put a
        // duplicate "died" line before the battle.
        GameManager.create(new RandomGenerator()).setCallbacks(System.out::println, DeathCallback.ignore());

        int heroIdx = promptHero();
        if (heroIdx < 0) return;    // input ended during hero selection
        LevelManager lm = new LevelManager(Path.of(args[0]), heroIdx);
        if (!lm.loadNextLevel()) {
            System.err.println("No levels found.");
            return;
        }

        System.out.println("ENJOY!\n");
        render(null);

        Player player = GameManager.I().getPlayer();

        while (true) {
            System.out.print("your turn:\n");
            Action act = input.nextAction();

            switch (act) {
                case QUIT -> { return; }
                case SKIP -> { /* do nothing */ }
                case CAST -> player.castAbility();
                case UP, DOWN, LEFT, RIGHT -> handleMove(act);
                default -> { /* ignore */ }
            }

            if (!GameManager.I().getEnemies().isEmpty()) {
                System.out.println("enemies turn:\n");
                GameManager.I().gameTick();
            }

            render(null);

            if (GameManager.I().getEnemies().isEmpty()) {
                System.out.println("*** LEVEL CLEARED ***\n");
                if (!lm.loadNextLevel()) {
                    System.out.println("Congratulations – all levels completed!");
                    break;
                }
                 render(null);
            }

            if (player.getHealth().isDead()) {
                break;
            }
        }
    }

    private static int promptHero() {
        List<Player> list = UnitFactory.catalogue();

        System.out.println("please choose your player:\n");
        for (int i = 0; i < list.size(); i++) {
            Player h = list.get(i);

            System.out.println(MenuFormatter.line(i + 1, h));
            System.out.println();
        }

        int idx;
        while (true) {
            System.out.print("\nyour choice (1-" + list.size() + "): ");
            String line = InputProvider.readLine();
            if (line == null) return -1;
            try { idx = Integer.parseInt(line.trim()) - 1; }
            catch (NumberFormatException e) { continue; }
            if (idx >= 0 && idx < list.size()) break;
        }

        System.out.println("your choice: " + list.get(idx).getName());
        System.out.println();
        return idx;
    }


    private static void handleMove(Action a) {
        Player p = GameManager.I().getPlayer();
        Position pos = p.getPosition();

        Position to = switch (a) {
            case UP -> pos.translate( 0, -1);
            case DOWN  -> pos.translate( 0,  1);
            case LEFT  -> pos.translate(-1,  0);
            case RIGHT -> pos.translate( 1,  0);
            default -> pos;
        };
        GameManager.I().getBoardController().move(p, to);
    }

    private static void render(String hdr) {
        if (hdr != null) System.out.println(hdr);
        // What happened this round first, then the board it led to — so the
        // stats under the board already reflect the battles printed above it.
        System.out.print(GameManager.I().flushRoundMessages());
        printBoard();
        System.out.println(GameManager.I().getPlayer().description());
        System.out.println();
    }

    private static void printBoard() {
        GameManager gm = GameManager.I();
        OUTER:
        for (int y = 0; ; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; ; x++) {
                try { sb.append(gm.tileAt(new Position(x, y))); }
                catch (ArrayIndexOutOfBoundsException e) {
                    if (x == 0) break OUTER;
                    else break;
                }
            }
            System.out.println(sb);
        }
        System.out.println();
    }
}
