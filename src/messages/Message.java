package messages;

public class Message {
    public static void info(String msg)    { System.out.println(msg); }
    public static void error(String msg)   { System.err.println(msg); }
    public static void board(String s)     { System.out.println(s); }
}