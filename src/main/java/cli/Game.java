package cli;

import cli.InputProvider.Action;
import game.GameManager;
import game.generator.RandomGenerator;
import tiles.Position;
import units.player.Player;
import util.UnitFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public final class Game {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: java cli.Game <levels_dir>");
            return;
        }

        Map<Character, Action> keys = InputProvider.promptForMapping();
        InputProvider input = new InputProvider(keys);

        GameManager.create(new RandomGenerator()).setCallbacks(System.out::println, u -> System.out.println(u.getName() + " died"));

        int heroIdx = promptHero();
        LevelManager lm = new LevelManager(Path.of(args[0]), heroIdx);
        if (!lm.loadNextLevel()) {
            System.err.println("No levels found.");
            return;
        }

        System.out.println("ENJOY!\n");
        render(null);

        Player player = GameManager.I().getPlayer();

        while (true) {
            System.out.print("your turn:\n\n");
            Action act = input.nextAction();

            switch (act) {
                case QUIT -> { return; }
                case SKIP -> { /* do nothing */ }
                case CAST -> player.castAbility();
                case UP, DOWN, LEFT, RIGHT -> handleMove(act);
                default -> { /* ignore */ }
            }

            System.out.println("\nenemies turn:\n");
            render(null);

            if (player.getHealth().isDead()) {
                System.out.println("You died – game over.");
                render(null);
                break;
            }
            if (GameManager.I().getEnemies().isEmpty()) {
                System.out.println("*** LEVEL CLEARED ***\n");
                if (!lm.loadNextLevel()) {
                    System.out.println("Congratulations – all levels completed!");
                    break;
                }
                render(null);
            }

            GameManager.I().gameTick();
        }
    }

    private static int promptHero() {
        List<Player> list = UnitFactory.catalogue();

        System.out.println("please choose your player:\n");
        for (int i = 0; i < list.size(); i++) {
            Player h = list.get(i);
            System.out.println((i + 1) + ". " + h.description());
            System.out.println();
        }

        Scanner sc = new Scanner(System.in);
        int idx;
        while (true) {
            System.out.print("\nyour choice (1-" + list.size() + "): ");
            try { idx = Integer.parseInt(sc.nextLine()) - 1; }
            catch (NumberFormatException e) { continue; }
            if (idx >= 0 && idx < list.size()) break;
        }

        System.out.println("your choice: " + list.get(idx).getName());
        System.out.println();
        return idx;
    }

    private static void handleMove(Action a) {
        Player p = GameManager.I().getPlayer();
        Position pos = p.getMovement().pos();
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
        printBoard();
        System.out.println(GameManager.I().getPlayer().description());
        System.out.print(GameManager.I().flushRoundMessages());
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
