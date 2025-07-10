import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws SQLException {

        PrintManager.printGameInstructions();
        System.out.println("Game starts");
        new GameController().run();

    }


}