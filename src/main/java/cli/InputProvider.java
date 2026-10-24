package cli;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class InputProvider {

    public enum Action { UP, DOWN, LEFT, RIGHT, CAST, SKIP, QUIT, NONE }

    private final Map<Character, Action> mapping;

    // The game's only reader of standard input. Each Scanner reads ahead and
    // buffers, so with several on System.in the first one swallowed lines
    // meant for the others: piped or redirected input crashed the game after
    // the key setup.
    private static final Scanner STDIN = new Scanner(System.in);

    /** The next input line, or {@code null} once input has ended. */
    static String readLine() {
        return STDIN.hasNextLine() ? STDIN.nextLine() : null;
    }

    public InputProvider(Map<Character, Action> mapping) {
        this.mapping = mapping;
    }

    public Action nextAction() {
        String raw = readLine();
        if (raw == null) return Action.QUIT;   // end of input ends the game
        String line = raw.trim().toLowerCase();
        if (line.isEmpty()) return Action.NONE;
        return mapping.getOrDefault(line.charAt(0), Action.NONE);
    }

    /** Asks for a key per action; returns {@code null} if input ends first. */
    public static Map<Character, Action> promptForMapping() {
        record Q(String prompt, Action act) {}
        Q[] qs = {
                new Q("MOVING UP", Action.UP),
                new Q("MOVING DOWN", Action.DOWN),
                new Q("MOVING LEFT", Action.LEFT),
                new Q("MOVING RIGHT", Action.RIGHT),
                new Q("SKIP TURN", Action.SKIP),
                new Q("CAST SPECIAL ABILITY", Action.CAST),
                new Q("QUIT GAME", Action.QUIT)
        };

        Map<Character, Action> map = new HashMap<>();

        for (Q q : qs) {
            while (true) {
                System.out.print("please choose the key for " + q.prompt() + " : ");
                String raw = readLine();
                if (raw == null) return null;
                String line = raw.trim().toLowerCase();
                if (line.length() != 1 || map.containsKey(line.charAt(0))) {
                    System.out.println("Invalid or already used – try again.\n");
                    continue;
                }
                map.put(line.charAt(0), q.act());
                System.out.println();
                break;
            }
        }
        return map;
    }
}
