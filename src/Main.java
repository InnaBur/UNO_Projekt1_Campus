import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        PrintManager.printGameInstructions();
        System.out.println("Game starts");
        new GameController().run();

    }
}