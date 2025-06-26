import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Instructions.printGameInstructions();
        System.out.println("Game starts");
        new GameController().run();

    }
}