import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws SQLException {

        // 1. Datenbankverbindung aufbauen
        DatabaseHelper dbHelper = new DatabaseHelper("uno.sqlite");
        dbHelper.initDatabaseIfNeeded();
        // 2. Spielanleitung anzeigen
        Instructions.printGameInstructions();
        //2.1. Vorherige Results aus der DB löschen (table "Sessions" leeren
        dbHelper.resetSessions();
        dbHelper.initDatabaseIfNeeded();
        // 3. GameController erzeugen
        System.out.println("Game starts");
        GameController controller = new GameController();
        //Test: new GameController().run();
        // 4. Datenbank setzen
        controller.setDatabaseHelper(dbHelper);
        // 5. Spiel starten
        controller.run();
    }


}