public class Main {
    public static void main(String[] args) {
       GameController.printGameInstructions();

        System.out.println("Game starts");

        new GameController().run();

    }
}