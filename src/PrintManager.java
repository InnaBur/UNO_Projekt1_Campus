public class PrintManager {

    public static void showMenu() {
        System.out.println("\u001B[30;47m"); // Black text on light gray background
        System.out.println("""
                            Make your choice:                 
                  ┌────────────┬────────────┬────────────┐               
                  │ [1] Draw   │ [2] Play   │ [3] Bluff  │            
                  │     a card │     a card │     check  │             
                  ├────────────┼────────────┼────────────┤            
                  │ [4] Play   │ [5] Suggest│ [6] Help   │             
                  │     & UNO  │     a move │     rules  │            
                  ├────────────┼────────────┼────────────┤             
                  │            │ [0] Exit   │            │            
                  │            │     game   │            │             
                  └────────────┴────────────┴────────────┘             
                """);
        System.out.print("\u001B[0m"); // Reset colors
    }

    public static void printInvalidInput(String s) {
        System.out.println("Invalid input " + s);
    }

    public static void colorChoice(String name) {
        System.out.println("\u001B[30;46m[" + name + "]\u001B[0m, choose the next color: " +
                "\u001B[30;41m[R]\u001B[0m Red, " +
                "\u001B[30;42m[G]\u001B[0m Green, " +
                "\u001B[30;44m[B]\u001B[0m Blue, " +
                "\u001B[30;43m[Y]\u001B[0m Yellow");
    }

    public static void printLine() {
        System.out.println("------------------------------");
    }

    public static void printChangeColorMessage(String playersName,  String newCardName) {
        System.out.println("\u001B[30;46m["+playersName +"]\u001B[0m  \u001B[30;45m! ! ! Color change ! ! !\u001B[0m to: "
                + CardsDeck.createColoredOutputForCard(newCardName));
    }

    public static void directionChangeMessage(String name, boolean clockwise) {
        System.out.println("\u001B[30;46m["+name
                + "]\u001B[0m made a \u001B[30;41mDirection change\u001B[0m to "
                + (clockwise ? "Clockwise" : "Counter-clockwise"));
    }

    public static void twoCardsMessage(String name) {
        System.out.println("\u001B[30;46m["+name +"]\u001B[0m \u001B[30;41mDraws 2 cards!\u001B[0m");
    }

    public static void skippMessage(String name) {
        System.out.println("\u001B[30;46m[" + name
                + "]\u001B[0m lost her/his turn: \u001B[30;45mSkipped!\u001B[0m");
    }
}
