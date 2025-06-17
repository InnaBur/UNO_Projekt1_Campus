import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        System.out.println("Game starts");
        Instructions.printGameInstructions();
        new GameController().run();

    }
}