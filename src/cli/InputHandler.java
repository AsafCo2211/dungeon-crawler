package cli;
import java.util.Scanner;
public class InputHandler {
    private Scanner sc = new Scanner(System.in);
    public char nextAction() { return sc.next().charAt(0); }
}